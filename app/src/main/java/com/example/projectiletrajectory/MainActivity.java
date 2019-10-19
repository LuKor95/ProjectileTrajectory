package com.example.projectiletrajectory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonElement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

public class MainActivity extends AppCompatActivity {

    private static final String BaSEURL = "http://10.0.2.2:80";

    SeekBar speedSeekBar, angleSeekBar;
    TextView speedText, angleText;
    Switch aSwitch;
    Button list_button, anim_button, graph_button;
    private ArrayList<Coordinates> coordData;
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
        aSwitch = findViewById(R.id.switch1);
        list_button = findViewById(R.id.list_button);
        anim_button = findViewById(R.id.anim_button);
        graph_button = findViewById(R.id.graph_button);

        setValueToButton(false);

        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedValue = progress;
                speedText.setText(progress + " km/h");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                choiseCompute();
            }
        });

        angleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                angleValue = progress;
                angleText.setText(progress + "°");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                choiseCompute();
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                choiseCompute();
            }
        });
    }

    public void choiseCompute() {
        if(speedValue > 0 && angleValue > 0) {
            if (aSwitch.isChecked()) {
                computeServer();
            } else {
                computeLocal();
            }
        }else{
            setValueToButton(false);
        }
    }

    public void computeLocal() {
        double t = 0.1;
        double g = 9.81;
        double x;
        double y = 1.0;

        coordData.clear();
        coordData.add(new Coordinates(0.0, 0.0, 0.0));

        while (y > 0) {
            x = (speedValue / 3.6) * t * cos((PI / 180) * angleValue);
            y = (speedValue / 3.6) * t * sin((PI / 180) * angleValue) - (g * pow(t, 2.0)) / 2;

            if (y < 0) {
                t = (2 * (speedValue / 3.6) * sin((PI / 180) * angleValue)) / g;
                x = (speedValue / 3.6) * t * cos((PI / 180) * angleValue);
                y = 0.0;
            }
            coordData.add(new Coordinates(t, x, y));
            t += 0.1;
        }
        Log.d("Calculation", "Coordinates was computed locally.");
        setValueToButton(true);
    }

    public void computeServer(){
        coordData.clear();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(BaSEURL).build();
        initphp api = restAdapter.create(initphp.class);
        api.computeCoordinates(
                speedValue,
                angleValue,
                new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement result, Response response) {

                        String myResponse = result.toString();

                        try {
                            JSONArray jsonarray = new JSONArray(myResponse);

                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject jsonObject = jsonarray.getJSONObject(i);

                                Coordinates coordinates = new Coordinates(jsonObject.getDouble("time"),jsonObject.getDouble("x"),jsonObject.getDouble("y"));
                                coordData.add(coordinates);
                            }
                            Log.d("Calculation", "Coordinates was computed via server.");
                            setValueToButton(true);
                        } catch (JSONException e) {
                            Log.e("JSONException", e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        aSwitch.setChecked(false);
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        Log.d("SERVER_FAILURE", error.toString());
                    }
                }
        );
    }

    public void setValueToButton(boolean status){
        list_button.setEnabled(status);
        anim_button.setEnabled(status);
        graph_button.setEnabled(status);
    }

    public void openActivity(Class activity){
        if(!coordData.isEmpty()) {
            Intent intent = new Intent(this, activity);
            intent.putParcelableArrayListExtra("data", coordData);
            startActivity(intent);
        }else{
            Toast.makeText(this, "CHYBA", Toast.LENGTH_SHORT).show();
            Log.d("Sending_data", "coordData is empty!");
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
