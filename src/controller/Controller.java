package controller;

import exception.MyException;
import model.PrgState;
import model.adt.MyIStack;
import model.statement.IStmt;
import repository.IRepository;

public class Controller {
    private IRepository repo;
    private boolean displayFlag;

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    public void oneStep(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getStk();
        if (stk.isEmpty())
            throw new MyException("Program stack is empty");

        IStmt crtStmt = stk.pop();
        crtStmt.execute(state);
    }

    public void allStep() throws MyException {
        PrgState prg = repo.getCrtPrg();

        if (displayFlag)
            System.out.println(prg.toString());

        while (!prg.getStk().isEmpty()) {
            oneStep(prg);
            if (displayFlag)
                System.out.println(prg.toString());
        }

        System.out.println("Final Output:");
        System.out.println(prg.getOut().toString());
    }
}
