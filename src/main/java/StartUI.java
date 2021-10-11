import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ����:
 *
 * @author Maserhe
 * @create 2021-09-29 20:39
 */
public class StartUI extends JFrame {
    public static void main(String[] args) {
        StartUI startUI = new StartUI();
    }

    // �û��� ʵ�������֡�
    public static String USERNAME = "Maserhe";

    // �ʼ���Ϣ  0 ��ʵ�ʼ� 1����α�� 2������α��
    public static int HANDWRITING_STATUS = 0;

    // �ʼ�������Ϣ 0, �涨���ݣ� 1�� �ǹ涨���ݣ� 2��ǩ��
    public static  int CONTENT_INFO_STATUS = 0;

    // ������Ϣ 0����һ���� 1����������
    public static int TASK_INFO_STATUS = 0;

    // ʵ��Ĵ��� 0�� �״�ʵ�� 1�����״�ʵ��
    public static int FREQUENCY_INFO = 1;


    private JPanel panel0 = new JPanel();
    private JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel();
    private JPanel panel3 = new JPanel();
    private JPanel panel4 = new JPanel();
    private JPanel panel5 = new JPanel();

    private JButton startButton = new JButton("��ʼ");
    private JButton stopButton = new JButton("����");

    // ���õ�ѡ��ť
    private ButtonGroup group1 = new ButtonGroup();
    private JRadioButton radiobutton1 = new JRadioButton("��ʵ�ʼ�",true);	//��������ť
    private JRadioButton radiobutton2 = new JRadioButton("��α��",false);
    private JRadioButton radiobutton3 = new JRadioButton("����α��",false);

    private ButtonGroup group2 = new ButtonGroup();
    private JRadioButton radiobutton4 = new JRadioButton("�涨����",true);	//��������ť��Ĭ���豸ΪDisplay
    private JRadioButton radiobutton5 = new JRadioButton("��������",false);
    private JRadioButton radiobutton6 = new JRadioButton("����ǩ��",false);

    private ButtonGroup group3 = new ButtonGroup();
    private JRadioButton radiobutton8 = new JRadioButton("������",true);
    private JRadioButton radiobutton9 = new JRadioButton("��������",false);

    private ButtonGroup group4 = new ButtonGroup();
    private JRadioButton radiobutton10 = new JRadioButton("block1",true);
    private JRadioButton radiobutton11 = new JRadioButton("block2",false);
    private JRadioButton radiobutton12 = new JRadioButton("block3",false);
    private JRadioButton radiobutton13 = new JRadioButton("block4",false);
    private JRadioButton radiobutton14 = new JRadioButton("block5",false);


    private JLabel jlabel1 = new JLabel();
    private JTextField jtext1 = new JTextField(20);


