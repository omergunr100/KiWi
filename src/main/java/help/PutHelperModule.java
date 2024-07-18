package help;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class PutHelperModule<K extends Comparable<? super K>, V> {
    // maximum number of threads supported
    private final int MAX_THREADS;
    // array for storing help requests from participating threads
    private final AtomicReferenceArray<PutHelpData<K, V>> putHelpArr;
    
    public PutHelperModule(int MAX_THREADS) {
        this.MAX_THREADS = MAX_THREADS;
        this.putHelpArr = new AtomicReferenceArray<>(MAX_THREADS);
    }
    
    // returns the first request for help or null if none exist
    public PutHelpData<K, V> FindHelpRequest() {
        PutHelpData<K, V> content = null;
        for (int i = 0; i < MAX_THREADS; i++) {
            content = putHelpArr.get(i);
            if (content != null) doSomething(content);
        }
        return content;
    }
    
    // adds a new help request to the array
    public void RequestHelp(PutHelpData<K, V> helpData) {
        int index = (int) Thread.currentThread().getId() % MAX_THREADS;
        putHelpArr.set(index, helpData);
    }
    
    // try to complete a request for help, returns true if found and deleted from array
    public boolean CompleteHelp(PutHelpData<K, V> helpData) {
        boolean found = false;
        for (int i = 0; i < MAX_THREADS && !found; i++) found = putHelpArr.compareAndSet(i, helpData, null);
        return found;
    }
    
    // get keys in range
    // todo: check how deletions are handled, might have to add edge case for null in entry value
    public List<PutHelpData<K, V>> GetKeysInRange(K min, K max, int version) {
        List<PutHelpData<K, V>> keyList = new ArrayList<>(MAX_THREADS / 2);
        PutHelpData<K, V> content = null;
        for (int i = 0; i < MAX_THREADS; i++) {
            content = putHelpArr.get(i);
            // if the cell is null, skip
            if (content == null) continue;
            // if the version is newer than that of the scan, skip
            if (content.version > version) continue;
            // else if the entry key is in range add to the list
            if (min.compareTo(content.key) <= 0 && content.key.compareTo(max) <= 0) keyList.add(content);
        }
        return keyList;
    }
}
