package sample.smtp;

import sample.exception.MyConnectException;
import sample.util.Base64Util;
import sample.util.ContentTypeUtil;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class SmtpMailSender {
    private final static int PORT = 25;
    //当连接SMTP服务器失败后重新连接的次数
    private final static int RETRY=3;
    //当连接SMTP服务器失败后重新连接的时间间隔
    private final static int INTERVAL=1000;
    //MIME分格符
    private final static String BOUNDARY = "Boundary-=_hMbeqwnGNoWeLsRMeKTIPeofyStu";
    private final static HashMap<String, String> EMAIL_TO_DOMAIN = new HashMap<>();
    static {
        EMAIL_TO_DOMAIN.put("@qq.com", "smtp.qq.com");
        EMAIL_TO_DOMAIN.put("@163.com", "smtp.163.com");
        EMAIL_TO_DOMAIN.put("@whu.edu.cn", "@whu.edu.cn");
    }
    private String currentAccount;
    private String currentPassword;
    private String currentEmail;
    public SmtpMailSender(String account, String password, String email){
        currentAccount = account;
        currentPassword = password;
        currentEmail = email;
    }
    public boolean isLegalAccount() throws IOException, InterruptedException, MyConnectException{
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        int times = 0;
        String server = EMAIL_TO_DOMAIN.get(currentEmail);
        for(; times < RETRY; ++times){
            socket = new Socket(server, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            writer = new PrintWriter(outputStream, true);
            if("220".equals(getResponse(reader))){
                break;
            }
            else{
                Thread.sleep(INTERVAL);
            }
        }
        if(times == RETRY){
            throw new MyConnectException("Can not connect to server: " + server);
        }
        sendCommand(writer, "helo a");
        getResponse(reader);
        sendCommand(writer, "auth login");
        getResponse(reader);
        sendCommand(writer, Base64Util.encodeBase64String(currentAccount));
        getResponse(reader);
        sendCommand(writer, Base64Util.encodeBase64String(currentPassword));
        boolean succeed = "235".equals(getResponse(reader));
        closeResource(writer, reader, outputStream, inputStream, socket);
        return succeed;
    }
    public boolean sendEmail(String receiver, String subject, String content,
                             ArrayList<File> attachments) throws Exception{
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        int times = 0;
        String server = EMAIL_TO_DOMAIN.get(currentEmail);
        for(; times < RETRY; ++times){
            socket = new Socket(server, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            writer = new PrintWriter(outputStream, true);
            if("220".equals(getResponse(reader))){
                break;
            }
            else{
                Thread.sleep(INTERVAL);
            }
        }
        if(times == RETRY){
            throw new MyConnectException("Can not connect to server: " + server);
        }
        sendCommand(writer, "helo a");
        getResponse(reader);
        sendCommand(writer, "auth login");
        getResponse(reader);
        sendCommand(writer, Base64Util.encodeBase64String(currentAccount));
        getResponse(reader);
        sendCommand(writer, Base64Util.encodeBase64String(currentPassword));
        getResponse(reader);
        sendCommand(writer, "mail from:<" + currentAccount + currentEmail + ">");
        getResponse(reader);
        sendCommand(writer, "rcpt to:<" + receiver + ">");
        if(!"250".equals(getResponse(reader))){
            closeResource(writer, reader, outputStream, inputStream, socket);
            return false;
        }
        //给自己发回一条，不给自己发一条的话163会认为是垃圾邮件，不让发
        if("@163.com".equals(currentEmail)){
            sendCommand(writer, "rcpt to:<" + currentAccount + currentEmail +">");
            getResponse(reader);
        }

        sendCommand(writer, "data");
        getResponse(reader);
        sendCommand(writer, "Subject:" + subject);
        sendCommand(writer, "from:" + currentAccount + currentEmail);
        sendCommand(writer, "to:" + receiver);

        int attachmentCount = attachments.size();
        if(attachmentCount != 0) {
            sendCommand(writer, "Content-Type: multipart/mixed; BOUNDARY=\""+BOUNDARY+"\"");
            sendNewlineCommand(writer);
            sendCommand(writer, "--"+BOUNDARY);
        }

        sendCommand(writer, "Content-Type: text/html;charset=UTF-8");
        sendNewlineCommand(writer);
        sendCommand(writer, content);

        if(attachments.size() != 0) {
            String fileName;
            for(int fileIndex = 0; fileIndex < attachments.size(); ++fileIndex) {
                File currentFile = attachments.get(fileIndex);
                fileName = currentFile.getName();
                sendNewlineCommand(writer);
                sendCommand(writer, "--"+BOUNDARY);
                sendCommand(writer, "Content-Type: "+ ContentTypeUtil.getContentType(currentFile.getAbsolutePath()) + "; charset=UTF-8; name=\"" + fileName +"\"");
                sendCommand(writer, "Content-Transfer-Encoding: base64");
                sendCommand(writer, "Content-Disposition: attachment; filename=\""+ fileName +"\"");
                sendNewlineCommand(writer);
                sendCommand(writer, Base64Util.encodeBase64File(currentFile));
            }
            sendCommand(writer, "--"+BOUNDARY+"--");
        }
        sendCommand(writer, ".");

        boolean sendSucceed = "250".equals(getResponse(reader));
        closeResource(writer, reader, outputStream, inputStream, socket);
        return sendSucceed;
    }
    private void closeResource(PrintWriter writer, BufferedReader reader, OutputStream outputStream,
                               InputStream inputStream, Socket socket) throws IOException{
        sendCommand(writer, "quit");
        getResponse(reader);
        writer.close();
        reader.close();
        outputStream.close();
        inputStream.close();
        socket.close();
    }
    private void sendCommand(PrintWriter writer, String command){
        writer.println(command);
    }
    private void sendNewlineCommand(PrintWriter writer){
        writer.println();
    }
    private String getResponse(BufferedReader reader){
        String response = null;
        try {
            response = reader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(response);
        return response.substring(0, 3);
    }

}
