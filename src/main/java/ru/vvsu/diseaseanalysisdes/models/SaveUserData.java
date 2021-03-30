package ru.vvsu.diseaseanalysisdes.models;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveUserData {
    private Path filePath;


    public SaveUserData(){
    }

    public SaveUserData(Path filePath){
        this.filePath = filePath;
    }

    public void createDir(Path path){
        try{
            if(!Files.exists(path)){
                Files.createDirectory(path);
            }
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    public void creteFile(Path path){
        int k = 0;
        while(Files.exists(path)){
            k++;
            try {
                Files.createFile(Paths.get(path+"\\"+"save"+k));
            }
            catch (IOException ex){
                ex.printStackTrace();
            }

        }
    }

    public void exportSave(Serializable saveUserDara, String path){
        try(
                FileOutputStream outputStream = new FileOutputStream(path);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)
            )
        {
            objectOutputStream.writeObject(saveUserDara);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public Object importSave(String path){
        try(
                FileInputStream fileInputStream = new FileInputStream(path);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        )
        {
            return objectInputStream.readObject();
        }
        catch(IOException | ClassNotFoundException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
