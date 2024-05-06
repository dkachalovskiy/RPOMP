package com.example.rpomp_lab1;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    String oldNum;
    String operator = "";
    Boolean isNew=true;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);

    }

    public void clickNumber(View view) {
        if (isNew)
            editText.setText("");
        isNew=false;
        String number = editText.getText().toString();
        if (view.getId() == R.id.buttOne){
            if (zeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number+"1";
        }
        else if (view.getId() == R.id.buttTwo){
            if (zeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number+"2";
        }
        else if (view.getId() == R.id.buttThree){
            if (zeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number+"3";
        }
        else if (view.getId() == R.id.buttFour){
            if (zeroFirst(number)&& number.length() == 1){
                number = number.substring(1);
            }
            number = number+"4";
        }
        else if (view.getId() == R.id.buttFive){
            if (zeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number+"5";
        }
        else if (view.getId() == R.id.buttSix){
            if (zeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number+"6";
        }
        else if (view.getId() == R.id.buttSeven){
            if (zeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number+"7";
        }
        else if (view.getId() == R.id.buttEight){
            if (zeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number+"8";
        }
        else if (view.getId() == R.id.buttNine){
            if (zeroFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number+"9";
        }
        else if (view.getId() == R.id.buttZero){
            if (zeroFirst(number) && number.length() == 1){
                number="0";
            } else {
                number = number+"0";
            }
        }
        else if (view.getId() == R.id.buttPoint){
            if (hasPoint(number)) {

            } else
            if (zeroFirst(number)){
                number = "0.";
            }
            else{
            number = number+".";
            }
        }
        else if (view.getId() == R.id.buttPlusMinus) {
            if (numIsZero(number)) {
                number = "0";
            } else {
                if (hasMinus(number)) {
                    number = number.substring(1);
                } else {
                    number = "-" + number;
                }
            }
        }
        editText.setText(number);

    }

    private boolean zeroFirst(String number) {
        if (number.equals("")){
            return true;
        }
        if (number.charAt(0) == '0'){
            return true;
        } else {
            return false;
        }
    }
    public void operation(View view) {
        isNew = true;
        oldNum = editText.getText().toString();
        if (view.getId() == R.id.buttDivide){
            operator="÷";
        }
        else if (view.getId() == R.id.buttMultiply){
            operator="x";
        }
        else if (view.getId() == R.id.buttPlus){
            operator="+";
        }
        else if (view.getId() == R.id.buttMinus){
            operator="-";
        }
    }
    public void clickResult(View view) {
        String newNum = editText.getText().toString();
        Double result = 0.0;

        if (newNum.equals("0") && operator=="÷" || newNum.equals("") && operator=="÷"){
            Toast.makeText(MainActivity.this, "Матюшик: На ноль делить нельзя", Toast.LENGTH_LONG).show();
        } else {
            switch (operator) {
                case "-":
                    result = Double.parseDouble(oldNum) - Double.parseDouble(newNum);
                    break;
                case "+":
                    result = Double.parseDouble(oldNum) + Double.parseDouble(newNum);
                    break;
                case "x":
                    result = Double.parseDouble(oldNum) * Double.parseDouble(newNum);
                    break;
                case "÷":
                    result = Double.parseDouble(oldNum) / Double.parseDouble(newNum);
                    break;
            }
            editText.setText(result + "");
        }
    }
    public void ACCleanUp(View view) {
        editText.setText("0");
        isNew=true;
    }
    public boolean hasPoint(String number){
       if (number.indexOf(".") == -1){
           return false;
       }    else{
           return true;
       }
    }
    public boolean hasMinus(String number) {
        if (number.charAt(0) == '-') {
            return true;
        } else {
            return false;
        }
    }
    public void PercentMethod(View view) {
        if (operator == ""){
            String number = editText.getText().toString();
            double temp = Double.parseDouble(number) / 100;
            number = temp + "";
            editText.setText(number);
        } else {
            Double result = 0.0;
            String newNum = editText.getText().toString();
            switch (operator){
                case "-":
                    result = Double.parseDouble(oldNum) - Double.parseDouble(oldNum) * Double.parseDouble(newNum) / 100;
                    break;
                case "+":
                    result = Double.parseDouble(oldNum) + Double.parseDouble(oldNum) * Double.parseDouble(newNum) / 100;
                    break;
                case "x":
                    result = Double.parseDouble(oldNum) * Double.parseDouble(oldNum) / 100;
                    break;
                case "÷":
                    result = Double.parseDouble(oldNum) / Double.parseDouble(newNum) * 100;
                    break;
            }
            editText.setText(result+"");
            operator="";
        }


    }
    private boolean numIsZero(String number) {
        if (number.equals("0") || number.equals("")) {
            return true;
        } else{
            return false;
        }
    }

}