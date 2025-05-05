package com.example.automating_text.controllers;

import com.example.automating_text.analysis.TextAnalyzer;
import com.example.automating_text.data.DataManager;
import com.example.automating_text.data.TextData;
import com.example.automating_text.file.FileProcessor;
import com.example.automating_text.regex.RegexProcessor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(contentArea.getScene().getWindow());

        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
            // Automatically load the file after selection
            loadFileContent(file);
        }
    }

    private void loadFileContent(File file) {
        try {
            if (file == null || !file.exists()) {
                showError("File does not exist");
                return;
            }

            String content = fileProcessor.readFile(file.getAbsolutePath());
            if (content == null || content.isEmpty()) {
                showError("File is empty");
                return;
            }

            contentArea.setText(content);
            updateStatus("File loaded successfully: " + file.getName());
            logger.info("Successfully loaded file: " + file.getAbsolutePath());
        } catch (IOException e) {
            logger.severe("Error reading file '" + file.getAbsolutePath() + "': " + e.getMessage());
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
        } catch (IOException e) {
            logger.severe("Error writing file  " + filePathField.getText() + e.getMessage() + e);
            showError(e.getMessage());
        } catch(IllegalArgumentException e) {
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


    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private void showError(String message) {
        statusLabel.setText("Error: " + message);
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }

    @FXML private ListView<TextData> dataList;
    private final ObservableList<TextData> observableDataList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize the data list with custom cell factory
        dataList.setCellFactory(param -> new ListCell<TextData>() {
            @Override
            protected void updateItem(TextData item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Display ID and first 30 characters of content
                    setText("ID: " + item.getId() + " - Content: " +
                            (item.getContent().length() > 30 ?
                                    item.getContent().substring(0, 30) + "..." :
                                    item.getContent()));
                }
            }
        });

        // Load all data at startup
        refreshDataList();

        // Add selection listener
        dataList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                dataIdField.setText(newVal.getId());
                contentArea.setText(newVal.getContent());
                updateStatus("Selected item with ID: " + newVal.getId());
            }
        });
    }

    private String summarize(String content) {
        return content.length() > 30 ? content.substring(0, 30).replaceAll("\n", " ") + "..." : content;
    }



    // Helper method to refresh the list view
    private void refreshDataList() {
        try {
            observableDataList.setAll(dataManager.getAllData());
            if (!observableDataList.isEmpty()) {
                dataList.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            showError("Error loading data: " + e.getMessage());
        }
    }

    private String generateNewId() {
        return "data-" + System.currentTimeMillis();
    }

    @FXML
    protected void onDeleteDataClick() {
        String id = dataIdField.getText().trim();
        if (id.isEmpty()) {
            showError("Please enter an ID to delete");
            return;
        }

        try {
            dataManager.deleteData(id);
            observableDataList.removeIf(d -> d.getId().equals(id));
            dataIdField.clear();
            contentArea.clear();
            updateStatus("Data deleted with ID: " + id);
        } catch (Exception e) {
            showError("Delete failed: " + e.getMessage());
        }
    }


    // Add these to your existing controller
    @FXML
    protected void onNewDataClick(ActionEvent event) {
        dataIdField.clear();
        contentArea.clear();
        dataIdField.setPromptText("ID will be auto-generated");
        updateStatus("Ready for new entry");
    }

    @FXML
    protected void onSaveDataClick(ActionEvent event) {
        try {
            String id = dataIdField.getText().trim();
            String content = contentArea.getText().trim();

            if (content.isEmpty()) {
                showError("Content cannot be empty");
                return;
            }

            // Generate ID if empty
            if (id.isEmpty()) {
                id = "data-" + UUID.randomUUID().toString().substring(0, 8);
                dataIdField.setText(id);
            }

            TextData data = new TextData(id, content);

            // Check if this is an update or new entry
            if (dataManager.getData(id) != null) {
                dataManager.updateData(data);
                updateStatus("Updated data with ID: " + id);
            } else {
                dataManager.addData(data);
                updateStatus("Saved new data with ID: " + id);
            }

            refreshDataList();

            // Select the newly added/updated item
            dataList.getSelectionModel().select(data);
        } catch (Exception e) {
            showError("Error saving data: " + e.getMessage());
        }
    }

    @FXML
    protected void onLoadDataClick(ActionEvent event) {
        try {
            String id = dataIdField.getText().trim();
            if (id.isEmpty()) {
                showError("Please enter an ID");
                return;
            }
            TextData data = dataManager.getData(id);
            contentArea.setText(data.getContent());
            updateStatus("Loaded: " + id);
        } catch (Exception e) {
            showError("Error loading: " + e.getMessage());
        }
    }

    @FXML
    protected void onUpdateDataClick() {
        String id = dataIdField.getText().trim();
        if (id.isEmpty()) {
            showError("Please enter an ID to update");
            return;
        }

        try {
            TextData updated = new TextData(id, contentArea.getText());
            dataManager.updateData(updated);

            observableDataList.removeIf(d -> d.getId().equals(id));
            observableDataList.add(updated);

            updateStatus("Data updated with ID: " + id);
        } catch (Exception e) {
            showError("Update failed: " + e.getMessage());
        }
    }

    @FXML
    protected void onListAllDataClick() {
        try {
            List<TextData> all = dataManager.getAllData();
            observableDataList.setAll(all);
            updateStatus("Loaded all data items");
        } catch (Exception e) {
            showError("Failed to list data: " + e.getMessage());
        }
    }
}