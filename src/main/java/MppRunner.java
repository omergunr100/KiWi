import kiwi.KiWiMap;

public class MppRunner {
    public static void main(String[] args) {
        KiWiMap kiwi = new KiWiMap();
        for (TestKiWi.Action action : TestKiWi.Action.values()) {
            for (int i = 2; i < 32; i *= 2) {
                TestKiWi.runTest(kiwi, 0, 10_000_000, action, 10_000, i);
            }
        }
    }
}
