package sample.ui.main_ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private String currentAccount;
    private String currentPassword;
    private String currentEmail;

    @FXML
    private Label partName;
    @FXML
    private Label account;

    @FXML
    private AnchorPane content;

    private AnchorPane userInfo;
    private UserInfoController userInfoController;
    private AnchorPane sendEmail;
    private SendEmailController sendEmailController;
    private AnchorPane emailList;
    private EmailListController emailListController;

    @FXML
    private void toUserInfo(MouseEvent event){
        toUserInfo();
    }
    private void toUserInfo(){
        partName.setText("User info");
        try {
            if(userInfo == null){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("user_info.fxml"));
                userInfo = loader.load();
                userInfoController = loader.getController();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        clearContentPane();
        //userInfoController.initData();
        content.getChildren().add(userInfo);
    }

    @FXML
    private void toSendEmail(MouseEvent event){
        partName.setText("Send email");
        try {
            if(sendEmail == null){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("send_email.fxml"));
                sendEmail = loader.load();
                sendEmailController = loader.getController();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        clearContentPane();
        sendEmailController.initData(currentAccount, currentPassword, currentEmail);
        content.getChildren().add(sendEmail);
    }

    @FXML
    private void toEmailList(MouseEvent event){
        partName.setText("Email list");
        try {
            if(emailList == null){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("email_list.fxml"));
                emailList = loader.load();
                emailListController = loader.getController();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        clearContentPane();
        //emailListController.initData();
        content.getChildren().add(emailList);
    }

    @FXML
    private void logout(MouseEvent event){
        ((Stage)(content.getScene().getWindow())).close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../login_ui.fxml"));
        Stage stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load(), 500, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setResizable(false);
        stage.setTitle("Login");
        stage.show();
    }

    private void clearContentPane(){
        int size = content.getChildren().size();
        if(size != 0){
            content.getChildren().remove(0, size);
        }
    }

    public void initData(String account, String password, String email){
        currentAccount = account;
        currentPassword = password;
        currentEmail = email;
        this.account.setText(account);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toUserInfo();
    }
}
