package gcom.tests;

import gcom.gui.GuiController;
import gcom.gui.Logic;
import gcom.gui.Main;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class TestPane extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(new File("src/main/java/gcom/gui/Start.fxml").toURL());

        primaryStage.setTitle("GCom");
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    @Override
    public void stop() {
        System.exit(1);
    }
}
