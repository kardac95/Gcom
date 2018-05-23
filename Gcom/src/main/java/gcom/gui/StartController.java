package gcom.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class StartController {

    private FXMLLoader loader;

    public StartController() {
    }

    @FXML Button continueToGuiButton;
    @FXML TextField userName;
    @FXML TextField port;
    @FXML Text text;


    public String getUname(){
        String uName = userName.getText();
        return uName;
    }

    public String getPort() {
        String portName = port.getText();
        return portName;
    }

    public void changeSceneToGUI(ActionEvent event) throws IOException {

        if(event.getSource()==continueToGuiButton)
        {
            if(!getPort().matches("[0-9]+")) {
                text.setText("Not a valid port number");
                return;
            }
            else if(!between(Integer.parseInt(getPort()),1025, 65535)) {
                text.setText("Not a valid port number");
                return;
            }

            Stage appStage=(Stage)continueToGuiButton.getScene().getWindow();

            String os = System.getProperty("os.name");

            if(os.equals("Linux") || os.equals("Windows 10")) {
                //These 2 lines are for Linux!
                URL url = new File("src/main/java/gcom/gui/gui.fxml").toURL();
                loader = new FXMLLoader(url);
            } else if(os.equals("Windows")) {
                //This line is for Windows!
                loader = new FXMLLoader(Main.class.getResource("gui.fxml"));
            }
            Parent root = loader.load();

            //SENDING TO gui CONTROLLER
            GuiController g = loader.getController();

            //new Thread(this.logic.monitorGroupManager(g));
            g.setUserName(getUname() + "    :   " + getPort());
            g.init();
            g.setGUILogic(new Logic(getUname(), getPort()));
            g.updateTree();
            g.addWelcomeTab();

            appStage.setScene(new Scene(root));
            appStage.setOnCloseRequest(windowEvent -> Platform.exit());
            appStage.show();
            g.monitorGroupManager();

        }
    }

    public static boolean between(int i, int minValue, int maxValue) {
        if (i >= minValue && i <= maxValue) {
            return true;
        }
        else{
            return false;
        }
    }
}
