package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Boolean isStart = true;
    String firstNumber;
    String operator="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
    }

    public void clickNum(View view) {
        if(isStart)
            editText.setText("");
        isStart = false;
        String number = editText.getText().toString();

        if (view.getId() == R.id.button1) {
            if (IsZeroFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number = number + "1";
        }

        else if(view.getId() == R.id.button2){
            if(IsZeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number += "2";
        }

        else if(view.getId() == R.id.button3){
            if(IsZeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number += "3";
        }

        else if(view.getId() == R.id.button4){
            if(IsZeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number += "4";
        }

        else if(view.getId() == R.id.button5){
            if(IsZeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number += "5";
        }

        else if(view.getId() == R.id.button6){
            if(IsZeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number += "6";
        }

        else if(view.getId() == R.id.button7){
            if(IsZeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number += "7";
        }

        else if(view.getId() == R.id.button8){
            if(IsZeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number += "8";
        }

        else if(view.getId() == R.id.button9){
            if(IsZeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number += "9";
        }

        else if(view.getId() == R.id.button0){
            if(IsZeroFirst(number) && number.length() == 1) {
                number = "0";
            }else{
                number += "0";
            }
        }

        else if(view.getId() == R.id.buttonPoint){
           if(IsDot(number)){}
           else if(IsZeroFirst(number)) {
                number = "0.";
            }
           else{
               number = number + ".";
           }
        }

        else if(view.getId() == R.id.buttonSign){
            if(!numberIsZero(number)){
            if(!IsMinus(number)){
            number = "-" + number;
            }else{
                number = number.substring(1);
            }
            }else{
                number = "0";
            }
        }

        editText.setText(number);
    }

    public void DoOperation(View view) {
        isStart = true;
        firstNumber = editText.getText().toString();
        if (view.getId() == R.id.buttonAdd){
            operator = "+";
        }else if(view.getId() == R.id.buttonSub){
            operator = "-";
        }else if(view.getId() == R.id.buttonDiv){
            operator = "/";
        }else if(view.getId() == R.id.buttonMult){
            operator = "*";
        }
    }

    public void ClickResult(View view) {
        String otherNumber = editText.getText().toString();
        double result = 0.0;
        if((Double.parseDouble(otherNumber)<0.0000001 || otherNumber.equals("")) && operator.equals("/")){
            Toast.makeText(MainActivity.this,"На ноль делить нельзя (Бердникова)", Toast.LENGTH_LONG).show();
        }else {
            switch (operator) {
                case "+":
                    result = Double.parseDouble(firstNumber) + Double.parseDouble(otherNumber);
                    break;

                case "-":
                    result = Double.parseDouble(firstNumber) - Double.parseDouble(otherNumber);
                    break;

                case "/":
                    result = Double.parseDouble(firstNumber) / Double.parseDouble(otherNumber);
                    break;

                case "*":
                    result = Double.parseDouble(firstNumber) * Double.parseDouble(otherNumber);
                    break;
            }
            editText.setText(String.valueOf(result));
        }
    }

    public void doAC(View view) {
        editText.setText("0");
        isStart = true;
    }

    public boolean IsDot(String number){
        return number.contains(".");
    }

    public boolean IsMinus(String number) {
        return number.charAt(0) == '-';
    }

    public void doPercent(View view) {
        if (Objects.equals(operator, "")){
            String number = editText.getText().toString();
            double temp = Double.parseDouble(number) / 100;
            number = String.valueOf(temp);
            editText.setText(number);
        }
        else{
            double result = 0.0;
            String otherNumber = editText.getText().toString();
            switch(operator){
                case "+": result = Double.parseDouble(firstNumber) + Double.parseDouble(firstNumber) * Double.parseDouble(otherNumber) / 100;
                    break;

                case "-": result = Double.parseDouble(firstNumber) - Double.parseDouble(firstNumber) * Double.parseDouble(otherNumber) / 100;
                    break;

                case "/": result = Double.parseDouble(firstNumber) / Double.parseDouble(firstNumber) * Double.parseDouble(otherNumber) * 100;
                    break;

                case "*": result = Double.parseDouble(firstNumber) * Double.parseDouble(otherNumber) / 100;
                    break;
            }
            editText.setText(String.valueOf(result));
        }


    }

    public boolean numberIsZero(String number){
        return number.equals("0") || number.equals("");
    }

    public boolean IsZeroFirst(String number){
        if(number.equals("")){
            return true;
        }
        return number.charAt(0) == '0';
    }
}