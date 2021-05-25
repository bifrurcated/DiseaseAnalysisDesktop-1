package ru.vvsu.diseaseanalysisdes.models;

public class StatisticsDiseases {
    private String osteochondrosis;
    private String rheumatoid_arthritis;
    private String stroke;
    private String myocardial_infarction;
    private String coronary_heart_disease;
    private String arrhythmia;
    private String kidney_disease;
    private String thyroid_disease;
    private String imb;

    public StatisticsDiseases(){}

    public String getOsteochondrosis() {
        return osteochondrosis + "%";
    }

    public void setOsteochondrosis(String osteochondrosis) {
        this.osteochondrosis = osteochondrosis;
    }

    public String getRheumatoid_arthritis() {
        return rheumatoid_arthritis + "%";
    }

    public void setRheumatoid_arthritis(String rheumatoid_arthritis) {
        this.rheumatoid_arthritis = rheumatoid_arthritis;
    }

    public String getStroke() {
        return stroke + "%";
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public String getMyocardial_infarction() {
        return myocardial_infarction + "%";
    }

    public void setMyocardial_infarction(String myocardial_infarction) {
        this.myocardial_infarction = myocardial_infarction;
    }

    public String getCoronary_heart_disease() {
        return coronary_heart_disease + "%";
    }

    public void setCoronary_heart_disease(String coronary_heart_disease) {
        this.coronary_heart_disease = coronary_heart_disease;
    }

    public String getArrhythmia() {
        return arrhythmia + "%";
    }

    public void setArrhythmia(String arrhythmia) {
        this.arrhythmia = arrhythmia;
    }

    public String getKidney_disease() {
        return kidney_disease + "%";
    }

    public void setKidney_disease(String kidney_disease) {
        this.kidney_disease = kidney_disease;
    }

    public String getThyroid_disease() {
        return thyroid_disease + "%";
    }

    public void setThyroid_disease(String thyroid_disease) {
        this.thyroid_disease = thyroid_disease;
    }
}
