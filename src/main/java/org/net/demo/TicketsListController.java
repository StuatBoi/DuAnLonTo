package org.net.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.util.List;
import java.util.Map;

import org.net.demo.DTO.TicketDTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import java.lang.reflect.Type;

public class TicketsListController extends Controller {
 
    private int currentPage=0;
    private int totalPage=0;
    
    @FXML
    private Button btnFirstPage;

    @FXML
    private Button btnLastPage;

    @FXML
    private Button btnNextPage;

    @FXML
    private Button btnPrevPage;

    @FXML
    private Label lblPageIndicator;

    @FXML
    private FlowPane ticketsContainer;

    @FXML
    private TextField txtJumpPage;

    @FXML
    public void initialize() {
        // initialization if needed
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        mainController.showPage(mainController.getLastPage());
    }

    @FXML
    void handleFirstPage(ActionEvent event) {
          goToPage(0);
    }

    @FXML
    void handleJumpPage(ActionEvent event) {
      String value = txtJumpPage.getText().trim();
    if (value.isEmpty()) {
        CineverseAlert.showToast("Vui lòng nhập số trang cần đến!", txtJumpPage);
        return;
    }
    
    try {
        int targetPage = Integer.parseInt(value)-1;
        System.out.println("jump page : "+ targetPage);
        if (targetPage < 0 || targetPage >= totalPage) {
            CineverseAlert.showToast("Số trang hợp lệ là từ 1 đến " + totalPage, txtJumpPage);
            return;
        }
    
        currentPage = targetPage;
        clearPage();
        
        goToPage(targetPage);
        
    } catch (NumberFormatException e) {
        CineverseAlert.showToast("Số trang nhập vào phải là ký tự số!", txtJumpPage);
    }
    }

    @FXML
    void handleLastPage(ActionEvent event) {
         goToPage(totalPage-1);
    }

    @FXML
    void handleNextPage(ActionEvent event) {
        if(currentPage + 1 >= totalPage )
        {
            CineverseAlert.showToast("Đã ở trang cuối", btnFirstPage);
        }
        else goToPage(currentPage+1);
    }

    @FXML
    void handlePrevPage(ActionEvent event) {
           if(currentPage - 1 <0 )
        {
            CineverseAlert.showToast("Đã ở trang đầu", btnFirstPage);
        }
        else goToPage(currentPage-1);
    }

    @Override
    public void OnShowing() {
      goToPage(0);
    }

    @Override
    public void Refresh() {
         goToPage(currentPage);
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

    public void loadTickets(List<TicketDTO> ticketDTOs) {
        for(TicketDTO ticket : ticketDTOs)
                    {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketItem.fxml"));
                            Parent ticketItem = loader.load();
                            TicketItemController controller = loader.getController();
                            controller.setData(ticket);
                            controller.getMainController(mainController);
                            ticketsContainer.getChildren().add(ticketItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
    }

    public void goToPage(Integer page)
    {
        Map<String,Object> params= Map.of("page",page);
        HTTPService.sendFullRequestAsync("GET", "/api/Ticket/getUsersTicketPage", params, null, mainController.getToken())
        .thenAccept(response->
            {
             if(response.statusCode()==200)
             {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(java.time.LocalDateTime.class, new LocalDateTimeAdapter())
                        .create();
                Type pageResponseType = new TypeToken<PageResponse<TicketDTO>>(){}.getType();
                PageResponse<TicketDTO> tiPageResponse= gson.fromJson(response.body(), pageResponseType);
                List<TicketDTO> ticketDTOs= tiPageResponse.getContent();
                Platform.runLater(()->{
                    
                    clearPage();
                    loadTickets(ticketDTOs);
                    setPageNavData(page, tiPageResponse.getTotalPages());
                });
             }
             else{
                CineverseAlert.showToast("Không thể lấy thông tin vé "+ response.statusCode(), btnFirstPage);
             }
             
            }
        );
    }

    public void setPageNavData(int currentPage,int totalPage)
    {
      this.currentPage=currentPage;System.out.print("current page set = "+ currentPage);
      this.totalPage=totalPage;System.out.print("totalPage set = "+totalPage);
      lblPageIndicator.setText(""+ (currentPage+1)+"/"+ (totalPage));
    }

    public void clearPage()
    {
        ticketsContainer.getChildren().clear();
    }
    
}
