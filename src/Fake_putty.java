import javax.swing.*;
import java.awt.*;

public class Fake_putty{

    /** public Attributes **/
    public static final Dimension MAIN_FRAME_DIMENSION = new Dimension(600, 450);
    private static GUI obj;// obj will be used for our second thread


    /** private Attributes **/


    /** public methods **/

    /**
     * Create new frame
     *
     * @param user_interface
     * @return JFrame object
     *
     * **/
    public static JFrame new_frame(GUI user_interface){

        JFrame frame= new JFrame();
        frame.setVisible(true);
        frame.setContentPane(user_interface.getPannello_main());

        return frame;
    }

    /** main method **/
    public static void main(String[] args) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            obj = new GUI();
            JFrame frame = new_frame(obj);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(Fake_putty.MAIN_FRAME_DIMENSION);
            ImageIcon foto = new ImageIcon("fake_putty.png");
            frame.setIconImage(foto.getImage());
            frame.setTitle("Fake_Putty");
            frame.setAlwaysOnTop(true);

        } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
 /**start listening thread, this thread will control if any change is committed on our variable data_recive **/
    public static void startSecondThreadClient(){
        Thread t1 = new Thread(obj);
        t1.start();
    }

    public static void startSecondThreadServer(){
        Thread t1 = new Thread(obj);
        t1.start();
    }
}
