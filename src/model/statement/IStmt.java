package model.statement;

import model.PrgState;
import exception.MyException;

public interface IStmt {
    PrgState execute(PrgState state) throws MyException;
}
