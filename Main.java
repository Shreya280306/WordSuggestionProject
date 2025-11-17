import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

        // INPUT LISTENER
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

        // UPDATE BUTTON HANDLER
        updateButton.setOnAction(e -> {

            String prefix = currentPrefix;
            String newWord = fullTypedWord;

            if (newWord.isEmpty()) return;

            WordSearchNode node = WordSearchFunctions.searchingSubstring(root, prefix);

            if (node == null) {
                WordSearchFunctions.insertingNodeInTrie(root, prefix);
                node = WordSearchFunctions.searchingSubstring(root, prefix);
                node.suggestions = new String[]{"", "", "", "", ""};
                node.freqArray = new int[]{0, 0, 0, 0, 0};
            }

            int minIndex = 0;
            int minVal = node.freqArray[0];

            for (int i = 1; i < 5; i++) {
                if (node.freqArray[i] < minVal) {
                    minVal = node.freqArray[i];
                    minIndex = i;
                }
            }

            node.suggestions[minIndex] = newWord;
            node.freqArray[minIndex] = 1;

            messageLabel.setText("✔ Added to library!");
            messageLabel.setVisible(true);
            updateButton.setVisible(false);

            suggestions.getItems().clear();

            String[] updated = WordSearchFunctions.getSuggestions(root, prefix);
            if (updated != null) {
                for (String w : updated) {
                    if (w != null && !w.isEmpty()) {
                        suggestions.getItems().add(w);
                    }
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
