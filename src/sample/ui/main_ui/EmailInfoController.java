package sample.ui.main_ui;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import sample.pop3.Email;
import sample.pop3.Pop3EmailManager;


public class EmailInfoController {

    private Pop3EmailManager pop3EmailManager;
    private AnchorPane content;
    private EmailListController emailListController;


    private Email currentEmail;

    @FXML
    private JFXTextField subject;
    @FXML
    private JFXTextField from;
    @FXML
    private JFXTextField date;

    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    private JFXListView<String> attachmentList;

    @FXML
    private void delete(ActionEvent event){
        pop3EmailManager.delEmail(currentEmail.getId());
        pop3EmailManager.quitClient();
        content.getChildren().remove(0, content.getChildren().size());

//        emailListController.initData();


//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("email_list.fxml"));
//            AnchorPane emailList = loader.load();
//            emailListController = loader.getController();
//            emailListController.initData(currentAccount, currentPassword, currentEmail, content);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        content.getChildren().add(emailList);
        //content.getChildren().add();
    }

    @FXML
    private void download(ActionEvent event){

    }

    @FXML
    private void downloadAll(ActionEvent event){

    }

    public void initData(Pop3EmailManager pop3EmailManager, AnchorPane content, EmailListController emailListController){
        currentEmail = pop3EmailManager.curEmail;
        subject.setText(currentEmail.getSubject());
        from.setText(currentEmail.getFrom());
        date.setText(currentEmail.getDate());
        htmlEditor.setHtmlText(currentEmail.getContent());

        this.pop3EmailManager = pop3EmailManager;
        this.content = content;
        this.emailListController = emailListController;
    }



}
