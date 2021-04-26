package ru.vvsu.diseaseanalysisdes.models;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;

public class AlgoSearch{
    private double percent; //value from 0 to 100
    private final String[] selections;
    private final String[] diseases;
    private boolean isNextSearch;
    private int rangeSelection;
    private int iSelectMultiple;
    private int iRangeIMB;
    private int variation;
    private int[] arr;

    private final Map<String,Integer> questionMap;
    private final List<String> list;

    public AlgoSearch (){ this(0); }
    public AlgoSearch (double percent){
        this.percent=percent;
        arr = null;
        isNextSearch=true;
        iSelectMultiple = 1;
        rangeSelection = 1;
        variation = 1;
        iRangeIMB = 1;
        diseases = new String[]{"osteochondrosis","rheumatoid_arthritis","stroke","myocardial_infarction",
                "coronary_heart_disease","arrhythmia","kidney_disease","thyroid_disease"};
        selections = new String[]{"waist","hips","age","walk","cigarettes",
                "average_systolic","average_diastolic","average_heart_rate","total_cholesterol","hdl","lpa",
                "apob","glucose","creatinine","uric_acid","crp","insulin","tsh","probnp"};
        questionMap = new HashMap<>(9);
        list = new ArrayList<>();
        initializeMap(1);
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
        if(isNextSearch){
            iSelectMultiple = 1;
            rangeSelection = 1;
            iRangeIMB = 1;
            variation = 1;
            initializeMap(rangeSelection);
            list.clear();
            arr = null;
        }
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

    private void setListAnswerOnQuestions(Serializable user){
        for(Field field: user.getClass().getFields()){
            try {
                String val = (String) field.get(user);
                if(val != null){
                    if(questionMap.containsKey(field.getName())){
                        list.add(field.getName());
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public int getFactorial(int f) {
        if (f <= 1) {
            return 1;
        }
        else {
            return IntStream.rangeClosed(2, f).reduce((x, y) -> x * y).orElse(0);
        }
    }

    public int getCombinations(int n, int m){
        if(n < m){
            throw new NoSuchElementException("Error n < m");
        }
        return getFactorial(n)/getFactorial(n-m)/getFactorial(m);
    }

    private int[] generateCombinations(int[] arr, int M, int N)
    {
        if (arr == null)
        {
            arr = new int[M];
            for (int i = 0; i < M; i++)
                arr[i] = i + 1;
            return arr;
        }
        for (int i = M - 1; i >= 0; i--)
            if (arr[i] < N - M + i + 1)
            {
                arr[i]++;
                for (int j = i; j < M - 1; j++)
                    arr[j + 1] = arr[j] + 1;
                return arr;
            }
        return null;
    }

    public StringBuilder getQueryNonSelections(Serializable user){
        StringBuilder sb = new StringBuilder();
        if(arr==null){
            if(list.isEmpty()){
                setListAnswerOnQuestions(user);
                System.out.println(list);
            }
            arr = generateCombinations(arr, iSelectMultiple, list.size());
        }
        int countSelectColumn = 0;
        String strHeight = "", strWeight = "";
        for(Field field: user.getClass().getFields()){
            try {
                String val = (String) field.get(user);
                if(val != null){
                    if(Arrays.stream(selections).noneMatch(n -> n.equals(field.getName()))) {
                        if(!isNextSearch && list.contains(field.getName()) &&
                            field.getName().equals(list.get(Objects.requireNonNull(arr)[countSelectColumn]-1))){
                            String str = field.getName() + ">=" + getMinVal(val) + " and "
                                    + field.getName() + "<=" + getMaxVal(val) + " and ";
                            sb.append(str);
                            countSelectColumn++;
                            if (countSelectColumn == iSelectMultiple) {
                                arr = generateCombinations(arr, iSelectMultiple, list.size());
                                isNextSearch = true;
                                variation++;
                                if(variation > getCombinations(list.size(),iSelectMultiple)){
                                    arr = null;
                                    variation=1;
                                    rangeSelection++;
                                    if (rangeSelection == 4) {
                                        rangeSelection = 1;
                                        iSelectMultiple++;
                                        if (iSelectMultiple > list.size()) {
                                            iSelectMultiple = 1;
                                            iRangeIMB++;
                                        }
                                    }
                                }
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
                    else if (val.equals("")) {
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
                                String str = "";
                                if(field.getName().equals("age")){
                                    if (Double.parseDouble(getMinK(val)) <= 23 && Double.parseDouble(getMaxK(val)) <= 23){
                                        str = field.getName()+"=23 and ";
                                    }else if(Double.parseDouble(getMinK(val)) <= 23){
                                        str = field.getName()+">=23 and "+field.getName()+"<="+getMaxK(val)+" and ";
                                    }else if (Double.parseDouble(getMaxK(val)) <= 23){
                                        str = field.getName()+">="+getMinK(val)+" and "+field.getName()+"<=23 and ";
                                    }
                                }
                                else {
                                    str = field.getName()+">="+getMinK(val)+" and "+field.getName()+"<="+getMaxK(val)+" and ";
                                }
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
