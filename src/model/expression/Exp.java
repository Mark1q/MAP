package model.expression;

import model.value.Value;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import exception.MyException;

public interface Exp {
    Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> heap) throws MyException;
}