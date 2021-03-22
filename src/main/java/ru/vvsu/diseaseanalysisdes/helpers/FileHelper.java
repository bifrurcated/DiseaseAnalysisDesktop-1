package ru.vvsu.diseaseanalysisdes.helpers;

import ru.vvsu.diseaseanalysisdes.Main;
import ru.vvsu.diseaseanalysisdes.Settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileHelper {

    public static int getPlatform() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("linux") || osName.contains("unix")) {
            return 0;
        } else if(osName.contains("win")) {
            return 1;
        } else if(osName.contains("mac")) {
            return 2;
        }
        return 3;
    }

    public static File getDir() {
        String home = System.getProperty("user.home", "");
        String path = Settings.DIR_NAME + File.separator;

        if (getPlatform() == 2) {
            return new File(home, "Library/Application Support/" + path);
        }
        return new File(home + File.separator + path);
    }

    public static File loadFile(String fileName, String path) {
        return loadFile(fileName, getDir(), path, getDir().getAbsolutePath() + File.separator + fileName);
    }

    public static File loadFile(String fileName, File dir, String path, String resultPath) {
        File file = new File(dir, fileName);
        if(!file.exists()) {
            InputStream inputStream = Main.instance.getClass().getResourceAsStream(path);

            try {
                FileOutputStream outputStream = new FileOutputStream(resultPath);

                int data;
                while ((data = inputStream.read()) != -1) {
                    outputStream.write(data);
                }

                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void setupDefault() {
        if(!getDir().exists()) {
            getDir().mkdir();
        }
        //todo something
    }
}
