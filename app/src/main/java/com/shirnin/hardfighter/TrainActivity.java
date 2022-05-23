package com.shirnin.hardfighter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hardfighter.R;

import java.io.Serializable;
import java.util.HashMap;

public class TrainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        Intent intent = getIntent();
        Fighter hero = null;
        if (intent != null) {
            hero = (Fighter) intent.getSerializableExtra(Fighter.class.getSimpleName());

//            Log.v("item", (String) Integer.toString(hero.getBottomLvl()));

            Spinner trainType = (Spinner) findViewById(R.id.trainType);
            String[] exercisesList = hero.getExerciseList().keySet().toArray(new String[0]);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exercisesList);
            trainType.setAdapter(adapter);


            Exercise currentExercise = hero.getExerciseList().get(exercisesList[0]);
            if (currentExercise != null) {
                this.updateScreen(currentExercise.type);
            }

            Fighter finalHero = hero;
            trainType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Exercise currentExercise = finalHero.getExerciseList().get((String) parent.getItemAtPosition(position));
                    if (currentExercise != null) {
                        updateScreen(currentExercise.type);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            Button buttonSave = findViewById(R.id.buttonSave);
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Spinner trainType = (Spinner) findViewById(R.id.trainType);
                    Exercise currentExercise = finalHero.getExerciseList().get(trainType.getSelectedItem().toString());
                    if (currentExercise != null) {
                        int score = 0;
                        if (currentExercise.type == ExerciseType.CONTINUOUS) {
                            EditText timeInput = findViewById(R.id.exerciseTimeInput);
                            String timeInputString = timeInput.getText().toString();
                            if (timeInputString.length() > 0) {
                                score = Integer.parseInt(timeInputString);
                            }
                        } else {
                            EditText approachesInput = findViewById(R.id.exerciseApproachesInput);
                            EditText timesInput = findViewById(R.id.exerciseTimesInput);
                            String approachesInputString = approachesInput.getText().toString();
                            String timesInputString = timesInput.getText().toString();
                            if (approachesInputString.length() > 0 && timesInputString.length() > 0) {
                                score = Integer.parseInt(approachesInputString) * Integer.parseInt(timesInputString);
                            }
                        }
                        if (score != 0) {
                            finalHero.addPoints(currentExercise, score);
                            Intent intent = new Intent(TrainActivity.this, MainActivity.class);
                            intent.putExtra(Fighter.class.getSimpleName(), (Serializable) finalHero);

                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Введите корректные данные",
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }

                }
            });
        }
    }

    private void updateScreen(ExerciseType type) {
        LinearLayout exerciseApproaches = findViewById(R.id.exerciseApproaches);
        LinearLayout exerciseTime = findViewById(R.id.exerciseTime);
        LinearLayout exerciseTimes = findViewById(R.id.exerciseTimes);

        if (type == ExerciseType.CONTINUOUS) {
            exerciseApproaches.setVisibility(View.GONE);
            exerciseTimes.setVisibility(View.GONE);
            exerciseTime.setVisibility(View.VISIBLE);
        } else {
            exerciseTime.setVisibility(View.GONE);
            exerciseApproaches.setVisibility(View.VISIBLE);
            exerciseTimes.setVisibility(View.VISIBLE);
        }
    }
}