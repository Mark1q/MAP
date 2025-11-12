package model.value;
import model.type.Type;
import model.type.StringType;

public class StringValue implements Value {
    private String val;

    public StringValue(String v) { val = v; }

    public String getVal() { return val; }

    public Type getType() { return new StringType(); }

    public String toString() { return val; }

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof StringValue))
            return false;
        return ((StringValue) another).getVal().equals(this.val);
    }
}