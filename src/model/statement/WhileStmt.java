package model.statement;

import model.PrgState;
import model.adt.MyIDictionary;
import model.expression.Exp;
import model.type.BoolType;
import model.type.Type;
import model.value.Value;
import model.value.BoolValue;
import exception.MyException;

public class WhileStmt implements IStmt {
    private Exp expression;
    private IStmt statement;

    public WhileStmt(Exp expression, IStmt statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        Value value = expression.eval(state.getSymTable(), state.getHeap());

        if (!value.getType().equals(new BoolType()))
            throw new MyException("Condition expression is not a boolean");

        BoolValue boolValue = (BoolValue) value;

        if (boolValue.getVal()) {
            state.getStk().push(this);
            state.getStk().push(statement);
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = expression.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            statement.typecheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new MyException("The condition of WHILE has not the type bool");
    }

    @Override
    public String toString() {
        return "while(" + expression.toString() + ") " + statement.toString();
    }
}