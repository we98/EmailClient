package sample.pop3;

import sun.misc.BASE64Decoder;

import java.io.*;
import java.net.Socket;

public class Pop3EmailManager {

    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    String recentResult;
    MailBox mailBox = new MailBox();
    public Email curEmail;


    public boolean loginClient(String email, String user, String password) {
        try {
            socket = initSocket(email);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            if (!executeCommand(""))
                throw new IOException("连接服务器失败!");
            if (!executeCommand("user " + user))
                throw new IOException("用户名错误!");
            if (!executeCommand("pass " + password))
                throw new IOException("密码错误!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private boolean stat() {
        try {
            if (!executeCommand("stat"))
                throw new IOException("获取邮箱状态失败!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        mailBox.setEmailNum(recentResult.charAt(4) - 48);
        return true;
    }

    public String[] fetchAllEmails() {
        try {
            if (!stat())
                throw new IOException("获取全部邮件失败!");
            String[] emailMenu = new String[mailBox.getEmailNum()];
            for (int i = 0; i < mailBox.getEmailNum(); i++) {
                mailBox.addEmail(retrEmail(i + 1));
                emailMenu[i] = curEmail.getDtails();
            }
            return emailMenu;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Email getEmail(int index) {
        if (index <= mailBox.getEmailNum()) {
            return curEmail = mailBox.getEmail(index - 1);
        }
        return null;
    }

    private Email retrEmail(int index) {
        try {
            if (!executeCommand("retr " + index))
                throw new IOException("获取邮件失败!");
            return curEmail = getEmailDetails(index);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    public boolean delEmail(int index) {
        try {
            if (!executeCommand("dele " + index))
                throw new IOException("删除邮件失败!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean quitClient() {
        try {
            if (!executeCommand("quit"))
                throw new IOException("退出邮箱失败!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private Email getEmailDetails(int index) throws Exception {
        Email email = new Email(index);
        String line = "";
        StringBuffer Base64Content = new StringBuffer();
        boolean isReadingContent = false;
        boolean isReadingCompleted = false;
        String codingFormat = "GBK";
        while (!line.contains("CST")) {
            line = reader.readLine();
        }
        email.setDate(line.substring(line.indexOf(";") + 1));
        while (!line.equals(".")) {
            if (isReadingCompleted) {
                line = reader.readLine();

                continue;
            }
            line = reader.readLine();
//            System.out.println(line);

            if (isReadingContent) {
                if (line.contains("------=")) {
                    email.setContent(decode(Base64Content.toString(), codingFormat));
                    isReadingCompleted = true;
                } else
                    Base64Content.append(line);
            } else if (line.length() > 5 && line.substring(0, 5).equalsIgnoreCase("From:")) {
                try{
                String[] elements = line.split("\\?|<|>");
                codingFormat = elements[1];
                email.setFrom(decode(elements[3], codingFormat) + " " + elements[5]);
            }catch(Exception e) {
                email.setFrom(line.substring(6));
            }
            } else if (line.length() > 8 && line.substring(0, 8).equals("Subject:")) {
               try {
                   String[] elements = line.split("\\?");
                   codingFormat = elements[1];
                   email.setSubjecct(decode(elements[3], codingFormat));
               }catch(Exception e) {
                    email.setSubjecct(line.substring(9));
                }
            } else if (line.length() > 3 && line.substring(0, 3).equals("To:")) {
                try {
                    email.setTo(line.substring(line.indexOf("<") + 1, line.indexOf(">")));
                }catch(Exception e){
                        email.setTo(line.substring(4));
                    }
            } else if (line.contains("base64")) {
                isReadingContent = true;
            }
        }
        return email;
    }

    private String decode(String content, String format) throws Exception {
        BASE64Decoder util = new BASE64Decoder();
        byte[] byteConent = util.decodeBuffer(content);
        String result = new String(byteConent, format);
        return result;
    }

    private boolean executeCommand(String command) throws Exception {
        if (!(command == null || command.equals(""))) {
            writer.println(command);
            writer.flush();
        }
        recentResult = reader.readLine() + "\n";
        System.out.println(recentResult);
        return recentResult.charAt(0) == '+';
    }

    private Socket initSocket(String email) throws Exception {


        try {
            if ("@qq.com".equals(email)) {
                return  new Socket("pop.qq.com", 995);
            } else if ("@163.com".equals(email)) {
                return new Socket("pop.163.com", 110);
            } else {
                return new Socket("whu.edu.cn", 110);
            }
        } catch (Exception e) {
            throw new Exception("连接超时");
        }
    }




}
