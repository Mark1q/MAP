package model.statement;

import model.PrgState;
import model.adt.MyIDictionary;
import model.expression.Exp;
import model.type.RefType;
import model.type.Type;
import model.value.Value;
import model.value.RefValue;
import exception.MyException;

public class WriteHeapStmt implements IStmt {
    private String varName;
    private Exp expression;

    public WriteHeapStmt(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!state.getSymTable().isDefined(varName))
            throw new MyException("Variable " + varName + " is not defined");

        Value varValue = state.getSymTable().lookup(varName);
        if (!(varValue instanceof RefValue))
            throw new MyException("Variable " + varName + " is not of RefType");

        RefValue refValue = (RefValue) varValue;
        int address = refValue.getAddr();

        if (!state.getHeap().isDefined(address))
            throw new MyException("Address " + address + " is not defined in heap");

        Value expValue = expression.eval(state.getSymTable(), state.getHeap());

        if (!expValue.getType().equals(refValue.getLocationType()))
            throw new MyException("Expression type does not match the location type");

        state.getHeap().update(address, expValue);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(varName);
        Type typexp = expression.typecheck(typeEnv);
        if (typevar instanceof RefType) {
            RefType refType = (RefType) typevar;
            if (refType.getInner().equals(typexp))
                return typeEnv;
            else
                throw new MyException("WriteHeap: expression type does not match the referenced type");
        } else
            throw new MyException("WriteHeap: variable is not of RefType");
    }

    @Override
    public String toString() {
        return "wH(" + varName + "," + expression.toString() + ")";
    }
}