package co.edu.stopwatch;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Locale;
import android.os.Handler;
import android.widget.TextView;

public class StopwatchActivity extends AppCompatActivity {

    private int seconds = 0;
    private boolean running;

    private ArrayList<Long> lapTimes = new ArrayList<>();
    private int lapCount = 0;
    private long lapStartTime = 0;
    private long lastLapTime = 0;


    //private long[] lapTimes = new long[5];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");

            lapStartTime = savedInstanceState.getLong("lapStartTime", -1); // Recuperar lapStartTime
        }

        runTimer();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
    }

    public void onClickStart(View view) {
        running = true;
    }

    public void onClickStop(View view) {
        running = false;
    }

    public void onClickReset(View view) {
        running = false;
        seconds = 0;
        lapCount = 0;
        lapTimes.clear();
        lapStartTime = -1;
        updateLapTimesView();

    }

    public void onClickLap(View view) {
        if (lapCount < 5) {
            if (running) {
                //lapTimes.add((long) seconds);
                long currentTime = System.currentTimeMillis() / 1000; // Convertir a segundos
                if (lapCount == 0) {
                    // Para la primera vuelta, simplemente agrega el tiempo actual
                    //lapTimes.add(currentTime);
                    lapTimes.add((long) seconds);
                } else {
                    // Para las vueltas posteriores, calcula la diferencia con respecto al tiempo de la última vuelta
                    long lapTime = currentTime - lastLapTime;
                    lapTimes.add(lapTime);
                }
                lapCount++;
                lastLapTime = currentTime; // Actualizar el tiempo de la última vuelta
                updateLapTimesView();
            }
        } 
            if (lapCount == 5) {
                running = false; // Detener el cronómetro después de la quinta vuelta
            }

    }


    private String formatTime(long timeInSeconds) {
        int hours = (int) (timeInSeconds / 3600);
        int minutes = (int) ((timeInSeconds % 3600) / 60);
        int seconds = (int) (timeInSeconds % 60);

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void updateLapTimesView() {
        TextView lapTimesView = findViewById(R.id.lap_times_view);
        StringBuilder lapTimesText = new StringBuilder();

        for (int i = 0; i < lapTimes.size(); i++) {
            lapTimesText.append("Lap ").append(i + 1).append(": ").append(formatTime(lapTimes.get(i))).append("\n");
        }

        lapTimesView.setText(lapTimesText.toString());
    }

    private void runTimer() {
        final TextView timeView = (TextView)findViewById(R.id.time_view);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds/3600;
                int minutes = (seconds%3600)/60;
                int secs = seconds%60;
                String time = String.format(Locale.getDefault(),
                        "%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

}