package sample.smtp;

import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.Socket;

public class EmailUtil {

//    private static Socket currentSocket;
//    private static InputStream currentInputStream;
//    private static OutputStream currentOutputStream;
//    private static BufferedReader currentReader;
//    private static PrintWriter currentWriter;

    public static boolean loginEmail(String account, String password, String email) throws Exception{
        BASE64Encoder util = new BASE64Encoder();
        String account64 = util.encode(account.getBytes());
        String password64 = util.encode(password.getBytes());
        Socket socket = initSocket(email);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter writer = new PrintWriter(outputStream, true);
        System.out.println(reader.readLine());
        writer.println("HELO huan");
        System.out.println(reader.readLine());
        writer.println("auth login");
        System.out.println(reader.readLine());
        writer.println(account64);
        System.out.println(reader.readLine());
        writer.println(password64);
        String authentication = reader.readLine();
        System.out.println(authentication);
        writer.println("quit");
        System.out.println(reader.readLine());
        writer.close();
        reader.close();
        outputStream.close();
        inputStream.close();
        socket.close();
        return "235".equals(authentication.substring(0, 3));
    }

    public static void sendEmail(String account, String password, String email, String receiver, String subject, String content) throws Exception{
        BASE64Encoder util = new BASE64Encoder();
        String sender64 = util.encode(account.getBytes());
        String password64 = util.encode(password.getBytes());
        Socket socket = initSocket(email);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter writer = new PrintWriter(outputStream, true);
        //我TM去 这个true太关键了，我刚才没写这个别坑了!你可以不加这个试下效果，下文附录2会写到为什么加true
        writer.println("HELO huan");
        System.out.println(reader.readLine());
        writer.println("auth login");
        System.out.println(reader.readLine());
        writer.println(sender64);
        System.out.println(reader.readLine());
        writer.println(password64);
        System.out.println(reader.readLine());
        writer.println("mail from:<" + account + email + ">");
        System.out.println(reader.readLine());
        writer.println("rcpt to:<" + receiver +">");
        System.out.println(reader.readLine());

        writer.println("data");
        System.out.println(reader.readLine());

        writer.println("subject:" + subject);
        writer.println("from:" + account + email);
        writer.println("to:" + receiver);
        writer.println("Content-Type: text/html;charset=UTF-8");
        //如果发送正文必须加这个，而且下面要有一个空行
        writer.println();
        writer.println(content);
        writer.println(".");
        //告诉服务器我发送的内容完毕了
        System.out.println(reader.readLine());

        writer.println("quit");
        System.out.println(reader.readLine());
        socket.close();
        inputStream.close();
        outputStream.close();
        reader.close();
        writer.close();

    }

    private static Socket initSocket(String email) throws Exception{
        if("@qq.com".equals(email)){
            return new Socket("smtp.qq.com", 25);
        }
        else if("@163.com".equals(email)){
            return new Socket("smtp.163.com", 25);
        }
        else{
            return new Socket("whu.edu.cn", 25);
        }
    }
}
