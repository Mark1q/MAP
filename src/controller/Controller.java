package controller;

import exception.MyException;
import model.PrgState;
import model.statement.IStmt;
import model.value.RefValue;
import model.value.Value;
import repository.IRepository;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Controller {
    private IRepository repo;
    private ExecutorService executor;

    public Controller(IRepository repo) {
        this.repo = repo;
    }

    private Map<Integer, Value> safeGarbageCollector(List<Map<String, Value>> symTables, Map<Integer, Value> heap) {
        Set<Integer> reachableAddresses = new HashSet<>();

        for (Map<String, Value> symTable : symTables) {
            symTable.values().stream()
                    .filter(v -> v instanceof RefValue)
                    .map(v -> ((RefValue) v).getAddr())
                    .forEach(reachableAddresses::add);
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            List<Integer> newAddresses = heap.entrySet().stream()
                    .filter(e -> reachableAddresses.contains(e.getKey()))
                    .map(Map.Entry::getValue)
                    .filter(v -> v instanceof RefValue)
                    .map(v -> ((RefValue) v).getAddr())
                    .filter(addr -> !reachableAddresses.contains(addr))
                    .toList();

            if (!newAddresses.isEmpty()) {
                reachableAddresses.addAll(newAddresses);
                changed = true;
            }
        }

        return heap.entrySet().stream()
                .filter(e -> reachableAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<Map<String, Value>> getAllSymTables(List<PrgState> prgList) {
        return prgList.stream()
                .map(p -> p.getSymTable().getContent())
                .collect(Collectors.toList());
    }


    public void oneStepForAllPrg(List<PrgState> allPrgList) throws MyException {
        List<PrgState> execPrgList = allPrgList.stream()
                .filter(PrgState::isNotCompleted)
                .toList();

        // 2. Log ONLY executable program states before execution
//        for (PrgState p : execPrgList) {
//            repo.logPrgStateExec(p);
//        }

        List<Callable<PrgState>> callList = execPrgList.stream()
                .map((PrgState p) -> (Callable<PrgState>)(p::oneStep))
                .collect(Collectors.toList());

        try {
            List<Future<PrgState>> futures = executor.invokeAll(callList);

            List<PrgState> newPrgList = new ArrayList<>();

            for (Future<PrgState> future : futures) {
                try {
                    PrgState result = future.get();
                    if (result != null) {
                        newPrgList.add(result);
                    }
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof MyException) {
                        throw (MyException) cause;
                    }
                    throw new MyException("Execution error: " + e.getMessage());
                }
            }

            allPrgList.addAll(newPrgList);

            for (PrgState p : execPrgList) {
                repo.logPrgStateExec(p);
            }

            repo.setPrgList(allPrgList);

        } catch (InterruptedException e) {
            throw new MyException("Thread execution interrupted: " + e.getMessage());
        }
    }


    public void allStep() throws MyException {
        executor = Executors.newFixedThreadPool(2);

        try {
            List<PrgState> allPrgList = repo.getPrgList();

            List<PrgState> execPrgList = allPrgList.stream()
                    .filter(PrgState::isNotCompleted)
                    .collect(Collectors.toList());

            while (!execPrgList.isEmpty()) {
                if (!allPrgList.isEmpty() && allPrgList.getFirst().getHeap() != null) {
                    List<Map<String, Value>> allSymTables = getAllSymTables(allPrgList);
                    Map<Integer, Value> heapContent = allPrgList.getFirst().getHeap().getContent();
                    Map<Integer, Value> newHeap = safeGarbageCollector(allSymTables, heapContent);

                    for (PrgState p : allPrgList) {
                        p.getHeap().setContent(new HashMap<>(newHeap));
                    }
                }

                oneStepForAllPrg(allPrgList);

                allPrgList = repo.getPrgList();
                execPrgList = allPrgList.stream()
                        .filter(PrgState::isNotCompleted)
                        .toList();
            }

            System.out.println("\n=== FINAL STATE ===");
            for (PrgState p : repo.getPrgList()) {
                System.out.println("Thread ID: " + p.getId());
                System.out.println("SymTable: " + p.getSymTable());
            }

            if (!repo.getPrgList().isEmpty()) {
                System.out.println("Heap: " + repo.getPrgList().getFirst().getHeap());
                System.out.println("Out: " + repo.getPrgList().getFirst().getOut());
            }

        } finally {
            executor.shutdownNow();
            try {
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}