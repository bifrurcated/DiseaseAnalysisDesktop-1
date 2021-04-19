package ru.vvsu.diseaseanalysisdes.models;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlgoSearch{
    private double percent; //value from 0 to 100
    private final String[] selections;
    private final String[] diseases;
    private boolean isNextSearch;
    private int rangeSelection;
    private int iWhichAnswer;
    private int iSelectMultiple;
    private int iRangeIMB;

    private final Map<String,Integer> questionMap;

    public AlgoSearch (){ this(0); }
    public AlgoSearch (double percent){
        this.percent=percent;
        isNextSearch=true;
        iWhichAnswer = 0;
        iSelectMultiple = 1;
        rangeSelection = 1;
        iRangeIMB = 1;
        diseases = new String[]{"osteochondrosis","rheumatoid_arthritis","stroke","myocardial_infarction",
                "coronary_heart_disease","arrhythmia","kidney_disease","thyroid_disease"};
        selections = new String[]{"waist","hips","age","walk","cigarettes",
                "average_systolic","average_diastolic","average_heart_rate","total_cholesterol","hdl","lpa",
                "apob","glucose","creatinine","uric_acid","crp","insulin","tsh","probnp"};
        questionMap = new HashMap<>(9);
        initializeMap(rangeSelection);
    }

    private void initializeMap(int defaultValue){
        questionMap.put("freq_meat",defaultValue);
        questionMap.put("freq_fish",defaultValue);
        questionMap.put("freq_vegatables",defaultValue);
        questionMap.put("freq_sweets",defaultValue);
        questionMap.put("freq_cottage_cheese",defaultValue);
        questionMap.put("freq_cheese",defaultValue);
        questionMap.put("fall_asleep",defaultValue);
        questionMap.put("abstinence_from_sleep",defaultValue);
    }

    public double getPercent(){
        return percent;
    }
    public void setPercent(double percent){
        this.percent = percent;
    }

    public boolean isNextSearch() {
        return isNextSearch;
    }
    public void nextSearch(int countSearch) {
        isNextSearch = countSearch > 0;
    }

    private String getMinK(String val){
        BigDecimal bigDecimal = new BigDecimal(val).multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100),4,BigDecimal.ROUND_UP)));
        if(bigDecimal.compareTo(BigDecimal.ONE) <= 0){
            return "1";
        }
        return bigDecimal.toString();
    }

    private String getMaxK(String val){
        return new BigDecimal(val).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100),4,BigDecimal.ROUND_UP))).toString();
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

    public StringBuilder getQueryNonSelections(Serializable user){
        StringBuilder sb = new StringBuilder();
        int countSelectColumn = 0;
        String strHeight = "", strWeight = "";
        for(Field field: user.getClass().getFields()){
            try {
                String val = (String) field.get(user);
                if(val != null){
                    if(Arrays.stream(selections).noneMatch(n -> n.equals(field.getName()))) {
                        if (isNextSearch) {
                            if (field.getName().equals("height") && strHeight.equals("")) {
                                if (!strWeight.equals("")) {
                                    if (iRangeIMB == 1) {
                                        String str = "imb >=" + getMinIMB(val, strWeight) + " and " + "imb <" + getMaxIMB(val, strWeight) + " and ";
                                        sb.append(str);
                                    } else {
                                        double p = percent;
                                        percent = 13.51 * (1 + (iRangeIMB - 2) * 1.96);
                                        String str = "imb >=" + getMinK(getMinIMB(val, strWeight)) + " and " + "imb <" + getMaxK(getMaxIMB(val, strWeight)) + " and ";
                                        sb.append(str);
                                        percent = p;
                                    }
                                } else {
                                    strHeight = val;
                                }
                            }
                            else if (field.getName().equals("weight") && strWeight.equals("")) {
                                if (!strHeight.equals("")) {
                                    if (iRangeIMB == 1) {
                                        String str = "imb >=" + getMinIMB(strHeight, val) + " and " + "imb <" + getMaxIMB(strHeight, val) + " and ";
                                        sb.append(str);
                                    } else {
                                        double p = percent;
                                        percent = 13.51 * (1 + (iRangeIMB - 2) * 1.96);
                                        String str = "imb >=" + getMinK(getMinIMB(strHeight, val)) + " and " + "imb <" + getMaxK(getMaxIMB(strHeight, val)) + " and ";
                                        sb.append(str);
                                        percent = p;
                                    }
                                } else {
                                    strWeight = val;
                                }
                            }
                            else {
                                String str = field.getName() + "=" + val + " and ";
                                sb.append(str);
                            }
                        }
                        else {
                            if (questionMap.containsKey(field.getName()) && questionMap.get(field.getName()) == rangeSelection) {
                                String str = field.getName() + ">=" + getMinVal(val) + " and " + field.getName() + "<=" + getMaxVal(val) + " and ";
                                sb.append(str);
                                questionMap.put(field.getName(), rangeSelection + 1);
                                iWhichAnswer++;
                                int countAnswerOnQuestions = getCountAnswerOnQuestions(user);
                                if (iWhichAnswer == countAnswerOnQuestions) {
                                    iWhichAnswer = 0;
                                    rangeSelection++;
                                    if (rangeSelection > 4) {
                                        rangeSelection = 1;
                                        iSelectMultiple++;
                                        if (iSelectMultiple > countAnswerOnQuestions) {
                                            iSelectMultiple = 1;
                                            iRangeIMB++;
                                        }
                                        initializeMap(rangeSelection);
                                    }
                                }
                                countSelectColumn++;
                                if (countSelectColumn == iSelectMultiple) {
                                    isNextSearch = true;
                                    System.out.println("isNextSearch = " + isNextSearch);
                                    System.out.println("str = " + str);
                                }
                            }
                            else {
                                if (field.getName().equals("height") && strHeight.equals("")) {
                                    if (!strWeight.equals("")) {
                                        if (iRangeIMB == 1) {
                                            String str = "imb >=" + getMinIMB(val, strWeight) + " and " + "imb <" + getMaxIMB(val, strWeight) + " and ";
                                            sb.append(str);
                                        } else {
                                            double p = percent;
                                            percent = 13.51 * (1 + (iRangeIMB - 2) * 1.96);
                                            String str = "imb >=" + getMinK(getMinIMB(val, strWeight)) + " and " + "imb <" + getMaxK(getMaxIMB(val, strWeight)) + " and ";
                                            sb.append(str);
                                            percent = p;
                                        }
                                    } else {
                                        strHeight = val;
                                    }
                                }
                                else if (field.getName().equals("weight") && strWeight.equals("")) {
                                    if (!strHeight.equals("")) {
                                        if (iRangeIMB == 1) {
                                            String str = "imb >=" + getMinIMB(strHeight, val) + " and " + "imb <" + getMaxIMB(strHeight, val) + " and ";
                                            sb.append(str);
                                        } else {
                                            double p = percent;
                                            percent = 13.51 * (1 + (iRangeIMB - 2) * 1.96);
                                            String str = "imb >=" + getMinK(getMinIMB(strHeight, val)) + " and " + "imb <" + getMaxK(getMaxIMB(strHeight, val)) + " and ";
                                            sb.append(str);
                                            percent = p;
                                        }
                                    } else {
                                        strWeight = val;
                                    }
                                }
                                else {
                                    String str = field.getName() + "=" + val + " and ";
                                    sb.append(str);
                                }
                            }
                        }
                    } else if (val.equals("")) {
                        sb.append(field.getName()).append(" is null and ");
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.delete(sb.length()-5,sb.length());
        return sb;
    }

    public StringBuilder getQuerySelections(Serializable user){
        StringBuilder sb = new StringBuilder();
        for(Field field: user.getClass().getFields()){
            try {
                String val = (String) field.get(user);
                if(val != null) {
                    if (!val.equals("") && Arrays.stream(selections).anyMatch(n -> n.equals(field.getName()))) {
                            if(percent==0){
                                sb.append(field.getName()).append("=").append(val).append(" and ");
                            } else {
                                String str = field.getName()+">="+getMinK(val)+" and "+field.getName()+"<="+getMaxK(val)+" and ";
                                sb.append(str);
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
}
