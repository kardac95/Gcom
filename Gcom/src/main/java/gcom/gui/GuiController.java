package gcom.gui;

import gcom.Message;
import gcom.groupmanagement.Group;
import gcom.groupmanagement.Member;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;


public class GuiController {

    public Logic logic;
    private Boolean debug = false;

    public GuiController() {

    }

    @FXML MenuItem updateGroup;
    @FXML MenuItem connectMenu;
    @FXML TreeView <String> treeView;
    @FXML TextArea sendArea;
    @FXML MenuBar myMenuBar;
    @FXML Button sendButton;
    @FXML Text UserName;
    @FXML TabPane tabPane;
    @FXML Dialog groupDialog;
    @FXML Dialog connectDialog;
    @FXML TextField groupName;
    @FXML Button groupButton;
    @FXML TextField connectPort;
    @FXML TextField connectHostIP;
    @FXML TextField connectGroup;
    @FXML TextField connectHostName;
    @FXML Button connectButton;
    @FXML MenuItem debugStart;

    public void setTextInTextFlow (final Message m) {
        Platform.runLater(() -> {
            String color = "magenta";
            if(m.getSender().equals(logic.getMe())) {
                color = "green";
            } else if(m.getType().equals("join")){
                color = "red";
            }
            if(tabPane == null) {
                System.out.println("TABPANE NULL");
            }
            CustomTab tab = (CustomTab)tabPane.getTabs().filtered((t) -> t.getText().equals(m.getGroup().getName())).get(0);
            tab.getSp().vvalueProperty().bind(tab.getTf().heightProperty());
            tab.setText(m.getSender().getName() + "> ", color);
            tab.setText(m.getMessage() + "\n", "black");

        });
    }

    public void sendMessage() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if(currentTab != null) {
            System.out.println("tab is " + currentTab.getText());
            logic.getGM().messageGroup(sendArea.getText(), logic.getMe(), currentTab.getText());
            sendArea.clear();
            sendArea.setText("");
        }

    }

    public void connectPopUP() {
        connectDialog = new Dialog();
        connectDialog.setTitle("CONNECT");
        connectDialog.setResizable(true);
        Text text1 = new Text("Connect to a host");
        connectHostIP = new TextField();
        connectHostIP.setPromptText("IP");
        connectHostIP.setText(logic.getLocalIp());
        connectPort = new TextField();
        connectPort.setPromptText("PORT");
        connectHostName = new TextField();
        connectHostName.setPromptText("Name");
        connectGroup = new TextField();
        connectGroup.setPromptText("Group");
        connectButton = new Button("Connect");

        GridPane grid = new GridPane();
        grid.add(text1, 1 ,1);
        grid.add(connectHostIP, 1, 2);
        grid.add(connectPort,1,3);
        grid.add(connectHostName,1,4);
        grid.add(connectGroup,1,5);
        grid.add(connectButton,1,6);
        connectDialog.getDialogPane().setContent(grid);
        Window window = connectDialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(windowEvent -> connectDialog.hide());
        connectButton.setOnAction(event -> connectToUser());
        connectDialog.show();
    }

    public void connectToUser(){
        logic.getGM().joinGroupRequest(new Member(
                        connectHostName.getText(),
                        connectHostIP.getText(),
                        connectPort.getText()),
                        connectGroup.getText());

        addGroupTab(connectGroup.getText());

        connectHostIP.clear();
        connectPort.clear();
        connectHostName.clear();
        connectGroup.clear();

    }

    public void groupPopUP() {
        groupDialog = new Dialog();
        groupDialog.setTitle("GROUP");
        groupDialog.setResizable(true);
        Text text1 = new Text("Create Group");
        groupName = new TextField();
        groupButton = new Button("Create");

        GridPane grid = new GridPane();
        grid.add(text1, 1 ,1);
        grid.add(groupName, 1, 2);
        grid.add(groupButton,1,3);
        groupDialog.getDialogPane().setContent(grid);
        Window window = groupDialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(windowEvent -> groupDialog.hide());
        groupButton.setOnAction(event -> createGroup());
        groupDialog.show();
    }

    public  void createGroup() {
        String groupName = this.groupName.getText();
        logic.getGM().createGroup(groupName);
        this.groupName.clear();
        updateTree();
       // Platform.runLater(() -> addGroupTab(groupName));
        addGroupTab(groupName);
    }

    public void setUserName(String uName) {
        UserName.setText(uName);
    }

    public void addGroupTab(String groupName) {
        CustomTab tab = new CustomTab();
        tab.setText(groupName);
        tab.setContentTextFlow(new TextFlow());
        tabPane.getTabs().add(tab);
    }

    public void updateTree() {
        Group[] groups = logic.getGM().getGroups();
        TreeItem<String> dummyroot = new TreeItem<>("MegaRoot");

        for (Group group : groups) {
            Member[] members = group.getMembers();
            TreeItem<String> root = new TreeItem<>(group.getName());
            for (Member member : members) {
                root.getChildren().add(new TreeItem<>(member.getName()));
            }
            dummyroot.getChildren().add(root);
        }
        treeView.setRoot(dummyroot);
    }

    public void setGUILogic(Logic logic) {
        this.logic = logic;
    }

    public void monitorGroupManager() {
      Thread t = new Thread(() -> {
          while(true) {
              Message m = null;
              try {
                  m = ((LinkedBlockingQueue<Message>)logic.getGM().getOutgoingQueue()).take();
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              System.out.println("Update!");
              Platform.runLater(this::updateTree);
              final Message message = m;
              Platform.runLater(() -> setTextInTextFlow(message));
          }
      });
      t.start();
    }

    public void startDebuggerTab() {
        debug = true;
        URL url = null;
        try {
            url = new File("src/main/java/gcom/gui/DebugTab.fxml").toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        FXMLLoader loader = new FXMLLoader(url);
        try {
            Node groupTab = loader.load();
            Tab tab = new Tab("Debugger");
          //  groupTab.getChildrenUnmodifiable().add(groupTab);
            tab.setContent(groupTab);
            tabPane.getTabs().add(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        sendArea.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                System.out.println("Send");
                sendMessage();
                sendArea.clear();
                keyEvent.consume();
            }
        });
    }

}
