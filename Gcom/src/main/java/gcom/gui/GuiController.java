package gcom.gui;

import gcom.Message;
import gcom.groupmanagement.Group;
import gcom.groupmanagement.Member;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Window;


public class GuiController {

    public Logic logic;

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
    @FXML TextFlow textFlow;


    @FXML
    public void setTextInTextFlow (Message m) {
        System.out.println("group" + m.getGroup().getName());
        System.out.println("Size of tablist is " + tabPane.getTabs().size());
       /* tabPane.getTabs().forEach((t) -> {
            System.out.println("tab" + t);
        });*/
        for (Tab t:tabPane.getTabs()) {
            System.out.println("Tab is " + t.getText());
            Text text = new Text(m.getMessage());
            TextFlow tx = (TextFlow) tabPane.lookup("#textFlow");
            tx.getChildren().add(text);
        }
      /* // System.out.println(tabPane.getTabs().filtered((t) -> t.getText().equals(m.getGroup().getName())).size());
        Tab tab = tabPane.getTabs().filtered((t) -> t.getText().equals(m.getGroup().getName())).get(0);
        //ScrollPane sp = (ScrollPane) ((Parent)tab.getContent()).getChildrenUnmodifiable().get(0);
        tab.
        TextFlow tf = (TextFlow) ((Parent) sp.getContent()).getChildrenUnmodifiable().get(0);
        Text text = new Text(m.getMessage());
      //  tf.setText(tab.getText() + "\n" + m.getMessage());
        tf.getChildren().add(text);*/

    }

    public void sendMessage() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if(currentTab != null) {
            System.out.println("tab is " + currentTab.getText());
            logic.getGM().messageGroup(sendArea.getText(), logic.getMe(), currentTab.getText());
        }

    }

    public void connectPopUP() {
        connectDialog = new Dialog();
        connectDialog.setTitle("CONNECT");
        connectDialog.setResizable(true);
        Text text1 = new Text("Connect to a host");
        connectHostIP = new TextField("IP");
        connectHostIP.setText(logic.getLocalIp());
        connectPort = new TextField("Port");
        connectHostName = new TextField("Name");
        connectGroup = new TextField("Group");
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
        String myTextMessage = sendArea.getText();
        System.out.println(myTextMessage);

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
        String groupname = groupName.getText();
        logic.getGM().createGroup(groupname);
        groupName.clear();
        updateTree();
        Platform.runLater(() -> addGroupTab(groupname));

    }

    public void setUserName(String uName) {
        UserName.setText(uName);
    }

    public void addGroupTab(String groupname) {
        Tab tab = new Tab();
        tab.setText(groupname);
        tabPane.getTabs().add(tab);
        ScrollPane sp = new ScrollPane();
        TextFlow tx = new TextFlow();
        tab.setContent(sp);
        sp.setContent(tx);
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

    public void connectFromTree() {
        //TODO
        System.out.println("TREEEEEEEEEEEEEEEEEEEe");
    }

    public void setGUILogic(Logic logic) {
        this.logic = logic;
    }
}
