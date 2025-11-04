package model.value;
import model.type.Type;
import model.type.IntType;

public class IntValue implements Value{
    private int val;

    public IntValue(int v) { val = v; }
    public int getVal() { return val; }
    public Type getType() { return new IntType(); }
    public String toString() { return String.valueOf(val); }
}
