package com.example.unitconverter;

import android.os.Bundle;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;


public class UnitConverter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_converter);

        final EditText editTextCelsius = findViewById(R.id.editTextCelsius);
        Button buttonConvert = findViewById(R.id.buttonConvert);

        buttonConvert.setOnClickListener(v -> {
            double celcius = Double.parseDouble(editTextCelsius.getText().toString());
            double farenheit = (celcius * 9/5) + 32;

            Intent intent = new Intent (UnitConverter.this, ResultActivity.class);
            intent.putExtra("FARENHEIT", farenheit);
            startActivity(intent);
        });
    }
}