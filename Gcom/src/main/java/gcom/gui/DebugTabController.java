package gcom.gui;

import gcom.Message;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DebugTabController {
    private Logic logic;
    private TabPane tabPane;
    private String selectedGroup;

    @FXML ListView<String> debugListView;
    @FXML ListView<String> debugMessageBuffer;
    @FXML Button debugUP;
    @FXML Button debugDown;
    @FXML Button clearDebug;
    @FXML Button debugStepButton;
    @FXML ComboBox<String> debugGroupBox;
    @FXML Button debugRemove;
    @FXML ToggleButton playStopToggle;
    public DebugTabController() {

    }

    private void monitorDebugBuffer() {
        logic.getGM().getDebugger().monitorDebugBuffer(() -> Platform.runLater(this::fillListView)).start();
    }

    public void fillDebugGroupBox() {
        selectedGroup = debugGroupBox.getSelectionModel().getSelectedItem();
        debugGroupBox.getItems().clear();
        Arrays.stream(logic.getGM().getGroups()).forEach(g -> {
            if(g.getName().equals(selectedGroup)) {
                debugGroupBox.getSelectionModel().select(selectedGroup);
            }
            debugGroupBox.getItems().add(g.getName());
        });
    }

    public void setSelectedItem() {
        selectedGroup = debugGroupBox.getSelectionModel().getSelectedItem();
        Platform.runLater(this::fillListView);
    }

    public void startDebuggerTab(Parent groupTab, FXMLLoader loader) throws IOException {
        //INIT NODES HERE
        debugGroupBox = (ComboBox) loader.getNamespace().get("debugGroupBox");
        debugListView = (ListView) loader.getNamespace().get("debugListView");


        Tab tab = new Tab("Debugger");
        tab.setContent(groupTab);
        tab.setOnClosed(event -> logic.getGM().getDebugger().stopDebugger());
        tabPane.getTabs().add(tab);
        monitorDebugBuffer();
        logic.getGM().getDebugger().startDebugger();
        Platform.runLater(this::fillDebugGroupBox);
    }

    public void step() {
        if(!debugListView.getItems().isEmpty()) {
            logic.getGM().getDebugger().step(selectedGroup);
        }
    }

    public void clearDebugging() {
        logic.getGM().getDebugger().getDebugBuffer(selectedGroup).forEach(m -> {
            logic.getGM().getDebugger().removeMessage(selectedGroup, 0);
        });
        debugListView.getItems().clear();
    }

    public void moveMessageUP() {
        if(debugListView.getItems().size() == 0) {
            return;
        }
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
        List<Message> debugBuffer = logic.getGM().getDebugger().getDebugBuffer(selectedGroup);
        if(debugListView == null || debugBuffer == null) {
            return;
        }

        String workingDebugTab = debugGroupBox.getSelectionModel().getSelectedItem();
        if(workingDebugTab == null) {
            return;
        }

        if(debugBuffer == null) {
            return;
        }

        debugListView.getItems().clear();


        debugBuffer.forEach((m) -> {
            if (workingDebugTab.equals(m.getGroup().getName())) {
                //debugListView.getItems().add(m.getMessage() +" ["+m.getVectorClock().getValue(m.getVectorClock().getMyId())+"]");
                debugListView.getItems().add(m.getVectorClock() + " : " + m.getSender().getName() + " : " + m.getMessage());
            }

        });
        List<Message> messageOrderBuffer = logic.getGM().getDebugger().getOrderBuffer(selectedGroup);

        if(debugMessageBuffer == null || messageOrderBuffer == null){
            return;
        }

        debugMessageBuffer.getItems().clear();

        messageOrderBuffer.forEach(m -> {
            debugMessageBuffer.getItems().add(m.getVectorClock().toString() +" : " + m.getSender().getName() + " : " + m.getMessage());
        });

    }



    public void changePlayOrStopState() {
        if(!debugListView.getItems().isEmpty() || playStopToggle.getText().equals("Stop")) {
            if (playStopToggle.isSelected()) {
                logic.getGM().getDebugger().play(selectedGroup);
                playStopToggle.setText("Stop");
            } else {
                logic.getGM().getDebugger().stop();
                playStopToggle.setText("Play");
            }
        }
    }

    public void removeItemFromList() {
        if(debugListView.getSelectionModel().getSelectedItem() != null) {
            logic.getGM().getDebugger().removeMessage(selectedGroup, debugListView.getSelectionModel().getSelectedIndex());
        }
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
