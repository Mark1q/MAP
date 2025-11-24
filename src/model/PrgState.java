package model;

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

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, Value> symtbl,
                    MyIList<Value> out, MyIFileTable<StringValue, BufferedReader> fileTable,
                    MyIHeap<Integer, Value> heap, IStmt prg) {
        this.exeStack = stk;
        this.symTable = symtbl;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        stk.push(prg);
    }

    public MyIStack<IStmt> getStk() {
        return exeStack;
    }

    public MyIDictionary<String, Value> getSymTable() {
        return symTable;
    }

    public MyIList<Value> getOut() {
        return out;
    }

    public MyIFileTable<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public MyIHeap<Integer, Value> getHeap() {
        return heap;
    }

    @Override
    public String toString() {
        return "ExeStack:\n" + exeStack.toString() +
                "\nSymTable:\n" + symTable.toString() +
                "\nOut:\n" + out.toString() +
                "\nFileTable:\n" + fileTable.toString() +
                "\nHeap:\n" + heap.toString() +
                "\n========================================\n";
    }

    public String toLogString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ExeStack:\n");
        sb.append(exeStack.toString());

        sb.append("SymTable:\n");
        sb.append(symTable.toString());

        sb.append("Out:\n");
        sb.append(out.toString());

        sb.append("FileTable:\n");
        sb.append(fileTable.toString());

        sb.append("Heap:\n");
        sb.append(heap.toString());

        sb.append("========================================\n");

        return sb.toString();
    }
}