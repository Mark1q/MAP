package model.adt;

import exception.MyException;
import javafx.util.Pair;
import java.util.List;
import java.util.Map;

public interface MyISemaphoreTable {
    int allocate(int value);
    Pair<Integer, List<Integer>> get(int address) throws MyException;
    void update(int address, Pair<Integer, List<Integer>> value) throws MyException;
    boolean isDefined(int address);
    void setContent(Map<Integer, Pair<Integer, List<Integer>>> newContent);
    Map<Integer, Pair<Integer, List<Integer>>> getContent();
    String toString();
}