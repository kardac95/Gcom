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

public class StartController {
    @FXML Button continueToGuiButton;

    public StartController() {

    }
    public void changeSceneToGUI(ActionEvent event) throws IOException {
        Stage appStage;
        Parent root;
        if(event.getSource()==continueToGuiButton)
        {
            appStage=(Stage)continueToGuiButton.getScene().getWindow();

            URL url = new File("src/main/java/GUI/gui.fxml").toURL();
            FXMLLoader loader = new FXMLLoader(url);
            root = loader.load();

            Scene scene=new Scene(root);
            appStage.setScene(scene);
            appStage.show();
        }
    }
}
