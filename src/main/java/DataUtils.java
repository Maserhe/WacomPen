import com.csvreader.CsvReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * ����:
 *  �������� ����·���� �ӹ���Ϣ�Ĺ�����
 * @author Maserhe
 * @create 2021-09-29 20:45
 */
public class DataUtils {

    /**
     * ��д ״̬ 1�������֣� 2�� ��д��
     */
    public static int status = 0;

    /**
     * ��ǰ��ʾ�� ��
     */
    public static int now = 0;

    public static List<String> structuredWord;

    public static List<List<List<WritePoint>>> handwritingWord;

    public static void init() {

        // �ж�״̬
        if (StartUI.HANDWRITING_STATUS == 0 && StartUI.CONTENT_INFO_STATUS == 0) {
            status = 1;
        }
        else if (StartUI.HANDWRITING_STATUS == 0 && StartUI.CONTENT_INFO_STATUS != 0) {
            status = 0;
        } else {
            status = 2;
        }

        // ��д����
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
            // ��ȡ����
            String path = getPath();

            String temp = "";
            if (StartUI.HANDWRITING_STATUS == 1) {
                temp = "��α��";
            } else {
                temp = "����α��";
            }
            int index = path.indexOf(temp);
            String pathStr = path.substring(0,index) + "��ʵ�ʼ�"+ path.substring(index + 4);
            File csvFile = new File(pathStr);
            try {
                if (!csvFile.exists()) {
                    csvFile.createNewFile();
                }
                reader = new CsvReader(pathStr, ',', Charset.forName("GBK"));
                // ��ȡ����
                reader.readHeaders();
                // �ڼ�����
                int number = 1;
                // �ڼ��ʻ�
                index = 1;

                // ��
                List<List<WritePoint>> word = new ArrayList<List<WritePoint>>();

                // �ʻ�
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
                System.out.println("�ļ�������");
            }

        }


    }

    /**
     * ���� �ļ���ַ
     * @return
     * @throws PenErrorException
     */
    public static String getPath() {
        return getDir() + "\\ʵ��" + String.valueOf(StartUI.FREQUENCY_INFO) + ".csv";
    }

    /**
     * �ļ�����Ŀ¼
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
                tmp = "��ʵ�ʼ�";
                break;
            case 1:
                tmp = "��α��";
                break;
            case 2:
                tmp = "����α��";
                break;
        }
        path.append("\\" + tmp);

        switch (StartUI.CONTENT_INFO_STATUS) {
            case 0:
                tmp = "�涨����";
                break;
            case 1:
                tmp = "��������";
                break;
            case 2:
                tmp = "����ǩ��";
                break;
        }
        path.append("\\" + tmp);

        switch (StartUI.TASK_INFO_STATUS) {
            case 0:
                tmp = "������";
                break;
            case 1:
                tmp = "��������";
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
