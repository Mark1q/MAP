package model.type;
import model.value.Value;

public interface Type {
    boolean equals(Object another);
    Value defaultValue();
}
