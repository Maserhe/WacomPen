import com.csvreader.CsvReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *  用做处理 数据路径， 加工信息的工具类
 * @author Maserhe
 * @create 2021-09-29 20:45
 */
public class DataUtils {

    /**
     * 读写 状态 1，电脑字， 2， 手写字
     */
    public static int status = 0;

    /**
     * 当前显示的 字
     */
    public static int now = 0;

    public static List<String> structuredWord;

    public static List<List<List<WritePoint>>> handwritingWord;

    public static void init() {

        // 判断状态
        if (StartUI.HANDWRITING_STATUS == 0 && StartUI.CONTENT_INFO_STATUS == 0) {
            status = 1;
        }
        else if (StartUI.HANDWRITING_STATUS == 0 && StartUI.CONTENT_INFO_STATUS != 0) {
            status = 0;
        } else {
            status = 2;
        }

        // 填写数据
        CsvReader reader = null;
        if (status == 1) {
            structuredWord = new ArrayList<String>();
            String path = System.getProperty("user.dir");
            if (StartUI.TASK_INFO_STATUS == 0) {
                path += "\\easy.csv";
            } else {
                path += "\\hard.csv";
            }
            try {
                reader = new CsvReader(path, ',', Charset.forName("GBK"));
                reader.readHeaders();
                while (reader.readRecord()) {
                    structuredWord.add(reader.get(0));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (status == 2){
            handwritingWord = new ArrayList<List<List<WritePoint>>>();
            // 读取数据
            String path = getPath();

            String temp = "";
            if (StartUI.HANDWRITING_STATUS == 1) {
                temp = "简单伪造";
            } else {
                temp = "熟练伪造";
            }
            int index = path.indexOf(temp);
            String pathStr = path.substring(0,index) + "真实笔迹"+ path.substring(index + 4);
            File csvFile = new File(pathStr);
            try {
                if (!csvFile.exists()) {
                    csvFile.createNewFile();
                }
                reader = new CsvReader(pathStr, ',', Charset.forName("GBK"));
                // 读取数据
                reader.readHeaders();
                // 第几个字
                int number = 1;
                // 第几笔画
                index = 1;

                // 字
                List<List<WritePoint>> word = new ArrayList<List<WritePoint>>();

                // 笔画
                List<WritePoint> stroke = new ArrayList<WritePoint>();

                // t t  add
                // t f  new bi
                // f   new zi new zi
                handwritingWord.add(word);
                word.add(stroke);

                while (reader.readRecord()) {
                    int x = Integer.valueOf(reader.get(2));
                    int y = Integer.valueOf(reader.get(3));
                    int tempIndex = Integer.valueOf(reader.get("Strokes_Number"));
                    int tempNumber = Integer.valueOf(reader.get("number"));

                    if (tempNumber == number) {
                        if (tempIndex == index) {
                            stroke.add(new WritePoint(x, y));
                        } else {
                            stroke = new ArrayList<WritePoint>();
                            stroke.add(new WritePoint(x, y));
                            index ++ ;
                            word.add(stroke);
                        }
                    } else {
                        word = new ArrayList<List<WritePoint>>();
                        number ++ ;
                        index = 1;
                        stroke = new ArrayList<WritePoint>();
                        stroke.add(new WritePoint(x, y));
                        word.add(stroke);
                        handwritingWord.add(word);
                    }
                }



            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件不存在");
            }

        }


    }

    /**
     * 保存 文件地址
     * @return
     * @throws PenErrorException
     */
    public static String getPath() {
        return getDir() + "\\实验" + String.valueOf(StartUI.FREQUENCY_INFO) + ".csv";
    }

    /**
     * 文件所在目录
     * @return
     */
    public static String getDir() {
        StringBuilder path = new StringBuilder(System.getProperty("user.dir") +"\\data\\" + relativelyPath());
        return path.toString();
    }

    private static String relativelyPath() {
        StringBuilder path = new StringBuilder();
        path.append(StartUI.USERNAME);
        String tmp = "";

        switch (StartUI.HANDWRITING_STATUS) {
            case 0:
                tmp = "真实笔迹";
                break;
            case 1:
                tmp = "简单伪造";
                break;
            case 2:
                tmp = "熟练伪造";
                break;
        }
        path.append("\\" + tmp);

        switch (StartUI.CONTENT_INFO_STATUS) {
            case 0:
                tmp = "规定内容";
                break;
            case 1:
                tmp = "自由内容";
                break;
            case 2:
                tmp = "个人签名";
                break;
        }
        path.append("\\" + tmp);

        switch (StartUI.TASK_INFO_STATUS) {
            case 0:
                tmp = "简单任务";
                break;
            case 1:
                tmp = "复杂任务";
                break;
        }

        path.append("\\" + tmp);
        return path.toString();
    }

    public static String getDescription() {
        String s = relativelyPath();
        String[] split = s.split("\\\\");

        StringBuilder ans = new StringBuilder(split[0]);
        for (int i = 1; i < split.length; i ++ ) {
            ans.append("--" + split[i]);
        }
        return ans.toString();
    }


}
