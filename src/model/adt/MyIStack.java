package model.adt;
import exception.MyException;

public interface MyIStack<T> {
    T pop() throws MyException;
    void push(T v);
    boolean isEmpty();
    String toString();
}
