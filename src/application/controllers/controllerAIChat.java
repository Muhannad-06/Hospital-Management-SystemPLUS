package application.controllers;

import application.services.ChatBotService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class controllerAIChat {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox messageContainer;
    @FXML
    private TextField txtInput;
    @FXML
    private Button btnSend;

    private final ChatBotService bot = new ChatBotService();

    @FXML
    public void initialize() {
    	// Press Enter to Send a message
        txtInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSend(null);
            }
        });
        String greeting = "Welcome. I am Dr. Muhannad, Emergency Doctor.\n Available now for your medical consultation.\nPlease describe your symptoms or concerns";
        addMessage(greeting, false);
    }

    @FXML
    private void handleSend(ActionEvent e) {
        String text = txtInput.getText().trim();
        if (text.isEmpty()) return;
        addMessage(text, true);
        txtInput.clear();
        String reply = bot.getResponse(text);
        addMessage(reply, false);
    }

    private void addMessage(String text, boolean isUser) {
        Text msg = new Text(text);
        msg.setWrappingWidth(340);
        msg.setFont(Font.font("System", 15));
        msg.setTextAlignment(isUser ? TextAlignment.RIGHT : TextAlignment.LEFT);

        Label bubble = new Label();
        bubble.setGraphic(msg);
        bubble.setWrapText(true);
        bubble.setPadding(new Insets(8, 12, 8, 12));
        bubble.setMaxWidth(360);
        
        // Differ between The user and the consultant message
        if (isUser) {
            bubble.setStyle("-fx-background-color: #DCF8C6; -fx-background-radius: 15;");
        } else {
            bubble.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 15; -fx-border-color: #E0E0E0; -fx-border-radius: 15;");
        }

        HBox row = new HBox(bubble);
        row.setPadding(new Insets(4, 8, 4, 8));
        if (isUser) {
            row.setAlignment(Pos.CENTER_RIGHT);
        } else {
            row.setAlignment(Pos.CENTER_LEFT);
        }

        messageContainer.getChildren().add(row);
        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }
}
