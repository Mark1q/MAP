package model.expression;

import model.value.Value;
import model.adt.MyIDictionary;
import exception.MyException;

public class ValueExp implements Exp {
    private Value e;

    public ValueExp(Value e) { this.e = e; }

    public Value eval(MyIDictionary<String, Value> tbl) throws MyException {
        return e;
    }

    public String toString() { return e.toString(); }
}
