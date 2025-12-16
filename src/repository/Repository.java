package repository;

import model.PrgState;
import exception.MyException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private List<PrgState> prgStates;
    private String logFilePath;

    public Repository(PrgState prg, String logFilePath) {
        this.prgStates = new ArrayList<>();
        this.prgStates.add(prg);
        this.logFilePath = logFilePath;
    }

    public List<PrgState> getPrgList() {
        return prgStates;
    }

    public void setPrgList(List<PrgState> prgList) {
        this.prgStates = prgList;
    }

    public void logPrgStateExec(PrgState state) throws MyException {
        try {
            PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
            logFile.println(state.toLogString());
            logFile.close();
        } catch (IOException e) {
            throw new MyException("Error writing to log file: " + e.getMessage());
        }
    }
}