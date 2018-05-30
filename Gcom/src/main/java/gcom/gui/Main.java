package gcom.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private FXMLLoader loader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        if(getParameters().getRaw().size() == 2) {
            String os = System.getProperty("os.name");

            if (os.equals("Linux") || os.equals("Windows 10")) {
                //These 2 lines are for Linux!
                /*URL url = new File("src/main/java/gcom/gui/gui.fxml").toURL();
                loader = new FXMLLoader(url);*/
                loader = new FXMLLoader(getClass().getResource("/gui.fxml"));
            }
            Parent root = loader.load();

            //SENDING TO gui CONTROLLER
            GuiController g = loader.getController();

            //new Thread(this.logic.monitorGroupManager(g));
            g.setUserName(getParameters().getRaw().get(0) + "    :   " + getParameters().getRaw().get(1));
            g.init();
            g.setGUILogic(new Logic(getParameters().getRaw().get(0), getParameters().getRaw().get(1)));
            g.updateTree();
            g.addWelcomeTab();

            primaryStage.setScene(new Scene(root));
            primaryStage.setOnCloseRequest(windowEvent -> Platform.exit());
            primaryStage.show();
            g.monitorGroupManager();

        } else {

            String os = System.getProperty("os.name");

            if (os.equals("Linux") || os.equals("Windows 10")) {
                //These 2 lines are for Linux!
                loader = new FXMLLoader(getClass().getResource("/Start.fxml"));
            }

            Parent root = loader.load();
            primaryStage.setTitle("GCom");
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);

            primaryStage.show();
        }
    }
    @Override
    public void stop() {
        System.exit(1);
    }
}
