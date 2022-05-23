package com.shirnin.hardfighter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.hardfighter.R;

import java.io.Serializable;

public class WinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
    }


    @Override
    protected void onStop() {
        super.onStop();

        Intent intent = new Intent(WinActivity.this, MainActivity.class);
        setResult(RESULT_OK, intent);
//        finish();
//
//        Log.v("AAAABBBBBBBBBBB", "stop");
    }
}