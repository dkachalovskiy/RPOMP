package com.example.mycalculator;
//Разработал Дарашкевич Д.И. ПО-9
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class MainActivity extends AppCompatActivity {

    EditText editText;
    Boolean isNew= true;

    String oldNumber;
    String operator = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
    }

    public void clickNumber(View view) {
        if (isNew){
            editText.setText("");
            isNew=false;
        }
        String number = editText.getText().toString();
        if (view.getId()==R.id.bu1) {
            if (zeroIsFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number + "1";
        }
        if (view.getId()==R.id.bu2) {
            if (zeroIsFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number + "2";
        }

        if (view.getId()==R.id.bu3) {
            if (zeroIsFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number + "3";
        }
        if (view.getId()==R.id.bu4) {
            if (zeroIsFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number + "4";
        }
        if (view.getId()==R.id.bu5) {
            if (zeroIsFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number + "5";
        }
        if (view.getId()==R.id.bu6) {
            if (zeroIsFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number + "6";
        }

        if (view.getId()==R.id.bu7) {
            if (zeroIsFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number + "7";
        }
        if (view.getId()==R.id.bu8) {
            if (zeroIsFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number + "8";
        }
        if (view.getId()==R.id.bu9) {
            if (zeroIsFirst(number) && number.length() == 1){
                number = number.substring(1);
            }
            number = number + "9";
        }

        if (view.getId()==R.id.bu0) {
            if(zeroIsFirst(number) && number.length() == 1){
                number = "0";
            }else{
                number = number + "0";
            }
        }

        if (view.getId()==R.id.buDot) {
            if (dotIsPresent(number)){

            } else
            if (dotIsPresent(number)) {

            }else {
                number = number + ".";
            }
        }


        if (view.getId()==R.id.buPlusMinus) {
            if (numberIsZero(number)){
                number = "0";
            }else {
                if (MinusIsPresent(number)) {
                    number = number.substring(1);
                } else {
                    number = "-" + number;
                }
            }
        }
        editText.setText(number);

    }

    public boolean zeroIsFirst(String number){

        if(number.equals("")){
            return true;
        }

        if(number.charAt(0) == '0'){
            return true;
        }else {
            return false;
        }
    }
    public void operation(View view) {
        isNew=true;
        oldNumber=editText.getText().toString();

        if (view.getId()==R.id.buMinus) {
            operator = "-";
        }
        if (view.getId()==R.id.buPlus) {
            operator = "+";

        }
        if (view.getId()==R.id.buDivide) {
            operator = "/";
        }
        if (view.getId()==R.id.buMultiply) {
            operator = "*";
        }
    }

    public void clickResult(View view) {
        String newNumber = editText.getText().toString();
        Double result = 0.0;

        if (operator=="-") {
            result = Double.parseDouble(oldNumber) - Double.parseDouble(newNumber);
        }
        if (operator=="+") {
            result = Double.parseDouble(oldNumber) + Double.parseDouble(newNumber);
        }
        if (operator=="*") {
            result = Double.parseDouble(oldNumber) * Double.parseDouble(newNumber);
        }
        if (operator=="/") {
            result = Double.parseDouble(oldNumber) / Double.parseDouble(newNumber);
        }
        if (result+"".length()>7)
        {
           editText.setText(String.format("%.4f",result));

        }else {
            editText.setText(result + "");
        }

    }

    public void clickAC(View view) {
        isNew=true;
        editText.setText("0");
    }

    public boolean dotIsPresent(String number){

        if (number.indexOf(".") == -1)
            return false;
        else{
            return true;
        }
    }

    public boolean MinusIsPresent(String number){
        if (number.charAt(0) == '-')
            return true;
        else{
            return false;
        }
    }

    public void clickPercent(View view) {

        if (operator == "" ){
            String number = editText.getText().toString();
            double temp = Double.parseDouble(number) / 100;
            number = temp+"";
            editText.setText(number);
        }else{
            Double result = 0.0;
            String newNumber = editText.getText().toString();

            if (operator=="-") {
                result = Double.parseDouble(oldNumber) - Double.parseDouble(oldNumber) * Double.parseDouble(newNumber)/100;
            }
            if (operator=="+") {
                result = Double.parseDouble(oldNumber) + Double.parseDouble(oldNumber) * Double.parseDouble(newNumber)/100;
            }
            if (operator=="*") {
                result = Double.parseDouble(oldNumber) * Double.parseDouble(newNumber) * Double.parseDouble(newNumber)/100;
            }
            if (operator=="/") {
                result = Double.parseDouble(oldNumber) / Double.parseDouble(newNumber) * Double.parseDouble(newNumber)/100;
            }
        }
    }

    public boolean numberIsZero(String number){
        if (number.equals("0") || number.equals("")){
            return true;
        }else{
            return false;
        }
    }
}