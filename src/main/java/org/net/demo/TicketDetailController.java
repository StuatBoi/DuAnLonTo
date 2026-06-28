package org.net.demo;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.net.demo.DTO.TicketDetail;
import org.net.demo.Service.LoadingOverlayManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Platform;
import javafx.event.ActionEvent;

public class TicketDetailController extends BaseController {

    @FXML
    private ImageView imgQRCode;

    @FXML
    private Label lblTicketCode;

    @FXML
    private Label lblMovieTitle;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblRoom;

    @FXML
    private Label lblSeats;

    @FXML
    private Label lblCustomer;

    @FXML
    private Label lblPrice;

    @FXML
    private Button btnClose;

    private String currentTicketCode;

    @FXML
    public void initialize() {
        
    }

    @FXML
    private void onCloseClick(ActionEvent event) {
        mainController.showPage(mainController.getLastPage());
    }

    @Override
    public void OnShowing() {
        
    }

    @Override
    public void Refresh() {
        placeHolder();
        getData(currentTicketCode);
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
    public void placeHolder()
    {
        lblTicketCode.setText("N/A");
        lblCustomer.setText("N/A");
        lblMovieTitle.setText("N/A");
        lblDate.setText("N/A");
        lblTime.setText("N/A");
        lblRoom.setText("N/A");
        lblSeats.setText("N/A");
        lblPrice.setText("N/A");
         imgQRCode.setImage(null);
    }

    public void setData(TicketDetail ticketDetail)
    {
        if(ticketDetail==null)
        {
            System.out.println("ticketDetail is null");
            placeHolder();
            return;
        }
        lblTicketCode.setText(ticketDetail.getTicketCode());
        lblCustomer.setText(ticketDetail.getCustomerName());
        lblMovieTitle.setText(ticketDetail.getMovieTitle());
        lblDate.setText(ticketDetail.getStartTime().toLocalDate().toString());
        lblTime.setText(ticketDetail.getStartTime().toLocalTime().toString());
        lblRoom.setText(ticketDetail.getRoomName());
        lblSeats.setText(ticketDetail.getSeatName());
        lblPrice.setText(String.format("%.2f", ticketDetail.getPrice()));
        loadBase64ToImageView(ticketDetail.getQrCodeBase64(), imgQRCode);
    }
    public void getData(String ticketCode)
    {
        currentTicketCode=ticketCode;
        LoadingOverlayManager.start(btnClose);
        Map<String,Object> params=Map.of("ticketCode", ticketCode);
        HTTPService.sendFullRequestAsync("GET", "/api/Ticket/getTicketDetail", params, null, mainController.getToken()).thenAccept(
            response->{
                try{
                if(response.statusCode()!=200)
                {
                    Platform.runLater(()->{
                        CineverseAlert.showToast("Failed to load ticket details! Please try again.", btnClose);});
                    return;
                }
                if(response.body()==null || response.body().isEmpty())
                {
                    Platform.runLater(()->{
                        CineverseAlert.showToast("No details found for this ticket!", btnClose);});
                     return;
                }
                Gson gson= new GsonBuilder().registerTypeAdapter(java.time.LocalDateTime.class, new LocalDateTimeAdapter())
                        .create();
                TicketDetail ticketDetail=gson.fromJson(response.body(), TicketDetail.class);
                Platform.runLater(()->{
                    setData(ticketDetail);
                });
            }
            catch(Exception e)
            {
                Platform.runLater(()->{
                    LoadingOverlayManager.stop();
                    CineverseAlert.showToast("An error occurred while loading ticket details!", btnClose);
                    e.printStackTrace();
                });
            }
            Platform.runLater(()->LoadingOverlayManager.stop());
        }
        )
        .orTimeout(10,TimeUnit.SECONDS)
     .exceptionallyAsync(ex->
        { 
            if (ex.getCause() instanceof TimeoutException) {
            System.err.println("Lỗi: Server không phản hồi trong vòng 10 giây!");
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (Timeout)", btnClose));
        } else {
            System.err.println("Lỗi hệ thống khác: " + ex.getMessage());
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (No Connection)", btnClose));
        }
        LoadingOverlayManager.stop();
        return null;
    }
     );

    
}
public void loadBase64ToImageView(String base64Data, ImageView imageView) {
        try {
            if (base64Data.contains(",")) {
                base64Data = base64Data.split(",")[1];
            }
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            Image image = new Image(bis);
            imageView.setImage(image);

        } catch (IllegalArgumentException e) {
            System.err.println("Chuỗi Base64 không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi khi load ảnh vào ImageView: " + e.getMessage());
        }
    }

@Override
public void OnExit() {
    
}
}
