package model.statement;

import model.PrgState;
import model.expression.Exp;
import model.value.Value;
import model.value.StringValue;
import model.value.IntValue;
import model.type.StringType;
import model.type.IntType;
import exception.MyException;
import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStmt implements IStmt {
    private Exp exp;
    private String varName;

    public ReadFileStmt(Exp exp, String varName) {
        this.exp = exp;
        this.varName = varName;
    }

    @Override
    public String toString() {
        return "readFile(" + exp.toString() + "," + varName + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!state.getSymTable().isDefined(varName))
            throw new MyException("Variable " + varName + " is not defined");

        Value varVal = state.getSymTable().lookup(varName);
        if (!varVal.getType().equals(new IntType()))
            throw new MyException("Variable " + varName + " is not of type int");

        Value val = exp.eval(state.getSymTable(), state.getHeap());
        if (!val.getType().equals(new StringType()))
            throw new MyException("Expression is not a string");

        StringValue strVal = (StringValue) val;
        BufferedReader reader = state.getFileTable().lookup(strVal);

        try {
            String line = reader.readLine();
            IntValue intVal;
            if (line == null)
                intVal = new IntValue(0);
            else
                intVal = new IntValue(Integer.parseInt(line));

            state.getSymTable().update(varName, intVal);
        } catch (IOException e) {
            throw new MyException("Error reading file: " + e.getMessage());
        }

        return state;
    }
}