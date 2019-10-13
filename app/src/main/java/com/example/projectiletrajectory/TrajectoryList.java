package com.example.projectiletrajectory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TrajectoryList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_trajectory_list);

        Intent intent = getIntent();
        ArrayList<? extends Coordinates> coordData = intent.getParcelableArrayListExtra("data");

        ArrayList<String> coordText = new ArrayList<>();
        if (coordData != null) {
            for (Coordinates coords : coordData) {
                coordText.add("t:"+coords.getTime()+", x:" + coords.getX()+", y:"+coords.getY());
            }
        }

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_listview, coordText);
        ListView listView = findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
    }
}
