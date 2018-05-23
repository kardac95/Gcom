package gcom.gui;

import gcom.Message;
import gcom.groupmanagement.Group;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DebugTabController {
    private Logic logic;
    private FXMLLoader loader;
    private TabPane tabPane;
    private String selectedGroup;

    @FXML ListView<String> debugListView;
    @FXML Button debugUP;
    @FXML Button debugDown;
    @FXML Button clearDebug;
    @FXML Button debugPlayButton;
    @FXML Button debugStepButton;
    @FXML ComboBox<String> debugGroupBox;
    @FXML Button stopMessageButton;

    public DebugTabController() {

    }

    private void monitorDebugBuffer() {
        logic.getGM().getDebugger().monitorDebugBuffer(() -> Platform.runLater(this::fillListView)).start();
    }

    public void fillDebugGroupBox() {
        selectedGroup = debugGroupBox.getSelectionModel().getSelectedItem();
        System.out.println("this is selected group and i am fine? " + selectedGroup);
        debugGroupBox.getItems().clear();
        System.out.println(logic.getGM().getGroups().length);
        Arrays.stream(logic.getGM().getGroups()).forEach(g -> {
            if(g.getName().equals(selectedGroup)) {
                System.out.println("INSIDE SLECTED GROUP");
                debugGroupBox.getSelectionModel().select(selectedGroup);
            }
            System.out.println("riperinos perpperinos");
            debugGroupBox.getItems().add(g.getName());
        });

        /*Group[] groups = logic.getGM().getGroups();
        for (Group g : groups) {
            if(g.getName().equals(selectedGroup)) {
                System.out.println("INSIDE SLECTED GROUP");
                debugGroupBox.getSelectionModel().select(selectedGroup);
            }

        }*/
    }

    public void setSelectedItem() {
        selectedGroup = debugGroupBox.getSelectionModel().getSelectedItem();
    }

    public void startDebuggerTab(Parent groupTab, FXMLLoader loader) throws IOException {
        //INIT NODES HERE
        debugGroupBox = (ComboBox) loader.getNamespace().get("debugGroupBox");
        stopMessageButton = (Button) loader.getNamespace().get("stopMessageButton");
        debugListView = (ListView) loader.getNamespace().get("debugListView");


        Tab tab = new Tab("Debugger");
        tab.setContent(groupTab);
        tab.setOnClosed(event -> logic.getGM().getDebugger().StopDebugger());
        tabPane.getTabs().add(tab);
        monitorDebugBuffer();
        logic.getGM().getDebugger().startDebugger();
        Platform.runLater(this::fillDebugGroupBox);
    }

    public void stop()  {
        System.out.println("Logic over here:   " + logic);
        logic.getGM().getDebugger().stop();
    }

    public void play() {
        logic.getGM().getDebugger().play(selectedGroup);
    }

    public void step() {
        System.out.println("Logic over here:   " + logic);
        logic.getGM().getDebugger().step(selectedGroup);
    }

    public void clearDebugging() {
        debugListView.getItems().clear();
    }

    public void moveMessageUP() {
        String item = debugListView.getSelectionModel().getSelectedItem();
        int curIndex = debugListView.getItems().indexOf(item);
        if(curIndex != 0) {
            String moveItem = debugListView.getItems().get(curIndex - 1);
            debugListView.getItems().set(curIndex,moveItem);
            debugListView.getItems().set(curIndex -1 , item);
            debugListView.getSelectionModel().select(curIndex -1 );
            logic.getGM().getDebugger().moveMessage(selectedGroup,curIndex, curIndex -1);
        }
    }

    public void moveMessageDown() {
        String item = debugListView.getSelectionModel().getSelectedItem();
        int curIndex = debugListView.getItems().indexOf(item);
        if(curIndex != debugListView.getItems().size() -1) {
            String moveItem = debugListView.getItems().get(curIndex + 1);
            debugListView.getItems().set(curIndex,moveItem);
            debugListView.getItems().set(curIndex +1 , item);
            debugListView.getSelectionModel().select(curIndex +1 );
            logic.getGM().getDebugger().moveMessage(selectedGroup, curIndex, curIndex +1);
        }
    }

    public void fillListView() {
        System.out.println("this is selected group: " + selectedGroup);
        List<Message> debugBuffer = logic.getGM().getDebugger().getDebugBuffer(selectedGroup);
        if(debugListView == null) {
            System.out.println("RETURN");
            return;
        }

        String workingDebugTab = debugGroupBox.getSelectionModel().getSelectedItem();
        if(workingDebugTab == null) {
            System.out.println("RETURN");
            return;
        }
        debugListView.getItems().clear();

        debugBuffer.forEach((m) -> {
            if(workingDebugTab.equals(m.getGroup().getName())) {
                //debugListView.getItems().add(m.getMessage() +" ["+m.getVectorClock().getValue(m.getVectorClock().getMyId())+"]");
                debugListView.getItems().add(m.getMessage());
            }

        });
    }

    public ComboBox<String> getDebugGroupBox() {
        return debugGroupBox;
    }

    public void initialize(Logic logic, TabPane tabPane) {
        this.logic = logic;
        this.tabPane = tabPane;
        Platform.runLater(this::fillDebugGroupBox);
    }
}
