package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIStack;
import model.adt.MyIDictionary;
import model.expression.Exp;
import model.type.BoolType;
import model.type.Type;
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

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = exp.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            thenS.typecheck(typeEnv.deepCopy());
            elseS.typecheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new MyException("The condition of IF has not the type bool");
    }
}