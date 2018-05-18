package gcom.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    private FXMLLoader loader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        String os = System.getProperty("os.name");

        if(os.equals("Linux")) {
            //These 2 lines are for Linux!
            URL url = new File("src/main/java/gcom/gui/Start.fxml").toURL();
            loader = new FXMLLoader(url);
        } else if(os.equals("Windows")) {
            //This line is for Windows!
            loader = new FXMLLoader(Main.class.getResource("Start.fxml"));
        }
        Parent root = loader.load();


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
