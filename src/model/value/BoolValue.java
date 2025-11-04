package model.value;

import model.type.Type;
import model.type.BoolType;

public class BoolValue implements Value{
    private boolean val;

    public BoolValue(boolean v) { val = v; }
    public boolean getVal() { return val; }
    public Type getType() { return new BoolType(); }
    public String toString() { return String.valueOf(val); }
}
