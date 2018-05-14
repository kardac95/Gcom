package gcom.gui;

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

public class StartController {


    public Logic logic;

    public StartController() {
        //this.logic = new Logic();
    }

    @FXML Button continueToGuiButton;
    @FXML TextField userName;
    @FXML TextField port;


    public String getUname(){
        String uName = userName.getText();
        return uName;
    }

    public String getPort() {
        String portName = port.getText();
        return portName;
    }

    public void changeSceneToGUI(ActionEvent event) throws IOException {
        Stage appStage;
        Parent root;
        if(event.getSource()==continueToGuiButton)
        {
            appStage=(Stage)continueToGuiButton.getScene().getWindow();

            URL url = new File("src/main/java/gcom/gui/gui.fxml").toURL();
            FXMLLoader loader = new FXMLLoader(url);
            root = loader.load();

            //SENDING TO gui CONTROLLER
            GuiController g = loader.getController();

            this.logic = new Logic(getUname(), getPort());
            //new Thread(this.logic.updateTask(g));

            g.setUserName(getUname());
            g.updateTree(logic);
            logic.updateTask(g);

            Scene scene=new Scene(root);
            appStage.setScene(scene);
            appStage.show();
        }
    }

}