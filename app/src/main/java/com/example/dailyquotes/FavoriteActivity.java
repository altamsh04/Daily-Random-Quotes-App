package com.example.dailyquotes;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView favoriteQuotesRecyclerView;
    private FavoriteQuotesAdapter favoriteQuotesAdapter;
    private List<Quote> favoriteQuotes;
    private DatabaseHelper databaseHelper;
    private ImageButton setTimerForQuotes, deleteInsertedTime;
    private TextView timeForQuotes;
    private String selectedTime = null;
    private TimerDatabaseHelper timerDatabaseHelper;
    private int hour, minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        timerDatabaseHelper = new TimerDatabaseHelper(this);
        timeForQuotes = findViewById(R.id.timeForQuotes);
        deleteInsertedTime = findViewById(R.id.deleteInsertedTime);
        is_timer_set();

        setTimerForQuotes = findViewById(R.id.setTimerForQuotes);
        setTimerForQuotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;
                        Log.d("Selected Time Is : ", "Hour : " + selectedHour + ", Minutes : " + selectedMinute);
                        AlarmScheduler.scheduleNotification(FavoriteActivity.this, hour, minute);
                        timerDatabaseHelper.insertOrUpdateTime(selectedHour, selectedMinute);
                        is_timer_set();
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(FavoriteActivity.this, onTimeSetListener, hour, minute, false);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        deleteInsertedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerDatabaseHelper.delete_inserted_time();
                is_timer_set();
            }
        });

        favoriteQuotesRecyclerView = findViewById(R.id.favoriteQuotesRecyclerView);
        favoriteQuotesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseHelper = new DatabaseHelper(this);

        try {
            favoriteQuotes = databaseHelper.get_favorite_quotes();
            favoriteQuotesAdapter = new FavoriteQuotesAdapter(this, favoriteQuotes);
            favoriteQuotesRecyclerView.setAdapter(favoriteQuotesAdapter);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.e("FavoriteActivity", "Database table not found: " + e.getMessage());
            databaseHelper.createFavoritesTableIfNotExists();
            favoriteQuotes = databaseHelper.get_favorite_quotes();
            favoriteQuotesAdapter = new FavoriteQuotesAdapter(this, favoriteQuotes);
            favoriteQuotesRecyclerView.setAdapter(favoriteQuotesAdapter);
        }
    }

    private void is_timer_set() {
        selectedTime = timerDatabaseHelper.getInsertedTime();
        if (selectedTime != null) {
            timeForQuotes.setText(selectedTime);
            deleteInsertedTime.setVisibility(View.VISIBLE);
        }
        else {
            timeForQuotes.setText("Set Remainder Time");
            deleteInsertedTime.setVisibility(View.GONE);
        }
    }
}
