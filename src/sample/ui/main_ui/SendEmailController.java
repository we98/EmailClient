package sample.ui.main_ui;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import sample.smtp.SmtpMailSender;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendEmailController {

    private String currentAccount;
    private String currentPassword;
    private String currentEmail;
    private ArrayList<File> attachments = new ArrayList<>();

    ExecutorService threadPool = Executors.newCachedThreadPool();

    @FXML
    private JFXTextField receiver;
    @FXML
    private JFXTextField subject;
    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    private JFXListView<String> attachmentList;

    @FXML
    private void send(ActionEvent event){
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    SmtpMailSender smtpMailSender = new SmtpMailSender(currentAccount, currentPassword, currentEmail);
                    boolean succeed = smtpMailSender.sendEmail(receiver.getText(), subject.getText(), htmlEditor.getHtmlText(), attachments);
                    if(succeed){
                        System.out.println("send succeed!");
                    }
                    else{

                    }
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
        File selected = fileChooser.showOpenDialog(receiver.getScene().getWindow());
        if(!attachments.contains(selected)) {
            attachments.add(selected);
            attachmentList.getItems().add(selected.getAbsolutePath());
        }
    }

    @FXML
    private void deleteAttachment(ActionEvent event){
        if(attachments.size() != 0){
            int selectedIdx = attachmentList.getSelectionModel().getSelectedIndex();
            attachmentList.getItems().remove(selectedIdx);
            attachments.remove(selectedIdx);
        }
    }

    public void initData(String account, String password, String email){
        currentAccount = account;
        currentPassword = password;
        currentEmail = email;
    }



}
