package sample.pop3;

import java.util.Date;

public class Email {
    int id;
    String content;
    String subject;
    String from;
    String to;
    String date;

    public Email() {}
    public Email(int id, String subject,String from, String to, String date, String content	) {
        this.from=from;
        this.id=id;
        this.to=to;
        this.date=date;
        this.subject=subject;
        this.content=content;
    }
    public Email(int id) {
        this.id= id;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id= id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content= content;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubjecct(String subject) {
        this.subject= subject;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from= from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to= to;
    }
    public String getDate() {
        Date dateString=new Date(date);
        System.out.println(dateString.toLocaleString());
        return dateString.toLocaleString();
    }
    public void setDate(String date) {
        this.date= date;
    }
    public String getDtails() {
        Date dateString=new Date(date);
        return subject+" "+from +" "+dateString.toLocaleString();
    }

}
