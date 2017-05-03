import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by BoogieJay
 * 5/1/17.
 */
public class WelcomePage {

    private static int[] answer = new int[2];

    public static int[] display() {
        Stage window = new Stage();
        window.setTitle("Tic Tac Toe");

        Button button = new Button("Fire The Game!");

        ChoiceBox<String> AIBox = new ChoiceBox<>();
        ChoiceBox<String> playerBox = new ChoiceBox<>();

        Label levelLable = new Label("AI level:");
        Label playerLabel = new Label("Starting Player:");

        //getItems returns the ObservableList object which you can add items to
        AIBox.getItems().addAll("EASY", "INTERMEDIATE", "DIFFICULT");
        playerBox.getItems().addAll("AI", "YOU");

        //Set a default value
        AIBox.setValue("EASY");
        playerBox.setValue("YOU");

        button.setOnAction(e -> {
            getChoice(AIBox, playerBox);
            window.close();
        });

        window.setOnCloseRequest(event -> {
            event.consume();
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        button.setTranslateX(75);
        button.setTranslateY(50);
        layout.getChildren().addAll(levelLable, AIBox, playerLabel, playerBox, button);

        Scene scene = new Scene(layout, 300, 300);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }

    private static void getChoice(ChoiceBox<String> aiBox, ChoiceBox<String> playerBox) {
        String AIResult = aiBox.getValue();
        String playerResult = playerBox.getValue();

        switch (AIResult) {
            case "EASY": answer[0] = 1; break;
            case "INTERMEDIATE": answer[0] = 2; break;
            case "DIFFICULT": answer[0] = 3; break;
        }

        switch (playerResult) {
            case "AI": answer[1] = 0; break;
            case "YOU": answer[1] = 1; break;
        }
    }


}
