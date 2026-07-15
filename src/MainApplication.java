import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("main-view.fxml"));

            Scene scene = new Scene(loader.load(), 1000, 650);

            stage.setTitle("Malabe Spares Depot");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}