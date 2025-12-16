package model.statement;

import model.PrgState;
import exception.MyException;
import model.type.BoolType;
import model.type.IntType;
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

        symTbl.put(name, typ.defaultValue());

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        typeEnv.put(name, typ);
        return typeEnv;
    }
}