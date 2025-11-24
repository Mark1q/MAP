package model.expression;

import model.value.Value;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import exception.MyException;

public class ValueExp implements Exp {
    private Value e;

    public ValueExp(Value e) {
        this.e = e;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> heap) throws MyException {
        return e;
    }

    @Override
    public String toString() {
        return e.toString();
    }
}