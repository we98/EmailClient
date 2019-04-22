package sample.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.smtp.EmailUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainController {

    private String currentAccount;
    private String currentPassword;
    private String currentEmail;
    @FXML
    private TextField account;
    @FXML
    private PasswordField password;
    @FXML
    private TextField from;
    @FXML
    private TextField receiver;
    @FXML
    private TextField to;
    @FXML
    private TextField subject;
    @FXML
    private HTMLEditor content;
    @FXML
    private Label attachment;

    ExecutorService threadPool = Executors.newCachedThreadPool();


    @FXML
    private void send(ActionEvent event){
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    EmailUtil.sendEmail(currentAccount, currentPassword, currentEmail,
                            receiver.getText(), subject.getText(), content.getHtmlText());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void addAttachment(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add attachment");
        File selected = fileChooser.showOpenDialog(account.getScene().getWindow());
        attachment.setText(selected.getAbsolutePath());
    }

    @FXML
    private void test(ActionEvent event) {
        File file = new File(this.getClass().getResource("/test.txt").getPath());
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            from.setText(reader.readLine());
            receiver.setText(reader.readLine());
            to.setText(reader.readLine());
            subject.setText(reader.readLine());
            content.setHtmlText(reader.readLine());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    @FXML
    private void logout(ActionEvent event){
        try{
            showLogin();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        ((Stage)(account.getScene().getWindow())).close();
        threadPool.shutdownNow();
    }


    private void showLogin() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login_ui.fxml"));
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(new Scene(loader.load(), 430, 290));
        stage.setResizable(false);
        stage.show();
    }

    public void initData(String account, String password, String email){
        this.account.setText(account);
        this.password.setText(password);
        currentAccount = account;
        currentPassword = password;
        currentEmail = email;
    }

}
