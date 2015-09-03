package com.benya.lambdaUtils.join;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SortedListJoinerTest {

    private static class Left {
        private String key;
        private String left;
        public Left(String key, String value) {
            super();
            this.key = key;
            this.left = value;
        }
        public String getKey() {
            return key;
        }
        
        public String getValue() {
            return left;
        }
    }
    
    
    private static class Right {
        private String key;
        private String value;
        public Right(String key, String value) {
            super();
            this.key = key;
            this.value = value;
        }
        public String getKey() {
            return key;
        }
        public String getValue() {
            return value;
        }
    }
    
    private static class Result {
        @SuppressWarnings("unused")
        private String key;
        @SuppressWarnings("unused")
        private String left;
        @SuppressWarnings("unused")
        private String right;
        public Result(String key, String left, String right) {
            super();
            this.key = key;
            this.left = left;
            this.right = right;
        }
        
    }
    
    
    BiFunction <Left, Right, Integer> comparator = (left, right) -> String.CASE_INSENSITIVE_ORDER.compare(left.getKey(), right.getKey());
    BiFunction <Left, Right, Result> combiner = (left, right) -> {
        if (left == null)
            return new Result(right.getKey(), null, right.getValue());
        else if (right == null)
            return new Result(left.getKey(), left.getValue(), null);
        else
            return new Result(left.getKey(), left.getValue(), right.getValue()); 
    };

    
    @Test
    public void testFullOuterJoinMatching() {
        List<Left> left = Arrays.asList(
                new Left("1", "one"),
                new Left("2", "two")
        );
        List<Right> right = Arrays.asList(
                new Right("1", "one"),
                new Right("2", "two")
                );
                
        List<Result> result = SortedListJoiner.fullOuterJoin(left, right, comparator, combiner);
        
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualToComparingFieldByField(new Result("1", "one", "one"));
        assertThat(result.get(1)).isEqualToComparingFieldByField(new Result("2", "two", "two"));
    }
    
    @Test
    public void testFullOuterJoinSmallerLeft() {
        List<Left> left = Arrays.asList(
                new Left("2", "two")
        );
        List<Right> right = Arrays.asList(
                new Right("1", "one"),
                new Right("2", "two"),
                new Right("3", "three")
                );        
        List<Result> result = SortedListJoiner.fullOuterJoin(left, right, comparator, combiner);

        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isEqualToComparingFieldByField(new Result("1", null, "one"));
        assertThat(result.get(1)).isEqualToComparingFieldByField(new Result("2", "two", "two"));
        assertThat(result.get(2)).isEqualToComparingFieldByField(new Result("3", null, "three"));
               
    }
    
    @Test
    public void testFullOuterJoinSmallerRight() {
        List<Left> left = Arrays.asList(
                new Left("1", "one"),
                new Left("2", "two"),
                new Left("3", "three")
        );
        List<Right> right = Arrays.asList(
                new Right("2", "two")
                );        
        List<Result> result = SortedListJoiner.fullOuterJoin(left, right, comparator, combiner);

        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isEqualToComparingFieldByField(new Result("1", "one", null));
        assertThat(result.get(1)).isEqualToComparingFieldByField(new Result("2", "two", "two"));
        assertThat(result.get(2)).isEqualToComparingFieldByField(new Result("3", "three", null));
               
    }    
    
    @Test
    public void testFullOuterJoinNoMatch() {
        List<Left> left = Arrays.asList(
                new Left("1", "one"),
                new Left("3", "three")
        );
        List<Right> right = Arrays.asList(
                new Right("2", "two"),
                new Right("4", "four")
                );        
        List<Result> result = SortedListJoiner.fullOuterJoin(left, right, comparator, combiner);

        assertThat(result).hasSize(4);
        assertThat(result.get(0)).isEqualToComparingFieldByField(new Result("1", "one", null));
        assertThat(result.get(1)).isEqualToComparingFieldByField(new Result("2", null, "two"));
        assertThat(result.get(2)).isEqualToComparingFieldByField(new Result("3", "three", null));
        assertThat(result.get(3)).isEqualToComparingFieldByField(new Result("4", null, "four"));
    }
    

    
}
