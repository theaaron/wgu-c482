package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

/**An inventory app that allows you to manage products and their corresponding parts.
 *
 *
 *JAVADOC LOCATION: task-1/src/javadoc
 *
 * @author Aaron
 * */
public class Main extends Application {
    /**
     * Loads MainScreen controller
     * @param stage
     * */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        root.setStyle("-fx-font-family: 'serif'");
        stage.setTitle("Main Screen");
        stage.setScene(new Scene(root, 1200, 400));
        stage.show();
    }
    /**
     *Launches Program.
     * @param args
     * */
    public static void main(String[] args){
        launch(args);
    }
}
