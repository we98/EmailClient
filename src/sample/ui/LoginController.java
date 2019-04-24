package sample.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.smtp.SmtpMailSender;
import sample.ui.main_ui.MainController;

public class LoginController{

    @FXML
    private JFXTextField account;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXComboBox emails;
    @FXML
    private Label loginFailed;

    @FXML
    private void login(ActionEvent event){
        startLogin();
    }

    @FXML
    private void keyEnter(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            startLogin();
        }
    }

    private void showMain(String account, String password, String email) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_ui/main_ui.fxml"));
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(new Scene(loader.load(), 1000, 750));
        stage.setTitle("Welcome");
        MainController mainController = loader.getController();
        mainController.initData(account, password, email);
        stage.setResizable(false);
        stage.show();
    }

    private void startLogin(){
        loginFailed.setVisible(false);
        String currentAccount = account.getText();
        String currentPassword = password.getText();
        if(currentAccount.length() < 1 || currentPassword.length() < 1){
            loginFailed.setVisible(true);
            return;
        }
        boolean loginSucceed = false;
        try{
            SmtpMailSender smtpMailSender = new SmtpMailSender(currentAccount, currentPassword, emails.getValue().toString());
            loginSucceed = smtpMailSender.isLegalAccount();
//            loginSucceed = SmtpMailSender.loginEmail(currentAccount, currentPassword, emails.getValue().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(loginSucceed){
            System.out.println("login succeed!");
            try{
                showMain(account.getText(), password.getText(), emails.getValue().toString());
                ((Stage)(account.getScene().getWindow())).close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            loginFailed.setVisible(true);
        }
    }

}
