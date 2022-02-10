package me.bantling.micro.util;

import java.util.List;

public class Util {
    public static class ListBuilder<T> {
        private final List<T> list;
        
        ListBuilder(
            final List<T> list
        ) {
            this.list = list;
        }
        
        public ListBuilder<T> add(
            final T e
        ) {
            list.add(e);
            return this;
        }
        
        public ListBuilder<T> add(
            final T e1,
            final T e2
        ) {
            list.add(e1);
            list.add(e2);
            return this;
        }
        
        public ListBuilder<T> add(
            final T e1,
            final T e2,
            final T e3
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            return this;
        }
        
        public ListBuilder<T> add(
            final T e1,
            final T e2,
            final T e3,
            final T e4
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            return this;
        }
        
        public ListBuilder<T> add(
            final T e1,
            final T e2,
            final T e3,
            final T e4,
            final T e5
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            list.add(e5);
            return this;
        }
        
        public ListBuilder<T> add(
            final T e1,
            final T e2,
            final T e3,
            final T e4,
            final T e5,
            final T e6
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            list.add(e5);
            list.add(e6);
            return this;
        }
        
        public ListBuilder<T> add(
            final T e1,
            final T e2,
            final T e3,
            final T e4,
            final T e5,
            final T e6,
            final T e7
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            list.add(e5);
            list.add(e6);
            list.add(e7);
            return this;
        }
        
        public ListBuilder<T> add(
            final T e1,
            final T e2,
            final T e3,
            final T e4,
            final T e5,
            final T e6,
            final T e7,
            final T e8
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            list.add(e5);
            list.add(e6);
            list.add(e7);
            list.add(e8);
            return this;
        }
        
        public ListBuilder<T> add(
            final T e1,
            final T e2,
            final T e3,
            final T e4,
            final T e5,
            final T e6,
            final T e7,
            final T e8,
            final T e9
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            list.add(e5);
            list.add(e6);
            list.add(e7);
            list.add(e8);
            list.add(e9);
            return this;
        }
        
        public ListBuilder<T> add(
            final T e1,
            final T e2,
            final T e3,
            final T e4,
            final T e5,
            final T e6,
            final T e7,
            final T e8,
            final T e9,
            final T e10
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            list.add(e5);
            list.add(e6);
            list.add(e7);
            list.add(e8);
            list.add(e9);
            list.add(e10);
            return this;
        }
        
        public List<T> done() {
            return list;
        }
    }
    
    public static <T> ListBuilder<T> listOf(final List<T> list) {
        return new ListBuilder<>(list);
    }
}
