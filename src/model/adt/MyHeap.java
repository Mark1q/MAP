package model.adt;

import exception.MyException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyHeap<K, V> implements MyIHeap<K, V> {
    private Map<K, V> heap;
    private int freeLocation;

    public MyHeap() {
        this.heap = new HashMap<>();
        this.freeLocation = 1;
    }

    @Override
    public int allocate(V value) {
        heap.put((K) Integer.valueOf(freeLocation), value);
        freeLocation++;
        return freeLocation - 1;
    }

    @Override
    public V get(K address) throws MyException {
        if (!heap.containsKey(address))
            throw new MyException("Address " + address + " is not defined in heap");
        return heap.get(address);
    }

    @Override
    public void update(K address, V value) throws MyException {
        if (!heap.containsKey(address))
            throw new MyException("Address " + address + " is not defined in heap");
        heap.put(address, value);
    }

    @Override
    public boolean isDefined(K address) {
        return heap.containsKey(address);
    }

    @Override
    public void setContent(Map<K, V> newContent) {
        this.heap = newContent;
    }

    @Override
    public Map<K, V> getContent() {
        return heap;
    }

    @Override
    public Set<K> keySet() {
        return heap.keySet();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : heap.entrySet()) {
            sb.append(entry.getKey()).append(" --> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}