package model.statement;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expression.Exp;
import model.expression.RelationalExp;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;

public class SwitchStmt implements IStmt{
    public Exp exp, exp1, exp2;
    public IStmt stmt1, stmt2, stmt3;

    public SwitchStmt(Exp exp, Exp exp1, Exp exp2, IStmt stmt1, IStmt stmt2, IStmt stmt3) {
        this.exp = exp;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;
        this.stmt3 = stmt3;
    }

    public String toString() {
        return "switch(" + exp.toString() + ")(case " + exp1.toString() + " : "
                + stmt1.toString() + ")(case " + exp2.toString() + " : " + stmt2.toString()
                + ")(default : " + stmt3.toString();
    }

    public PrgState execute(PrgState state) throws MyException {
        state.getStk().push(
                new IfStmt(new RelationalExp("==", exp, exp1), stmt1, new IfStmt(
                        new RelationalExp("==", exp, exp2), stmt2, stmt3
                ))
        );

        return null;
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = exp.typecheck(typeEnv);
        Type typexp1 = exp1.typecheck(typeEnv);
        Type typexp2 = exp2.typecheck(typeEnv);

        if (!(typexp.equals(typexp1) && typexp.equals(typexp2) && typexp1.equals(typexp2))) {
            throw new MyException("Types don't match between expressions");
        } else {
            stmt1.typecheck(typeEnv.deepCopy());
            stmt2.typecheck(typeEnv.deepCopy());
            stmt3.typecheck(typeEnv.deepCopy());

            return typeEnv;
        }

    }
}
