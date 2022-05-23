package com.shirnin.hardfighter;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hardfighter.R;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Fighter hero;

    public static final String FILE_NAME = "PROGRESS_DATA";
    private SharedPreferences progress;

    private static final int TRAIN_ACTIVITY_REQUEST_CODE = 0;
    private static final int FIGHT_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.hero = new Fighter(1 , 1, 1, 1, 1, R.drawable.hero_1);

        progress = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        loadData();

        try {
            boolean isIdleness = hero.checkIdleness();
            if (isIdleness) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Вы долго не занимались, поэтому ваши характеристики могли измениться",
                        Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 700);
                toast.show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Button btnFight = findViewById(R.id.btnFight);

        Button btnTrain = findViewById(R.id.btnTrain);
        btnTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TrainActivity.class);
                intent.putExtra(Fighter.class.getSimpleName(), (Serializable) hero);
                startActivityForResult(intent, TRAIN_ACTIVITY_REQUEST_CODE);
            }
        });

        btnFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FightActivity.class);
                intent.putExtra(Fighter.class.getSimpleName(), (Serializable) hero);
                startActivityForResult(intent, FIGHT_ACTIVITY_REQUEST_CODE);
            }
        });

        updScreenData();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        saveData();
//    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TRAIN_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    hero = (Fighter) data.getSerializableExtra(Fighter.class.getSimpleName());
                    updScreenData();
                }
                break;
            case FIGHT_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    hero.lvlUp();
                    updScreenData();
                }
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    public void updScreenData() {
        TextView mainLvl = findViewById(R.id.mainLvl);
        TextView topLvl = findViewById(R.id.topLvl);
        TextView centerLvl = findViewById(R.id.centerLvl);
        TextView bottomLvl = findViewById(R.id.bottomLvl);
        TextView enduranceLvl = findViewById(R.id.enduranceLvl);

        mainLvl.setText(Integer.toString(hero.getCharacteristics().get(Characteristic.mainLvl)));
        topLvl.setText(Integer.toString(hero.getCharacteristics().get(Characteristic.arms)));
        centerLvl.setText(Integer.toString(hero.getCharacteristics().get(Characteristic.torso)));
        bottomLvl.setText(Integer.toString(hero.getCharacteristics().get(Characteristic.legs)));
        enduranceLvl.setText(Integer.toString(hero.getCharacteristics().get(Characteristic.endurance)));

        ProgressBar progressBarCharTop = findViewById(R.id.progressBarCharTop);
        ProgressBar progressBarCharCenter = findViewById(R.id.progressBarCharCenter);
        ProgressBar progressBarCharBottom = findViewById(R.id.progressBarCharBottom);
        ProgressBar progressBarCharEndurance = findViewById(R.id.progressBarCharEndurance);

        progressBarCharTop.setProgress(hero.getCharacteristics().get(Characteristic.arms));
        progressBarCharCenter.setProgress(hero.getCharacteristics().get(Characteristic.torso));
        progressBarCharBottom.setProgress(hero.getCharacteristics().get(Characteristic.legs));
        progressBarCharEndurance.setProgress(hero.getCharacteristics().get(Characteristic.endurance));

        ImageView heroImg = findViewById(R.id.heroImg);
        heroImg.setImageResource(hero.getImgSrc());

    }

    public void saveData() {
        SharedPreferences.Editor editor = progress.edit();
        editor.putInt(Characteristic.mainLvl, hero.getCharacteristics().get(Characteristic.mainLvl));
        editor.putInt(Characteristic.arms, hero.getCharacteristics().get(Characteristic.arms));
        editor.putInt(Characteristic.torso, hero.getCharacteristics().get(Characteristic.torso));
        editor.putInt(Characteristic.legs, hero.getCharacteristics().get(Characteristic.legs));
        editor.putInt(Characteristic.endurance, hero.getCharacteristics().get(Characteristic.endurance));

        List<String> exerciseNames = new ArrayList<String>(hero.getExerciseList().keySet());
        for(int i = 0; i < exerciseNames.size(); i++) {
            String key = exerciseNames.get(i);
            Exercise exerciseIntance = hero.getExerciseList().get(key);
            editor.putString(exerciseIntance.name, exerciseIntance.getLastExerciseDate());
        }

        editor.apply();
    }

    public void loadData() {
        if (progress.contains(Characteristic.mainLvl)) {
            hero.getCharacteristics().put(Characteristic.mainLvl, progress.getInt(Characteristic.mainLvl, 1));
            hero.getCharacteristics().put(Characteristic.arms, progress.getInt(Characteristic.arms, 1));
            hero.getCharacteristics().put(Characteristic.torso, progress.getInt(Characteristic.torso, 1));
            hero.getCharacteristics().put(Characteristic.legs, progress.getInt(Characteristic.legs, 1));
            hero.getCharacteristics().put(Characteristic.endurance, progress.getInt(Characteristic.endurance, 1));

            List<String> exerciseNames = new ArrayList<String>(hero.getExerciseList().keySet());
            for(int i = 0; i < exerciseNames.size(); i++) {
                String key = exerciseNames.get(i);
                Exercise exerciseIntance = hero.getExerciseList().get(key);
                exerciseIntance.setLastExerciseDate(progress.getString(exerciseIntance.name, ""));
            }
        }
    }

//    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//
//                }
//            });

}