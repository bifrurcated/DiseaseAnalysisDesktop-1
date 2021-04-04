package ru.vvsu.diseaseanalysisdes.models;

import java.io.Serializable;
import java.lang.reflect.Field;

public class Algo {
    private double percent; //value from 0 to 100

    public Algo (){ }
    public Algo (double percent){ this.percent=percent; }

    public double getPercent(){
        return percent;
    }
    public void setPercent(double percent){
        this.percent = percent;
    }

    private String getMinK(String val){
        return String.valueOf(Double.parseDouble(val)*(1-(percent/100)));
    }

    private String getMaxK(String val){
        return String.valueOf(Double.parseDouble(val)*(1+(percent/100)));
    }

    public StringBuilder getQuery(Serializable user){
        StringBuilder sb = new StringBuilder();
        for(Field field: user.getClass().getFields()){
            try {
                String val = (String) field.get(user);
                if(val != null){
                    String str = field.getName()+">="+getMinK(val)+" and "+field.getName()+"<="+getMaxK(val)+" and ";
                    sb.append(str);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.delete(sb.length()-5,sb.length());
        return sb;
    }
}
