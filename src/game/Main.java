package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
public Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("blackJackWithDeck.fxml"));
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 1240, 390));
        stage.setResizable(false);
        stage.show();

        AnimationTimer checkQuit = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(Controller.quit)
                    stage.close();
            }
        };
        checkQuit.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
