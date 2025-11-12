package model.type;
import model.value.StringValue;
import model.value.Value;

public class StringType implements Type {
    public boolean equals(Object another) {
        return another instanceof StringType;
    }

    public String toString() { return "string"; }

    public Value defaultValue() {
        return new StringValue("");
    }
}