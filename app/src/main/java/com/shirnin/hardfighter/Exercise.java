package com.shirnin.hardfighter;

import android.util.Log;

import java.io.Serializable;
import java.util.Date;

public class Exercise implements Serializable {
    public String name;
    public ExerciseType type;
    private final int difficultyFactor;
    private final String characteristicsLlvUp;
    private String lastExerciseDate;


    public Exercise(String name, ExerciseType type, int difficultyFactor, String characteristicsLlvUp) {
        this.name = name;
        this.type = type;
        this.difficultyFactor = difficultyFactor;
        this.characteristicsLlvUp = characteristicsLlvUp;
        this.lastExerciseDate = "";
    }

    public int getScore(int trainData) {
        return (int) Math.round(trainData * (difficultyFactor / 20.0));
    }

    public String getCharacteristicsLlvUp() {
        return characteristicsLlvUp;
    }

    public void setLastExerciseDate(String lastExerciseDate) {
        this.lastExerciseDate = lastExerciseDate;
    }

    public String getLastExerciseDate() {
        return lastExerciseDate;
    }
}
