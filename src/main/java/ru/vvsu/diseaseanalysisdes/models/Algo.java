package ru.vvsu.diseaseanalysisdes.models;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Algo {
    private double percent; //value from 0 to 100
    private final String[] selections;
    private final String[] diseases;
    private boolean isWithoutSelection;
    private boolean isNextSearch;
    private int rangeSelection;
    private int iWhichAnswer;
    private int iSelectMultiple;

    private final Map<String,Integer> questionMap;

    public Algo (){ this(0,true); }
    public Algo (double percent, boolean isWithoutSelection){
        this.isWithoutSelection=isWithoutSelection;
        this.percent=percent;
        isNextSearch=true;
        iWhichAnswer = 0;
        iSelectMultiple = 1;
        rangeSelection = 1;
        selections = new String[]{"waist","hips","age","walk","cigarettes",
        "average_systolic","average_diastolic","average_heart_rate","total_cholesterol","hdl","lpa",
        "apob","glucose","creatinine","uric_acid","crp","insulin","tsh","probnp"};
        diseases = new String[]{"osteochondrosis","rheumatoid_arthritis","stroke","myocardial_infarction",
                "coronary_heart_disease","arrhythmia","kidney_disease","thyroid_disease"};
        questionMap = new HashMap<>(9);
        questionMap.put("freq_meat",rangeSelection);
        questionMap.put("freq_fish",rangeSelection);
        questionMap.put("freq_vegatables",rangeSelection);
        questionMap.put("freq_sweets",rangeSelection);
        questionMap.put("freq_cottage_cheese",rangeSelection);
        questionMap.put("freq_cheese",rangeSelection);
        questionMap.put("fall_asleep",rangeSelection);
        questionMap.put("abstinence_from_sleep",rangeSelection);
    }

    public double getPercent(){
        return percent;
    }
    public void setPercent(double percent){
        this.percent = percent;
    }

    public boolean isWithoutSelection() {
        return isWithoutSelection;
    }
    public void setWithoutSelection(boolean withoutSelection) {
        isWithoutSelection = withoutSelection;
    }

    public boolean isNextSearch() {
        return isNextSearch;
    }
    public void setNextSearch(boolean nextSearch) {
        isNextSearch = nextSearch;
    }

    private String getMinK(String val){
        BigDecimal bigDecimal = new BigDecimal(val).multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_UP)));
        if(bigDecimal.compareTo(BigDecimal.ONE) <= 0){
            return "1";
        }
        return bigDecimal.toString();
    }

    private String getMaxK(String val){
        return new BigDecimal(val).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_UP))).toString();
    }


    private String getMinVal(String val){
        int min = Integer.parseInt(val);
        if(min > 1){
            min -= rangeSelection;
        }
        if(min < 1){min = 1;}
        return String.valueOf(min);
    }

    private String getMaxVal(String val){
        int max = Integer.parseInt(val);
        if(max > 1){
            max += rangeSelection-1;
        }else{
            max += rangeSelection;
        }
        if(max > 4){max = 4;}
        return String.valueOf(max);
    }

    private int getCountAnswerOnQuestions(Serializable user){
        int count = 0;
        for(Field field: user.getClass().getFields()){
            try {
                String val = (String) field.get(user);
                if(val != null){
                    if(questionMap.containsKey(field.getName())){
                        count++;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public StringBuilder getQuerySelections(Serializable user){
        StringBuilder sb = new StringBuilder();
        int countSelectColumn = 0;
        String strHeight = "", strWeight = "";
        for(Field field: user.getClass().getFields()){
            try {
                String val = (String) field.get(user);
                if(val != null){
                    boolean isSelections = false;
                    if(!isWithoutSelection){
                        for (String sel: selections) {
                            if(field.getName().equals(sel)){
                                String str;
                                if(val.equals("") || val.equals("0")){
                                    str = field.getName()+" is null and ";
                                }
                                else {
                                    str = field.getName()+">="+getMinK(val)+" and "+field.getName()+"<="+getMaxK(val)+" and ";
                                }
                                sb.append(str);
                                isSelections = true;
                                break;
                            }
                        }

                    }
                    else {
                        for (String sel: selections) {
                            if (field.getName().equals(sel)) {
                                isSelections = true;
                                break;
                            }
                        }
                    }
                    if(!isSelections){
                        if(isNextSearch){
                            if(field.getName().equals("height") && strHeight.equals("")){
                                if(!strWeight.equals("")){
                                    if(rangeSelection >= 1){
                                        String str =  "imb >=" + getMinIMB(val,strWeight) + " and " + "imb <" + getMaxIMB(val,strWeight) + " and ";
                                        sb.append(str);
                                    }else{
                                        double p = percent;
                                        percent = 2*rangeSelection;
                                        String str =  "imb >=" + getMinK(getMinIMB(val,strWeight)) + " and " + "imb <" + getMaxK(getMaxIMB(val,strWeight)) + " and ";
                                        sb.append(str);
                                        percent = p;
                                    }
                                }
                                else {
                                    strHeight=val;
                                }
                            }
                            else if (field.getName().equals("weight") && strWeight.equals("")){
                                if(!strHeight.equals("")){
                                    if(rangeSelection == 1) {
                                        String str = "imb >=" + getMinIMB(strHeight, val) + " and " + "imb <" + getMaxIMB(strHeight, val) + " and ";
                                        sb.append(str);
                                    }else{
                                        double p = percent;
                                        percent = 2*rangeSelection;
                                        String str =  "imb >=" + getMinK(getMinIMB(strHeight,val)) + " and " + "imb <" + getMaxK(getMaxIMB(strHeight,val)) + " and ";
                                        sb.append(str);
                                        percent = p;
                                    }
                                }
                                else{
                                    strWeight=val;
                                }
                            }
                            else {
                                String str = field.getName() + "=" + val + " and ";
                                sb.append(str);
                            }
                        }
                        else{
                            if(questionMap.containsKey(field.getName()) && questionMap.get(field.getName()) == rangeSelection){
                                String str = field.getName() + ">=" + getMinVal(val) + " and " + field.getName() + "<=" + getMaxVal(val) + " and ";
                                sb.append(str);
                                questionMap.put(field.getName(),rangeSelection+1);
                                iWhichAnswer++;
                                int countAnswerOnQuestions = getCountAnswerOnQuestions(user);
                                if(iWhichAnswer == countAnswerOnQuestions){
                                    iWhichAnswer = 0;
                                    rangeSelection++;
                                    if(rangeSelection > 4){
                                        rangeSelection = 1;
                                        iSelectMultiple++;
                                        if(iSelectMultiple > countAnswerOnQuestions){
                                            iSelectMultiple = 1;
                                            //isWithoutSelection = false;
                                            //isNextSearch = true;
                                        }
                                        questionMap.put("freq_meat",rangeSelection);
                                        questionMap.put("freq_fish",rangeSelection);
                                        questionMap.put("freq_vegatables",rangeSelection);
                                        questionMap.put("freq_sweets",rangeSelection);
                                        questionMap.put("freq_cottage_cheese",rangeSelection);
                                        questionMap.put("freq_cheese",rangeSelection);
                                        questionMap.put("fall_asleep",rangeSelection);
                                        questionMap.put("abstinence_from_sleep",rangeSelection);
                                    }
                                }
                                countSelectColumn++;
                                if(countSelectColumn == iSelectMultiple){
                                    isNextSearch = true;
                                    System.out.println("isNextSearch = "+isNextSearch);
                                    System.out.println("str = "+str);
                                }
                            }
                            else{
                                if(field.getName().equals("height") && strHeight.equals("")){
                                    if(!strWeight.equals("")){
                                        if(rangeSelection == 1){
                                            String str =  "imb >=" + getMinIMB(val,strWeight) + " and " + "imb <" + getMaxIMB(val,strWeight) + " and ";
                                            sb.append(str);
                                        }else{
                                            double p = percent;
                                            percent = 2*rangeSelection;
                                            String str =  "imb >=" + getMinK(getMinIMB(val,strWeight)) + " and " + "imb <" + getMaxK(getMaxIMB(val,strWeight)) + " and ";
                                            sb.append(str);
                                            percent = p;
                                        }
                                    }
                                    else {
                                        strHeight=val;
                                    }
                                }
                                else if (field.getName().equals("weight") && strWeight.equals("")){
                                    if(!strHeight.equals("")){
                                        if(rangeSelection >= 1) {
                                            String str = "imb >=" + getMinIMB(strHeight, val) + " and " + "imb <" + getMaxIMB(strHeight, val) + " and ";
                                            sb.append(str);
                                        }else{
                                            double p = percent;
                                            percent = 5*rangeSelection;
                                            String str =  "imb >=" + getMinK(getMinIMB(strHeight,val)) + " and " + "imb <" + getMaxK(getMaxIMB(strHeight,val)) + " and ";
                                            sb.append(str);
                                            percent = p;
                                        }
                                    }
                                    else{
                                        strWeight=val;
                                    }
                                }
                                else {
                                    String str = field.getName() + "=" + val + " and ";
                                    sb.append(str);
                                }

                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.delete(sb.length()-5,sb.length());
        return sb;
    }

    public double getIndexMassBody(String height, String weight){
        double h = Double.parseDouble(height);
        double w = Double.parseDouble(weight);
        return 10000*w/(h*h);
    }

    private String getMaxIMB(String height, String weight){
        double IMB = getIndexMassBody(height,weight);
        if(IMB < 16.00){
            return "16.00";
        } else if (IMB >= 16.00 && IMB < 18.50) {
            return "18.50";
        } else if (IMB >= 18.50 && IMB < 25.00) {
            return "25.00";
        } else if (IMB >= 25.00 && IMB < 30.00) {
            return "30.00";
        } else if (IMB >= 30.00 && IMB < 35.00) {
            return "35.00";
        } else if (IMB >= 40.00) {
            return "100.00";
        }
        return "100.00";
    }
    private String getMinIMB(String height, String weight){
        double IMB = getIndexMassBody(height,weight);
        if(IMB < 16.00){
            return "0.00";
        } else if (IMB >= 16.00 && IMB < 18.50) {
            return "16.00";
        } else if (IMB >= 18.50 && IMB < 25.00) {
            return "18.50";
        } else if (IMB >= 25.00 && IMB < 30.00) {
            return "25.00";
        } else if (IMB >= 30.00 && IMB < 35.00) {
            return "30.00";
        } else if (IMB >= 40.00) {
            return "40.00";
        }
        return "0.00";
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
