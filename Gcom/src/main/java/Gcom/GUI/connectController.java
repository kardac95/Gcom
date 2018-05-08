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

public class connectController {

    public Logic logic;

    public connectController() {

    }

    @FXML Button connectButton;
    @FXML TextField groupName;
    @FXML TextField hostName;
    @FXML TextField port;

    public void changeSceneToGUI(ActionEvent event) throws IOException {
        Stage appStage;
        Parent root;
        if(event.getSource()==connectButton)
        {
            appStage=(Stage)connectButton.getScene().getWindow();

            URL url = new File("src/main/java/Gcom/GUI/gui.fxml").toURL();
            FXMLLoader loader = new FXMLLoader(url);
            root = loader.load();

            //SENDING
            GuiController g = loader.getController();
            g.setGUILogic(logic);
            g.setUserName(logic.getUserName());
            g.updateTree(logic);

            Scene scene=new Scene(root);
            appStage.setScene(scene);
            appStage.show();
        }
    }

    public void setConLogic(Logic logic) {
        this.logic = logic;
    }

}
