package sample.pop3;

import java.util.ArrayList;
import java.util.List;

public class MailBox {
    int emailNum;
    List<Email> emailList =new ArrayList<>();
    public void setEmailNum(int emailNum) {
        this.emailNum=emailNum;
    }
    public int getEmailNum() {
        return emailNum;
    }
    public List<Email> getEmails() {
        return emailList;
    }
    public Email getEmail(int index) {
        return emailList.get(index);
    }
    public void addEmail(int id, String subject,String from, String to, String data, String content) {
        emailList.add(new Email(id,subject,from,to,data,content));
    }
    public void removeEmail(int index) {
        emailNum--;
        emailList.remove(index);
    }
    public void addEmail(Email email) {
        emailList.add(email);
    }


}
