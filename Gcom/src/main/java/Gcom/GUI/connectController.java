package Gcom.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class connectController {

    public Logic logic;

    public connectController() {

    }

    @FXML Button connectButton;

    public void changeSceneToGUI(ActionEvent event) throws IOException {
        Stage appStage;
        Parent root;
        if(event.getSource()==connectButton)
        {
            appStage=(Stage)connectButton.getScene().getWindow();

            URL url = new File("src/main/java/GUI/gui.fxml").toURL();
            FXMLLoader loader = new FXMLLoader(url);
            root = loader.load();

            Scene scene=new Scene(root);
            appStage.setScene(scene);
            appStage.show();
        }
    }


    public Logic getConnectILogic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

}
