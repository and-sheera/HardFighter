package com.shirnin.hardfighter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hardfighter.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class FightActivity extends AppCompatActivity {

    Monster currentMonster;
    Fighter hero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        Intent intent = getIntent();
        hero = null;
        if (intent != null) {
            hero = (Fighter) intent.getSerializableExtra(Fighter.class.getSimpleName());
        }

        HashMap<String, Integer> winCharacteristicsMonster1 = new HashMap<String, Integer>();
        winCharacteristicsMonster1.put(Characteristic.arms, 2);
        Monster monster1 = new Monster(winCharacteristicsMonster1, R.drawable.monster_1);

        HashMap<String, Integer> winCharacteristicsMonster2 = new HashMap<String, Integer>();
        winCharacteristicsMonster2.put(Characteristic.torso, 3);
        Monster monster2 = new Monster(winCharacteristicsMonster2, R.drawable.monster_2);

        HashMap<String, Integer> winCharacteristicsMonster3 = new HashMap<String, Integer>();
        winCharacteristicsMonster3.put(Characteristic.legs, 2);
        Monster monster3 = new Monster(winCharacteristicsMonster3, R.drawable.monster_3);

        HashMap<String, Integer> winCharacteristicsMonster4 = new HashMap<String, Integer>();
        winCharacteristicsMonster4.put(Characteristic.endurance, 4);
        Monster monster4 = new Monster(winCharacteristicsMonster4, R.drawable.monster_4);

        HashMap<String, Integer> winCharacteristicsMonster5 = new HashMap<String, Integer>();
        winCharacteristicsMonster5.put(Characteristic.arms, 2);
        winCharacteristicsMonster5.put(Characteristic.legs, 3);
        Monster monster5 = new Monster(winCharacteristicsMonster5, R.drawable.monster_5);

        HashMap<String, Integer> winCharacteristicsMonster6 = new HashMap<String, Integer>();
        winCharacteristicsMonster6.put(Characteristic.torso, 3);
        winCharacteristicsMonster6.put(Characteristic.endurance, 3);
        Monster monster6 = new Monster(winCharacteristicsMonster6, R.drawable.monster_6);

        HashMap<String, Integer> winCharacteristicsMonster7 = new HashMap<String, Integer>();
        winCharacteristicsMonster7.put(Characteristic.arms, 5);
        winCharacteristicsMonster7.put(Characteristic.torso, 4);
        winCharacteristicsMonster7.put(Characteristic.endurance, 4);
        Monster monster7 = new Monster(winCharacteristicsMonster7, R.drawable.monster_7);

        ArrayList<Monster> monstersList = new ArrayList<>();
        monstersList.add(monster1);
        monstersList.add(monster2);
        monstersList.add(monster3);
        monstersList.add(monster4);
        monstersList.add(monster5);
        monstersList.add(monster6);
        monstersList.add(monster7);

        currentMonster = monstersList.get((hero.getMainLvl() - 1) % monstersList.size());
        currentMonster.setLevel((int) ((hero.getMainLvl() - 1) / monstersList.size()) + 1);

        updateScreen();

        Button buttonFight = findViewById(R.id.buttonFight);
        buttonFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isWin = currentMonster.attack(hero.getCharacteristics());
                if (isWin) {
                    ImageView imgFlame = findViewById(R.id.imgFlame);
                    imgFlame.setVisibility(View.VISIBLE);

                    new android.os.Handler(Looper.getMainLooper()).postDelayed(
                        new Runnable() {
                            public void run() {
                                Intent intent = new Intent(FightActivity.this, WinActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);

                                Intent intentMain = new Intent(FightActivity.this, MainActivity.class);
                                setResult(RESULT_OK, intentMain);

                                finish();
                            }
                        },
                    1000);
                } else {
                    Intent intent = new Intent(FightActivity.this, DefeatActivity.class);
                    intent.putExtra("requirements", currentMonster.getRequirements());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
//                Intent intent = new Intent(FightActivity.this, FightActivity.class);
//                intent.putExtra(Fighter.class.getSimpleName(), (Serializable) hero);
//                startActivityForResult(intent, FIGHT_ACTIVITY_REQUEST_CODE);
            }
        });

    }

    private void updateScreen() {
        ImageView heroImg = findViewById(R.id.heroImg);
        heroImg.setImageResource(hero.getImgSrc());

        ImageView monsterImg = findViewById(R.id.monsterImg);
        monsterImg.setImageResource(currentMonster.getImgSrc());

        TextView monsterLvl = findViewById(R.id.monsterLvl);
        monsterLvl.setText("Уровень: " + currentMonster.getLevel());

        TextView mosnterDesc = findViewById(R.id.mosnterDesc);
        mosnterDesc.setText(currentMonster.getRequirements());
    }
}