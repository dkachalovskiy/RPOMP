package comdashubaa.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Boolean isNew = true;
    String oldNumber;
    String operator = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
    }


    public void ckickNumber(View view) {
        if(isNew){
            editText.setText("");
        }
        isNew = false;
        String number = editText.getText().toString();
        if (view.getId() == R.id.bu7) {
            number = number + "7";
        } else if (view.getId() == R.id.bu8) {
            number = number + "8";
        } else if (view.getId() == R.id.bu9) {
            number = number + "9";
        } else if (view.getId() == R.id.bu6) {
            number = number + "6";
        } else if (view.getId() == R.id.bu5) {
            number = number + "5";
        } else if (view.getId() == R.id.bu4) {
            number = number + "4";
        } else if (view.getId() == R.id.bu3) {
            number = number + "3";
        } else if (view.getId() == R.id.bu2) {
            number = number + "2";
        } else if (view.getId() == R.id.bu1) {
            number = number + "1";
        } else if (view.getId() == R.id.bu0) {
            number = number + "0";
        } else if (view.getId() == R.id.buDot) {
            if(dotIsPresent(number)) {
              //nothing
            } else {
                number = number + ".";
            }
        } else if (view.getId() == R.id.buPlusMinus) {
            if(minusIsPresent(number)){
               number = number.substring(1);
            } else {
                number = "-" + number;
            }
        }

        editText.setText(number);
    }

    public void operation(View view) {
        isNew = true;
        oldNumber = editText.getText().toString();
        if (view.getId() == R.id.buMinus) {
            operator = "-";
        } else if (view.getId() == R.id.buMultiply) {
            operator = "*";
        } else if (view.getId() == R.id.buDivide) {
            operator = "/";
        } else if (view.getId() == R.id.buPlus) {
            operator = "+";
        }
    }

    public void ckickEqual(View view) {
        String newNumber= editText.getText().toString();
        Double result = 0.0;
        switch(operator){
            case "-": result = Double.parseDouble(oldNumber) - Double.parseDouble(newNumber); break;
            case "*": result = Double.parseDouble(oldNumber) * Double.parseDouble(newNumber); break;
            case "+": result = Double.parseDouble(oldNumber) + Double.parseDouble(newNumber); break;
        }
        if (operator == "/" && Double.parseDouble(newNumber) != 0.0){
            result = Double.parseDouble(oldNumber) / Double.parseDouble(newNumber);
        } else if (operator == "/" && Double.parseDouble(newNumber) == 0.0) {
            Toast.makeText(MainActivity.this, "Cant divide on zero!", Toast.LENGTH_LONG).show();
            isNew = true;
            result = 0.0;
        }
        editText.setText(result + "");
    }

    public void clickAC(View view) {
        isNew = true;
        editText.setText("0");

    }
    public boolean dotIsPresent(String number){
        if (number.indexOf(".") == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean minusIsPresent(String number) {
        if (number.indexOf("-") == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void clickPrecent(View view) {
        if (operator == ""){
            String number = editText.getText().toString();
            double temp = Double.parseDouble(number) / 100;
            number = temp + "";
            editText.setText(number);
        } else {
            String newNumber= editText.getText().toString();
            Double result = 0.0;
            switch(operator){
                case "-": result = Double.parseDouble(oldNumber) - Double.parseDouble(oldNumber)/100 * Double.parseDouble(newNumber);
                case "*": result = Double.parseDouble(oldNumber) * Double.parseDouble(newNumber)/100; break;
                case "+": result = Double.parseDouble(oldNumber) + Double.parseDouble(oldNumber)/100 * Double.parseDouble(newNumber); break;
                case "/": result = Double.parseDouble(oldNumber) / Double.parseDouble(newNumber) * 100; break;
            }
            editText.setText(result+" ");
            operator = "";
        }

    }
}