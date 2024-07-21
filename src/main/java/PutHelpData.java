import java.util.Objects;
public final class PutHelpData<K, V> {
    public K key;
    public V value;
    public int version;
    
    public PutHelpData(K key, V value, int version) {
        this.key = key;
        this.value = value;
        this.version = version;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PutHelpData<?, ?> that = (PutHelpData<?, ?>) o;
        return version == that.version && key.equals(that.key) && Objects.equals(value, that.value);
    }
    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + Objects.hashCode(value);
        result = 31 * result + version;
        return result;
    }
}
