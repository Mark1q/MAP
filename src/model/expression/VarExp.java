package model.expression;

import model.value.Value;
import model.adt.MyIDictionary;
import exception.MyException;

public class VarExp implements Exp {
    private String id;

    public VarExp(String id) { this.id = id; }

    public Value eval(MyIDictionary<String, Value> tbl) throws MyException {
        return tbl.lookup(id);
    }

    public String toString() { return id; }
}