package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expression.Exp;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;

public class CreateSemaphoreStmt implements IStmt {
    private String varName;
    private Exp expression;

    public CreateSemaphoreStmt(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        // Check if variable is defined and is int type
        if (!state.getSymTable().isDefined(varName))
            throw new MyException("Variable " + varName + " is not defined");

        Value varValue = state.getSymTable().lookup(varName);
        if (!varValue.getType().equals(new IntType()))
            throw new MyException("Variable " + varName + " is not of type int");

        // Evaluate the expression
        Value expValue = expression.eval(state.getSymTable(), state.getHeap());
        if (!expValue.getType().equals(new IntType()))
            throw new MyException("Expression does not evaluate to int");

        IntValue intValue = (IntValue) expValue;
        int number1 = intValue.getVal();

        // Allocate a new semaphore in the table
        int newLocation = state.getSemaphoreTable().allocate(number1);

        // Update the variable with the new location
        state.getSymTable().update(varName, new IntValue(newLocation));

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(varName);
        Type typexp = expression.typecheck(typeEnv);

        if (!typevar.equals(new IntType()))
            throw new MyException("CreateSemaphore: variable is not of type int");

        if (!typexp.equals(new IntType()))
            throw new MyException("CreateSemaphore: expression is not of type int");

        return typeEnv;
    }

    @Override
    public String toString() {
        return "createSemaphore(" + varName + ", " + expression.toString() + ")";
    }
}