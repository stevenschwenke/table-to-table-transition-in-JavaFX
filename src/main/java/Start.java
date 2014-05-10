import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("tableToTableTransition.fxml"));


        Scene scene = new Scene(root, 600, 500);

        stage.setTitle("Table to table transition");
        stage.setScene(scene);
        stage.show();
    }
}
