package model;

import model.adt.MyIStack;
import model.adt.MyIDictionary;
import model.adt.MyIList;
import model.statement.IStmt;
import model.value.Value;

public class PrgState {
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, Value> symTable;
    private MyIList<Value> out;

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, Value> symtbl,
                    MyIList<Value> out, IStmt prg) {
        this.exeStack = stk;
        this.symTable = symtbl;
        this.out = out;
        stk.push(prg);  // Start with the whole program on the stack
    }

    // Getters
    public MyIStack<IStmt> getStk() {
        return exeStack;
    }

    public MyIDictionary<String, Value> getSymTable() {
        return symTable;
    }

    public MyIList<Value> getOut() {
        return out;
    }

    @Override
    public String toString() {
        return "ExeStack:\n" + exeStack.toString() +
                "\nSymTable:\n" + symTable.toString() +
                "\nOut:\n" + out.toString() +
                "\n========================================\n";
    }
}
