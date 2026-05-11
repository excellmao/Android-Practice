package com.example.note;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

public class Note extends AppCompatActivity {

    CalendarView calendarView;
    EditText noteEditText;
    Button saveButton;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);
        calendarView = findViewById(R.id.calendarView);
        noteEditText = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.saveButton);

        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        int savedYear = pref.getInt("year", 0);
        if (savedYear != 0){
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, savedYear);
            cal.set(Calendar.MONTH, pref.getInt("month", 0));
            cal.set(Calendar.DAY_OF_MONTH, pref.getInt("dayOfMonth", 0));
            calendarView.setDate(cal.getTimeInMillis());
        }

        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            fileName = String.format("%02d_%02d_%04d", dayOfMonth, month + 1, year);
            noteEditText.setText("");
            try {
                FileInputStream fis = openFileInput(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    sb.append(line).append("\n");
                }
                fis.close();
                noteEditText.setText(sb);
            } catch (IOException e){
                e.printStackTrace();
            }

            pref.edit().putInt("year", year).apply();
            pref.edit().putInt("month", month).apply();
            pref.edit().putInt("dayOfMonth", dayOfMonth).apply();
        });

        saveButton.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(calendarView.getDate()));
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            Toast.makeText(this, String.format("%02d_%02d_%04d", dayOfMonth, month + 1, year), Toast.LENGTH_LONG).show();
        });

        saveButton.setOnClickListener(view -> {
            String noteContent = noteEditText.getText().toString();
            try {
                FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
                fos.write(noteContent.getBytes());
                fos.close();
                Toast.makeText(this, "Ghi chu luu thanh cong", Toast.LENGTH_LONG).show();
            } catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this, "Loi khi luu ghi chu", Toast.LENGTH_LONG).show();
            }
        });
    }
}