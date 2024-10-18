package com.creativecoders;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ConcurentLRUCacheTest {
    Map yourLinkedHashMap = new ConcurrentHashMap(new LinkedHashMap());

    @org.junit.jupiter.api.Test
    void removeItems() {

        IntStream.range(1, 100)
                .forEach(index -> yourLinkedHashMap.put(index, index));

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                shrinkSize();
                return null;
            }
        };

        List<Future<Object>> list = new ArrayList<>();

        for(int i=0; i< 2; i++){
            Future<Object> future = executor.submit(callable);
            list.add(future);
        }

        for(Future<Object> future : list){
            try {
                System.out.println(new Date()+ "::"+future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        System.out.println(yourLinkedHashMap);
    }

    public void shrinkSize() {

        Iterator<Map.Entry<Integer, Integer>> iter = yourLinkedHashMap.entrySet().iterator();
        while (iter.hasNext()) {
            if (yourLinkedHashMap.size() >= 50) {
                try {
                    iter.next();
                    iter.remove();
                    Thread.sleep(100);
                } catch (ConcurrentModificationException | InterruptedException | NoSuchElementException e) {
                    throw new RuntimeException(e);
                }
            } else {
                break;
            }
        }
    }
}