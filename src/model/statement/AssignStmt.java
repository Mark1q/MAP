package model.statement;

import model.expression.Exp;
import model.PrgState;
import exception.MyException;
import model.value.Value;
import model.adt.MyIStack;
import model.adt.MyIDictionary;
import model.type.Type;

public class AssignStmt implements IStmt {
    private String id;
    private Exp exp;

    public AssignStmt(String id, Exp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return id + " = " + exp.toString();
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getStk();
        MyIDictionary<String, Value> symTbl = state.getSymTable();

        if (!symTbl.isDefined(id))
            throw new MyException("Variable " + id + " was not declared");

        Value val = exp.eval(symTbl, state.getHeap());
        Type typId = symTbl.lookup(id).getType();

        if (!val.getType().equals(typId))
            throw new MyException("Type of variable " + id + " and assigned expression do not match");

        symTbl.update(id, val);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(id);
        Type typexp = exp.typecheck(typeEnv);
        if (typevar.equals(typexp))
            return typeEnv;
        else
            throw new MyException("Assignment: right hand side and left hand side have different types");
    }
}