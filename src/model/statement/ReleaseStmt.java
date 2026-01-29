package model.statement;

import exception.MyException;
import javafx.util.Pair;
import model.PrgState;
import model.adt.MyIDictionary;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;

import java.util.ArrayList;
import java.util.List;

public class ReleaseStmt implements IStmt {
    private String varName;

    public ReleaseStmt(String varName) {
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        // Check if variable is defined and is int type
        if (!state.getSymTable().isDefined(varName))
            throw new MyException("Variable " + varName + " is not defined");

        Value varValue = state.getSymTable().lookup(varName);
        if (!varValue.getType().equals(new IntType()))
            throw new MyException("Variable " + varName + " is not of type int");

        IntValue intValue = (IntValue) varValue;
        int foundIndex = intValue.getVal();

        // Check if the index exists in semaphore table
        if (!state.getSemaphoreTable().isDefined(foundIndex))
            throw new MyException("Index " + foundIndex + " is not in the semaphore table");

        // Get the semaphore entry
        Pair<Integer, List<Integer>> semaphoreEntry = state.getSemaphoreTable().get(foundIndex);
        int N1 = semaphoreEntry.getKey();
        List<Integer> List1 = semaphoreEntry.getValue();

        // Check if current thread ID is in the list
        if (List1.contains(state.getId())) {
            // Remove the current thread ID from the list
            List<Integer> newList = new ArrayList<>(List1);
            newList.remove(Integer.valueOf(state.getId()));
            state.getSemaphoreTable().update(foundIndex, new Pair<>(N1, newList));
        }
        // If not in list, do nothing

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(varName);

        if (!typevar.equals(new IntType()))
            throw new MyException("Release: variable is not of type int");

        return typeEnv;
    }

    @Override
    public String toString() {
        return "release(" + varName + ")";
    }
}