package model.adt;

import exception.MyException;
import java.util.Map;
import java.util.Set;

public interface MyIHeap<K, V> {
    int allocate(V value);
    V get(K address) throws MyException;
    void update(K address, V value) throws MyException;
    boolean isDefined(K address);
    void setContent(Map<K, V> newContent);
    Map<K, V> getContent();
    Set<K> keySet();
    String toString();
}