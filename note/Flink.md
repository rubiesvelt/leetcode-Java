# Flink

---

## state and checkpoint

### State

+ 其实 非 keyBy 的 function，可以通过自己定义字段，实现state

+ keyBy 的 function 就不好这样做，一般通过实现聚合函数从而进行聚合

#### Keyed State

Keyed State 支持如下***状态***，状态通过 `RuntimeContext` 进行访问，因此只能在 *rich functions* 中使用

> `ValueState<T>`: 保存一个可以更新和检索的值，可以通过`update(T)`进行更新，通过`T value()`进行检索
>
> `ListState<T>`: 保存一个元素的列表，可以通过`add(T)`或者`addAll(List<T>)`进行添加元素，通过`Iterable<T> get()`获得整个列表。还可以通过`update(List<T>)`覆盖当前的列表
>
> `ReducingState<T>`: 保存一个单值，表示添加到状态的所有值的聚合，使用`add(T)`增加元素，会使用提供的 ReduceFunction 进行聚合
>
> `AggregatingState<IN, OUT>`: 保留一个单值，表示添加到状态的所有值的聚合，***聚合类型可能与 添加到状态的元素的类型不同***，使用`add(IN)`添加的元素会用指定的`AggregateFunction`进行聚合
>
> `MapState<UK, UV>`: 保留一个映射表，广播流里在用

#### Operator State (or non-keyed state)

Operator State 与单个并行算子相关，与key无关 —— Operator State (or non-keyed state) is state that is bound to one parallel operator instance.

Kafka Connector 是一个很好的例子 —— The Kafka Connector is a good motivating example for the use of Operator State in Flink.

每一个 Kafka Consumer 并行实例都保存了一个 topic partitions -> offset 的 map —— Each parallel instance of the Kafka consumer maintains a map of topic partitions and offsets as its Operator State.

#### Broadcast State

Broadcast State 是 Operator State 的一种特殊的形式 —— Broadcast State is a special type of Operator State.

由于当前流需要广播到下游算子，故下游算子需要维护相同的 state，该 state 可在处理第二个流元素时候被访问 —— It was introduced to support use cases where records of one stream need to be broadcasted to all downstream tasks, 
where they are used to maintain the same state among all subtasks. This state can then be accessed while processing records of a second stream. 

最佳使用场景：吞吐量小的流，拥有规则，提供给其他流中的元素 —— As an example where broadcast state can emerge as a natural fit, one can imagine a low-throughput stream containing a set of rules which we want to evaluate against all elements coming from another stream. 

综上所述，Broadcast State 区分于其他 Operator State 在一下几个方面：
1. `map`形式
2. 只适用于可处理广播流的算子
3. 单个算子可以拥有多个不同名字的 Broadcast State

—— Having the above type of use cases in mind, broadcast state differs from the rest of operator states in that:
1. it has a map format
2. it is only available to specific operators that have as inputs a broadcasted stream and a non-broadcasted one, and
3. such an operator can have multiple broadcast states with different names.


### 状态有效期(TTl)

任何类型的 keyed state 都可以有 有效期 (TTL)

如果配置了 TTL 且状态值已过期，则会尽最大可能清除对应的值

### checkpoint



---

## Window

+ `TimeWindow` extends `Window` —— A Window that represents a time interval from start (inclusive) to end (exclusive).
+ `GlobalWindow` extends `Window` —— The default window into which all data is placed (via org.apache.flink.streaming.api.windowing.assigners.GlobalWindows).

### WindowAssigner

+ Tumbling Windows

  > `TumblingEventTimeWindows` extends `WindowAssigner` —— `TimeWindow` with `EventTimeTrigger`
  >
  > `TumblingProcessingTimeWindows` —— `TimeWindow` with `ProcessingTimeTrigger`

+ Sliding Windows

  > `SlidingEventTimeWindows`
  >
  > `SlidingProcessingTimeWindows`

+ Session Windows
+ Global Window —— with no Trigger

```java
/**
 * KeyedStream 的 countWindow() 方法，就是 GlobalWindow + CountTrigger
 * Windows this {@code KeyedStream} into tumbling count windows.
 **/
public WindowedStream<T, KEY, GlobalWindow> countWindow(long size) {
   return window(GlobalWindows.create()).trigger(PurgingTrigger.of(CountTrigger.of(size)));
}
```

### Trigger

A `Trigger` determines when a window (as formed by the *window assigner*) is ready to be processed by the *window function*.

Built-in Triggers extents `Trigger`

+ `EventTimeTrigger`
+ `ProcessingTimeTrigger`
+ `CountTrigger`
+ `PurgingTrigger`

---

## Function

### Window Functions

以上，是定义如何触发窗口，这个是触发后如何处理窗口元素

+ `ReduceFunction<T>` —— 增量聚合，输入`T`和输出`T`需要为相同类
+ `AggregateFunction<IN, ACC, OUT>` —— 输入`IN`，累加器`ACC`，输出`OUT`
+ `ProcessWindowFunction` —— extends `AbstractRichFunction`

增量聚合: 窗口不维护原始数据，只维护中间结果，每次基于中间结果和增量数据进行聚合。如: `ReduceFunction`、`AggregateFunction`。

全量聚合: 窗口需要维护全部原始数据，窗口触发进行全量聚合。如:`ProcessWindowFunction`

---

### RichFunction

`RichFunction` —— This class defines methods for the life cycle of the functions, as well as methods to access the context in which the functions are executed.

`AbstractRichFunction` —— 可获取`RuntimeContext`

`RichFilterFunction` extends `AbstractRichFunction` —— 

`ProcessWindowFunction` extends `AbstractRichFunction` —— 

---

### ProcessFunction

`ProcessFunction` —— The `ProcessFunction` is a low-level stream processing operation, giving access to the basic building blocks of all (acyclic) streaming applications:

+ **events** (stream elements)
+ **state** (fault-tolerant, consistent, only on keyed stream)
+ **timers** (event time and processing time, only on keyed stream)

It handles events by being invoked for each event received in the input stream(s).

---

## Time and watermark

### 时间语义

+ Event Time

  是事件在现实世界中发生的时间，它通常由事件中的时间戳描述。

+ Ingestion Time

  是数据进入Apache Flink流处理系统的时间，也就是Flink读取数据源时间。

+ Processing Time

  是数据流入到具体某个算子 (消息被计算处理) 时候相应的系统时间。也就是Flink程序处理该事件时当前系统时间。

---

### WaterMark

> WaterMark时间 >= window_end_time
>
> [window_start_time,window_end_time)中有数据存在
>
> watermark是一种特殊的时间戳，也是一种被插入到数据流的特殊的数据结构，用于表示eventTime小于watermark的事件已经全部落入到相应的窗口中，此时可进行窗口操作

watermark与 实际时间，processing time，ingestion time 都无关，只于 event time 有关

allowLateNess 是将窗口关闭时间再延迟一段时间

---

### 如何处理迟到过多的数据？

迟到事件出现时窗口已经关闭并产出了计算结果，因此处理的方法有3种：
+ 重新激活已经关闭的窗口并重新计算以修正结果。—— Allowed Lateness
+ 将迟到事件收集起来另外处理 —— SideOutput
+ 将迟到事件视为错误消息并丢弃 —— 默认

---

### 有waterMark的情况下，窗口何时触发？

waterMark时间 超过了 窗口结束时间