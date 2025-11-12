package repository;

import model.PrgState;
import exception.MyException;

public interface IRepository {
    PrgState getCrtPrg();
    void logPrgStateExec() throws MyException;
}