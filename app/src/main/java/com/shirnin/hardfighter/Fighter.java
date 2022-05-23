package com.shirnin.hardfighter;

import android.util.Log;

import com.example.hardfighter.R;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Fighter implements Serializable {
    private int imgSrc;
    private HashMap<String, Integer> characteristics;
    private HashMap<String, Exercise> exerciseList;

    public Fighter(int mainLvl, int topLvl, int centerLvl, int bottomLvl, int enduranceLvl, int imgSrc) {
        characteristics = new HashMap<>();
        characteristics.put(Characteristic.mainLvl, mainLvl);
        characteristics.put(Characteristic.arms, topLvl);
        characteristics.put(Characteristic.torso, centerLvl);
        characteristics.put(Characteristic.legs,bottomLvl);
        characteristics.put(Characteristic.endurance, enduranceLvl);
        this.imgSrc = imgSrc;

        Exercise pushups = new Exercise("Отжимания", ExerciseType.DISCRETE, 2, Characteristic.arms);
        Exercise running = new Exercise("Бег", ExerciseType.CONTINUOUS, 1, Characteristic.legs);
        Exercise andominalCrunches = new Exercise("Пресс", ExerciseType.DISCRETE, 1, Characteristic.torso);
        Exercise plank = new Exercise("Планка", ExerciseType.CONTINUOUS, 2, Characteristic.endurance);

        exerciseList = new HashMap<>();
        exerciseList.put(pushups.name, pushups);
        exerciseList.put(running.name, running);
        exerciseList.put(andominalCrunches.name, andominalCrunches);
        exerciseList.put(plank.name, plank);
    }

    public HashMap<String, Integer> getCharacteristics() {
        return characteristics;
    }

    public Map<String, Exercise> getExerciseList() {
        return exerciseList;
    }

    public int getMainLvl() {
        return characteristics.get(Characteristic.mainLvl);
    }

    public void lvlUp() {
        characteristics.put(Characteristic.mainLvl, characteristics.get(Characteristic.mainLvl) + 1);
        switch (getMainLvl()) {
            case 1:
                imgSrc = R.drawable.hero_1;
                break;
            case 2:
            case 3:
                imgSrc = R.drawable.hero_2;
                break;
            case 4:
            case 5:
            case 6:
                imgSrc = R.drawable.hero_3;
                break;
            case 7:
            case 8:
            case 9:
                imgSrc = R.drawable.hero_4;
                break;
            default:
                imgSrc = R.drawable.hero_5;
        }
    }

    public void addPoints(Exercise exercise, int score) {
        int totalScore = characteristics.get(exercise.getCharacteristicsLlvUp()) + exercise.getScore(score);
        if (totalScore > 100) {
            totalScore = 100;
        }

        exerciseList.get(exercise.name).setLastExerciseDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
        characteristics.put(exercise.getCharacteristicsLlvUp(), totalScore);
    }

    public boolean checkIdleness() throws ParseException {
        List<String> exerciseNames = new ArrayList<>(exerciseList.keySet());
        boolean isIdleness = false;
        for(int i = 0; i < exerciseNames.size(); i++) {
            String key = exerciseNames.get(i);
            Exercise exerciseInstance = exerciseList.get(key);
            String lastExDateString =  exerciseInstance.getLastExerciseDate();

            if (!Objects.equals(lastExDateString, "")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date lastExerciseDate = sdf.parse(lastExDateString);
                Date nowDate = new Date();

                long differenceInTime = nowDate.getTime() - lastExerciseDate.getTime();
                long differenceInDays = (differenceInTime / (1000 * 60 * 60 * 24)) % 365;
                if (differenceInDays >= 7) {
                    isIdleness = true;
                    int nowValueCharacteristic = characteristics.get(exerciseInstance.getCharacteristicsLlvUp());
                    if(nowValueCharacteristic != 0) {
                        characteristics.put(exerciseInstance.getCharacteristicsLlvUp(), nowValueCharacteristic / 2);
                    }

                    exerciseInstance.setLastExerciseDate(sdf.format(nowDate));
                }
            }
        }
        return isIdleness;
    }

    public int getImgSrc() {
        return imgSrc;
    }
}
