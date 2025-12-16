package model.adt;

import exception.MyException;
import java.util.Map;
import java.util.Set;

public interface MyIDictionary<K, V> {
    void put(K key, V value);
    V lookup(K key) throws MyException;
    boolean isDefined(K key);
    void update(K key, V value) throws MyException;
    void remove(K key) throws MyException;
    Set<K> keySet();
    Map<K, V> getContent();
    String toString();
    MyIDictionary<K, V> deepCopy();
}