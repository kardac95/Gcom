package Gcom.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GroupController {
    public Logic logic;

    @FXML Button createButton;
    @FXML TextField groupName;

    public GroupController() {

    }

    public void changeSceneToGUI(ActionEvent event) throws IOException {
        Stage appStage;
        Parent root;
        if(event.getSource()==createButton)
        {
            appStage=(Stage)createButton.getScene().getWindow();

            URL url = new File("src/main/java/Gcom/GUI/gui.fxml").toURL();
            FXMLLoader loader = new FXMLLoader(url);
            root = loader.load();

            //SENDING
            GuiController g = loader.getController();
            logic.getGM().createGroup(groupName.getText());
            g.setUserName(logic.getUserName());
            g.setGUILogic(logic);
            g.updateTree(logic);
            Scene scene=new Scene(root);
            appStage.setScene(scene);
            appStage.show();
        }
    }

    public void setGroupLogic(Logic logic) {
        this.logic = logic;
    }
}
