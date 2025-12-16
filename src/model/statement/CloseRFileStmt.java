package model.statement;

import model.PrgState;
import model.adt.MyIDictionary;
import model.expression.Exp;
import model.value.Value;
import model.value.StringValue;
import model.type.StringType;
import model.type.Type;
import exception.MyException;
import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFileStmt implements IStmt {
    private Exp exp;

    public CloseRFileStmt(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        Value val = exp.eval(state.getSymTable(), state.getHeap());

        if (!val.getType().equals(new StringType()))
            throw new MyException("Expression is not a string");

        StringValue strVal = (StringValue) val;
        BufferedReader reader = state.getFileTable().lookup(strVal);

        try {
            reader.close();
            state.getFileTable().remove(strVal);
        } catch (IOException e) {
            throw new MyException("Error closing file: " + e.getMessage());
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ = exp.typecheck(typeEnv);
        if (typ.equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("CloseRFile: expression is not a string");
    }
}