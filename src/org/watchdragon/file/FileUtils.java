package org.watchdragon.file;

import org.watchdragon.Main;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;

/**
 * Created by tt36999 on 2017/6/23.
 */
public class FileUtils {
    public static String readFileAsString(String path) throws IOException {
        File file = new File(path);
        if(file.isFile()&&file.exists()){
            Long filelength = file.length();
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[filelength.intValue()];
            fileInputStream.read(buffer);
            fileInputStream.close();
            return new String(buffer);
        }else{
            throw new FileNotFoundException("can not found expected file");
        }
    }

    public static void writeFileFromString(String content,String path,boolean append) throws IOException {
        File file = new File(path);
        if(!append)
            file.deleteOnExit();
        if(!file.isFile()||!file.exists()) {
            mkDir(file.getParentFile());
            file.createNewFile();
        }
        if(!file.canWrite())
            file.setWritable(true);
        FileOutputStream fileOutputStream = new FileOutputStream(file,true);
        byte[] buffer = new byte[content.length()];
        buffer = content.getBytes("UTF8");
        fileOutputStream.write(buffer);
        fileOutputStream.close();
    }

    public static void mkDir(File file) {
        if (file.getParentFile().exists()) {
            file.mkdir();
        } else {
            mkDir(file.getParentFile());
            file.mkdir();
        }
    }
}
