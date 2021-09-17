# Flink

---

## state and checkpoint

### Keyed State

Keyed State 支持如下***状态***，状态通过 `RuntimeContext` 进行访问，因此只能在 *rich functions* 中使用

> `ValueState<T>`
>
> `ListState<T>`
>
> `ReducingState<T>`
>
> `AggregatingState<IN, OUT>`
>
> `MapState<UK, UV>`

### Operator State (or non-keyed state)

`SingleOutputStreamOperator` extends `DataStream` —— `SingleOutputStreamOperator` represents a user defined transformation applied on a DataStream with one predefined output type



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

- events (stream elements)
- state (fault-tolerant, consistent, only on keyed stream)
- timers (event time and processing time, only on keyed stream)

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