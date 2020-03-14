package application.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    	ViewModel.get().makeConnection();
    	this.enableChat(true);
    }
    
    @FXML
    private void onInputKeyPressed(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
    		ViewModel.get().send();
    		this.inputTextArea.textProperty().setValue("");
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
