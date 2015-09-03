## useful stuff

## join
merge collections similar to sql joins

#### SortedListJoiner
merge two sorted lists (of types T and U) and produces a list of type R where items are created by applying a combiner to instances of T and U

fullOuterJoin: all elements of both lists included, missing matches filled by null 

```java
    public static <T, U, R> List<R> fullOuterJoin(List<T> left, List<U> right, BiFunction<T, U, Integer> comparator, BiFunction<T, U, R> combiner)
    
```
    