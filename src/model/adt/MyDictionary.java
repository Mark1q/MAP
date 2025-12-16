package model.adt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import exception.MyException;

public class MyDictionary<K, V> implements MyIDictionary<K, V> {
    private HashMap<K, V> dictionary;

    public MyDictionary() {
        dictionary = new HashMap<>();
    }

    // NEW CONSTRUCTOR for deep copy
    private MyDictionary(HashMap<K, V> dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public void put(K key, V value) {
        dictionary.put(key, value);
    }

    @Override
    public V lookup(K key) throws MyException {
        if (!dictionary.containsKey(key))
            throw new MyException("Variable " + key + " is not defined");
        return dictionary.get(key);
    }

    @Override
    public boolean isDefined(K key) {
        return dictionary.containsKey(key);
    }

    @Override
    public void update(K key, V value) throws MyException {
        if (!dictionary.containsKey(key))
            throw new MyException("Variable " + key + " is not defined");
        dictionary.put(key, value);
    }

    @Override
    public void remove(K key) throws MyException {
        if (!dictionary.containsKey(key))
            throw new MyException("Key " + key + " is not defined");
        dictionary.remove(key);
    }

    @Override
    public Set<K> keySet() {
        return dictionary.keySet();
    }

    @Override
    public Map<K, V> getContent() {
        return dictionary;
    }

    @Override
    public MyIDictionary<K, V> deepCopy() {
        HashMap<K, V> newDict = new HashMap<>(dictionary);
        return new MyDictionary<K, V>(newDict);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : dictionary.entrySet()) {
            sb.append(entry.getKey()).append(" --> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}