---

### 术语

term: 倒排索引的 key

posting list: 倒排索引的 value

term dictionary: 将term以字典序排序，可以二分查找term 的字典

term index: lucene将部分term dictionary中的前缀抽取出来，以trie的数据结构进行组织并存储在内存中，这就是term index

---

### Lucene 倒排索引

-> FST + posting list

或

-> FST + Bitmap

Elasticsearch支持以上两种的联合索引方式 (FST + posting list 或 FST + Bitmap)
如果查询的filter***缓存到了内存中***（以 bitmap 的形式），那么合并就是两个 bitmap 的AND
如果查询的filter***没有缓存***，那么就用 skip list 的方式去遍历两个 ***on disk*** 的 posting list

#### FST

Finite State Transducer, Trie 的升级版，共享前后缀，用于定位 term

---

### doc value

列存，每一列 为一个 存储 id -> value 的映射
