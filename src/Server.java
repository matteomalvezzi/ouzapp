import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server implements Runnable{

    ServerSocket server = null;
    Socket client = null;

    private String nickname;
    private int porta;
    public int error_code;
    public String data_recive;

    DataInputStream in;
    DataOutputStream out;

    /**public constructor**/
    public Server(int porta,String nickname){
        this.porta = porta;
        this.nickname = nickname;
        conn_client(); // waiting for a client to connect
    }

    /** Send data method **/
    public void send_data(String testo){
        try {

            System.out.print("Messaggio inviato: " + testo + "\n") ;
            out.writeBytes(testo + "\n");

            if (testo.equals("quit")){
                client.close();
                java.lang.System.exit(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Connect to client **/
    public void conn_client(){

        try {
            server = new ServerSocket(porta); //inizializziamo il servizio
            System.out.println("in attesa....");
            client = server.accept();

            error_code = 1; // connection was succesful

            server.close(); // stop listening for other clients

            System.out.println("client connesso");
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            error_code = 2; // something went wrong
            e.printStackTrace();
        }
    }

    /** this method will be executed by the thread tServer **/
    @Override
    public void run() {
        System.out.println("Sto ascoltando...");
        while(true){
            try{
                data_recive = in.readLine();
                System.out.println(data_recive);
            }catch (Exception e){e.printStackTrace(); }
        }
    }
}