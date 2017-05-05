import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * WelcomePage class stands for the welcome window for user to choose the AI level and starting person
 *
 * Created by BoogieJay
 * 5/1/17.
 */
public class WelcomePage {
    private static final String GAME_NAME = "Tic Tac Toe";
    private static int[] answer = new int[2];

    /**
     * display the window and return the user choices
     *
     * @return
     */
    public static int[] display() {
        Stage window = new Stage();
        window.setTitle(GAME_NAME);

        Button button = new Button("Fire The Game!");

        ChoiceBox<String> AIBox = new ChoiceBox<>();
        ChoiceBox<String> playerBox = new ChoiceBox<>();

        Label levelLable = new Label("AI level:");
        Label playerLabel = new Label("Starting Player:");

        AIBox.getItems().addAll("EASY", "INTERMEDIATE", "DIFFICULT");
        playerBox.getItems().addAll("YOU", "AI");

        // set default value
        AIBox.setValue("EASY");
        playerBox.setValue("YOU");

        // set button action
        button.setOnAction(e -> {
            getChoice(AIBox, playerBox);
            window.close();
        });

        // force user to start
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

    /**
     * extract the result selected by user and store in array
     * @param aiBox
     * @param playerBox
     */
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
