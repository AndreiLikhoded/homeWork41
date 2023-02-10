import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {

    private final int port;


    private EchoServer(int port){
        this.port = port;
    }

    public static EchoServer bindToPort(int port){
        return new EchoServer(port);
    }

    public void run(){
        try(var server = new ServerSocket(port)){
            try(var clientSocket = server.accept()){
                handle(clientSocket);
            }
        }catch (IOException e){
            System.out.printf("Connection is failed, port %n is busy.%n", port);
            e.printStackTrace();
        }
    }

    private void handle(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(input, "UTF-8");

        try(Scanner sc = new Scanner(isr);
            PrintWriter writer = new PrintWriter(socket.getOutputStream())){
            while(true){
                var message = sc.nextLine().strip();
                System.out.printf("Got: %s%n", reverseString(message));
                writer.write(reverseString(message));
                writer.write(System.lineSeparator());

                writer.flush();
                if("buy".equalsIgnoreCase(message)){
                    System.out.printf("End program!%n");
                    return;
                }
            }
        }catch (NoSuchElementException e){
            System.out.println("Client dropped the connection.");
        }
    }
    public static String reverseString(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            result.insert(0, str.charAt(i));
        }
        return result.toString();
    }
}
