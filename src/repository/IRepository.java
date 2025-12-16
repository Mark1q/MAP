package repository;

import model.PrgState;
import exception.MyException;
import java.util.List;

public interface IRepository {
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> prgList);
    void logPrgStateExec(PrgState state) throws MyException;
}