package model.expression;

import model.value.Value;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.type.Type;
import exception.MyException;

public interface Exp {
    Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> heap) throws MyException;
    Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException;
}