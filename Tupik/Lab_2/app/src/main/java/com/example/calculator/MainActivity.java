package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    double firstnum;
    String operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button num0 = findViewById(R.id.num0);
        Button num1 = findViewById(R.id.num1);
        Button num2 = findViewById(R.id.num2);
        Button num3 = findViewById(R.id.num3);
        Button num4 = findViewById(R.id.num4);
        Button num5 = findViewById(R.id.num5);
        Button num6 = findViewById(R.id.num6);
        Button num7 = findViewById(R.id.num7);
        Button num8 = findViewById(R.id.num8);
        Button num9 = findViewById(R.id.num9);

        Button minus = findViewById(R.id.minus);
        Button percent = findViewById(R.id.percent);
        Button ac = findViewById(R.id.ac);
        Button del = findViewById(R.id.del);
        Button div = findViewById(R.id.div);
        Button times = findViewById(R.id.times);
        Button min = findViewById(R.id.min);
        Button equal = findViewById(R.id.equal);
        Button plus = findViewById(R.id.plus);
        Button point = findViewById(R.id.point);

        TextView screen = findViewById(R.id.screen);

        ac.setOnClickListener(view -> {
            firstnum = 0;
            screen.setText("0");
        });

        ArrayList<Button> nums = new ArrayList<Button>();
        nums.add(num0);
        nums.add(num1);
        nums.add(num2);
        nums.add(num3);
        nums.add(num4);
        nums.add(num5);
        nums.add(num6);
        nums.add(num7);
        nums.add(num8);
        nums.add(num9);

        for (Button b : nums) {
            b.setOnClickListener(view -> {
                String currentText = screen.getText().toString();
                if (currentText.equals("0") && !b.getText().toString().equals("0")) {
                    screen.setText(b.getText().toString());
                } else if (!currentText.equals("0")) {

                    screen.setText(currentText + b.getText().toString());
                }
            });
        }

        ArrayList<Button> opers = new ArrayList<Button>();
        opers.add(div);
        opers.add(times);
        opers.add(plus);
        opers.add(min);

        for (Button b : opers) {
            b.setOnClickListener(view -> {
                firstnum = Double.parseDouble(screen.getText().toString());
                operation = b.getText().toString();
                screen.setText("0");
            });
        }

        del.setOnClickListener(view -> {
            String num = screen.getText().toString();
            if (num.length()>1) {
                screen.setText(num.substring(0, num.length()-1));
            } else if (num.length() == 1 && !num.equals("0")) {
                screen.setText("0");
            }
        });

        point.setOnClickListener(view -> {
            if (!screen.getText().toString().contains(".")) {
                screen.setText(screen.getText().toString() + ".");
            }
        });

        minus.setOnClickListener(view -> {
            String currentText = screen.getText().toString();
            if (currentText.equals("0")) {
                screen.setText("-");
            } else if (!currentText.startsWith("-")) {
                screen.setText("-" + currentText);
            } else {
                screen.setText(currentText.substring(1));
            }
        });

        percent.setOnClickListener(view -> {
            double num = Double.parseDouble(screen.getText().toString());
            num /= 100;
            screen.setText(String.valueOf(num));
        });

        equal.setOnClickListener(view -> {
            double secondNum = Double.parseDouble(screen.getText().toString());
            double result;

                if (operation.equals("/") && secondNum == 0) {
                    screen.setText("Делить на 0 нельзя (Тупик)");
                    return;
                }

            switch (operation) {
                case "/":
                    result = firstnum / secondNum;
                    break;
                case "x":
                    result = firstnum * secondNum;
                    break;
                case "+":
                    result = firstnum + secondNum;
                    break;
                case "-":
                    result = firstnum - secondNum;
                    break;
                default:
                    result = firstnum + secondNum;
            }
            screen.setText(String.valueOf(result));
            firstnum = result;
        });
    }

}

