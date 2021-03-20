package models;

import java.io.Serializable;
import java.io.*;


public class Human implements Serializable {
    private String paramA, paramB, paramC;
    private String gender,
            meatEat,
            fishEat,
            vegEat,
            sweetsEat,
            curdEat,
            cheeseEat,
            physicalLoad,
            walkMinutesADay,
            physicalLoadAWeek,
            cigarettesInADay,
            longOfSleep,
            sleep,
            waitSleep,
            osteochondrosis,
            rheumatoidArthritis,
            stroke,
            myocardialInfarction,
            IBS,
            arrhythmia,
            kidneyDiseases,
            SHCHZHZabol,
            headaches,
            restless,
            height,
            weight,
            waistCircumference,
            hipCircumference,
            age,
            avgSAD,
            avgDAD,
            avgCHSS,
            cholesterol,
            LPVP,
            LPa,
            AROV,
            glucose,
            creatinine,
            uricAcid,
            cReactedProtein,
            insulin,
            TTG,
            PROBNP,
            ID;


    public Human(){
        this.paramA = "construct a";
        this.paramB = "construct b";
        this.paramC = "construct c";
    }

    public Human(String a, String b, String c){
        this.paramA = a;
        this.paramB = b;
        this.paramC = c;
    }


    public void serializeToFile(){
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream("person.out"));
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static Human serializeFromFile() throws ClassNotFoundException {
        Human objectClass = null;
        try {
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream("person.out"));
            objectClass = (Human) objectInputStream.readObject();
        objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objectClass;
    }

    public String getString(){
        String buf = ("A:\""+this.paramA+"\"\nB:\""+this.paramB+"\"\nC:\""+this.paramC+"\"");
        System.out.println("A:\""+this.paramA+"\"\nB:\""+this.paramB+"\"\nC:\""+this.paramC+"\"");
        return buf;
    }

    @Override
    public String toString() {
        return "TestClass{" +
                "paramA='" + paramA + '\'' +
                ", paramB='" + paramB + '\'' +
                ", paramC='" + paramC + '\'' +
                '}';
    }
}
