package gcom.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class TestGui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = new File("src/main/java/gcom/tests/Test.fxml").toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("kalle");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
