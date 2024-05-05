package com.example.calculator;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    String oldNumber;
    String operator = "";
    Boolean isNew = true;

    EditText editText;
    EditText NoeditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);

    }

    // ТусюкТ.В.
    public void OnClickNumber(View view) {

        if (isNew)
            editText.setText("");
        isNew = false;

        String number = editText.getText().toString();

        if (view.getId() == R.id.buZero) {
            if (zeroIsFirst(number) && number.length() == 1) {  // Проверка на правильное отображение нулей для каждой цифры
                number = "0";
            } else {
                number = number + "0";
            }

        } else if (view.getId() == R.id.buOne) {
            if (zeroIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number = number + "1";

        } else if (view.getId() == R.id.buTwo) {
            if (zeroIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number = number + "2";

        } else if (view.getId() == R.id.buThree) {
            if (zeroIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number = number + "3";

        } else if (view.getId() == R.id.buFour) {
            if (zeroIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number = number + "4";

        } else if (view.getId() == R.id.buFive) {
            if (zeroIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number = number + "5";

        } else if (view.getId() == R.id.buSix) {
            if (zeroIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number = number + "6";

        } else if (view.getId() == R.id.buSeven) {
            if (zeroIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number = number + "7";

        } else if (view.getId() == R.id.buEight) {
            if (zeroIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number = number + "8";

        } else if (view.getId() == R.id.buNine) {
            if (zeroIsFirst(number) && number.length() == 1) {
                number = number.substring(1);
            }
            number = number + "9";

        } else if (view.getId() == R.id.buComma) {

            if (commaIsPresent(number) && number.length() == 1) {
            } else
            if (zeroIsFirst(number)) {
                number = "0.";
            } else {
                number = number + ".";
            }

        } else if (view.getId() == R.id.buPlusMinus) {

            if(numberIsZero(number)) {
                number = "0";
            } else {
                if (minusIsPresent(number)) {
                    number = number.substring(1);
                } else {
                    number = "-" + number;
                }
            }

        }
        editText.setText(number);
    }

    public void OnClickOperator(View view) {
        isNew = true;
        oldNumber = editText.getText().toString();

        if (view.getId() == R.id.buMinus) {
            operator = "-";
        } else if (view.getId() == R.id.buPlus) {
            operator = "+";
        } else if (view.getId() == R.id.buMultiply) {
            operator = "×";
        } else if (view.getId() == R.id.buDivide) {
            operator = "÷";
        }
    }


    public void OnClickPercent(View view) {
        if (operator == "") {
            String number = editText.getText().toString();
            double temp = Double.parseDouble(number) / 100;
            number = temp + "";
            editText.setText(number);

        } else {
            String newNumber = editText.getText().toString();
            Double resultNumber = 0.0;

            switch (operator) {
                case "-":
                    resultNumber = Double.parseDouble(oldNumber) - Double.parseDouble(oldNumber) * Double.parseDouble(newNumber) / 100;
                    break;

                case "+":
                    resultNumber = Double.parseDouble(oldNumber) + Double.parseDouble(oldNumber) * Double.parseDouble(newNumber) / 100;
                    break;

                case "×":
                    resultNumber = Double.parseDouble(oldNumber) * Double.parseDouble(newNumber) / 100;
                    break;

                case "÷":
                    resultNumber = Double.parseDouble(oldNumber) / Double.parseDouble(newNumber) * 100;
                    break;
            }
            editText.setText(resultNumber + "");
            operator = "";
        }
    }


    public void OnClickEqual (View view) {

        String newNumber = editText.getText().toString();
        Double resultNumber = 0.0;

        if (Double.parseDouble(newNumber) < 0.00000001 && operator == "/" || newNumber.equals("") && operator == "/") {
            Toast.makeText(MainActivity.this, "Операция невозможна", Toast.LENGTH_LONG).show();
        } else {

            switch (operator) {
                case "-":
                    resultNumber = Double.parseDouble(oldNumber) - Double.parseDouble(newNumber);
                    break;

                case "+":
                    resultNumber = Double.parseDouble(oldNumber) + Double.parseDouble(newNumber);
                    break;

                case "×":
                    resultNumber = Double.parseDouble(oldNumber) * Double.parseDouble(newNumber);
                    break;

                case "÷":
                    resultNumber = Double.parseDouble(oldNumber) / Double.parseDouble(newNumber);
                    break;
            }
            editText.setText(resultNumber + "");
        }
    }


    public void OnClickC (View view){
        editText.setText("0");
        isNew = true;
    }


    public boolean commaIsPresent (String number){
        if (!number.contains(".")) {
            return false;
        } else {
            return true;
        }
    }


    public boolean minusIsPresent (String number){
        if (number.charAt(0) == '-') {
            return true;
        } else {
            return false;
        }
    }


    // Обработчик для первого нуля
    // (чтобы изменялся на цифру и не было много нулей)
    private boolean zeroIsFirst(String number) {
        if(number.equals("")) {
            return true;
        }
        if (number.charAt(0) == '0') {
            return true;
        } else {
            return false;
        }
    }

    private boolean numberIsZero(String number) {
        if(number.equals("0") || number.equals("")) {
            return true;
        } else
            return false;
    }




}