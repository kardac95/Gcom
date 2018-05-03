package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Set;


public class Controller {

    private Logic logic;

    public Controller() {
        this.logic = new Logic();
    }

    @FXML Button myButton;
    @FXML Button continueButton;
    @FXML MenuItem updateGroup;
    @FXML TreeView <String> treeView;
    @FXML MenuItem joinMenuItem;

    @FXML Circle myCircle;

    public void changeCirleColor(){
        System.out.println("Green");
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
