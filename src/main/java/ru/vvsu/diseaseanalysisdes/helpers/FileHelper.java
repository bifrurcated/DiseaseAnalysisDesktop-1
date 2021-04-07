package ru.vvsu.diseaseanalysisdes.helpers;

import ru.vvsu.diseaseanalysisdes.Main;
import ru.vvsu.diseaseanalysisdes.Settings;

import java.io.*;

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

    public static void serialize(Serializable object, String fileName) {
        try (
                FileOutputStream outputStream = new FileOutputStream(getDir() + File.separator + fileName);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)
            )
        {
            objectOutputStream.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object deserialize(String fileName) {
        try(
                FileInputStream fileInputStream = new FileInputStream(getDir() + File.separator + fileName);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
            )
        {
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
