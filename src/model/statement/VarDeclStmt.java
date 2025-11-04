package model.statement;

import model.PrgState;
import exception.MyException;
import model.type.BoolType;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.Value;
import model.adt.MyIDictionary;
import model.type.Type;

public class VarDeclStmt implements IStmt {
    private String name;
    private Type typ;

    public VarDeclStmt(String name, Type typ) {
        this.name = name;
        this.typ = typ;
    }

    public String toString() { return typ.toString() + " " + name; }

    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl = state.getSymTable();

        if (symTbl.isDefined(name))
            throw new MyException("Variable " + name + " is already declared");

        if (typ.equals(new IntType()))
            symTbl.put(name, new IntValue(0));
        else if (typ.equals(new BoolType()))
            symTbl.put(name, new BoolValue(false));

        return state;
    }
}
