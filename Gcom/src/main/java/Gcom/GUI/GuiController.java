package Gcom.GUI;

import Gcom.GroupManagement.Group;
import Gcom.GroupManagement.Member;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;


public class GuiController {

    public Logic logic;

    public GuiController() {

    }

    @FXML Button myButton;
    @FXML Button continueButton;
    @FXML MenuItem updateGroup;
    @FXML MenuItem connectMenu;
    @FXML TreeView <String> treeView;
    @FXML MenuItem joinMenuItem;
    @FXML TextArea sendArea;
    @FXML TextFlow receiveArea;
    @FXML MenuBar myMenuBar;
    @FXML Button sendButton;
    @FXML Circle myCircle;
    @FXML Text UserName;
    @FXML TabPane tabPane;
    @FXML Dialog dialog;
    @FXML Dialog connectPop;


    @FXML
    public void setTextInTextFlow (String s) {
        String myTextMessage = sendArea.getText();
        Text newText = new Text(myTextMessage);
        receiveArea.getChildren().add(newText);
    }

    public void sendMessage() {
        System.out.println("DIALOG");
        String myTextMessage = sendArea.getText();
        System.out.println(myTextMessage);

        dialog = new Dialog();
        dialog.setTitle("GROUP");
        dialog.setResizable(true);

        TextField text1 = new TextField();
        TextField text2 = new TextField();

        GridPane grid = new GridPane();
        grid.add(text1, 2, 1);
        grid.add(text2, 2, 2);
        dialog.getDialogPane().setContent(grid);
        dialog.onCloseRequestProperty();
        dialog.show();

        //sendArea.clear();
        //logic.getGM().messageGroup(myTextMessage, new Member(logic.getUserName(),logic.getLocalIp(),logic.getPort()),logic.getGM().getGroup(logic.getUserName()));
    }

    public void popUP() {

    }

    public void changeCirleColor(){
        System.out.println("Green");
    }

    public void setUserName(String uName) {
        UserName.setText(uName);
    }

    public void addGroupTab() {
        TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
        Tab tab = new Tab();
        if(item != null) {
            System.out.println("NOT NULL" + item.getValue());
            tab.setText(item.getValue());
        } else {
            System.out.println("NULL");
        }


        tabPane.getTabs().add(tab);

        ScrollPane sp = new ScrollPane();
        TextFlow tx = new TextFlow();
        tab.setContent(sp);
        sp.setContent(tx);
    }

    public void changeSceneToConnect(ActionEvent event) throws IOException {
        if(event.getSource()==connectMenu)
        {
            URL url = new File("src/main/java/Gcom/GUI/Connect.fxml").toURL();

            FXMLLoader loader = new FXMLLoader(url);
            Parent GCOm = loader.load();

            //SENDING
            connectController g = loader.getController();
            g.setConLogic(logic);

            Scene scene = new Scene(GCOm);

            Stage window = (Stage) myMenuBar.getScene().getWindow();
            window.setTitle("GCom");
            window.setScene(scene);
            window.show();
        }
    }


    public void changeSceneToGroup(ActionEvent event) throws IOException {
        if(event.getSource()==updateGroup) {
            URL url = new File("src/main/java/Gcom/GUI/CreateGroup.fxml").toURL();

            FXMLLoader loader = new FXMLLoader(url);
            Parent GCOm = loader.load();

            //SENDING
            GroupController g = loader.getController();
            g.setGroupLogic(logic);

            Scene scene = new Scene(GCOm);

            Stage window = (Stage) myMenuBar.getScene().getWindow();
            window.setTitle("GCom");
            window.setScene(scene);
            window.show();
        }
    }

    public void updateTree(Logic logic) {
        this.logic = logic;
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
        System.out.println("UPDATE TREE");
        treeView.setRoot(dummyroot);
        System.out.println("AFTERR UPDATE TREE");
    }

    public void connectFromTree() {
        //TODO
        System.out.println("TREEEEEEEEEEEEEEEEEEEe");
    }

    public void setGUILogic(Logic logic) {
        this.logic = logic;
    }
}
