package gcom.gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Controller {
    private FXMLLoader loader;
    private Logic logic;

    public Controller(Logic logic) {
        this.logic = logic;
    }

    public void startGuiController(Stage stage) throws IOException {
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

        g.setUserName(logic.getUserName() + " : " + logic.getPort());
        g.init();
        g.setGUILogic(logic);
        g.updateTree();

        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(windowEvent -> Platform.exit());
        stage.show();
        g.monitorGroupManager();
    }
}
