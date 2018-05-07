package GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Set;


public class GuiController {

    private Logic logic;

    public GuiController() {
        this.logic = new Logic();

    }

    @FXML Button myButton;
    @FXML Button continueButton;
    @FXML MenuItem updateGroup;
    @FXML MenuItem connectMenu;
    @FXML TreeView <String> treeView;
    @FXML MenuItem joinMenuItem;
    @FXML TextArea sendArea;
    @FXML TextFlow receiveArea;
    @FXML Button sendButton;
    @FXML Circle myCircle;

    public void setTextInTextFlow () {
        String myTextMessage = sendArea.getText();
        Text newText = new Text(myTextMessage);
        receiveArea.getChildren().add(newText);
    }

    public void changeCirleColor(){
        System.out.println("Green");
    }

    public void changeSceneToConnect(ActionEvent event) throws IOException {
        Stage appStage;
        Parent root;
        if(event.getSource()==connectMenu)
        {
            //appStage=(Stage)sendButton.getScene().getWindow();

            connectMenu = (MenuItem) event.getTarget();
            ContextMenu cm = connectMenu.getParentPopup();
            Scene scene1 = cm.getScene();
            appStage = (Stage)scene1.getWindow();

            URL url = new File("src/main/java/GUI/gui.fxml").toURL();
            FXMLLoader loader = new FXMLLoader(url);
            root = loader.load();


            Scene scene=new Scene(root);
            appStage.setScene(scene);
            appStage.show();
        }
    }

    public void changeSceneToGUI(ActionEvent event) throws IOException {
        URL url = new File("src/main/java/GUI/gui.fxml").toURL();

        FXMLLoader loader = new FXMLLoader(url);
        Parent GCOm = loader.load();

        Scene scene = new Scene(GCOm);

        //This line gets stage inforamtion
        Stage window =  (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("GCom");
        window.setScene(scene);
        window.show();
        Platform.runLater(this::updateTree);

    }



    public void updateTree() {
        Set groups = logic.getGM().getGroups();

        TreeItem<String> dummyroot = new TreeItem<>("MegaRoot");

        for (Object group : groups) {
            String groupName = (String) group;
            Set members = logic.getGM().getGroup((String)group).getMembers();
            TreeItem<String> root = new TreeItem<>(groupName);
            for (Object member : members) {
                String memberName = (String) member;
                root.getChildren().add(new TreeItem<>(memberName));
            }
            dummyroot.getChildren().add(root);
        }

        treeView.setRoot(dummyroot);
    }

    public void connectFromTree() {
        //TODO
        System.out.println("TREEEEEEEEEEEEEEEEEEEe");
    }
}
