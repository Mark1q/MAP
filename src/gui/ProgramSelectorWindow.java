package gui;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.PrgState;
import model.adt.*;
import model.statement.IStmt;
import model.type.Type;
import repository.IRepository;
import repository.Repository;
import exception.MyException;

import java.util.List;

public class ProgramSelectorWindow {
    private List<IStmt> programs;

    public ProgramSelectorWindow(List<IStmt> programs) {
        this.programs = programs;
    }

    public void show(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Create ListView with program descriptions
        ObservableList<String> programDescriptions = FXCollections.observableArrayList();
        for (int i = 0; i < programs.size(); i++) {
            programDescriptions.add((i + 1) + ". " + programs.get(i).toString());
        }

        ListView<String> programListView = new ListView<>(programDescriptions);
        programListView.setPrefHeight(400);

        Button selectButton = new Button("Select Program");
        selectButton.setOnAction(e -> {
            int selectedIndex = programListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                IStmt selectedProgram = programs.get(selectedIndex);
                try {
                    // Type check the program
                    MyIDictionary<String, Type> typeEnv = new MyDictionary<>();
                    selectedProgram.typecheck(typeEnv);

                    // Create controller for the selected program
                    PrgState prgState = new PrgState(
                            new MyStack<>(),
                            new MyDictionary<>(),
                            new MyList<>(),
                            new MyFileTable<>(),
                            new MyHeap<>(),
                            selectedProgram
                    );
                    IRepository repo = new Repository(prgState, "log" + (selectedIndex + 1) + ".txt");
                    Controller controller = new Controller(repo);

                    // Open execution window
                    ExecutionWindow executionWindow = new ExecutionWindow(controller);
                    Stage executionStage = new Stage();
                    executionWindow.show(executionStage);

                    stage.close();
                } catch (MyException ex) {
                    System.out.println("Type check error: " + ex.getMessage());
                }
            }
        });

        layout.getChildren().addAll(programListView, selectButton);

        Scene scene = new Scene(layout, 800, 500);
        stage.setTitle("Select Program");
        stage.setScene(scene);
        stage.show();
    }
}