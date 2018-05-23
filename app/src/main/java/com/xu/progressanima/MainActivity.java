package com.xu.progressanima;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xu.progressanima.progressbar.CirProgress;
import com.xu.progressanima.progressbar.HorProgress;

public class MainActivity extends AppCompatActivity {

    private HorProgress horProgress;
    private CirProgress cirProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        horProgress = findViewById(R.id.hor_progress);
        cirProgress = findViewById(R.id.cir_progress);
        horProgress.setProgressWithAnimation(100,0.5f,6);
        cirProgress.setProgressWithAnimation(26,0.65f);
    }
}
