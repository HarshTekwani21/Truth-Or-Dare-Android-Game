package com.example.truth_dare;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button btn, truthBtn, dareBtn;
    private ViewPager viewPager;
    private Random random = new Random();
    private int lastDirection;
    private MediaPlayer mp;
    private Values values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.button);
        truthBtn = findViewById(R.id.btn1);
        dareBtn = findViewById(R.id.btn2);
        viewPager = findViewById(R.id.viewPager);
        values = new Values();

            BottlePagerAdapter adapter = new BottlePagerAdapter(this, Values.bottles);
        viewPager.setAdapter(adapter);

        truthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String randomTruth = getRandomTruth();
                startTruthActivity(randomTruth);
            }
        });

        dareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String randomDare = getRandomDare();
                startDareActivity(randomDare);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        truthBtn.setEnabled(false);
        dareBtn.setEnabled(false);
        btn.setEnabled(true);
    }

    public void spin(View view) {

        int newDirection = random.nextInt(5400);
        float pivotX = viewPager.getWidth() / 2;
        float pivotY = viewPager.getHeight() / 2;

        Animation rotate = new RotateAnimation(lastDirection, newDirection, pivotX, pivotY);
        rotate.setDuration(2000);
        rotate.setFillAfter(true);
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mp = MediaPlayer.create(MainActivity.this, R.raw.audio);
                mp.start();
                btn.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mp.stop();
                mp.release();
                mp = null;
                truthBtn.setEnabled(true);
                dareBtn.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        lastDirection = newDirection;
        viewPager.startAnimation(rotate);
    }

    private String getRandomTruth() {
        return values.truths[random.nextInt(values.truths.length)];
    }

    private String getRandomDare() {
        return values.dares[random.nextInt(values.dares.length)];
    }

    private void startTruthActivity(String randomTruth) {
        Intent intent = new Intent(MainActivity.this, TruthActivity.class);
        intent.putExtra("randomTruth", randomTruth);
        startActivity(intent);
    }

    private void startDareActivity(String randomDare) {
        Intent intent = new Intent(MainActivity.this, DareActivity.class);
        intent.putExtra("randomDare", randomDare);
        startActivity(intent);
    }
}
