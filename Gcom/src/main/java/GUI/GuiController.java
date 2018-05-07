package GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.Observable;
import java.util.Set;


public class GuiController extends Observable {

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


    @FXML
    public void setTextInTextFlow (String s) {
       /* String myTextMessage = sendArea.getText();*/
        Text newText = new Text(s);
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
            URL url = new File("src/main/java/GUI/Connect.fxml").toURL();

            FXMLLoader loader = new FXMLLoader(url);
            Parent GCOm = loader.load();

            Scene scene = new Scene(GCOm);

           // Stage window =  (Stage) ((Node)event.getSource()).getScene().getWindow();
            Stage window = (Stage) myMenuBar.getScene().getWindow();
            window.setTitle("GCom");
            window.setScene(scene);
            window.show();
        }
    }

    public void updateTree(Logic logic) {
        this.logic = logic;
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
