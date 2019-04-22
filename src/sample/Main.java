package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ui/login_ui.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 430, 290));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        // 定义一些下面会用到的邮箱密码等数据 但正常编程没人会把账号密码写的这么明显的
//        String sender = "1320018234@qq.com";
//        String user = new BASE64Encoder().encode(sender.substring(0,
//                sender.indexOf("@")).getBytes());
//        String pass = new BASE64Encoder().encode(password.getBytes());
//
//        try {
//            Socket socket = new Socket("smtp.qq.com", 25);
//            InputStream inputStream = socket.getInputStream();
//            OutputStream outputStream = socket.getOutputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    inputStream));
//            PrintWriter writter = new PrintWriter(outputStream, true); // 我他妈去
//            // 这个true太关键了!
//
//            System.out.println(reader.readLine());
//
//            // HELO
//            writter.println("HELO huan");
//            System.out.println(reader.readLine());
//
//            // AUTH LOGIN
//            writter.println("auth login");
//            System.out.println(reader.readLine());
//            writter.println(user);
//            System.out.println(reader.readLine());
//            writter.println(pass);
//            System.out.println(reader.readLine());
//            // Above Authentication successful
//
//            // Set "mail from" and "rcpt to"
//            writter.println("mail from:<" + sender + ">");
//            System.out.println(reader.readLine());
//            writter.println("rcpt to:<" + receiver + ">");
//            System.out.println(reader.readLine());
//
//            // Set "data"
//            writter.println("data");
//            System.out.println(reader.readLine());
//
//            writter.println("subject:女神，是我");
//            writter.println("from:" + sender);
//            writter.println("to:" + receiver);
//            writter.println("Content-Type: text/plain;charset=\"gb2312\"");
//            writter.println();
//            writter.println("女神，晚上可以共进晚餐吗？");
//            writter.println(".");
//            System.out.println(reader.readLine());
//
//            // 发送完毕了，我要和服务器说拜拜了
//            writter.println("rset");
//            System.out.println(reader.readLine());
//
//            writter.println("quit");
//            System.out.println(reader.readLine());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
