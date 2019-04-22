package sample.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.smtp.EmailUtil;

public class LoginController{

    @FXML
    private TextField account;
    @FXML
    private PasswordField password;
    @FXML
    private ComboBox emails;
    @FXML
    private Label loginFailed;

    @FXML
    private void login(ActionEvent event){
        loginFailed.setVisible(false);
        String currentAccount = account.getText();
        String currentPassword = password.getText();
        if(currentAccount.length() < 1 || currentPassword.length() < 1){
            loginFailed.setVisible(true);
            return;
        }
        boolean loginSucceed = false;
        try{
            loginSucceed = EmailUtil.loginEmail(currentAccount, currentPassword, emails.getValue().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(loginSucceed){
            System.out.println("login succeed!");
            try{
                showMain(account.getText() + emails.getValue(), password.getText());
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

    private void showMain(String account, String password) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_ui.fxml"));
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(new Scene(loader.load(), 500, 620));
        MainController mainController = loader.getController();
        mainController.initData(account, password);
        stage.setResizable(false);
        stage.show();
    }

}
