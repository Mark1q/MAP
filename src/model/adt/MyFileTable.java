package model.adt;
import exception.MyException;
import java.util.HashMap;
import java.util.Map;

public class MyFileTable<K, V> implements MyIFileTable<K, V> {
    private HashMap<K, V> table;

    public MyFileTable() { table = new HashMap<>(); }

    public void put(K key, V value) { table.put(key, value); }

    public V lookup(K key) throws MyException {
        if (!table.containsKey(key))
            throw new MyException("File " + key + " is not opened");
        return table.get(key);
    }

    public boolean isDefined(K key) { return table.containsKey(key); }

    public void remove(K key) throws MyException {
        if (!table.containsKey(key))
            throw new MyException("File " + key + " is not opened");
        table.remove(key);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (K key : table.keySet()) {
            sb.append(key.toString()).append("\n");
        }
        return sb.toString();
    }
}