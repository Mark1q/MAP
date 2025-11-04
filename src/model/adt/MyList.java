package model.adt;

import java.util.ArrayList;

public class MyList<T> implements MyIList<T> {
    private ArrayList<T> list;

    public MyList() { list = new ArrayList<>(); }

    public void add(T elem) { list.add(elem); }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (T elem : list) {
            sb.append(elem.toString()).append("\n");
        }
        return sb.toString();
    }
}