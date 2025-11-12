package model.expression;
import model.value.Value;
import model.value.IntValue;
import model.value.BoolValue;
import model.adt.MyIDictionary;
import model.type.IntType;
import exception.MyException;

public class RelationalExp implements Exp {
    private Exp e1;
    private Exp e2;
    private String op; // "<", "<=", "==", "!=", ">", ">="

    public RelationalExp(String op, Exp e1, Exp e2) {
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    public Value eval(MyIDictionary<String, Value> tbl) throws MyException {
        Value v1 = e1.eval(tbl);
        if (!v1.getType().equals(new IntType()))
            throw new MyException("First operand is not an integer");

        Value v2 = e2.eval(tbl);
        if (!v2.getType().equals(new IntType()))
            throw new MyException("Second operand is not an integer");

        IntValue i1 = (IntValue) v1;
        IntValue i2 = (IntValue) v2;
        int n1 = i1.getVal();
        int n2 = i2.getVal();

        switch (op) {
            case "<": return new BoolValue(n1 < n2);
            case "<=": return new BoolValue(n1 <= n2);
            case "==": return new BoolValue(n1 == n2);
            case "!=": return new BoolValue(n1 != n2);
            case ">": return new BoolValue(n1 > n2);
            case ">=": return new BoolValue(n1 >= n2);
            default: throw new MyException("Invalid relational operator");
        }
    }

    public String toString() {
        return e1.toString() + " " + op + " " + e2.toString();
    }
}