package model.expression;

import model.value.Value;
import model.adt.MyIDictionary;
import exception.MyException;

public interface Exp {
    Value eval(MyIDictionary<String, Value> tbl) throws MyException;
}
