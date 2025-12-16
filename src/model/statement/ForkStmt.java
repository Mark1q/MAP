package model.statement;

import model.PrgState;
import model.adt.MyIStack;
import model.adt.MyIDictionary;
import model.type.Type;
import exception.MyException;

public class ForkStmt implements IStmt {
    private IStmt statement;

    public ForkStmt(IStmt statement) {
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, model.value.Value> clonedSymTable = state.getSymTable().deepCopy();

        MyIStack<IStmt> newStack = state.getStk().createEmpty();
        newStack.push(statement);

        PrgState childState = new PrgState(
                newStack,
                clonedSymTable,
                state.getOut(),
                state.getFileTable(),
                state.getHeap(),
                PrgState.generateId()
        );

        return childState;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        statement.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "fork(" + statement.toString() + ")";
    }
}