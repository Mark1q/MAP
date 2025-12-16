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
import java.io.FileReader;
import java.io.IOException;

public class OpenRFileStmt implements IStmt {
    private Exp exp;

    public OpenRFileStmt(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "openRFile(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        Value val = exp.eval(state.getSymTable(), state.getHeap());

        if (!val.getType().equals(new StringType()))
            throw new MyException("Expression is not a string");

        StringValue strVal = (StringValue) val;
        String filename = strVal.getVal();

        if (state.getFileTable().isDefined(strVal))
            throw new MyException("File " + filename + " is already opened");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            state.getFileTable().put(strVal, reader);
        } catch (IOException e) {
            throw new MyException("Error opening file: " + e.getMessage());
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ = exp.typecheck(typeEnv);
        if (typ.equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("OpenRFile: expression is not a string");
    }
}