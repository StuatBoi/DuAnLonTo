package org.net.demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.net.demo.DTO.Message;
import org.net.demo.Service.LoadingOverlayManager;

import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ChatViewController extends BaseController{

    @FXML
    private Button btnClearChat;

    @FXML
    private Button btnSend;

    @FXML
    private VBox chatBox;

    @FXML
    private VBox chatRoot;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private TextField txtMessage;

    @FXML
    public void initialize() {
       btnClearChat.setOnAction(event -> chatBox.getChildren().clear());
       chatBox.setFillWidth(true);
    
    chatBox.heightProperty().addListener((observable, oldValue, newValue) -> {
        javafx.scene.layout.VBox.setVgrow(chatScrollPane, javafx.scene.layout.Priority.ALWAYS);
        chatScrollPane.setVvalue(1.0); 
    });
    }

    @FXML
    void handleSendMessage(ActionEvent event) {
      
      addMessage(txtMessage.getText(), true);
      Message message= new Message(txtMessage.getText());
      Gson gson=new Gson();
      String jsonbody=gson.toJson(message);
      HBox placeholder = addMessage("Đang chờ phản hồi...", false);
      cleartxtMessage();
      HTTPService.sendFullRequestAsync("POST", "/api/chat", null, jsonbody, mainController.getToken()).thenAcceptAsync(res->
        {
            if(res.statusCode()==200)
            {
                Platform.runLater(() -> updateMessage(placeholder, res.body()));
            }
            else
            {
                Platform.runLater(() -> {
                    chatBox.getChildren().remove(placeholder);
                    CineverseAlert.showToast("Có lỗi xảy ra khi lấy phản hồi từ chat bot, vui lòng phản hồi lại", btnClearChat);
                });
            }
        }
      ).orTimeout(30, TimeUnit.SECONDS).exceptionallyAsync(ex->
        { 
            if (ex.getCause() instanceof TimeoutException) {
            System.err.println("Lỗi: Server không phản hồi trong vòng 10 giây!");
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (Timeout)", btnClearChat));
        } else {
            System.err.println("Lỗi hệ thống khác: " + ex.getMessage());
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (No Connection)", btnClearChat));
        }
        LoadingOverlayManager.stop();
        return null;
    }
     );
    }

    @Override
    public void OnShowing() {
        
    }

    @Override
    public void Refresh() {
        
    }

    @Override
    public void OnAttached() {
        
    }

    @Override
    public void OnLogin() {
        }
        

    @Override
    public void OnLogout() {
        
    }

    @Override
    public void OnExit() {
       clearMessage();
       cleartxtMessage();
    }

    private HBox addMessage(String text, boolean isUser) {
    Label lblMessage = new Label(text);
    lblMessage.setWrapText(true);
    lblMessage.setMaxWidth(450); // Giới hạn độ rộng tối đa của chữ

    VBox bubble = new VBox(lblMessage);
    bubble.getStyleClass().add("msg-bubble");
    // Ép bubble thu nhỏ vừa vặn với nội dung chữ bên trong, tối đa 450px
    bubble.setMaxWidth(Region.USE_PREF_SIZE); 

    HBox wrapper = new HBox(bubble);
    wrapper.setMaxWidth(Double.MAX_VALUE); // Cho phép wrapper giãn hết chiều ngang VBox

    if (isUser) {
        wrapper.getStyleClass().addAll("msg-wrapper", "msg-user");
        wrapper.setAlignment(Pos.CENTER_RIGHT); // Đẩy tin nhắn sát mép phải
    } else {
        wrapper.getStyleClass().addAll("msg-wrapper", "msg-bot");
        wrapper.setAlignment(Pos.CENTER_LEFT);  // Đẩy tin nhắn sát mép trái
    }

    chatBox.getChildren().add(wrapper);
    return wrapper;
}



    private void updateMessage(HBox messageWrapper, String text) {
        if (messageWrapper == null) {
            return;
        }
        for (var child : messageWrapper.getChildren()) {
            if (child instanceof VBox bubble) {
                if (!bubble.getChildren().isEmpty() && bubble.getChildren().get(0) instanceof Label) {
                    Label lblMessage = (Label) bubble.getChildren().get(0);
                    lblMessage.setText(text);
                    break;
                }
            }
        }
    }

    private void clearMessage()
    {
      chatBox.getChildren().clear();
    }
    private void cleartxtMessage()
    {
        txtMessage.clear();
    }
}
