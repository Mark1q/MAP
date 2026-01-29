package gui;

import controller.Controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.PrgState;
import model.value.Value;
import exception.MyException;

import java.util.List;
import java.util.Map;

public class ExecutionWindow {
    private Controller controller;

    // UI Components
    private TextField prgStateCountField;
    private TableView<HeapEntry> heapTableView;
    private ListView<String> outListView;
    private ListView<String> fileTableListView;
    private ListView<Integer> prgStateIdListView;
    private TableView<SymTableEntry> symTableView;
    private ListView<String> exeStackListView;
    private TableView<SemaphoreEntry> semaphoreTableView;
    private Button runOneStepButton;
    private Button runAllStepsButton;

    public ExecutionWindow(Controller controller) {
        this.controller = controller;
    }

    public void show(Stage stage) {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));

        // Top: Number of PrgStates
        HBox topBox = new HBox(10);
        topBox.setPadding(new Insets(5));
        Label prgStateLabel = new Label("Number of PrgStates:");
        prgStateCountField = new TextField();
        prgStateCountField.setEditable(false);
        prgStateCountField.setPrefWidth(100);
        topBox.getChildren().addAll(prgStateLabel, prgStateCountField);

        // Center: Split into left and right
        SplitPane centerSplit = new SplitPane();

        // Left side: Heap, Out, FileTable, PrgState IDs, SemaphoreTable
        VBox leftBox = new VBox(10);
        leftBox.setPadding(new Insets(5));

        // Heap Table
        Label heapLabel = new Label("Heap Table:");
        heapTableView = new TableView<>();
        TableColumn<HeapEntry, Integer> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(data -> data.getValue().addressProperty().asObject());
        TableColumn<HeapEntry, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(data -> data.getValue().valueProperty());
        heapTableView.getColumns().addAll(addressCol, valueCol);
        heapTableView.setPrefHeight(120);

        // Out List
        Label outLabel = new Label("Output:");
        outListView = new ListView<>();
        outListView.setPrefHeight(80);

        // FileTable List
        Label fileTableLabel = new Label("File Table:");
        fileTableListView = new ListView<>();
        fileTableListView.setPrefHeight(60);

        // Semaphore Table
        Label semaphoreLabel = new Label("Semaphore Table:");
        semaphoreTableView = new TableView<>();
        TableColumn<SemaphoreEntry, Integer> semIndexCol = new TableColumn<>("Index");
        semIndexCol.setCellValueFactory(data -> data.getValue().indexProperty().asObject());
        TableColumn<SemaphoreEntry, Integer> semValueCol = new TableColumn<>("Value");
        semValueCol.setCellValueFactory(data -> data.getValue().valueProperty().asObject());
        TableColumn<SemaphoreEntry, String> semListCol = new TableColumn<>("List");
        semListCol.setCellValueFactory(data -> data.getValue().listProperty());
        semaphoreTableView.getColumns().addAll(semIndexCol, semValueCol, semListCol);
        semaphoreTableView.setPrefHeight(120);

        // PrgState IDs List
        Label prgStateIdsLabel = new Label("PrgState IDs:");
        prgStateIdListView = new ListView<>();
        prgStateIdListView.setPrefHeight(60);
        prgStateIdListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> updateSelectedPrgState(newVal)
        );

        leftBox.getChildren().addAll(
                heapLabel, heapTableView,
                outLabel, outListView,
                fileTableLabel, fileTableListView,
                semaphoreLabel, semaphoreTableView,
                prgStateIdsLabel, prgStateIdListView
        );

        // Right side: SymTable and ExeStack
        VBox rightBox = new VBox(10);
        rightBox.setPadding(new Insets(5));

        // SymTable
        Label symTableLabel = new Label("Symbol Table:");
        symTableView = new TableView<>();
        TableColumn<SymTableEntry, String> varNameCol = new TableColumn<>("Variable Name");
        varNameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        TableColumn<SymTableEntry, String> varValueCol = new TableColumn<>("Value");
        varValueCol.setCellValueFactory(data -> data.getValue().valueProperty());
        symTableView.getColumns().addAll(varNameCol, varValueCol);
        symTableView.setPrefHeight(250);

        // ExeStack
        Label exeStackLabel = new Label("Execution Stack:");
        exeStackListView = new ListView<>();
        exeStackListView.setPrefHeight(250);

        rightBox.getChildren().addAll(
                symTableLabel, symTableView,
                exeStackLabel, exeStackListView
        );

        centerSplit.getItems().addAll(leftBox, rightBox);
        centerSplit.setDividerPositions(0.5);

        // Bottom: Run One Step and Run All Steps buttons
        HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(5));
        runOneStepButton = new Button("Run One Step");
        runOneStepButton.setOnAction(e -> runOneStep());
        runAllStepsButton = new Button("Run All Steps");
        runAllStepsButton.setOnAction(e -> runAllSteps());
        bottomBox.getChildren().addAll(runOneStepButton, runAllStepsButton);

        mainLayout.setTop(topBox);
        mainLayout.setCenter(centerSplit);
        mainLayout.setBottom(bottomBox);

        Scene scene = new Scene(mainLayout, 1400, 800);
        stage.setTitle("Program Execution");
        stage.setScene(scene);
        stage.show();

        // Initial update
        updateAll();
    }

    private void runOneStep() {
        try {
            List<PrgState> allPrgList = controller.getRepo().getPrgList();

            if (allPrgList.stream().anyMatch(PrgState::isNotCompleted)) {
                controller.oneStepForAllPrg(allPrgList);
                updateAll();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Execution Complete");
                alert.setHeaderText(null);
                alert.setContentText("Program execution has completed!");
                alert.showAndWait();
                runOneStepButton.setDisable(true);
                runAllStepsButton.setDisable(true);
            }
        } catch (MyException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Execution Error");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void runAllSteps() {
        runOneStepButton.setDisable(true);
        runAllStepsButton.setDisable(true);

        Thread executionThread = new Thread(() -> {
            try {
                List<PrgState> allPrgList = controller.getRepo().getPrgList();

                while (allPrgList.stream().anyMatch(PrgState::isNotCompleted)) {
                    controller.oneStepForAllPrg(allPrgList);
                    javafx.application.Platform.runLater(() -> updateAll());
                    Thread.sleep(50);
                    allPrgList = controller.getRepo().getPrgList();
                }

                javafx.application.Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Execution Complete");
                    alert.setHeaderText(null);
                    alert.setContentText("Program execution has completed!");
                    alert.showAndWait();
                    runOneStepButton.setDisable(true);
                    runAllStepsButton.setDisable(true);
                });

            } catch (MyException e) {
                javafx.application.Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Execution Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Error: " + e.getMessage());
                    alert.showAndWait();
                    runOneStepButton.setDisable(false);
                    runAllStepsButton.setDisable(false);
                });
            } catch (InterruptedException e) {
                javafx.application.Platform.runLater(() -> {
                    runOneStepButton.setDisable(false);
                    runAllStepsButton.setDisable(false);
                });
            }
        });

        executionThread.setDaemon(true);
        executionThread.start();
    }

    private void updateAll() {
        List<PrgState> prgList = controller.getRepo().getPrgList();

        prgStateCountField.setText(String.valueOf(prgList.size()));

        if (prgList.isEmpty()) {
            heapTableView.setItems(FXCollections.observableArrayList());
            outListView.setItems(FXCollections.observableArrayList());
            fileTableListView.setItems(FXCollections.observableArrayList());
            semaphoreTableView.setItems(FXCollections.observableArrayList());
            prgStateIdListView.setItems(FXCollections.observableArrayList());
            symTableView.setItems(FXCollections.observableArrayList());
            exeStackListView.setItems(FXCollections.observableArrayList());
            return;
        }

        PrgState firstState = prgList.get(0);

        // Update Heap
        ObservableList<HeapEntry> heapEntries = FXCollections.observableArrayList();
        Map<Integer, Value> heapContent = firstState.getHeap().getContent();
        for (Map.Entry<Integer, Value> entry : heapContent.entrySet()) {
            heapEntries.add(new HeapEntry(entry.getKey(), entry.getValue().toString()));
        }
        heapTableView.setItems(heapEntries);

        // Update Out
        ObservableList<String> outItems = FXCollections.observableArrayList();
        String outString = firstState.getOut().toString();
        if (!outString.trim().isEmpty()) {
            String[] outLines = outString.split("\n");
            for (String line : outLines) {
                if (!line.trim().isEmpty()) {
                    outItems.add(line);
                }
            }
        }
        outListView.setItems(outItems);

        // Update FileTable
        ObservableList<String> fileItems = FXCollections.observableArrayList();
        String fileTableString = firstState.getFileTable().toString();
        if (!fileTableString.trim().isEmpty()) {
            String[] fileLines = fileTableString.split("\n");
            for (String line : fileLines) {
                if (!line.trim().isEmpty()) {
                    fileItems.add(line);
                }
            }
        }
        fileTableListView.setItems(fileItems);

        // Update SemaphoreTable
        ObservableList<SemaphoreEntry> semaphoreEntries = FXCollections.observableArrayList();
        Map<Integer, Pair<Integer, List<Integer>>> semaphoreContent = firstState.getSemaphoreTable().getContent();
        for (Map.Entry<Integer, Pair<Integer, List<Integer>>> entry : semaphoreContent.entrySet()) {
            semaphoreEntries.add(new SemaphoreEntry(
                    entry.getKey(),
                    entry.getValue().getKey(),
                    entry.getValue().getValue().toString()
            ));
        }
        semaphoreTableView.setItems(semaphoreEntries);

        // Update PrgState IDs
        ObservableList<Integer> idItems = FXCollections.observableArrayList();
        for (PrgState state : prgList) {
            idItems.add(state.getId());
        }
        prgStateIdListView.setItems(idItems);

        if (!idItems.isEmpty() && prgStateIdListView.getSelectionModel().getSelectedItem() == null) {
            prgStateIdListView.getSelectionModel().selectFirst();
        }
    }

    private void updateSelectedPrgState(Integer selectedId) {
        if (selectedId == null) {
            symTableView.setItems(FXCollections.observableArrayList());
            exeStackListView.setItems(FXCollections.observableArrayList());
            return;
        }

        List<PrgState> prgList = controller.getRepo().getPrgList();
        PrgState selectedState = prgList.stream()
                .filter(p -> p.getId() == selectedId)
                .findFirst()
                .orElse(null);

        if (selectedState == null) return;

        // Update SymTable
        ObservableList<SymTableEntry> symEntries = FXCollections.observableArrayList();
        Map<String, Value> symTableContent = selectedState.getSymTable().getContent();
        for (Map.Entry<String, Value> entry : symTableContent.entrySet()) {
            symEntries.add(new SymTableEntry(entry.getKey(), entry.getValue().toString()));
        }
        symTableView.setItems(symEntries);

        // Update ExeStack
        ObservableList<String> stackItems = FXCollections.observableArrayList();
        String stackString = selectedState.getStk().toString();
        if (!stackString.trim().isEmpty()) {
            String[] stackLines = stackString.split("\n");
            for (String line : stackLines) {
                if (!line.trim().isEmpty()) {
                    stackItems.add(line);
                }
            }
        }
        exeStackListView.setItems(stackItems);
    }

    // Helper classes for TableView
    public static class HeapEntry {
        private final SimpleIntegerProperty address;
        private final SimpleStringProperty value;

        public HeapEntry(int address, String value) {
            this.address = new SimpleIntegerProperty(address);
            this.value = new SimpleStringProperty(value);
        }

        public SimpleIntegerProperty addressProperty() { return address; }
        public SimpleStringProperty valueProperty() { return value; }
    }

    public static class SymTableEntry {
        private final SimpleStringProperty name;
        private final SimpleStringProperty value;

        public SymTableEntry(String name, String value) {
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleStringProperty(value);
        }

        public SimpleStringProperty nameProperty() { return name; }
        public SimpleStringProperty valueProperty() { return value; }
    }

    public static class SemaphoreEntry {
        private final SimpleIntegerProperty index;
        private final SimpleIntegerProperty value;
        private final SimpleStringProperty list;

        public SemaphoreEntry(int index, int value, String list) {
            this.index = new SimpleIntegerProperty(index);
            this.value = new SimpleIntegerProperty(value);
            this.list = new SimpleStringProperty(list);
        }

        public SimpleIntegerProperty indexProperty() { return index; }
        public SimpleIntegerProperty valueProperty() { return value; }
        public SimpleStringProperty listProperty() { return list; }
    }
}