package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIStack;
import model.expression.Exp;
import model.type.BoolType;
import model.value.BoolValue;
import model.value.Value;

public class IfStmt implements IStmt {
    private Exp exp;
    private IStmt thenS, elseS;

    public IfStmt(Exp exp, IStmt thenS, IStmt elseS) {
        this.exp = exp;
        this.thenS = thenS;
        this.elseS = elseS;
    }

    @Override
    public String toString() {
        return "(IF(" + exp.toString() + ") THEN(" + thenS.toString() +
                ") ELSE(" + elseS.toString() + "))";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        Value condition = exp.eval(state.getSymTable(), state.getHeap());

        if (!condition.getType().equals(new BoolType()))
            throw new MyException("Condition is not a boolean");

        BoolValue boolCond = (BoolValue) condition;
        MyIStack<IStmt> stk = state.getStk();

        if (boolCond.getVal())
            stk.push(thenS);
        else
            stk.push(elseS);

        return state;
    }
}