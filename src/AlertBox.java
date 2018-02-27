import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

/**
 * AlertBox class has static method to display the alert information, which implemented with JavaJX
 *
 * Created by TN
 * 5/3/17.
 */

public class AlertBox {

    /**
     * display the alert window
     *
     * @param title
     * @param message
     */
    public static void display(String title, String message) {
        Stage window = new Stage();

        // block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("End the Game");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        // display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }

}
