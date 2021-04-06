package ru.vvsu.diseaseanalysisdes.models;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;

public class Algo {
    private double percent; //value from 0 to 100
    private final String[] selections;

    public Algo (){ this(0); }
    public Algo (double percent){
        this.percent=percent;
        selections = new String[]{"height","weight","waist","hips","age","walk","cigarettes",
        "average_systolic","average_diastolic","average_heart_rate","total_cholesterol","hdl","lpa",
        "apob","glucose","creatinine","uric_acid","crp","insulin","tsh","probnp"};
    }

    public double getPercent(){
        return percent;
    }
    public void setPercent(double percent){
        this.percent = percent;
    }

    private String getMinK(String val){
        return new BigDecimal(val).multiply(BigDecimal.valueOf(1-(percent/100))).toString();
    }

    private String getMaxK(String val){
        return new BigDecimal(val).multiply(BigDecimal.valueOf(1+(percent/100))).toString();
    }

    public StringBuilder getQuerySelections(Serializable user){
        StringBuilder sb = new StringBuilder();
        for(Field field: user.getClass().getFields()){
            try {
                String val = (String) field.get(user);
                if(val != null){
                    boolean isSelections = false;
                    for (String sel: selections) {
                        if(field.getName().equals(sel)){
                            String str = field.getName()+">="+getMinK(val)+" and "+field.getName()+"<="+getMaxK(val)+" and ";
                            sb.append(str);
                            isSelections = true;
                        }
                    }
                    if(!isSelections){
                        String str = field.getName()+"="+val+" and ";
                        sb.append(str);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.delete(sb.length()-5,sb.length());
        return sb;
    }

    public String getIndexMassBody(String height, String weight){
        double h = Double.parseDouble(height);
        double w = Double.parseDouble(weight);
        double IMB = 10000*w/(h*h);
        return String.valueOf(IMB);
    }
}
