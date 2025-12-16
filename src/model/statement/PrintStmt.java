package model.statement;

import model.expression.Exp;
import model.PrgState;
import model.value.Value;
import exception.MyException;
import model.adt.MyIList;

public class PrintStmt implements IStmt {
    private Exp exp;

    public PrintStmt(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "print(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIList<Value> out = state.getOut();
        Value val = exp.eval(state.getSymTable(), state.getHeap());
        out.add(val);
        return null;
    }
}