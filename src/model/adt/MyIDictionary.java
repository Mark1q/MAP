package model.adt;
import exception.MyException;

public interface MyIDictionary<K, V> {
    void put(K key, V value);
    V lookup(K key) throws MyException;
    boolean isDefined(K key);
    void update(K key, V value) throws MyException;
    String toString();
}