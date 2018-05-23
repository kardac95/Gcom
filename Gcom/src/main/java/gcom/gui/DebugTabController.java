package gcom.gui;

import gcom.Message;
import gcom.groupmanagement.Group;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DebugTabController {
    private Logic logic;
    private FXMLLoader loader;
    private TabPane tabPane;

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
        String current = debugGroupBox.getSelectionModel().getSelectedItem();
        debugGroupBox.getItems().clear();
        System.out.println("Fill me Logic over here fill debug:   " + logic);

        Group[] groups = logic.getGM().getGroups();
        for (Group g : groups) {
            if(g.getName().equals(current)) {
                debugGroupBox.getSelectionModel().select(current);
            }
            debugGroupBox.getItems().add(g.getName());
        }
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
        logic.getGM().getDebugger().play();
    }

    public void step() {
        System.out.println("Logic over here:   " + logic);
        logic.getGM().getDebugger().step();
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
            logic.getGM().getDebugger().moveMessage(curIndex, curIndex -1);
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
            logic.getGM().getDebugger().moveMessage(curIndex, curIndex +1);
        }
    }

    public void fillListView() {
        List <Message>debugBuffer = logic.getGM().getDebugger().getDebugBuffer();
        System.out.println("UPDATE DEBUGGER");
        //List<Message> debugBuffer = logic.getGM().getDebugger().getDebugBuffer();
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

    public void initialize(Logic logic, TabPane tabPane) {
        this.logic = logic;
        this.tabPane = tabPane;
    }
}
