import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {

    private final int port;
    private final String host;


    private EchoClient(int port, String host){
        this.port = port;
        this.host = host;
    }

   public static EchoClient connectTo(int port){
        String localhost = "127.0.0.1";
        return new EchoClient(port, localhost);
   }

   public void run(){
       System.out.printf("send 'end' to finish program%n%n%n");

       try(Socket socket = new Socket(host, port)){
           Scanner scanner = new Scanner(System.in, "UTF-8");
           InputStream input = socket.getInputStream();
           InputStreamReader isr = new InputStreamReader(input);


           try(Scanner sc = new Scanner(isr);
               PrintWriter writer = new PrintWriter(socket.getOutputStream())){
               while(true){
                   String message = scanner.nextLine();
                   writer.write(message);
                   writer.write(System.lineSeparator());

                   writer.flush();
                   String serverBack = sc.nextLine();
                   System.out.println("My message in reverse-> " + serverBack);

                   if("end".equalsIgnoreCase(message)){
                       return;
                   }
               }
           }
       }catch (NoSuchElementException ex){
           System.out.println("Connection dropped");
       }catch (IOException e){
           System.out.printf("Can not connect to %s:%s !%n", host, port);
       }
   }
}
