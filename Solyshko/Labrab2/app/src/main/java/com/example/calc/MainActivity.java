package com.example.calc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private boolean isResultDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
    }

    public void onClickNumber(View view) {
        if (isResultDisplayed) {
            editText.getText().clear();
            isResultDisplayed = false;
        }

        Button button = (Button) view;
        String currentText = editText.getText().toString();
        String newText = currentText + button.getText().toString();
        editText.setText(newText);
    }

    public void onClickOperator(View view) {
        if (isResultDisplayed) {
            isResultDisplayed = false;
        }

        Button button = (Button) view;
        String currentText = editText.getText().toString();
        String newText = currentText + button.getText().toString();
        editText.setText(newText);
    }
    public void onClickEqual(View view) {
        String expression = editText.getText().toString();

        try {
            double result = evaluateExpression(expression);
            editText.setText(String.valueOf(result));
            isResultDisplayed = true;
        } catch (Exception e) {
            editText.setText("Error");
        }
    }
    public void onClickClear(View view) {
        editText.getText().clear();
        isResultDisplayed = false;
    }
    public void onClickDot(View view) {
        if (isResultDisplayed) {
            editText.getText().clear();
            isResultDisplayed = false;
        }

        String currentText = editText.getText().toString();

        // Проверяем, что текущий текст пуст или последний символ не является десятичной точкой
        if (currentText.isEmpty() || currentText.charAt(currentText.length() - 1) != '.') {
            editText.append(".");
        }
    }
    private double evaluateExpression(String expression) {
        try {
            Stack<Double> numbers = new Stack<>();
            Stack<Character> operators = new Stack<>();

            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i);

                if (Character.isDigit(c) || c == '.') {
                    StringBuilder num = new StringBuilder();
                    while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        num.append(expression.charAt(i));
                        i++;
                    }
                    i--;

                    double number = Double.parseDouble(num.toString());
                    numbers.push(number);
                } else if (c == '-' && (i == 0 || isOperator(expression.charAt(i - 1)) || expression.charAt(i - 1) == '(')) {
                    // Унарный минус
                    i++;
                    StringBuilder num = new StringBuilder("-");
                    while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        num.append(expression.charAt(i));
                        i++;
                    }
                    i--;
                    double number = Double.parseDouble(num.toString());
                    numbers.push(number);
                }else if (isOperator(c)) {
                    while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                        applyOperator(numbers, operators);
                    }
                    operators.push(c);
                } else if (c == '(') {
                    operators.push(c);
                } else if (c == ')') {
                    while (operators.peek() != '(') {
                        applyOperator(numbers, operators);
                    }
                    operators.pop();
                }
            }

            while (!operators.isEmpty()) {
                applyOperator(numbers, operators);
            }

            return numbers.pop();
        } catch (ArithmeticException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error evaluating expression");
        }
    }


    private void applyOperator(Stack<Double> numbers, Stack<Character> operators) {
        char operator = operators.pop();
        double num2 = numbers.pop();
        double num1 = numbers.pop();
        double result = performOperation(num1, num2, operator);

        numbers.push(result);
    }
    private double performOperation(double num1, double num2, char operator) {
        switch (operator) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '%':
                return num1 % num2;
            case '/':
                if (num2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
    }

    private int precedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/' || operator == '%') {
            return 2;
        }
        return 0;
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
    }
}
