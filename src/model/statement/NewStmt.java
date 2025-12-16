package model.statement;

import model.PrgState;
import model.adt.MyIDictionary;
import model.expression.Exp;
import model.type.RefType;
import model.type.Type;
import model.value.Value;
import model.value.RefValue;
import exception.MyException;

public class NewStmt implements IStmt {
    private String varName;
    private Exp expression;

    public NewStmt(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!state.getSymTable().isDefined(varName))
            throw new MyException("Variable " + varName + " is not defined");

        Value varValue = state.getSymTable().lookup(varName);
        if (!(varValue.getType() instanceof RefType))
            throw new MyException("Variable " + varName + " is not of RefType");

        Value expValue = expression.eval(state.getSymTable(), state.getHeap());
        RefValue refValue = (RefValue) varValue;

        if (!expValue.getType().equals(refValue.getLocationType()))
            throw new MyException("Expression type does not match the location type of " + varName);

        int newAddress = state.getHeap().allocate(expValue);
        state.getSymTable().update(varName, new RefValue(newAddress, refValue.getLocationType()));

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(varName);
        Type typexp = expression.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp)))
            return typeEnv;
        else
            throw new MyException("NEW stmt: right hand side and left hand side have different types");
    }

    @Override
    public String toString() {
        return "new(" + varName + "," + expression.toString() + ")";
    }
}