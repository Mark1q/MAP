package repository;

import model.PrgState;
import exception.MyException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class Repository implements IRepository {
    private PrgState prgState;
    private String logFilePath;

    public Repository(PrgState prg, String logFilePath) {
        this.prgState = prg;
        this.logFilePath = logFilePath;
    }

    public PrgState getCrtPrg() {
        return prgState;
    }

    public void logPrgStateExec() throws MyException {
        try {
            PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
            logFile.println(prgState.toLogString());
            logFile.close();
        } catch (IOException e) {
            throw new MyException("Error writing to log file: " + e.getMessage());
        }
    }
}