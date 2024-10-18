package com.creativecoders;

import java.util.LinkedHashMap;
import java.util.Map;

/***
 * Proposed by https://stackoverflow.com/users/139985/stephen-c
 */
public class LRUCache<K, V> {

    private static class MyCache<KK, VV> extends LinkedHashMap<KK, VV> {
        private final int maxSize;

        private MyCache(int maxSize) {
            super(maxSize, 0.75f, true);
            this.maxSize = maxSize;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<KK, VV> eldest) {
            return size() > this.maxSize;
        }
    }

    public final MyCache<K, V> myCache;  // don't leak this

    public LRUCache(int maxSize) {
       myCache = new MyCache(maxSize);
    }

    public synchronized V get(K key) {
        return myCache.get(key);
    }

    public synchronized V put(K key, V value) {
        return myCache.put(key, value);
    }

    @Override
    public String toString( ){

        return myCache.toString();
    }
}