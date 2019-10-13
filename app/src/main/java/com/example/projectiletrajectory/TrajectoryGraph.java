package com.example.projectiletrajectory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.ArrayList;

import static java.lang.Math.round;

public class TrajectoryGraph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_trajectory_graph);

        double dopad = 0.0;
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        Intent intent = getIntent();
        ArrayList<? extends Coordinates> coordData = intent.getParcelableArrayListExtra("data");

        GraphView graph = findViewById(R.id.graph);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Čas [s]");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Výška [m]");
        graph.getGridLabelRenderer().setLabelVerticalWidth(30);
//        Toast.makeText(this, String.valueOf(coordData.size()), Toast.LENGTH_LONG).show();

        if (coordData != null) {
            for (Coordinates coords : coordData) {
                series.appendData(new DataPoint(coords.getTime(), coords.getY()), true, coordData.size());
                dopad = coords.getTime();
            }
        }
        graph.getViewport().setMaxX((round(dopad * 10.0) / 10.0) + 0.2);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.addSeries(series);
    }
}
