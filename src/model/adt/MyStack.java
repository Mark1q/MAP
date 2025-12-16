package model.adt;

import java.util.Stack;
import exception.MyException;

public class MyStack<T> implements MyIStack<T> {
    private Stack<T> stack;

    public MyStack() { stack = new Stack<>(); }

    // NEW CONSTRUCTOR for creating empty stack
    private MyStack(Stack<T> stack) {
        this.stack = stack;
    }

    public T pop() throws MyException {
        if (stack.isEmpty())
            throw new MyException("Stack is empty");
        return stack.pop();
    }

    public void push(T v) { stack.push(v); }
    public boolean isEmpty() { return stack.isEmpty(); }

    public MyIStack<T> createEmpty() {
        return new MyStack<T>(new Stack<T>());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = stack.size() - 1; i >= 0; i--) {
            sb.append(stack.get(i).toString()).append("\n");
        }
        return sb.toString();
    }
}