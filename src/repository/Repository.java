package repository;

import model.PrgState;

public class Repository implements IRepository {
    private PrgState prgState;

    public Repository(PrgState prg) { this.prgState = prg; }

    public PrgState getCrtPrg() { return prgState; }
}
