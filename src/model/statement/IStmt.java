package model.statement;

import model.PrgState;
import model.adt.MyIDictionary;
import model.type.Type;
import exception.MyException;

public interface IStmt {
    PrgState execute(PrgState state) throws MyException;
    MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException;
}