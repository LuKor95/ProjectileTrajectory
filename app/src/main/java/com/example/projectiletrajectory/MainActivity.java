package com.example.projectiletrajectory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

public class MainActivity extends AppCompatActivity {

    private static boolean DataComputed = false;
    private ArrayList<Coordinates> coordData;
    private SeekBar speedSeekBar, angleSeekBar;
    private TextView speedText, angleText;
    private int speedValue = 0, angleValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        coordData = new ArrayList<>();
        speedSeekBar = findViewById(R.id.speed);
        angleSeekBar = findViewById(R.id.angle);
        speedText = findViewById(R.id.speedValue);
        angleText = findViewById(R.id.angleValue);

        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedValue = progress;
                speedText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                DataComputed = false;
            }
        });

        angleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                angleValue = progress;
                angleText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                DataComputed = false;
            }
        });
    }

    public void choiseCompute() {
        boolean local = true;

        if (speedValue > 0 && angleValue > 0) {
            if (local) {
                coordData = computeLocalCoord();
            }
        }
    }

    public ArrayList<Coordinates> computeLocalCoord() {
        double t = 0.1;
        double g = 9.81;
        double x;
        double y = 1.0;

        ArrayList<Coordinates> coordDataLocal = new ArrayList<>();
        coordDataLocal.add(new Coordinates(0.0, 0.0, 0.0));

        while (y > 0) {
            x = (speedValue / 3.6) * t * cos((PI / 180) * angleValue);
            y = (speedValue / 3.6) * t * sin((PI / 180) * angleValue) - (g * pow(t, 2.0)) / 2;

//            x = round(x * 1000.0) / 1000.0;
//            y = round(y * 1000.0) / 1000.0;
//            t = round(t * 10.0) / 10.0;

            if (y < 0) {
                t = (2 * (speedValue / 3.6) * sin((PI / 180) * angleValue)) / g;
//                t = round(t * 100.0) / 100.0;
                x = (speedValue / 3.6) * t * cos((PI / 180) * angleValue);
                y = 0.0;
            }
            coordDataLocal.add(new Coordinates(t, x, y));
            t += 0.1;
        }
        return coordDataLocal;
    }

    public void openActivity(Class activity){
        if(speedValue != 0 && angleValue != 0) {
            if (!DataComputed) {
                choiseCompute(); // potom zakonponovat s prepinacom
                DataComputed = true;
            }
            Intent intent = new Intent(this, activity);
            intent.putParcelableArrayListExtra("data", coordData);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Zle zadane udaje", Toast.LENGTH_SHORT).show();
        }
    }

    public void openTrajectoryList(View v) {
        openActivity(TrajectoryList.class);
    }

    public void openTrajectoryAnimation(View v) {
        openActivity(TrajectoryAnimation.class);
    }

    public void openTrajectoryGraph(View v) {
        openActivity(TrajectoryGraph.class);
    }
}
