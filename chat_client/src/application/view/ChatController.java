package application.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChatController {

	@FXML
    private AnchorPane pane;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private HBox loginDisplayBox;

    @FXML
    private Label usernameDisplayLabel;

    @FXML
    private VBox chatBox;

    @FXML
    private TextArea mainChatTextArea;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private Button joinButton;

    @FXML
    private void initialize() {
    	this.enableChat(false);
    	this.setBindings();
    	this.inputTextArea.textProperty().setValue("");
    }
    
    @FXML
    private void onJoinButtonAction(ActionEvent event) {
    	try {
			ViewModel.get().makeConnection();
			this.enableChat(true);
			this.inputTextArea.requestFocus();
		} catch (IOException e) {
			Alert serverUnavailable = new Alert(AlertType.ERROR);
			serverUnavailable.setGraphic(null);
			serverUnavailable.getDialogPane().getChildren().clear();
			
			TextArea information = new TextArea();
			information.setWrapText(true);
			information.setPrefHeight(0);
			information.setEditable(false);
			information.setText("The chat server is not available at this time.");
			
			serverUnavailable.getDialogPane().setContent(information);
			serverUnavailable.showAndWait();
		}
    }
    
    @FXML
    private void onInputKeyPressed(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
    		ViewModel.get().send();
    		this.inputTextArea.textProperty().setValue("");
    	}
    }
    
    @FXML
    private void usernameOnKeyPressed(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
    		this.joinButton.requestFocus();
    	}
    }
    
    private void enableChat(boolean shouldEnable) {
    	this.usernameLabel.setVisible(!shouldEnable);
    	this.usernameTextField.setVisible(!shouldEnable);
    	this.joinButton.setVisible(!shouldEnable);
    	
    	this.mainChatTextArea.setDisable(!shouldEnable);
    	this.inputTextArea.setDisable(!shouldEnable);
    	this.loginDisplayBox.setVisible(shouldEnable);
    }
    
    private void setBindings() {
    	this.mainChatTextArea.textProperty().bind(ViewModel.get().chatProperty());
    	ViewModel.get().inputProperty().bind(this.inputTextArea.textProperty());
    	ViewModel.get().usernameProperty().bind(this.usernameTextField.textProperty());
    	this.usernameDisplayLabel.textProperty().bind(ViewModel.get().usernameProperty());
    }
}
