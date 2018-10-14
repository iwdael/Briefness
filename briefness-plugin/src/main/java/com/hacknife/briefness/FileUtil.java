package com.hacknife.briefness;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */

public class FileUtil {
    private static String filenameTemp;


    public static boolean createFile(String path, String filecontent) {
        Boolean bool = false;
        filenameTemp = path;
        File file = new File(filenameTemp);
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();

            }
            if (!file.exists()) {
                file.createNewFile();
                bool = true;


                writeFileContent(filenameTemp, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }


    public static boolean writeFileContent(String filepath, String newstr) throws IOException {
        Boolean bool = false;
        String filein = newstr + "\r\n";
        String temp ;

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);

            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();


            for (int i = 0; (temp = br.readLine()) != null; i++) {
                buffer.append(temp);

                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }

    public static String readTextFile(String path) {
        StringBuffer sb = new StringBuffer();
        try {
            File file = new File(path);
            if (!file.exists() || file.isDirectory())
                throw new FileNotFoundException();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String temp = null;

            temp = br.readLine();
            while (temp != null) {
                sb.append(temp + " ");
                temp = br.readLine();
            }

        } catch (Exception e) {

        }
        return sb.toString();
    }

}