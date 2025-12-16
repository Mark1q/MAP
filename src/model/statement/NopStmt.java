package model.statement;

import exception.MyException;
import model.PrgState;

public class NopStmt implements IStmt {
    public String toString() { return "nop"; }

    public PrgState execute(PrgState state) throws MyException {
        return null;
    }
}
