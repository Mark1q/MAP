package model.adt;

import exception.MyException;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySemaphoreTable implements MyISemaphoreTable {
    private Map<Integer, Pair<Integer, List<Integer>>> semaphoreTable;
    private int freeLocation;

    public MySemaphoreTable() {
        this.semaphoreTable = new HashMap<>();
        this.freeLocation = 1;
    }

    @Override
    public synchronized int allocate(int value) {
        semaphoreTable.put(freeLocation, new Pair<>(value, new ArrayList<>()));
        freeLocation++;
        return freeLocation - 1;
    }

    @Override
    public synchronized Pair<Integer, List<Integer>> get(int address) throws MyException {
        if (!semaphoreTable.containsKey(address))
            throw new MyException("Semaphore index " + address + " is not defined");
        return semaphoreTable.get(address);
    }

    @Override
    public synchronized void update(int address, Pair<Integer, List<Integer>> value) throws MyException {
        if (!semaphoreTable.containsKey(address))
            throw new MyException("Semaphore index " + address + " is not defined");
        semaphoreTable.put(address, value);
    }

    @Override
    public synchronized boolean isDefined(int address) {
        return semaphoreTable.containsKey(address);
    }

    @Override
    public synchronized void setContent(Map<Integer, Pair<Integer, List<Integer>>> newContent) {
        this.semaphoreTable = newContent;
    }

    @Override
    public synchronized Map<Integer, Pair<Integer, List<Integer>>> getContent() {
        return semaphoreTable;
    }

    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Pair<Integer, List<Integer>>> entry : semaphoreTable.entrySet()) {
            sb.append(entry.getKey()).append(" -> (")
                    .append(entry.getValue().getKey()).append(", ")
                    .append(entry.getValue().getValue()).append(")\n");
        }
        return sb.toString();
    }
}