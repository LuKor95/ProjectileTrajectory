package com.example.projectiletrajectory;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import static java.lang.Math.round;

public class TrajectoryList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajectory_list);

        Intent intent = getIntent();
        ArrayList<Coordinates> coordData = intent.getParcelableArrayListExtra("data");

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_listview, format(coordData));
        ListView listView = findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
    }

    private ArrayList<String> format(ArrayList<Coordinates> coordinates){
        ArrayList<String> coordText = new ArrayList<>();
        if (coordinates != null) {
            for (Coordinates coords : coordinates) {
                coordText.add("Čas: "+roundCoordinate(coords.getTime())+", X:" + roundCoordinate(coords.getX())+", Y:"+roundCoordinate(coords.getY()));
            }
        }
        return coordText;
    }

    private String roundCoordinate(double coord){
        double roundedCoord;
        roundedCoord = round(coord * 1000.0) / 1000.0;
        return String.valueOf(roundedCoord);
    }
}
