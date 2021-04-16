package ru.vvsu.diseaseanalysisdes.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Human implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String,String> countScaleMap;
    private final Map<String,String> diseasesScaleMap;

    public String
            height,
            weight,
            waist,
            hips,
            age,
            walk,
            cigarettes,
            average_systolic,
            average_diastolic,
            average_heart_rate,
            total_cholesterol,
            hdl,
            lpa,
            apob,
            glucose,
            creatinine,
            uric_acid,
            crp,
            insulin,
            tsh,
            probnp,

            id,
            sex,
            freq_meat,
            freq_fish,
            freq_vegatables,
            freq_sweets,
            freq_cottage_cheese,
            freq_cheese,
            exercise_stress_on_work,
            exercise_stress,
            sleep,
            fall_asleep,
            abstinence_from_sleep,
            osteochondrosis,
            rheumatoid_arthritis,
            stroke,
            myocardial_infarction,
            coronary_heart_disease,
            arrhythmia,
            kidney_disease,
            thyroid_disease,
            headaches,
            restless;


    public Human(){
        countScaleMap = new HashMap<>(5);
        countScaleMap.put("1","Редко");
        countScaleMap.put("2","Один или два раза в неделю");
        countScaleMap.put("3","Несколько раз в неделю");
        countScaleMap.put("4","Каждый день");
        diseasesScaleMap = new HashMap<>( 4);
        diseasesScaleMap.put("1","Нет");
        diseasesScaleMap.put("2","Есть, но не доставляет особых проблем");
        diseasesScaleMap.put("3","Есть в интенсивной фазе");
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getHips() {
        return hips;
    }

    public void setHips(String hips) {
        this.hips = hips;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWalk() {
        return walk;
    }

    public void setWalk(String walk) {
        this.walk = walk;
    }

    public String getCigarettes() {
        return cigarettes;
    }

    public void setCigarettes(String cigarettes) {
        this.cigarettes = cigarettes;
    }

    public String getAverage_systolic() {
        return average_systolic;
    }

    public void setAverage_systolic(String average_systolic) {
        this.average_systolic = average_systolic;
    }

    public String getAverage_diastolic() {
        return average_diastolic;
    }

    public void setAverage_diastolic(String average_diastolic) {
        this.average_diastolic = average_diastolic;
    }

    public String getAverage_heart_rate() {
        return average_heart_rate;
    }

    public void setAverage_heart_rate(String average_heart_rate) {
        this.average_heart_rate = average_heart_rate;
    }

    public String getTotal_cholesterol() {
        return total_cholesterol;
    }

    public void setTotal_cholesterol(String total_cholesterol) {
        this.total_cholesterol = total_cholesterol;
    }

    public String getHdl() {
        return hdl;
    }

    public void setHdl(String hdl) {
        this.hdl = hdl;
    }

    public String getLpa() {
        return lpa;
    }

    public void setLpa(String lpa) {
        this.lpa = lpa;
    }

    public String getApob() {
        return apob;
    }

    public void setApob(String apob) {
        this.apob = apob;
    }

    public String getGlucose() {
        return glucose;
    }

    public void setGlucose(String glucose) {
        this.glucose = glucose;
    }

    public String getCreatinine() {
        return creatinine;
    }

    public void setCreatinine(String creatinine) {
        this.creatinine = creatinine;
    }

    public String getUric_acid() {
        return uric_acid;
    }

    public void setUric_acid(String uric_acid) {
        this.uric_acid = uric_acid;
    }

    public String getCrp() {
        return crp;
    }

    public void setCrp(String crp) {
        this.crp = crp;
    }

    public String getInsulin() {
        return insulin;
    }

    public void setInsulin(String insulin) {
        this.insulin = insulin;
    }

    public String getTsh() {
        return tsh;
    }

    public void setTsh(String tsh) {
        this.tsh = tsh;
    }

    public String getProbnp() {
        return probnp;
    }

    public void setProbnp(String probnp) {
        this.probnp = probnp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSex() {
        if(sex != null){
            if(sex.equals("1")){
                return "Мужской";
            }
            return "Женский";
        }
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFreq_meat() {
        return countScaleMap.get(freq_meat);
    }

    public void setFreq_meat(String freq_meat) {
        this.freq_meat = freq_meat;
    }

    public String getFreq_fish() {
        return countScaleMap.get(freq_fish);
    }

    public void setFreq_fish(String freq_fish) {
        this.freq_fish = freq_fish;
    }

    public String getFreq_vegatables() {
        return countScaleMap.get(freq_vegatables);
    }

    public void setFreq_vegatables(String freq_vegatables) {
        this.freq_vegatables = freq_vegatables;
    }

    public String getFreq_sweets() {
        return countScaleMap.get(freq_sweets);
    }

    public void setFreq_sweets(String freq_sweets) {
        this.freq_sweets = freq_sweets;
    }

    public String getFreq_cottage_cheese() {
        return countScaleMap.get(freq_cottage_cheese);
    }

    public void setFreq_cottage_cheese(String freq_cottage_cheese) {
        this.freq_cottage_cheese = freq_cottage_cheese;
    }

    public String getFreq_cheese() {
        return countScaleMap.get(freq_cheese);
    }

    public void setFreq_cheese(String freq_cheese) {
        this.freq_cheese = freq_cheese;
    }

    public String getExercise_stress_on_work() {
        return exercise_stress_on_work;
    }

    public void setExercise_stress_on_work(String exercise_stress_on_work) {
        this.exercise_stress_on_work = exercise_stress_on_work;
    }

    public String getExercise_stress() {
        return exercise_stress;
    }

    public void setExercise_stress(String exercise_stress) {
        this.exercise_stress = exercise_stress;
    }

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    public String getFall_asleep() {
        return countScaleMap.get(fall_asleep);
    }

    public void setFall_asleep(String fall_asleep) {
        this.fall_asleep = fall_asleep;
    }

    public String getAbstinence_from_sleep() {
        return countScaleMap.get(abstinence_from_sleep);
    }

    public void setAbstinence_from_sleep(String abstinence_from_sleep) {
        this.abstinence_from_sleep = abstinence_from_sleep;
    }

    public String getOsteochondrosis() {
        return diseasesScaleMap.get(osteochondrosis);
    }

    public void setOsteochondrosis(String osteochondrosis) {
        this.osteochondrosis = osteochondrosis;
    }

    public String getRheumatoid_arthritis() {
        return diseasesScaleMap.get(rheumatoid_arthritis);
    }

    public void setRheumatoid_arthritis(String rheumatoid_arthritis) {
        this.rheumatoid_arthritis = rheumatoid_arthritis;
    }

    public String getStroke() {
        return diseasesScaleMap.get(stroke);
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public String getMyocardial_infarction() {
        return diseasesScaleMap.get(myocardial_infarction);
    }

    public void setMyocardial_infarction(String myocardial_infarction) {
        this.myocardial_infarction = myocardial_infarction;
    }

    public String getCoronary_heart_disease() {
        return diseasesScaleMap.get(coronary_heart_disease);
    }

    public void setCoronary_heart_disease(String coronary_heart_disease) {
        this.coronary_heart_disease = coronary_heart_disease;
    }

    public String getArrhythmia() {
        return diseasesScaleMap.get(arrhythmia);
    }

    public void setArrhythmia(String arrhythmia) {
        this.arrhythmia = arrhythmia;
    }

    public String getKidney_disease() {
        return diseasesScaleMap.get(kidney_disease);
    }

    public void setKidney_disease(String kidney_disease) {
        this.kidney_disease = kidney_disease;
    }

    public String getThyroid_disease() {
        return diseasesScaleMap.get(thyroid_disease);
    }

    public void setThyroid_disease(String thyroid_disease) {
        this.thyroid_disease = thyroid_disease;
    }

    public String getHeadaches() {
        if(headaches != null){
            if(headaches.equals("1")){
                return "Нет";
            }
            return "Да";
        }
        return headaches;
    }

    public void setHeadaches(String headaches) {
        this.headaches = headaches;
    }

    public String getRestless() {
        return restless;
    }

    public void setRestless(String restless) {
        this.restless = restless;
    }
}
