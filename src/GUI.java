import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class GUI implements Runnable{

    /** private Attributes **/
    private JPanel pannello_main;
    private JTabbedPane selezione;
    private JButton inviaButton;
    private JButton procediButton;
    private JTextField nickname;
    private JTextField ip_serverTextField;
    private JSpinner porta_server;
    private JSpinner porta_client;
    private JTextField ip_clientTextField;
    private JTextArea errori;
    private JTextField contenutoMessaggio;
    private JTextPane areaMessaggi;
    private Client client;
    private Server server;
    private int codiceErrore;


    /** method to set gui style **/
    public void set_Gui_style(){
        this.inviaButton.setEnabled(false);
        this.areaMessaggi.setFont(new Font("helevtica",Font.PLAIN, 17));
        this.areaMessaggi.setEditable(false);
        this.errori.setEditable(false);
        this.ip_serverTextField.setEditable(false);
        this.ip_serverTextField.setText(get_ip_host());
    }

    /** this method will write any message on the chatBox areaMessaggi
     *
     * @param message  ----> that will be write on the chatBox
     *
     * **/
    public void write_on_areaMessaggi(String message){
        areaMessaggi.setText(areaMessaggi.getText() + "\n" + message);
    }

    /** method to set listener **/
    public void set_Listener(){
        /** if the textfield is modified the button will be clickable **/
        contenutoMessaggio.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            public void changed() {
                if (contenutoMessaggio.getText().equals("")){
                    inviaButton.setEnabled(false);
                }
                else {
                    inviaButton.setEnabled(true);
                }
            }
        });

        /** Set start message button **/
        this.procediButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start_message(); //Start message method
            }
        });

        /** Set invia messaggio button **/
        inviaButton.addActionListener(e1 -> {
            if (isClient()){
                client.send_data(contenutoMessaggio.getText());
                contenutoMessaggio.setText("");
            }else if (isServer()){
                server.send_data(contenutoMessaggio.getText());
                contenutoMessaggio.setText("");
            }
        });

        /** Set send message with Enter key**/
        this.contenutoMessaggio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int evento = e.getKeyCode();
                if (evento == KeyEvent.VK_ENTER) {
                    if (!contenutoMessaggio.getText().isEmpty()) {
                        write_on_areaMessaggi(contenutoMessaggio.getText());
                        if (isClient()){
                            client.send_data(nickname.getText() + ": " +contenutoMessaggio.getText());
                            contenutoMessaggio.setText("");
                        }
                        else if (isServer())
                        {
                            server.send_data(nickname.getText() + ": " +contenutoMessaggio.getText());
                            contenutoMessaggio.setText("");
                        }
                    }
                }
            }
        });
    }

    /** get ip host method**/
    public String get_ip_host(){
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** set error method **/
    public void set_error(String error){
        this.errori.setFont(new Font("helvetica",Font.BOLD,13));
        this.errori.setForeground(Color.RED);
        this.errori.setText(error);
    }
    public void set_information(String information){
        this.errori.setFont(new Font("helvetica",Font.BOLD,13));
        this.errori.setForeground(Color.GREEN);
        this.errori.setText(information);
    }




    /** Method that will start program as a client **/
    public void start_client(){
        System.out.println("Provo a connettermi...");
        set_information("Provo a connettermi...\n");
        client = new Client((Integer) porta_client.getValue(), ip_clientTextField.getText());
        if (client.error_code == 1){
            /** the pc becomes the client **/
            set_information("---CONNESSIONE STABILITA---\n");
            Thread tClient = new Thread(client);
            tClient.start();

            client.send_data(nickname.getText() + " partecipa\n");

            Fake_putty.startSecondThreadClient();

            System.out.println("GUI: " + client.data_recive);

        }else if (client.error_code == 2){
            set_error("Errore, host sconosciuto!\n");
        }else if (client.error_code == 3){
            set_error("Errore, impossibile stabilire la connessione!\n");
        }
    }




    /** Method that will start program as a server **/
    public void start_server(){
        set_information("Server in attesa di un client...");
        server = new Server((Integer) porta_server.getValue(), nickname.getText());

        if (server.error_code == 1) {
            Thread tServer = new Thread(server); // we're using threads to listen and write any change
            tServer.start();

            server.send_data(nickname.getText() + " partecipa\n");
            set_information("connessione avvenuta correttamente...");


            Fake_putty.startSecondThreadServer();

        } else if (server.error_code == 2) {
            set_error("errore, qualcosa è andato storto, nessun client si e' connesso");
        }
    }



    /** onClick procediButton method Start_message**/
    public void start_message(){
        if(nickname.getText().isEmpty()){
            set_error("Errore, inserisci il nickname!");
        }
        else {
            if (isClient()) {
                start_client();
            } else if (isServer()) {
                /** the pc becomes the server **/
                    start_server();
            } else {
                set_error("Errore, inserisci i parametri correttemente!");
            }
        }
    }


    /** public Constructor **/
    public GUI() {

        set_Gui_style();

        set_Listener();

    }


    /** client is listening for receiving message **/
    public void client_is_listening(){
        String recived_data = client.data_recive;
        while (true){
            try {
                System.out.println("dato dopo " + client.data_recive);
                if(!client.data_recive.equals(recived_data)){
                    System.out.println("dentro   " + recived_data);
                    recived_data = client.data_recive;
                    areaMessaggi.setText(areaMessaggi.getText() + "\n" + recived_data);
                }
            }
            catch (NullPointerException e) { e.printStackTrace(); }
        }
    }


    /** server is listening for receiving message **/
    public void server_is_listening(){
        String recived_data = server.data_recive;

        while (true){
            try{
                System.out.println("dato dopo " + server.data_recive);
                if(!server.data_recive.equals(recived_data)){
                    System.out.println("dentro   " + recived_data);
                    recived_data = server.data_recive;
                    areaMessaggi.setText(areaMessaggi.getText() + "\n" + recived_data);
                }
            }
            catch (NullPointerException e) { e.printStackTrace(); }
        }
    }
    /** This method will be executed by the second thread **/
    public void run() {


        if (isClient()){
            /** in case the pc acts as a client this function will listen to anything the server will write,
             * if something is added, this will write it on the chatbox**/
            client_is_listening();

        }else if (isServer()){
            /**in case the pc acts as a server this function will listen to anythig the client will write,
             * if something is added, this will write it on the chatbox**/
            server_is_listening();
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    /**this method used in the method Start message is used to check if the pc acts as a client**/
    public boolean isClient(){
        if (Integer.parseInt(porta_client.getValue().toString()) > 0 && !ip_clientTextField.getText().isEmpty() && porta_server.getValue().equals(0))
            return true;
        else
            return false;
    }

    /**this method used in the method Start message is used to check if the pc acts as a server**/
    public boolean isServer(){
        if (Integer.parseInt(porta_server.getValue().toString()) > 0 && !ip_serverTextField.getText().isEmpty() && porta_client.getValue().equals(0) && ip_clientTextField.getText().isEmpty())
            return true;
        else
            return false;
    }

    /** getter methods **/
    public JPanel getPannello_main() {
        return pannello_main;
    }
}