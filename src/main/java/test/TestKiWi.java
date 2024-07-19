package test;

import kiwi.KiWiMap;

import java.util.Arrays;
import java.util.Random;

public class TestKiWi extends Thread {
    public enum Action {
        Put, Get, Scan
    }
    
    public long counter;
    private final KiWiMap kiwi;
    private final Integer min_key;
    private final Integer max_key;
    private final Action action;
    
    public TestKiWi(KiWiMap kiwi, Integer min_key, Integer max_key, Action action) {
        this.counter = 0;
        this.kiwi = kiwi;
        this.min_key = min_key;
        this.max_key = max_key;
        this.action = action;
    }

    @Override
    public void run() {
        Random random = new Random();
        random.setSeed(42);
        int offset = Integer.min(max_key - min_key, 32_000);
        switch (action) {
            case Put -> {
                while (true) {
                    if(interrupted()) return;
                    int key = random.nextInt(min_key, max_key);
                    if (random.nextFloat() < 0.5) {
                        int value = random.nextInt();
                        kiwi.put(key, value);
                    }
                    else 
                        kiwi.remove(key);
                    counter++;
                }
            }
            case Get -> {
                while (true) {
                    if(interrupted()) return;
                    int key = random.nextInt(min_key, max_key);
                    kiwi.get(key);
                    counter++;
                }
            }
            case Scan -> {
                while (true) {
                    if(interrupted()) return;
                    int min = random.nextInt(min_key, max_key - offset);
                    int max = min + offset;
                    Integer[] result = new Integer[max - min + 1];
                    kiwi.getRange(result, min, max);
                    counter++;
                }
            }
        }
    }
    
    public static String runTest(KiWiMap kiwi, Integer min_key, Integer max_key, Action action, int ms, int threadsCount) {
        // create all threads
        TestKiWi[] threads = new TestKiWi[threadsCount];
        for (int i = 0; i < threadsCount; i++)
            threads[i] = new TestKiWi(kiwi, min_key, max_key, action);
        // start the threads
        for (int i = 0; i < threadsCount; i++) 
            threads[i].start();
        // wait for the time to pass
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
        // interrupt all threads
        for (int i = 0; i < threadsCount; i++) 
            threads[i].interrupt();
        // join threads
        for (int i = 0; i < threadsCount; i++) {
            try {
                threads[i].join(100);
            } catch (InterruptedException ignored) {
            }
        }
        // sum up all thread operations and return
        return action + ", " + threadsCount + ", " + Arrays.stream(threads).mapToLong(t -> t.counter).sum();
    }
}
