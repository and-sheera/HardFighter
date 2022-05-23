package com.shirnin.hardfighter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Monster implements Serializable {
    private int level;
    private HashMap<String, Integer> winCharacteristics;
    private int imgSrc;

    public Monster(HashMap<String, Integer> winCharacteristics, int imgSrc) {
        this.level = 1;
        this.winCharacteristics = winCharacteristics;
        this.imgSrc = imgSrc;
    }

    public boolean attack(HashMap<String, Integer> heroCharacteristic) {
        List<String> keys = new ArrayList<String>(winCharacteristics.keySet());
        for(int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            int winCharacteristic = winCharacteristics.get(key);

            if (winCharacteristic + (level - 1) > heroCharacteristic.get(key)) {
                return false; // defeat
            }
        }
        return true;
    }

    public int getImgSrc() {
        return imgSrc;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRequirements() {
        String requirements = "Необходмо:";
        List<String> keys = new ArrayList<String>(winCharacteristics.keySet());
        for(int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            int winCharacteristic = winCharacteristics.get(key);
            requirements += "\n" + key + ": " + winCharacteristic + (level - 1);
        }
        return requirements;
    }
}
