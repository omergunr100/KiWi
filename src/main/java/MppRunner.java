import java.util.Random;
public class MppRunner {
    final static int MIN_KEY = 0;
    final static int MAX_KEY = 10_000_000;
    
    public static void main(String[] args) {
        System.out.println("Action, Threads, Count, Slow");
        for (TestKiWi.Action action : TestKiWi.Action.values()) {
            for (int i = 2; i <= 32; i *= 2) {
                // initialize kiwi map
                KiWiMap kiwi = new KiWiMap();
                // insert 1M random pairs
                Random random = new Random();
                random.setSeed(42);
                for (int j = 0; j < 1_000_000; j++)
                    kiwi.put(random.nextInt(MIN_KEY, MAX_KEY), random.nextInt());
                // test the action on the map
                String result = TestKiWi.runTest(kiwi, MIN_KEY, MAX_KEY, action, 10_000, i) + ", " + kiwi.kiwi.putHelper.GetSlowCounter();
                System.out.println(result);
            }
        }
    }
}
