package peter;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for Peter's main JavaFX window.
 */
public class MainWindow extends AnchorPane {
    private static final Duration EXIT_DELAY = Duration.millis(900);

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private final Image peterImage = new Image(this.getClass().getResourceAsStream("/images/Peter.png"));

    private Peter peter;

    /** Binds the dialog scroll position to the container's height. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects Peter and displays the opening messages for this session.
     *
     * @param peter the chatbot instance backing this window
     */
    public void setPeter(Peter peter) {
        this.peter = peter;
        dialogContainer.getChildren().addAll(
                DialogBox.getPeterDialog(peter.getWelcomeMessage(), peterImage),
                DialogBox.getPeterDialog(peter.getLoadingMessage(), peterImage));
    }

    /**
     * Creates dialog boxes for the user's input and Peter's response, appends
     * them to the dialog container, and clears the input field. Ends the
     * session shortly after a {@code bye} command so the reply stays visible.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isEmpty()) {
            return;
        }

        String response = peter.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getPeterDialog(response, peterImage));
        userInput.clear();

        if (peter.isExit()) {
            endSession();
        }
    }

    /** Disables further input and closes the application after the goodbye is visible. */
    private void endSession() {
        userInput.setDisable(true);
        sendButton.setDisable(true);

        PauseTransition exitPause = new PauseTransition(EXIT_DELAY);
        exitPause.setOnFinished(event -> Platform.exit());
        exitPause.play();
    }
}
