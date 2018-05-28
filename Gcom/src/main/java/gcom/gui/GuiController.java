package gcom.gui;

import gcom.Message;
import gcom.groupmanagement.Group;
import gcom.groupmanagement.Member;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.LinkedBlockingQueue;


public class GuiController {

    private Logic logic;
    private FXMLLoader loader;
    DebugTabController dtc;

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
            if(m.getType().equals("connect")) {
                return;
            }
            CustomTab tab = (CustomTab)tabPane.getTabs().filtered((t) -> t.getText().equals(m.getGroup().getName())).get(0);

            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

            tab.getSp().vvalueProperty().bind(tab.getTf().heightProperty());
            tab.setText("[" + timeStamp + "] ", "red");
            tab.setText(m.getSender().getName() + "> ", m.getSender().equals(logic.getMe()) ? "green" : "magenta");
            tab.setText(m.getMessage() + "\n", "black");

    }

    public void sendMessage() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if(currentTab == null || currentTab.getText().equals("Welcome")) {
            return;
        }
        if(!(currentTab.getText().equals("Debugger"))) {
            //System.out.println("Current tab: " + currentTab.getText());
            logic.getGM().messageGroup(sendArea.getText(), logic.getMe(), currentTab.getText());
            sendArea.clear();
            sendArea.setText("");
        } else {
            ComboBox<String> cb = dtc.getDebugGroupBox();
            logic.getGM().messageGroup(sendArea.getText(),logic.getMe(),cb.getSelectionModel().getSelectedItem());
            sendArea.clear();
            sendArea.setText("");
        }
    }

    public void leaveGroup(String group) {
        System.out.println("Leaving group: " + group);
        //logic.getGM().messageGroup(logic.getUserName() + " is leaving!", logic.getMe(),group);
        logic.getGM().leaveGroup(group);
        logic.getGM().removeGroup(group);
       // fillDebugGroupBox();
        updateTree();
        if(tabPane.getTabs().isEmpty()) {
            addWelcomeTab();
        }
        if(dtc != null) {
            Platform.runLater(() -> dtc.initialize(logic, tabPane));
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

    public void createGroup() {
        String groupName = this.groupName.getText();
        if(groupName.equals("Debugger")) {
            System.err.println("Debugger is a reserved name.");
            return;
        }
        logic.getGM().createGroup(groupName);
        this.groupName.clear();
        updateTree();
        addGroupTab(groupName);
        if(dtc != null) {
            Platform.runLater(() -> dtc.initialize(logic, tabPane));
        }
    }

    public void setUserName(String uName) {
        UserName.setText(uName);
    }

    public void addWelcomeTab() {
        CustomTab customTab = new CustomTab();
        customTab.setText("Welcome");
        customTab.setContentTextFlow(new TextFlow());
        String helpText = "\n To create or connect to a group, use the tools menu. \n";
        customTab.setText(helpText, "black");
        customTab.setClosable(false);
        tabPane.getTabs().add(customTab);
    }

    public void addGroupTab(String groupName) {
        CustomTab tab = new CustomTab();
        tab.setText(groupName);
        tab.setContentTextFlow(new TextFlow());
        tabPane.getTabs().add(tab);
        tab.setOnClosed(event -> leaveGroup(groupName));
        FilteredList<Tab> list = tabPane.getTabs().filtered((t) -> t.getText().equals("Welcome"));
        if (!list.isEmpty()) {
            CustomTab wTab = (CustomTab) list.get(0);
            tabPane.getTabs().remove(wTab);
        }
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
              if(m.getType().equals("connect")) {
                  continue;
              }
              //System.out.println("Update!");
              Platform.runLater(this::updateTree);
              final Message message = m;
              Platform.runLater(() -> setTextInTextFlow(message));
          }
      });
      t.start();
    }

    public void startDebuggerTab() throws IOException {

        String os = System.getProperty("os.name");
        if(os.equals("Linux") || os.equals("Windows 10")) {
            //These 2 lines are for Linux!
            URL url = new File("src/main/java/gcom/gui/DebugTab.fxml").toURL();
            loader = new FXMLLoader(url);
        } else if(os.equals("Windows")) {
            //This line is for Windows!
            loader = new FXMLLoader(Main.class.getResource("DebugTab.fxml"));
        }
        Parent groupTab = loader.load();
        dtc = loader.getController();
        dtc.initialize(logic, tabPane);
        dtc.startDebuggerTab(groupTab, loader);
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
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

}
