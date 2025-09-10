package chip;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A GUI for Chip using FXML.
 */
public class Main extends Application {

    private Chip chip = new Chip("./data/chip.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setTitle("Chip Task Manager");
            stage.setScene(scene);
            stage.setMinHeight(600);
            stage.setMinWidth(400);
            fxmlLoader.<MainWindow>getController().setChip(chip);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}