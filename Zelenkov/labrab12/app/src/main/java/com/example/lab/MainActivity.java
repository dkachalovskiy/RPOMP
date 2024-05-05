package com.example.lab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String operator = "";
    String oldNumber;
    EditText editText;
    Boolean isNew = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Configuration configuration = getResources().getConfiguration();

        // Проверяем ориентацию экрана
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main2);
            // Экран горизонтальный
        } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);
            // Экран вертикальный
        }

        super.onCreate(savedInstanceState);

        editText = findViewById(R.id.editText);
    }

    public  void clickNumber(View view) {

        if (isNew)
            editText.setText("");
        isNew = false;
        String number = editText.getText().toString();
        if(view.getId() == R.id.bu9) number +="9";
        else  if (view.getId() == R.id.bu8) number += "8";
        else  if (view.getId() == R.id.bu7) number += "7";
        else  if (view.getId() == R.id.bu6) number += "6";
        else  if (view.getId() == R.id.bu5) number += "5";
        else  if (view.getId() == R.id.bu4) number += "4";
        else  if (view.getId() == R.id.bu3) number += "3";
        else  if (view.getId() == R.id.bu2) number += "2";
        else  if (view.getId() == R.id.bu1) number += "1";
        else  if (view.getId() == R.id.bu0) number += "0";
        else  if (view.getId() == R.id.buDot) {
            if(!dotIsPresent(number)) {
                number += ".";
            }
        }
        else  if (view.getId() == R.id.buPlusMinus) {
            if(!minusIsPresent(number)){
                number =  "-"+number;
            }
        }

        editText.setText(number);
    }

    public boolean dotIsPresent(String number){
        if(number.indexOf(".") == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean minusIsPresent(String number){
        if(number.charAt(0) == '-'){
            return true;
        } else {
            return false;
        }
    }


        public void operation(View view){
        isNew = true;
        oldNumber = editText.getText().toString();

        if(view.getId() == R.id.buMultiply) operator="*";
        else  if (view.getId() == R.id.buPlus) operator="+";
        else  if (view.getId() == R.id.buMinus) operator="-";
        else  if (view.getId() == R.id.buDivide) operator="/";

    }

    public void clickEqual(View view){

        String newNumber = editText.getText().toString();

        if(newNumber.equals("0") && operator.equals("/")||newNumber.equals("") && operator.equals("/")){
            Toast.makeText(MainActivity.this, "На ноль делить нельзя!", Toast.LENGTH_SHORT).show();
        }

        Double result = 0.0;

        if(operator.equals("-")) result = Double.parseDouble(oldNumber) - Double.parseDouble(newNumber);
        else if(operator.equals("+")) result = Double.parseDouble(oldNumber) + Double.parseDouble(newNumber);
        else if(operator.equals("*")) result = Double.parseDouble(oldNumber) * Double.parseDouble(newNumber);
        else if(operator.equals("/")) result = Double.parseDouble(oldNumber) / Double.parseDouble(newNumber);

        editText.setText(result.toString());
    }

    public void clearClick(View view){
        editText.setText("0");
        isNew = true;
    }

    public void clickPercent(View view) {

        if(operator==""){
            String number = editText.getText().toString();
            double temp = Double.parseDouble(number) / 100;
            number = temp+"";
            editText.setText(number);

        }else {
            Double result = 0.0;
            String newNumber = editText.getText().toString();

            if(operator.equals("-")) result = Double.parseDouble(oldNumber) -
                    Double.parseDouble(oldNumber) * Double.parseDouble(oldNumber)/100;

            else if(operator.equals("+")) result = Double.parseDouble(oldNumber) +
                    Double.parseDouble(oldNumber) * Double.parseDouble(oldNumber)/100;

            else if(operator.equals("*")) result = Double.parseDouble(oldNumber) *
                    Double.parseDouble(newNumber)/100;

            else if(operator.equals("/")) result = Double.parseDouble(oldNumber) /
                    Double.parseDouble(newNumber)*100;
        }
    }
}