package com.example.lab1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView resultView;
    private StringBuilder inputStringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultView = findViewById(R.id.resultView);
        inputStringBuilder = new StringBuilder();

        setupNumberButtons();
        setupOperatorButtons();
        setupClearButton();
        setupEqualButton();
        setupCommaButton();
        setupPlMinButton();
    }

    private void setupNumberButtons() {
        int[] numberButtonIds = {
                R.id.Num0, R.id.Num1, R.id.Num2, R.id.Num3,
                R.id.Num4, R.id.Num5, R.id.Num6, R.id.Num7,
                R.id.Num8, R.id.Num9
        };

        for (int buttonId : numberButtonIds) {
            Button button = findViewById(buttonId);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appendToInput(button.getText().toString());
                }
            });
        }
    }

    private void setupOperatorButtons() {
        int[] operatorButtonIds = {
                R.id.NumPlus, R.id.NumMin, R.id.NumMulti, R.id.NumDiv,
                R.id.NumPerc
        };

        for (int buttonId : operatorButtonIds) {
            Button button = findViewById(buttonId);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appendToInput(button.getText().toString());
                }
            });
        }
    }

    private void setupClearButton() {
        Button clearButton = findViewById(R.id.NumClr);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInput();
            }
        });
    }

    private void setupEqualButton() {
        Button equalButton = findViewById(R.id.NumEqual);
        equalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult();
            }
        });
    }

    private void setupCommaButton() {
        Button commaButton = findViewById(R.id.NumComma);
        commaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToInput(".");
            }
        });
    }

    private void setupPlMinButton(){
        Button plMinButton = findViewById(R.id.NumPlMin);
        plMinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = resultView.getText().toString();
                input = "-1×" + input;
                    double result = evaluateExpression(input);
                    resultView.setText(String.valueOf(result));
            }
        });
    }

    private void appendToInput(String value) {
        inputStringBuilder.append(value);
        updateResultView();
    }

    private void clearInput() {
        inputStringBuilder.setLength(0);
        updateResultView();
    }

    private void updateResultView() {
        resultView.setText(inputStringBuilder.toString());
    }

    private void calculateResult() {
        String input = resultView.getText().toString();

        try {
            double result = evaluateExpression(input);
            resultView.setText(String.valueOf(result));
        } catch (Exception e) {
            resultView.setText("Error");
            e.printStackTrace();
        }
    }

    private double evaluateExpression(String expression) {
        String[] tokens = expression.split("(?<=\\d)(?=[+−×%÷])|(?<=[+−×%÷])(?=[-\\d.])");

        double currentOperand = Double.parseDouble(tokens[0]);
        String currentOperator = "";

        for (int i = 1; i < tokens.length; i++) {
            currentOperator = tokens[i];
            double nextOperand;
            if (tokens[i + 1].startsWith("-")) {
                if (tokens[i + 1].equals("-")) {
                    nextOperand = Double.parseDouble(tokens[i + 2]);
                    i += 2;
                } else {
                    nextOperand = Double.parseDouble(tokens[i + 1]);
                    i++;
                }
            } else {
                nextOperand = Double.parseDouble(tokens[++i]);
            }
            if (currentOperator.equals("÷") && nextOperand == 0) {
                throw new ArithmeticException("Division by zero is not allowed.");
            }
                switch (currentOperator) {
                    case "+":
                        currentOperand += nextOperand;
                        break;
                    case "-":
                        currentOperand -= nextOperand;
                        break;
                    case "×":
                        currentOperand *= nextOperand;
                        break;
                    case "÷":
                        currentOperand /= nextOperand;
                        break;
                    case "%":
                        currentOperand %= nextOperand;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operator: " + currentOperator);
                }
            }
        return currentOperand;
    }
}
