package GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    public Button myButton;

    @FXML
    TreeView <String> treeView;

    @FXML
    public void setCrapbutton(){

        System.out.println("CRAPPPPPP");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //create dummyroot
        TreeItem<String> dummyroot = new TreeItem<>();
        //create root
        TreeItem<String> root1 = new TreeItem<>("Group1");
        //root.setExpanded(true);
        //create child
        TreeItem<String> nodeA = new TreeItem<>("Node A");
        TreeItem<String> nodeB = new TreeItem<>("Node B");
        TreeItem<String> nodeC = new TreeItem<>("Node C");
        //root is the parent of itemChild
        root1.getChildren().add(nodeA);
        root1.getChildren().add(nodeB);
        root1.getChildren().add(nodeC);


        TreeItem<String> root2 = new TreeItem<>("Group2");
        //root.setExpanded(true);
        //create child
        TreeItem<String> nodeD = new TreeItem<>("Node D");
        TreeItem<String> nodeE = new TreeItem<>("Node E");
        TreeItem<String> nodeF = new TreeItem<>("Node F");
        TreeItem<String> nodeG = new TreeItem<>("Node G");
        //root is the parent of itemChild
        root2.getChildren().add(nodeA);
        root2.getChildren().add(nodeB);
        root2.getChildren().add(nodeC);
        root2.getChildren().add(nodeD);

        dummyroot.getChildren().addAll(root1, root2);

        treeView.setRoot(dummyroot);
    }
}
