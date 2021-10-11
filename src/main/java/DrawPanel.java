import com.csvreader.CsvWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.StrictMath.sqrt;


/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-09-29 21:27
 */
public class DrawPanel extends JPanel {

    // 字的个数
    public static int number = 1;

    CopyOnWriteArrayList<CopyOnWriteArrayList<PointInfo>> pointInfo;
    CopyOnWriteArrayList<PointInfo> tempPoint;

    Graphics og;

    // 图像缓冲。
    Image image;
    PointInfo last;
    public DrawPanel() {

        // 初始化。
        pointInfo = new CopyOnWriteArrayList<CopyOnWriteArrayList<PointInfo>>();
        tempPoint = new CopyOnWriteArrayList<PointInfo>();
        last = new PointInfo();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(tempPoint.size() > 3) pointInfo.add(tempPoint);
                tempPoint = new CopyOnWriteArrayList<PointInfo>();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                // 记录点的信息。
                // 刷新笔。
                //PenData.refresh();
                PointInfo newPoint = new PointInfo();
                //设置当前点的信息， 以及众多的参数。
                newPoint.setTime(new SimpleDateFormat("yy:MM:dd:HH:mm:ss:SS").format(new Date()));
                newPoint.setX(e.getX());
                newPoint.setY(e.getY());
                // 参数z 可以由角度 和 笔的长度 乘以sin 角度得到。
                newPoint.setAltitude(PenData.altitude());
                newPoint.setPressure(PenData.pressure());
                newPoint.setAzimuth(PenData.azimuth());

                if (!last.getTime().equals(newPoint.getTime())) {
                    tempPoint.add(newPoint);
                    last = newPoint;
                }

            }

        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    writeCsv();
                    // 绘制图形
                    number ++ ;
                    DataUtils.now ++ ;

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                og = null;
            }
        });

        this.setFocusable(true);
    }

    void writeCsv() throws IOException {

        // 文件名
        if (StartUI.USERNAME.equals("")) StartUI.USERNAME = "null";
        // 创建文件路径
        String path = DataUtils.getDir();

        File f = new File(path);

        if (!f.exists()) {
            f.mkdirs(); // 创建文件目录
        }

        File csvFile = new File(DataUtils.getPath());
        if (!csvFile.exists()) {
            csvFile.createNewFile();
        }

        // 表头和内容
        String[]  headers = {"number", "milliseconds", "x", "y", "pressure", "azimuth", "altitude", "time", "Speed_x", "Speed_Ax", "Speed_y", "Speed_Ay", "Speed_abs", "Speed_A_abs", "Strokes_Number", "PerStrokes_Time"};
        // 写表头和内容，因为csv文件中区分没有那么明确，所以都使用同一函数，写成功就行
        try {
            // csvWriter.writeRecord(headers, true); // 可以传入第二个参数， 将写入csv中的文件进行追加。
            if (number == 1 ){
                CsvWriter csvWriter = new CsvWriter(DataUtils.getPath(), ',', Charset.forName("UTF-8"));
                csvWriter.writeRecord(headers);
                csvWriter.close();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd:HH:mm:ss:SS");

            double[][] ss = getInformation();
            int index = 0;

            for (int i = 0; i < pointInfo.size(); i ++ ) {
                long startT = sdf.parse(pointInfo.get(0).get(0).getTime()).getTime();
                long startPerPenDraw = sdf.parse(pointInfo.get(i).get(0).getTime()).getTime();
                for (int j = 0; j < pointInfo.get(i).size(); j ++ ) {
                    //String[] content = {String.valueOf(i.get(j).getX()), String.valueOf(i.get(j).getY()),String.valueOf(i.get(j).getPressure()),String.valueOf(i.get(j).getAzimuth()),String.valueOf(i.get(j).getAltitude()),String.valueOf(i.get(j).getTangentPressure()),i.get(j).getTime(), String.valueOf(i + 1)};
                    String[] content = {
                            String.valueOf(number),
                            String.valueOf(sdf.parse(pointInfo.get(i).get(j).getTime()).getTime() - startT),
                            String.valueOf(pointInfo.get(i).get(j).getX()),
                            String.valueOf(Toolkit.getDefaultToolkit().getScreenSize().height - pointInfo.get(i).get(j).getY()),
                            String.valueOf(pointInfo.get(i).get(j).getPressure()),
                            String.valueOf(pointInfo.get(i).get(j).getAzimuth()/10),
                            String.valueOf(pointInfo.get(i).get(j).getAltitude()/10),
                            pointInfo.get(i).get(j).getTime(),
                            String.valueOf(ss[index][0]),
                            String.valueOf(ss[index][1]),
                            String.valueOf(ss[index][2]),
                            String.valueOf(ss[index][3]),
                            String.valueOf(sqrt(ss[index][0]*ss[index][0] + ss[index][2]*ss[index][2])),
                            String.valueOf(sqrt(ss[index][1]*ss[index][1] + ss[index][3]*ss[index][3])),
                            String.valueOf(i + 1),
                            String.valueOf(sdf.parse(pointInfo.get(i).get(j).getTime()).getTime() - startPerPenDraw)
                    };

                    writeFileToCsv(content, DataUtils.getPath());
                    index ++ ;
                }
            }
            pointInfo = new CopyOnWriteArrayList<CopyOnWriteArrayList<PointInfo>>();
            tempPoint = new CopyOnWriteArrayList<PointInfo>();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 准备绘图。
    public void paintComponent(){
        if (og == null) {
            // 创建一个和 JPanel 一样的 图形缓冲。
            image = this.createImage(this.getWidth(),this.getHeight());
            if (image != null) og = image.getGraphics();
        }

        if (og != null) {
            super.paint(og);    //调用父类的  paint 会刷新屏幕。
            // 画笔加粗,向下强制转型。
            Graphics2D tempG = (Graphics2D)og;

            // 开始画图。
            // 先把 当前画笔里面的 先画出来。
            Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize().width / 5,Toolkit.getDefaultToolkit().getScreenSize().height / 5);
            tempG.setColor(Color.pink);
            tempG.fill(rectangle);
            tempG.setColor(Color.black);
            tempG.setStroke(new BasicStroke(2.0f));

            if (DataUtils.status == 0) {
                tempG.setFont(new Font("微软雅黑", Font.BOLD, 20));
                tempG.drawString(DataUtils.getDescription(), 0,90);
            }
            // 在右上角进行绘图
            else if (DataUtils.status == 1) {
                tempG.setFont(new Font("微软雅黑", Font.BOLD, 20));
                tempG.drawString(DataUtils.getDescription(), 0,Toolkit.getDefaultToolkit().getScreenSize().height / 5 + 20);

                List<String> word = DataUtils.structuredWord;
                if (DataUtils.now < word.size()) {
                    tempG.setFont(new Font("微软雅黑", Font.BOLD, 80));
                    tempG.drawString(word.get(DataUtils.now), Toolkit.getDefaultToolkit().getScreenSize().height / 10,Toolkit.getDefaultToolkit().getScreenSize().height / 10);
                }
            } else if (DataUtils.status == 2){

                tempG.setFont(new Font("微软雅黑", Font.BOLD, 20));
                tempG.drawString(DataUtils.getDescription(), 0,Toolkit.getDefaultToolkit().getScreenSize().height / 5 + 20);

                List<List<List<WritePoint>>> handwritingWord = DataUtils.handwritingWord;
                if (DataUtils.now < handwritingWord.size()) {
                    List<List<WritePoint>> word = handwritingWord.get(DataUtils.now);
                    for (List<WritePoint> list: word) {
                        int startX = list.get(0).x;
                        int startY = Toolkit.getDefaultToolkit().getScreenSize().height - list.get(0).y;


                        for (WritePoint writePoint : list) {
                            tempG.drawLine(startX / 5 , startY / 5 , writePoint.x / 5,Toolkit.getDefaultToolkit().getScreenSize().height / 5 - writePoint.y / 5);
                            startX = writePoint.x;
                            startY = Toolkit.getDefaultToolkit().getScreenSize().height - writePoint.y;
                        }
                    }
                }
            }
            tempG.setStroke(new BasicStroke(4.0f));
            for (int i = 1; i < tempPoint.size(); i ++ ) {
                if (i < tempPoint.size()) tempG.drawLine(tempPoint.get(i - 1).getX(), tempPoint.get(i - 1).getY(),tempPoint.get(i).getX(),tempPoint.get(i).getY());
            }
            // 然后把每一个笔画都给绘出来。
            for (CopyOnWriteArrayList<PointInfo> i: pointInfo) {
                for (int j = 1; j < i.size(); j ++ ) {
                    tempG.drawLine(i.get(j - 1).getX(), i.get(j - 1).getY(), i.get(j).getX(), i.get(j).getY());
                }
            }
        }
        // 重新绘制。
        this.repaint();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image, 0, 0, this);     // 进行2D绘图
    }

    // 通过三次样条获取速度。返回一个四列的二维数组。 1，x速度 2，x加速度 3, y轴速度 4，y轴加速度。
    double[][] getInformation() throws ParseException {

        int index = 0;
        for (int i = 0; i < pointInfo.size(); i ++ ) index += pointInfo.get(i).size();
        // 创建返回的结果。
        double[][] answer = new double[index][4];
        index = 0;

        // 时间格式。
        SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd:HH:mm:ss:SS");
        for (int i = 0; i < pointInfo.size(); i ++ ) {
            // 对于每一条线。// 一条线的点数。
            int count = pointInfo.get(i).size();
            double startX = pointInfo.get(i).get(0).getX();
            double startY = pointInfo.get(i).get(0).getY();
            double startT = sdf.parse(pointInfo.get(i).get(0).getTime()).getTime();
            // 创建可以 用于三次样条计算的 数组。

            double[] tInfo = new double[count];
            double[] xInfo = new double[count];
            double[] yInfo = new double[count];

            for (int j = 0; j < pointInfo.get(i).size(); j ++ ) {

                xInfo[j] = pointInfo.get(i).get(j).getX() - startX;
                yInfo[j] = pointInfo.get(i).get(j).getY() - startY;
                tInfo[j] = sdf.parse(pointInfo.get(i).get(j).getTime()).getTime() - startT;
            }

            // 进行三次样条计算。返回一个二位数组，第一列速度，第二列加速度。
            double[][] ansX = Spline.spline(tInfo, xInfo, 0, 0);
            double[][] ansY = Spline.spline(tInfo, yInfo, 0, 0);

//            for (int j = 0; j < count; j ++ ){
//                System.out.println(ansX[j][0] + " " + ansX[j][1] + " ------- " + ansY[j][0] + " " + ansY[j][1]);
//            }

            // 将结果保存。
            for (int j = 0; j < count; j ++ ) {
                answer[index][0] = ansX[j][0];
                answer[index][1] = ansX[j][1];
                answer[index][2] = ansY[j][0];
                answer[index][3] = ansY[j][1];
                index ++ ;
            }
        }
        return answer;
    }

    public void writeFileToCsv(String[] str, String file) {
        File f = new File(file);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f,true));
            CsvWriter cwriter = new CsvWriter(writer,',');
            cwriter.writeRecord(str,false);
            cwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
