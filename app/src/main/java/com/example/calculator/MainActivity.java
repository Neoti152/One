package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private boolean isPointSet = false;
    private int countBracket = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = (EditText) findViewById(R.id.edit);
        editText.setShowSoftInputOnFocus(false);
        editText.requestFocus();
        if (savedInstanceState != null) {
            TextView result = (TextView) findViewById(R.id.result);
            editText.setText(savedInstanceState.getString("Formula"));
            result.setText(savedInstanceState.getString("Result"));
            int start = savedInstanceState.getInt("Start");
            int end = savedInstanceState.getInt("End");
            if (start == end) {
                editText.setSelection(start);
            } else {
                editText.setSelection(start, end);
            }
        }


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        EditText editText = (EditText) findViewById(R.id.edit);
        TextView result = (TextView) findViewById(R.id.result);
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        savedInstanceState.putString("Result", result.getText().toString());
        savedInstanceState.putString("Formula", editText.getText().toString());
        savedInstanceState.putInt("Start", start);
        savedInstanceState.putInt("End", end);
    }

    public void onClickOne(View view) {
        addDigit('1');
    }

    public void onClickTwo(View view) {
        addDigit('2');
    }

    public void onClickThree(View view) {
        addDigit('3');
    }

    public void onClickFour(View view) {
        addDigit('4');
    }

    public void onClickFive(View view) {
        addDigit('5');
    }

    public void onClickSix(View view) {
        addDigit('6');
    }

    public void onClickSeven(View view) {
        addDigit('7');
    }

    public void onClickEight(View view) {
        addDigit('8');
    }

    public void onClickNine(View view) {
        addDigit('9');
    }

    public void onClickZero(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);

        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        StringBuilder sb = new StringBuilder(editText.getText());
        int[] counts = numberLength(sb, start);
        if(counts[0]>=15){
            Toast toast = Toast.makeText(this, "Не больше 15 цифр!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if(counts[1]>=10){
            if (start > counts[2]) {
                Toast toast = Toast.makeText(this, "Не больше 10 цифр после точки!", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }

        if (sb.length() == 1 && sb.charAt(start - 1) == '0') {
            show();
            return;
        } else if (start == 0) {
            if (isPointInNumber(sb, start)) {
                show();
                return;
            } else {
                if (start == end) {
                    sb.insert(start, '0');
                    sb.insert(++start, '.');

                } else {
                    sb.delete(start, end);
                    sb.insert(start, '0');
                    sb.insert(++start, '.');

                }
                editText.setText(sb);
                editText.setSelection(++start);
                isPointSet = true;
            }
        } else if ((sb.length() > 1) && (sb.charAt(start - 1) == '0') && (isZnak(sb.charAt(start - 2)))) {
            if (start == end) {
                sb.insert(start, '.');

            } else {
                sb.delete(start, end);
                sb.insert(start, ".");

            }
            editText.setText(sb);
            editText.setSelection(++start);
            isPointSet = true;
        } else {
            if (start == end) {
                sb.insert(start, '0');
            } else {
                sb.delete(start, end);
                sb.insert(start, "0");

            }
            editText.setText(sb);
            editText.setSelection(++start);
        }
    }


    public void onClickPoint(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        int start = editText.getSelectionStart();
        if (sb.length() == 0 || start == 0) {
            sb.insert(start, '0');
            sb.insert(++start, '.');
            editText.setText(sb);
            editText.setSelection(2);
        } else {


            int end = editText.getSelectionEnd();
            char znak = sb.charAt(start - 1);
            if (!isPointInNumber(sb, start)) {
                if (isZnak(znak)) {
                    sb.insert(start, '0');
                    sb.insert(++start, '.');
                } else if (isPointInNumber(sb, start) && start == end) {
                    sb.insert(start, '.');
                } else {
                    sb.delete(start, end);
                    sb.insert(start, '.');
                }
                editText.setText(sb);
                editText.setSelection(++start);

                isPointSet = true;
            } else {
                show();
            }

        }

    }

    public void onClickUp(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);

        TextView textView = (TextView) findViewById(R.id.result);
        String s = textView.getText().toString();
        if (!s.equals("Result")) {
            editText.setText(s);
            editText.setSelection(s.length());
        }
    }

    public void onClickDelete(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        if (sb.length() == 0) {
            return;
        }

        int start = editText.getSelectionStart();


        int end = editText.getSelectionEnd();


        if (start == end) {

            StringBuilder s = sb.deleteCharAt(--start);
            if (s.toString().contains(".")) {
                isPointSet = false;
            }
        } else {
            String s = sb.substring(start, end);
            sb.delete(start, end);
            if (s.contains(".") & (!s.contains("+") || !s.contains("-") || !s.contains("*") || !s.contains("/"))) {
                isPointSet = false;
            }
        }

        editText.setText(sb);
        editText.setSelection(start);
    }

    public void onClickPlus(View view) {
        addOperation('+');
    }

    public void onClickMinus(View view) {
        addOperation('-');
    }

    public void onClickMultiply(View view) {
        addOperation('*');
    }

    public void onClickDivide(View view) {
        addOperation('/');
    }


    public void onClickReset(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        editText.setText("");
        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText("Result");
        isPointSet = false;
    }

    public void addDigit(char c) {
        EditText editText = (EditText) findViewById(R.id.edit);

        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        StringBuilder sb = new StringBuilder(editText.getText());
        int[] counts = numberLength(sb, start);

        if(counts[0]>=15){
            Toast toast = Toast.makeText(this, "Не больше 15 цифр!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if(counts[1]>=10){
            if (start > counts[2]) {
                Toast toast = Toast.makeText(this, "Не больше 10 цифр после точки!", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }



        if (sb.length() > 1 && start != 0 && sb.charAt(start - 1) == ')') {
            show();
            return;
        }

        if (start == 0) {
            if (start == end) {
                sb.insert(start, c);
            } else {
                sb.delete(start, end);
                sb.insert(start, c);

            }
            editText.setText(sb);
            editText.setSelection(++start);
            return;
        }

        if (sb.length() == 1 && sb.charAt(start - 1) == '0') {
            if (start == end) {
                sb.insert(start, '.');
                sb.insert(++start, c);
            } else {
                sb.delete(start, end);
                sb.insert(start, ".");
                sb.insert(++start, c);
            }
            editText.setText(sb);
            editText.setSelection(++start);
            isPointSet = true;
        } else if ((sb.length() > 1) && (sb.charAt(start - 1) == '0') && (isZnak(sb.charAt(start - 2)))) {
            if (start == end) {
                sb.insert(start, '.');
                sb.insert(++start, c);
            } else {
                sb.delete(start, end);
                sb.insert(start, ".");
                sb.insert(++start, c);
            }
            editText.setText(sb);
            editText.setSelection(++start);
            isPointSet = true;
        } else {
            if (start == end) {
                sb.insert(start, c);
            } else {
                sb.delete(start, end);
                sb.insert(start, c);

            }
            editText.setText(sb);
            editText.setSelection(++start);
        }
    }

    public void addOperation(char c) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        if ((start == 0) && (c == '-')) {
            sb.insert(start, c);
            editText.setText(sb);
            editText.setSelection(++start);
        } else if (start == 0) {
            show();
     /*  } else if (sb.length() > 2 && sb.charAt(start - 2) == '(') {
            show();
            return;*/
        } else {

            char znak = sb.charAt(start - 1);
            if (isZnak(znak) || znak == '.') {
                if (start == 1) {
                    show();
                    return;
                } else {
                    sb.deleteCharAt(--start);
                    sb.insert(start, c);
                }
            } else if (znak == '(' && c != '-') {
                show();
                return;
            } else if (start == end) {
                sb.insert(start, c);
            } else {
                sb.delete(start, end);
                sb.insert(start, c);
            }
            editText.setText(sb);
            editText.setSelection(++start);

        }


        isPointSet = false;
    }

    public void onClickBrackets(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        int start = editText.getSelectionStart();
        if (sb.length() == 0 || start == 0) {
            sb.insert(0, '(');
            editText.setText(sb);
            editText.setSelection(1);
            countBracket++;
        } else {

            int end = editText.getSelectionEnd();


            char znak = sb.charAt(start - 1);

            if (znak == '(' || isZnak(znak)) {
                if (start == end) {
                    sb.insert(start, '(');
                } else {
                    sb.delete(start, end);
                    sb.insert(start, '(');
                }
                editText.setText(sb);
                editText.setSelection(++start);
                countBracket++;
            } else if (countBracket != 0 && !isZnak(znak)) {
                if (start == end) {
                    sb.insert(start, ')');
                } else {
                    sb.delete(start, end);
                    sb.insert(start, ')');
                }
                editText.setText(sb);
                editText.setSelection(++start);
                countBracket--;
            } else {
                show();
            }
        }
    }


    public void onClickEnter(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());

        if (sb.length() == 0) {
            return;
        }

        if (sb.length() == 1 && sb.charAt(0) == '-') {
            show();
            return;
        }

        int end = sb.length() - 1;
        char znak = sb.charAt(end);
        if (isZnak(znak) || znak == '.') {
            sb.deleteCharAt(end);
        }
        if (countBracket != 0) {
            while (countBracket != 0) {
                sb.append(')');
                countBracket--;
            }
        }
        TextView result = (TextView) findViewById(R.id.result);
        try {
            String res = Calculate.getResult(sb.toString());
            result.setText(res);
            editText.setText(sb);
            editText.setSelection(sb.length());
        } catch (NumberFormatException e) {
            show();
            editText.setText("");

            result.setText("Result");
            isPointSet = false;
        }

    }

    public boolean isPointInNumber(StringBuilder sb, int point) {
        char[] chars = sb.toString().toCharArray();
        boolean b = false;
        for (int i = point; i < chars.length; i++) {
            if (isZnak(chars[i])) {
                break;
            }
            if (chars[i] == '.') {
                b = true;
                break;
            }
        }
        for (int i = point - 1; i >= 0; i--) {
            if (isZnak(chars[i])) {
                break;
            }
            if (chars[i] == '.') {
                b = true;
                break;
            }
        }
        return b;


    }


    public int[] numberLength(StringBuilder sb, int point) {
        char[] chars = sb.toString().toCharArray();
        int[] counts = {0, 0, 0};
        int countn = 0;
        int end = 0;
        int countp = 0;
        int pointPosishion=0;
        boolean b = false;
        for (int i = point; i < chars.length; i++) {
            if (isZnak(chars[i])) {
                break;
            }
            if (chars[i] == '.') {
                pointPosishion = i;
                b = true;
                countn--;
            }
            countn++;
            end = i;
        }
        if (end == 0){
            end = sb.length()-1;
        }

        for (int i = point - 1; i >= 0; i--) {
            if (isZnak(chars[i])) {
                break;
            }
            if (chars[i] == '.') {
                pointPosishion = i;
                b = true;
                countn--;

            }
            countn++;
        }
        if (b) {
            for (int i = end; i >= 0; i--) {
                if (chars[i] == '.') {
                    break;

                }
                countp++;
            }

        }
        counts[0] = countn;
        counts[1] = countp;
        counts[2] = pointPosishion;
        return counts;
    }

    public boolean isZnak(char c) {
        if (c == '+' || c == '-' || c == '*' || c == '/') {
            return true;
        } else {
            return false;
        }
    }


    public void show() {
        Toast toast = Toast.makeText(this, "Неверный формат!", Toast.LENGTH_SHORT);
        toast.show();
    }
}

