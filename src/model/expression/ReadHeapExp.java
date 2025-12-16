package model.expression;

import model.value.Value;
import model.value.RefValue;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.type.Type;
import model.type.RefType;
import exception.MyException;

public class ReadHeapExp implements Exp {
    private Exp expression;

    public ReadHeapExp(Exp expression) {
        this.expression = expression;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> heap) throws MyException {
        Value value = expression.eval(tbl, heap);

        if (!(value instanceof RefValue))
            throw new MyException("Expression does not evaluate to a RefValue");

        RefValue refValue = (RefValue) value;
        int address = refValue.getAddr();

        if (!heap.isDefined(address))
            throw new MyException("Address " + address + " is not defined in heap");

        return heap.get(address);
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ = expression.typecheck(typeEnv);
        if (typ instanceof RefType) {
            RefType reft = (RefType) typ;
            return reft.getInner();
        } else
            throw new MyException("The rH argument is not a Ref Type");
    }

    @Override
    public String toString() {
        return "rH(" + expression.toString() + ")";
    }
}