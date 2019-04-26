package sample.ui.main_ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import sample.pop3.Pop3EmailManager;


public class EmailListController {

    private String currentAccount;
    private String password;
    private String email;

    private Pop3EmailManager pop3EmailManager;
    private AnchorPane content;
    private EmailListController currentThis = this;

    @FXML
    private ListView<String> listView;


    public void initData(String currentAccount, String password, String email, AnchorPane content) {

        this.content = content;
        this.currentAccount = currentAccount;
        this.password = password;
        this.email = email;

       initData();

    }

    public void initData(){
        pop3EmailManager = new Pop3EmailManager();
        pop3EmailManager.loginClient(email, currentAccount, password);
        String[] list = pop3EmailManager.fetchAllEmails();
        ObservableList<String> listData = FXCollections.observableArrayList();
        for (int i = 0; i < list.length; i++) {
            listData.add(list[i]);
        }
        listView.setItems(listData);
        listView.setCellFactory(new Callback<ListView<String>,ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param)
            {
                return new MyListCell();
            }
        });
//        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
//            @Override
//            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//
//                //System.out.println("323");
//
//                try {
//                    //if(userInfo == null){
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("email_info.fxml"));
//                    AnchorPane emailInfo = loader.load();
//                    EmailInfoController emailInfoController = loader.getController();
//                    emailInfoController.initData(pop3EmailManager, content, currentThis);
//                    content.getChildren().remove(0, content.getChildren().size());
//                    content.getChildren().add(emailInfo);
//                    System.out.println(content.getChildren().size());
//                    //}
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }}
//        );
    }

    @FXML
    public void handleListView(MouseEvent event) {
        if(event.getClickCount() == 2){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("email_info.fxml"));
                AnchorPane emailInfo = loader.load();
                EmailInfoController emailInfoController = loader.getController();
                int i = listView.getSelectionModel().getSelectedIndex();
                pop3EmailManager.curEmail = pop3EmailManager.getEmail(i + 1);
                emailInfoController.initData(pop3EmailManager, content, currentThis);
                content.getChildren().remove(0, content.getChildren().size());
                content.getChildren().add(emailInfo);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


    }



    static class MyListCell extends ListCell<String> {

        @Override
        protected void updateItem(String arg0, boolean arg1) {
            // FX框架要求必须先调用 super.updateItem()
            super.updateItem(arg0, arg1);

            // 自己要实现的单元格显示
            if (arg0 == null) {
                this.setText("");
            } else {
                this.setText(arg0);
            }
        }

    }

}
