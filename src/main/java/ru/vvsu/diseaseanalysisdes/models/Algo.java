package ru.vvsu.diseaseanalysisdes.models;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

public class Algo {
    private double percent; //value from 0 to 100
    private final String[] selections;
    private final String[] diseases;

    public Algo (){ this(0); }
    public Algo (double percent){
        this.percent=percent;
        selections = new String[]{"height","weight","waist","hips","age","walk","cigarettes",
        "average_systolic","average_diastolic","average_heart_rate","total_cholesterol","hdl","lpa",
        "apob","glucose","creatinine","uric_acid","crp","insulin","tsh","probnp"};
        diseases = new String[]{"osteochondrosis","rheumatoid_arthritis","stroke","myocardial_infarction",
                "coronary_heart_disease","arrhythmia","kidney_disease","thyroid_disease"};
    }

    public double getPercent(){
        return percent;
    }
    public void setPercent(double percent){
        this.percent = percent;
    }

    private String getMinK(String val){
        return new BigDecimal(val).multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_UP))).toString();
    }

    private String getMaxK(String val){
        return new BigDecimal(val).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_UP))).toString();
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

    /**
     * Функция для подсчёта вероятности
     * @param list список людей попавших под выборку
     * @return null if list.size() < 2 else Map ключом является имя болезни,
     * а значение вероятность того, что ты здоров. Вероятность того, что ты болен
     * можно посчитать как 1 - вероятность того, что ты здоров.
     */
    public Map<String, Double> getProbabilityHealthy(List<Human> list){
        Map<String,Integer> map = new HashMap<>(18);
        if(list.size() > 2) {
            list.forEach(human ->
                    Arrays.stream(human.getClass().getFields())
                    .forEach(var -> {
                        for (String str: diseases) {
                            if(var.getName().equals(str)){
                                try {
                                    String value = (String)var.get(human);
                                    if(value != null){
                                        if(value.equals("1")){
                                            int i;
                                            if(map.containsKey(str+"1")){
                                                i = 1+map.get(str+"1");
                                            }else{
                                                i = 1;
                                            }
                                            map.put(str+"1",i); //количество здоровых
                                        }
                                        else if(value.equals("2") || value.equals("3")){
                                            int i;
                                            if(map.containsKey(str+"23")){
                                                i = 1+map.get(str+"23");
                                            }else{
                                                i = 1;
                                            }
                                            map.put(str+"23",i); //количество больных
                                        }
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    })
            );
            Map<String,Double> probabilityMap = new HashMap<>(9);
            for (String dis: diseases) {
                System.out.println(dis+"23");
                int countHealthy = 0, countDisease = 0;
                if(map.containsKey(dis+"1")){
                    countHealthy = map.get(dis+"1");
                }
                if(map.containsKey(dis+"23")){
                    countDisease = map.get(dis+"23");
                }
                int totalCount = countHealthy+countDisease;
                BigDecimal decimal = BigDecimal.valueOf(countHealthy).divide(BigDecimal.valueOf(totalCount),2,BigDecimal.ROUND_UP);
                probabilityMap.put(dis,decimal.doubleValue());
            }
            return probabilityMap;
        }
        return null;
    }
}
