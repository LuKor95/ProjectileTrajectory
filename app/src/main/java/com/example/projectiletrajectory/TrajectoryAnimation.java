package com.example.projectiletrajectory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TrajectoryAnimation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        ArrayList<Coordinates> coordData = intent.getParcelableArrayListExtra("data");

        AnimationView anim = new AnimationView(this, coordData);
        setContentView(anim);
    }
}
