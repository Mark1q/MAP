package model.expression;

import exception.MyException;
import model.value.Value;
import model.value.BoolValue;
import model.type.BoolType;
import model.adt.MyIDictionary;

public class LogicExp implements Exp {
    private Exp e1;
    private Exp e2;
    private String op; // "and", "or"

    public LogicExp(String op, Exp e1, Exp e2) {
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    public Value eval(MyIDictionary<String, Value> tbl) throws MyException {
        Value v1 = e1.eval(tbl);
        if (!v1.getType().equals(new BoolType()))
            throw new MyException("First operand is not a boolean");

        Value v2 = e2.eval(tbl);
        if (!v2.getType().equals(new BoolType()))
            throw new MyException("Second operand is not a boolean");

        BoolValue b1 = (BoolValue) v1;
        BoolValue b2 = (BoolValue) v2;
        boolean val1 = b1.getVal();
        boolean val2 = b2.getVal();

        if (op.equals("and"))
            return new BoolValue(val1 && val2);
        else if (op.equals("or"))
            return new BoolValue(val1 || val2);
        else
            throw new MyException("Invalid logical operator");
    }

    public String toString() {
        return e1.toString() + " " + op + " " + e2.toString();
    }
}
