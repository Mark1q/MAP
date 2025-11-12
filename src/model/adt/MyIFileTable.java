package model.adt;
import exception.MyException;
import java.io.BufferedReader;

public interface MyIFileTable<K, V> {
    void put(K key, V value);
    V lookup(K key) throws MyException;
    boolean isDefined(K key);
    void remove(K key) throws MyException;
    String toString();
}
