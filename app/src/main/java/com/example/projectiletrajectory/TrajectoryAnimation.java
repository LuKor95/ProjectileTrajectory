package com.example.projectiletrajectory;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TrajectoryAnimation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        ArrayList<Coordinates> coordData = intent.getParcelableArrayListExtra("data");

        AnimationView animationView = new AnimationView(this, coordData);
        setContentView(animationView);
    }
}
