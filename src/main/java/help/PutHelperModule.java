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
    
    // returns a snapshot of the help requests array
    public PutHelpData<K, V>[] GetHelpArrSnapshot() {
        PutHelpData<K, V>[] arr = new PutHelpData[MAX_THREADS];
        for (int i = 0; i < MAX_THREADS; i++) 
            arr[i] = putHelpArr.get(i);
        return arr;
    }
    
    // adds a new help request to the array
    public void RequestHelp(PutHelpData<K, V> helpData) {
        int index = (int) Thread.currentThread().getId() % MAX_THREADS;
        putHelpArr.set(index, helpData);
    }
    
    // try to complete a request for help, returns true if found and deleted from array
    public boolean CompleteHelp(PutHelpData<K, V> helpData) {
        boolean found = false;
        for (int i = 0; i < MAX_THREADS; i++) found |= putHelpArr.compareAndSet(i, helpData, null);
        return found;
    }
    
    // check if the request is still waiting for completion
    public boolean FindRequest(PutHelpData<K, V> helpData) {
        boolean found = false;
        for (int i = 0; i < MAX_THREADS && !found; i++)
            if (putHelpArr.get(i) == helpData)
                found = true;
        return found;
    }
    
    // get keys in range
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
