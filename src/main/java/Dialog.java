import javax.swing.*;
import java.awt.*;

/**
 * ÃèÊö:
 * ¶Ô»°¿òÏÔÊ¾
 *
 * @author Maserhe
 * @create 2021-09-29 21:25
 */
public class Dialog extends JFrame implements Runnable {

    DrawPanel drawPanel = new DrawPanel();

    public Dialog() throws HeadlessException {

        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());      // ¸²¸ÇÈ«ÆÁÄ»¡£
        this.getContentPane().add(drawPanel);                           // Ìí¼Ó»­°å¡£
        drawPanel.setBounds(0, 0, getWidth(), getHeight());
        drawPanel.setLayout(null);
        drawPanel.setOpaque(false);
        this.setResizable(false);
        setVisible(true);
    }

    @Override
    public void run() {
        while (true) {

            try {
                // »æÍ¼
                drawPanel.paintComponent();
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