    StartUI(){
        super();
        Container container = this.getContentPane();
        container.setLayout(null);

        setBounds(480, 260, 390, 450);

        panel0.setBounds(10,10,343,45);
        panel2.setBounds(10, 70, 343, 45);
        panel3.setBounds(10, 130, 343, 45);
        panel4.setBounds(10, 190, 343, 45);
        panel1.setBounds(10, 300, 343, 45);
        panel5.setBounds(10,250, 343,45);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jlabel1.setText("ID:");
        jlabel1.setFont(new Font("Times New Roman", Font.PLAIN, 12));

        jlabel1.setBounds(20, 40, 135, 25);
        jtext1.setBounds(20, 45, 108, 24);
        jtext1.setFont(new Font("     ", Font.PLAIN, 11));

        radiobutton1.setBounds(60, 85, 75, 27);
        radiobutton2.setBounds(190, 85, 75, 27);
        radiobutton3.setBounds(190, 85, 75, 27);

        group1.add(radiobutton1);
        group1.add(radiobutton2);
        group1.add(radiobutton3);

        radiobutton4.setBounds(60, 35, 75, 27);
        radiobutton5.setBounds(190, 35, 75, 27);
        radiobutton6.setBounds(190, 35, 75, 27);

        group2.add(radiobutton4);
        group2.add(radiobutton5);
        group2.add(radiobutton6);


        // radiobutton7.setBounds(60, 35, 75, 37);
        radiobutton8.setBounds(190, 35, 75, 37);
        radiobutton9.setBounds(190, 35, 75, 37);
        // group3.add(radiobutton7);
        group3.add(radiobutton8);
        group3.add(radiobutton9);


        radiobutton10.setBounds(30, 35, 75, 37);
        radiobutton11.setBounds(130, 35, 75, 37);
        radiobutton12.setBounds(230, 35, 75, 37);
        radiobutton13.setBounds(330, 35, 75, 37);
        radiobutton14.setBounds(430, 35, 75, 37);

        group4.add(radiobutton10);
        group4.add(radiobutton11);
        group4.add(radiobutton12);
        group4.add(radiobutton13);
        group4.add(radiobutton14);


        startButton.setBounds(35, 350, 294, 27);

        // ���ӿؼ���
        panel0.add(jlabel1);
        panel0.add(jtext1);

        panel1.add(startButton);
        panel2.add(radiobutton1);
        panel2.add(radiobutton2);
        panel2.add(radiobutton3);

        panel3.add(radiobutton4);
        panel3.add(radiobutton5);
        panel3.add(radiobutton6);

        // panel4.add(radiobutton7);
        panel4.add(radiobutton8);
        panel4.add(radiobutton9);

        panel5.add(radiobutton10);
        panel5.add(radiobutton11);
        panel5.add(radiobutton12);
        panel5.add(radiobutton13);
        panel5.add(radiobutton14);


        container.add(panel0);
        container.add(panel2);
        container.add(panel3);
        container.add(panel4);
        container.add(panel1);
        container.add(panel5);

        setVisible(true);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataUtils.init();
                new Thread(new Dialog()).start();
                // ��һ����
                DrawPanel.number = 1;
                // ��ʾ���±�һ ��ʼ
                DataUtils.now = 0;
                System.out.println("ʵ�����");

            }
        });

        // ��һ������¼�
        ActionListener listener1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                if ("��ʵ�ʼ�".equals(actionCommand)) {
                    HANDWRITING_STATUS = 0;
                } else if ("��α��".equals(actionCommand)) {
                    HANDWRITING_STATUS = 1;
                } else if ("����α��".equals(actionCommand)) {
                    HANDWRITING_STATUS = 2;
                }
                System.out.println("��д״̬" + HANDWRITING_STATUS);
            }
        };

        // �������� ��һ�����
        radiobutton1.addActionListener(listener1);
        radiobutton2.addActionListener(listener1);
        radiobutton3.addActionListener(listener1);

        // �ڶ�������¼�
        ActionListener listener2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                if ("�涨����".equals(actionCommand)) {
                    CONTENT_INFO_STATUS = 0;
                } else if ("��������".equals(actionCommand)) {
                    CONTENT_INFO_STATUS = 1;
                } else if ("����ǩ��".equals(actionCommand)) {
                    CONTENT_INFO_STATUS = 2;
                }
                System.out.println("�涨״̬" + CONTENT_INFO_STATUS);
            }
        };

        // �������� �ڶ������
        radiobutton4.addActionListener(listener2);
        radiobutton5.addActionListener(listener2);
        radiobutton6.addActionListener(listener2);

        // ����������¼�
        ActionListener listener3 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                if ("������".equals(actionCommand)) {
                    TASK_INFO_STATUS = 0;
                } else if ("��������".equals(actionCommand)) {
                    TASK_INFO_STATUS = 1;
                }
                System.out.println("�����Ѷ�" + TASK_INFO_STATUS);
            }
        };

        // �������� ���������
        // radiobutton7.addActionListener(listener3);
        radiobutton8.addActionListener(listener3);
        radiobutton9.addActionListener(listener3);

        // ����������¼�
        ActionListener listener4 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                if ("block1".equals(actionCommand)) {
                    FREQUENCY_INFO = 1;
                } else if ("block2".equals(actionCommand)) {
                    FREQUENCY_INFO = 2;
                } else if ("block3".equals(actionCommand)) {
                    FREQUENCY_INFO = 3;
                } else if ("block4".equals(actionCommand)) {
                    FREQUENCY_INFO = 4;
                } else if ("block5".equals(actionCommand)) {
                    FREQUENCY_INFO = 5;
                }
                System.out.println("ʵ�����" + FREQUENCY_INFO);
            }
        };

        radiobutton10.addActionListener(listener4);
        radiobutton11.addActionListener(listener4);
        radiobutton12.addActionListener(listener4);
        radiobutton13.addActionListener(listener4);
        radiobutton14.addActionListener(listener4);


        // Ĭ��ʵ������
        jtext1.setText(USERNAME);

        // ʵ���������ļ�����
        jtext1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                USERNAME = jtext1.getText().trim();
                System.out.println("������Ϣ" + USERNAME);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                USERNAME = jtext1.getText().trim();
                System.out.println("ɾ����Ϣ" + USERNAME);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }


        });
    }
}