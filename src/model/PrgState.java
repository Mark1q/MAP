package model;

import exception.MyException;
import model.adt.MyIStack;
import model.adt.MyIDictionary;
import model.adt.MyIList;
import model.adt.MyIFileTable;
import model.adt.MyIHeap;
import model.statement.IStmt;
import model.value.Value;
import model.value.StringValue;
import java.io.BufferedReader;

public class PrgState {
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, Value> symTable;
    private MyIList<Value> out;
    private MyIFileTable<StringValue, BufferedReader> fileTable;
    private MyIHeap<Integer, Value> heap;
    private int id;
    private static int nextId = 1;  // Static counter

    // Synchronized method to generate unique IDs
    public static synchronized int generateId() {
        return nextId++;
    }

    // Regular constructor
    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, Value> symtbl,
                    MyIList<Value> out, MyIFileTable<StringValue, BufferedReader> fileTable,
                    MyIHeap<Integer, Value> heap, IStmt prg) {
        this.exeStack = stk;
        this.symTable = symtbl;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.id = generateId();
        stk.push(prg);
    }

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, Value> symtbl,
                    MyIList<Value> out, MyIFileTable<StringValue, BufferedReader> fileTable,
                    MyIHeap<Integer, Value> heap, int id) {
        this.exeStack = stk;
        this.symTable = symtbl;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.id = id;
    }

    public boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    public PrgState oneStep() throws MyException {
        if (exeStack.isEmpty())
            throw new MyException("Program stack is empty");

        IStmt crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }

    public int getId() { return id; }
    public MyIStack<IStmt> getStk() { return exeStack; }
    public MyIDictionary<String, Value> getSymTable() { return symTable; }
    public MyIList<Value> getOut() { return out; }
    public MyIFileTable<StringValue, BufferedReader> getFileTable() { return fileTable; }
    public MyIHeap<Integer, Value> getHeap() { return heap; }

    @Override
    public String toString() {
        return "Thread ID: " + id + "\n" +
                "ExeStack:\n" + exeStack.toString() +
                "\nSymTable:\n" + symTable.toString() +
                "\nOut:\n" + out.toString() +
                "\nFileTable:\n" + fileTable.toString() +
                "\nHeap:\n" + heap.toString() +
                "\n========================================\n";
    }

    public String toLogString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ID: ").append(id).append("\n");
        sb.append("ExeStack:\n").append(exeStack.toString());
        sb.append("SymTable:\n").append(symTable.toString());
        sb.append("Out:\n").append(out.toString());
        sb.append("FileTable:\n").append(fileTable.toString());
        sb.append("Heap:\n").append(heap.toString());
        sb.append("========================================\n");
        return sb.toString();
    }
}