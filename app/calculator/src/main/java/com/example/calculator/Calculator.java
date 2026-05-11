package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.mariuszgromada.math.mxparser.Expression;

public class Calculator extends AppCompatActivity {

    private TextView editTextText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        editTextText = findViewById(R.id.textViewResult);
        setButtonClickListeners();
    }

    private void setButtonClickListeners () {
        int[] buttonIds = {
                R.id.button0,
                R.id.button1,
                R.id.button2,
                R.id.button3,
                R.id.button4,
                R.id.button5,
                R.id.button6,
                R.id.button7,
                R.id.button8,
                R.id.button9,
                R.id.buttonAdd,
                R.id.buttonSubtract,
                R.id.buttonMultiply,
                R.id.buttonDivide,
                R.id.buttonEqual,
                R.id.buttonBack,
                R.id.buttonDot,
                R.id.buttonAC,
                R.id.buttonParentheses,
                R.id.buttonPercent
        };
        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            button.setOnClickListener(v -> onButttonClick(v));
        }
    }
    private void onButttonClick(View view){
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        switch (buttonText) {
            case "=":
                calculateResult();
                break;
            case "AC":
                clearInput();
                break;
            case "⌫":
                removeLastInput();
                break;
            case "()":
                handleParentheses();
                break;
            default:
                appendInput(buttonText);
                break;
        }
    }
    private void calculateResult(){
        try {
            String expression = editTextText.getText().toString();
            Expression expressionEval = new Expression(expression);
            double result = expressionEval.calculate();
            editTextText.setText(String.valueOf(result));
        } catch (Exception e){
            editTextText.setText("Error");
        }
    }

    private void clearInput() {
        editTextText.setText("");
    }

    private void removeLastInput(){
        String s = editTextText.getText().toString();
        if (s.length() > 0){
            editTextText.setText(s.substring(0, s.length() - 1));
        }
    }

    private boolean isOpenParentheses = false;
    private void handleParentheses(){
        if (isOpenParentheses){
            appendInput(")");
            isOpenParentheses = false;
        } else {
            appendInput("(");
            isOpenParentheses = true;
        }
    }

    private void appendInput(String input){
        editTextText.setText(editTextText.getText().toString() + input);
    }
}