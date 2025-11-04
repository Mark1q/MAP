package model.adt;

import java.util.HashMap;
import java.util.Map;
import exception.MyException;

public class MyDictionary<K, V> implements MyIDictionary<K, V> {
    private HashMap<K, V> dictionary;

    public MyDictionary() { dictionary = new HashMap<>(); }

    public void put(K key, V value) { dictionary.put(key, value); }

    public V lookup(K key) throws MyException {
        if (!dictionary.containsKey(key))
            throw new MyException("Variable " + key + " is not defined");
        return dictionary.get(key);
    }

    public boolean isDefined(K key) { return dictionary.containsKey(key); }

    public void update(K key, V value) throws MyException {
        if (!dictionary.containsKey(key))
            throw new MyException("Variable " + key + " is not defined");
        dictionary.put(key, value);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : dictionary.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
