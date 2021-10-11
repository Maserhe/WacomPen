import javax.swing.*;
import java.awt.*;

/**
 * ����:
 * �Ի�����ʾ
 *
 * @author Maserhe
 * @create 2021-09-29 21:25
 */
public class Dialog extends JFrame implements Runnable {

    DrawPanel drawPanel = new DrawPanel();

    public Dialog() throws HeadlessException {

        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());      // ����ȫ��Ļ��
        this.getContentPane().add(drawPanel);                           // ��ӻ��塣
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
                // ��ͼ
                drawPanel.paintComponent();
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
