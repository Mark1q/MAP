package view;

import controller.Controller;
import exception.MyException;
import model.PrgState;
import model.adt.*;
import model.expression.ArithExp;
import model.expression.ValueExp;
import model.expression.VarExp;
import model.statement.*;
import model.type.BoolType;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.Value;
import repository.IRepository;
import repository.Repository;

import java.util.Scanner;

public class TextMenu {
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Toy Language Interpreter ===");
            System.out.println("1. Example 1: int v; v=2; Print(v)");
            System.out.println("2. Example 2: int a; int b; a=2+3*5; b=a+1; Print(b)");
            System.out.println("3. Example 3: bool a; int v; a=true; If a Then v=2 Else v=3; Print(v)");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            if (choice == 0) break;

            IStmt program = null;
            switch (choice) {
                case 1:
                    program = createExample1();
                    break;
                case 2:
                    program = createExample2();
                    break;
                case 3:
                    program = createExample3();
                    break;
                default:
                    System.out.println("Invalid choice!");
                    continue;
            }

            executeProgram(program);
        }

        scanner.close();
    }

    private IStmt createExample1() {
        return new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                )
        );
    }

    private IStmt createExample2() {
        return new CompStmt(
                new VarDeclStmt("a", new IntType()),
                new CompStmt(
                        new VarDeclStmt("b", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ArithExp('+', new ValueExp(new IntValue(2)),
                                        new ArithExp('*', new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                                new CompStmt(
                                        new AssignStmt("b", new ArithExp('+', new VarExp("a"), new ValueExp(new IntValue(1)))),
                                        new PrintStmt(new VarExp("b"))
                                )
                        )
                )
        );
    }

    // faci vineri la 4 ora nu marti prost

    private IStmt createExample3() {
        return new CompStmt(
                new VarDeclStmt("a", new BoolType()),
                new CompStmt(
                        new VarDeclStmt("v", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(
                                        new IfStmt(new VarExp("a"),
                                                new AssignStmt("v", new ValueExp(new IntValue(2))),
                                                new AssignStmt("v", new ValueExp(new IntValue(3)))),
                                        new PrintStmt(new VarExp("v"))
                                )
                        )
                )
        );
    }

    private void executeProgram(IStmt program) {
        try {
            MyIStack<IStmt> stack = new MyStack<>();
            MyIDictionary<String, Value> symTable = new MyDictionary<>();
            MyIList<Value> out = new MyList<>();

            PrgState prgState = new PrgState(stack, symTable, out, program);
            IRepository repo = new Repository(prgState);
            Controller ctrl = new Controller(repo, true);

            ctrl.allStep();

        } catch (MyException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        TextMenu menu = new TextMenu();
        menu.run();
    }
}
