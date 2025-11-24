package controller;

import exception.MyException;
import model.PrgState;
import model.adt.MyIStack;
import model.statement.IStmt;
import model.value.RefValue;
import model.value.Value;
import repository.IRepository;

import java.util.*;
import java.util.stream.Collectors;

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

    // Safe garbage collector that considers heap references
    private Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddr, Map<Integer, Value> heap) {
        // Get all reachable addresses (from symTable and heap)
        Set<Integer> reachableAddresses = new HashSet<>(symTableAddr);
        boolean changed = true;

        // Keep adding addresses referenced from heap until no new addresses are found
        while (changed) {
            changed = false;
            List<Integer> newAddresses = heap.entrySet().stream()
                    .filter(e -> reachableAddresses.contains(e.getKey()))
                    .map(Map.Entry::getValue)
                    .filter(v -> v instanceof RefValue)
                    .map(v -> ((RefValue) v).getAddr())
                    .filter(addr -> !reachableAddresses.contains(addr))
                    .collect(Collectors.toList());

            if (!newAddresses.isEmpty()) {
                reachableAddresses.addAll(newAddresses);
                changed = true;
            }
        }

        // Keep only reachable addresses
        return heap.entrySet().stream()
                .filter(e -> reachableAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return v1.getAddr();
                })
                .collect(Collectors.toList());
    }

    public void allStep() throws MyException {
        PrgState prg = repo.getCrtPrg();
        repo.logPrgStateExec();

        if (displayFlag)
            System.out.println(prg.toString());

        while (!prg.getStk().isEmpty()) {
            oneStep(prg);
            prg.getHeap().setContent(safeGarbageCollector(
                    getAddrFromSymTable(prg.getSymTable().getContent().values()),
                    prg.getHeap().getContent()));
            repo.logPrgStateExec();
            if (displayFlag)
                System.out.println(prg.toString());
        }

        System.out.println("Final Output:");
        System.out.println(prg.getOut().toString());
    }
}