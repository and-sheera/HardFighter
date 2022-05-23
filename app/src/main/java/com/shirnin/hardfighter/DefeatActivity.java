package com.shirnin.hardfighter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hardfighter.R;

import org.w3c.dom.Text;

public class DefeatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defeat);

        Intent intent = getIntent();
        String requirements = intent.getStringExtra("requirements");
        TextView defeatRequirements = findViewById(R.id.defeatRequirements);
        defeatRequirements.setText(requirements);
    }
}