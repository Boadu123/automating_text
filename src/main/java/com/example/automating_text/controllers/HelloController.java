package com.example.automating_text.controllers;

import com.example.automating_text.analysis.TextAnalyzer;
import com.example.automating_text.data.DataManager;
import com.example.automating_text.data.TextData;
import com.example.automating_text.file.FileProcessor;
import com.example.automating_text.regex.RegexProcessor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

public class HelloController {
    // Dependency injection
    private final TextAnalyzer textAnalyzer = new TextAnalyzer();
    private final FileProcessor fileProcessor = new FileProcessor();
    private final RegexProcessor regexProcessor = new RegexProcessor();
    private final DataManager dataManager = new DataManager();
    private static final Logger logger = Logger.getLogger(FileProcessor.class.getName());

    // UI Components
    @FXML private TextArea contentArea;
    @FXML private TextField filePathField;
    @FXML private TextField searchWordField;
    @FXML private TextField regexPatternField;
    @FXML private TextField replacementField;
    @FXML private TextField dataIdField;
    @FXML private Label statusLabel;
    @FXML private TabPane tabPane;
    @FXML private ListView<String> wordCountList;
    @FXML private ListView<String> regexMatchesList;

    @FXML
    protected void onBrowseClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        File file = fileChooser.showOpenDialog(contentArea.getScene().getWindow());
        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    protected void onLoadFileClick() {
        try {
            String content = fileProcessor.readFile(filePathField.getText());
            contentArea.setText(content);
            updateStatus("File loaded successfully");
            logger.info("Successfully loaded file: " + filePathField.getText());
        } catch (IOException e) {
            logger.severe("Error reading file '" + filePathField.getText() + "': " + e.getMessage());
            showError("Failed to load file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.severe("Invalid file path: " + e.getMessage());
            showError(e.getMessage());
        }
    }

    @FXML
    protected void onSaveFileClick() {
        logger.info("User requested to save file: "+ filePathField.getText());

        try {
            fileProcessor.writeFile(filePathField.getText(), contentArea.getText());
            updateStatus("File saved successfully");
            logger.info("Successfully saved File");
        } catch (IOException | IllegalArgumentException e) {
            logger.severe("Error writing file  " + filePathField.getText() + e.getMessage() + e);
            showError(e.getMessage());
        }
    }

    @FXML
    protected void onCountWordsClick() {
        try {
            Map<String, Long> counts = textAnalyzer.numberOfWords(contentArea.getText());
            logger.info("User triggered word count");
            wordCountList.getItems().clear();
            counts.forEach((word, count) ->
                    wordCountList.getItems().add(word + ": " + count)
            );
            updateStatus("Word counts calculated");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    protected void onFindWordClick() {
        try {
            Map<String, Long> count = textAnalyzer.countWord(
                    contentArea.getText(),
                    searchWordField.getText()
            );
            wordCountList.getItems().clear();
            count.forEach((word, cnt) ->
                    wordCountList.getItems().add(word + ": " + cnt)
            );
            updateStatus("Word count calculated");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    protected void onFindRegexClick() {
        try {
            List<String> matches = regexProcessor.searchMatches(
                    regexPatternField.getText(),
                    contentArea.getText()
            );
            regexMatchesList.getItems().clear();
            regexMatchesList.getItems().addAll(matches);
            updateStatus("Found " + matches.size() + " matches");
            logger.info("Found {} matches for pattern: " + matches.size() + regexPatternField.getText());
        } catch (PatternSyntaxException e)  {
            showError(e.getMessage());
            logger.severe("Invalid regex: " + e.getDescription() + " (at position " + e.getIndex() + ")");
        }catch (IllegalArgumentException e) {
            showError("Invalid input: " + e.getMessage());
            logger.warning("Regex input validation failed: " + e.getMessage());
        }
    }

    @FXML
    protected void onReplaceRegexClick() {
        try {
            String result = regexProcessor.replaceMatches(
                    regexPatternField.getText(),
                    contentArea.getText(),
                    replacementField.getText()
            );
            if (result.equals(contentArea.getText())) {
                updateStatus("No matches found for replacement");
            } else {
                contentArea.setText(result);
                updateStatus("Replacement completed");
            }
            logger.info("Replaced matches for pattern: " + regexPatternField.getText());
            updateStatus("Replacement completed");
        } catch (PatternSyntaxException e)  {
            showError(e.getMessage());
            logger.severe("Regex syntax error: " + e.getMessage());
        }catch (IllegalArgumentException e) {
            showError("Invalid input: " + e.getMessage());
            logger.warning("Regex input validation failed: " + e.getMessage());
        }
    }

    @FXML
    protected void onSaveDataClick() {
        try {
            TextData data = new TextData(
                    dataIdField.getText(),
                    contentArea.getText()
            );
            dataManager.addData(data);
            updateStatus("Data saved with ID: " + data.getId());
        } catch (IllegalArgumentException e) {
            logger.warning("Attempted to load non-existing data ID: " + dataIdField.getText());
            showError(e.getMessage());
        }
    }

    @FXML
    protected void onLoadDataClick() {
        try {
            TextData data = dataManager.getData(dataIdField.getText());
            contentArea.setText(data.getContent());
            updateStatus("Data loaded: " + data.getId());
        } catch (NullPointerException e) {
            logger.warning("Attempted to load non-existing data ID: " + dataIdField.getText());
            showError("No data found with this ID "+ e.getMessage());
        }
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private void showError(String message) {
        statusLabel.setText("Error: " + message);
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }

    public void handleExit(ActionEvent actionEvent) {
    }
}