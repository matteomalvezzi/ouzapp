import javax.swing.*;
import java.awt.*;

public class Fake_putty{

    /** public Attributes **/
    public static final Dimension MAIN_FRAME_DIMENSION = new Dimension(600, 450);

    /** private Attributes **/
    private static GUI user_interface;// obj will be used for our second thread

    /** public methods **/


    /** Centre the frame
     *
     * @param frame
     *
     * **/
    public static void centreFrame(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }


    /** Create new frame **/
    public static void new_frame(){

        user_interface = new GUI();
        JFrame frame= new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(Fake_putty.MAIN_FRAME_DIMENSION);
        ImageIcon foto = new ImageIcon("fake_putty.png");
        frame.setIconImage(foto.getImage());
        frame.setTitle("Fake Putty");
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setContentPane(user_interface.getPannello_main());
        centreFrame(frame);
        frame.setVisible(true);
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

            new_frame();

        } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**start listening thread, this thread will control if any change is committed on our variable data_recive **/
    public static void startSecondThreadClient(){
        Thread t1 = new Thread(user_interface);
        t1.start();
    }

    public static void startSecondThreadServer(){
        Thread t1 = new Thread(user_interface);
        t1.start();
    }
}
