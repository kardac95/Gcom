package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = new File("src/main/java/GUI/Start.fxml").toURL();

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();

        primaryStage.setTitle("GCom");
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
