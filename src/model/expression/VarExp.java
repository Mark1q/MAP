package model.expression;

import model.value.Value;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import exception.MyException;

public class VarExp implements Exp {
    private String id;

    public VarExp(String id) {
        this.id = id;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> heap) throws MyException {
        return tbl.lookup(id);
    }

    @Override
    public String toString() {
        return id;
    }
}