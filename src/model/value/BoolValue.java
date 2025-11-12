package model.value;

import model.type.Type;
import model.type.BoolType;

public class BoolValue implements Value{
    private boolean val;

    public BoolValue(boolean v) { val = v; }
    public boolean getVal() { return val; }
    public Type getType() { return new BoolType(); }
    public String toString() { return String.valueOf(val); }

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof BoolValue))
            return false;
        return ((BoolValue) another).getVal() == this.val;
    }
}