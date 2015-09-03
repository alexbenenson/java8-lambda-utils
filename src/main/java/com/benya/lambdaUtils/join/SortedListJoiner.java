package com.benya.lambdaUtils.join;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;



public class SortedListJoiner {
    
    /**
     * 
     * @param left first of the two lists to join, must be sorted.
     * @param right second of the two lists to join. must be sorted. like in SQL, designations left and right don't mean much in a full join<br>
     * the ordering of left and right must be generally compatible and respected by the comparator parameter
     * @param comparator - a function that compares an item from left list (T) with an item from right list (U) using some application-specific logic and returns <br>
     * a negative integer if left item is less than right item, given the same ordering as the sorting of left and right lists <br> 
     * 0 of they are equal (meaning the equality in terms of the ordering of left and right lists) <br>
     * a positive integer if right is greater than left <p>
     * 
     * 
     * @param combiner - a function that takes an instance of T and an instance of U and produces a new R. either T or U may be null in a full outer join, so combiner needs to be able to create an R with one parameter being null
     *
     * @return a List of R, sorted in the same order as original lists
     */
    public static <T, U, R> List<R> fullOuterJoin(List<T> left, List<U> right, BiFunction<T, U, Integer> comparator, BiFunction<T, U, R> combiner) {
        
        List<R> result = new LinkedList<>();
        Iterator<T> leftIterator = left.iterator();
        Iterator<U> rightIterator = right.iterator();
        
        Supplier<Optional<U>> rightItemSupplier = () -> rightIterator.hasNext() ? Optional.of(rightIterator.next()) : Optional.empty();
        Optional<U> rightItem = rightItemSupplier.get();
        
        while (leftIterator.hasNext()) {
            T leftItem = leftIterator.next();
            
            while (rightItem.isPresent()  && comparator.apply(leftItem, rightItem.get()) > 0) {
                result.add(combiner.apply(null, rightItem.get()));
                rightItem = rightItemSupplier.get();
            }
            
            if (rightItem.isPresent() && comparator.apply(leftItem, rightItem.get()) == 0) {
                result.add(combiner.apply(leftItem, rightItem.get()));
                rightItem = rightItemSupplier.get();
            }
            else
                result.add(combiner.apply(leftItem, null));
        }

        while (rightItem.isPresent()) {
            result.add(combiner.apply(null, rightItem.get()));
            rightItem = rightItemSupplier.get();
        }
        
        return result;
    }
        
    // sortedInnerJoin
    // sortedLeftOuterJoin

}
