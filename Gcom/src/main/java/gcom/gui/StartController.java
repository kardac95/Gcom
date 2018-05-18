package gcom.gui;

import javafx.application.Platform;
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
    private FXMLLoader loader;

    public StartController() {

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

            String os = System.getProperty("os.name");

            if(os.equals("Linux")) {
                //These 2 lines are for Linux!
                URL url = new File("src/main/java/gcom/gui/gui.fxml").toURL();
                loader = new FXMLLoader(url);
            } else if(os.equals("Windows")) {
                //This line is for Windows!
                loader = new FXMLLoader(Main.class.getResource("gui.fxml"));
            }
            root = loader.load();

            //SENDING TO gui CONTROLLER
            GuiController g = loader.getController();

            this.logic = new Logic(getUname(), getPort());
            //new Thread(this.logic.monitorGroupManager(g));

            g.setUserName(getUname());
            g.init();
            g.setGUILogic(logic);
            g.updateTree();


            Scene scene=new Scene(root);
            appStage.setScene(scene);
            appStage.setOnCloseRequest(windowEvent -> Platform.exit());
            appStage.show();
            g.monitorGroupManager();
        }
    }


}
