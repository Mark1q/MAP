package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIStack;
import model.adt.MyIDictionary;
import model.type.Type;

public class CompStmt implements IStmt {
    private IStmt first;
    private IStmt snd;

    public CompStmt(IStmt first, IStmt snd) {
        this.first = first;
        this.snd = snd;
    }

    public String toString() {
        return "(" + first.toString() + "; " + snd.toString() + ")";
    }

    public PrgState execute(PrgState state) throws MyException {
        state.getStk().push(snd);
        state.getStk().push(first);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return snd.typecheck(first.typecheck(typeEnv));
    }
}