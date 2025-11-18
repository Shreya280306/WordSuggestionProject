import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;

public class Main extends Application {

    private WordSearchNode root;

    private Label messageLabel;
    private Button updateButton;

    private ListView<String> suggestions;

    private String currentPrefix = "";
    private String fullTypedWord = "";

    @Override
    public void start(Stage stage) {

        root = new WordSearchNode();
        WordSearchFunctions.readWordsFromFile1(root, "C:\\Users\\shrey\\Downloads\\file with words.csv");
        WordSearchFunctions.readSuggestionsFromFile2(root, "C:\\Users\\shrey\\Downloads\\file with substring suggestions.csv");

        TextField input = new TextField();
        input.setPromptText("Type a word...");
        input.getStyleClass().add("input-box");

        suggestions = new ListView<>();
        suggestions.getStyleClass().add("suggestion-list");

        messageLabel = new Label("");
        messageLabel.getStyleClass().add("message-label");
        messageLabel.setVisible(false);

        updateButton = new Button("Add to Library");
        updateButton.getStyleClass().add("update-button");
        updateButton.setVisible(false);
        
        input.textProperty().addListener((obs, oldText, newText) -> {

            fullTypedWord = newText.trim().toLowerCase();
            currentPrefix = fullTypedWord;

            suggestions.getItems().clear();
            messageLabel.setVisible(false);
            updateButton.setVisible(false);

            if (newText.isEmpty()) return;

            String[] result = WordSearchFunctions.getSuggestions(root, currentPrefix);

            if (result != null) {
                for (String w : result) {
                    if (w != null && !w.isEmpty()) {
                        suggestions.getItems().add(w);
                    }
                }
            }

            suggestions.getSelectionModel().clearSelection();

            if (suggestions.getItems().isEmpty()) {
                updateButton.setVisible(true);
            }
        });

        suggestions.setOnMouseClicked(e -> {

            String selected = suggestions.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            WordSearchNode node = WordSearchFunctions.searchingSubstring(root, currentPrefix);
            if (node != null) {
                for (int i = 0; i < 5; i++) {
                    if (selected.equals(node.suggestions[i])) {
                        node.freqArray[i] += 1;
                        break;
                    }
                }
            }

            messageLabel.setText("✔ Frequency updated!");
            messageLabel.setVisible(true);
        });

        updateButton.setOnAction(e -> {

            String fullWord = input.getText().toLowerCase();
            StringBuilder substring = new StringBuilder();

            for (int i = 0; i < fullWord.length(); i++) {

                substring.append(fullWord.charAt(i));
                if (substring.length() < 2)
                    continue;

                String sub = substring.toString();
                WordSearchNode node = WordSearchFunctions.createSubstringNode(root, sub);
                if (node == null)
                    continue;
                String[] current = WordSearchFunctions.getSuggestions(root, sub);
                if (current == null)
                    current = new String[5];
                String[] updated = Arrays.copyOf(current, current.length);

                boolean exists = false;
                for (String s : updated) {
                    if (fullWord.equals(s)) {
                        exists = true;
                        break;
                    }
                }
                if (exists)
                    continue;

                int position = 0;
                int least = node.freqArray[0];

                for (int j = 0; j < 5; j++) {
                    if (node.freqArray[j] == 0) {
                        position = j;
                        break;
                    }
                    if (node.freqArray[j] < least) {
                        least = node.freqArray[j];
                        position = j;
                    }
                }

                updated[position] = fullWord;
                node.freqArray[position] = 1;

                WordSearchFunctions.insertSuggestions(root, sub, updated);
            }

            messageLabel.setText("✔ Added to library!");
            messageLabel.setVisible(true);
            updateButton.setVisible(false);

            suggestions.getItems().clear();

            String[] updatedFinal = WordSearchFunctions.getSuggestions(root, fullWord);
            if (updatedFinal != null) {
                for (String w : updatedFinal) {
                    if (w != null && !w.isEmpty())
                        suggestions.getItems().add(w);
                }
            }
        });



        VBox layout = new VBox(12);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(input, suggestions, updateButton, messageLabel);

        Scene scene = new Scene(layout, 450, 420);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("Word Suggestion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
