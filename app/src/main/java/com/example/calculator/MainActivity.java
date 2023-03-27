package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int countBracket = 0;

    private boolean isRad = false;
private boolean off = false;
    private int radix = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = (EditText) findViewById(R.id.edit);
        editText.setShowSoftInputOnFocus(false);
        editText.requestFocus();
        if (savedInstanceState != null) {
            TextView result = (TextView) findViewById(R.id.result);
            Button button = (Button) findViewById(R.id.buttonBinary);
            TextView statusBinary = (TextView) findViewById(R.id.statusBinary);
            TextView resultBinary = (TextView) findViewById(R.id.resultBinary);
            editText.setText(savedInstanceState.getString("Formula"));
            result.setText(savedInstanceState.getString("Result"));
            int start = savedInstanceState.getInt("Start");
            int end = savedInstanceState.getInt("End");
            if (start == end) {
                editText.setSelection(start);
            } else {
                editText.setSelection(start, end);
            }
            radix = savedInstanceState.getInt("Radix");
            statusBinary.setText(savedInstanceState.getString("StatusBinary"));
            resultBinary.setText(savedInstanceState.getString("ResultBinary"));
            button.setText(savedInstanceState.getString("Button"));
        }

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED){
            Handler handler = new Handler();
            handler.postDelayed(() -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED), 5000);

        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        EditText editText = (EditText) findViewById(R.id.edit);
        TextView result = (TextView) findViewById(R.id.result);
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        Button button = (Button) findViewById(R.id.buttonBinary);
        TextView statusBinary = (TextView) findViewById(R.id.statusBinary);
        TextView resultBinary = (TextView) findViewById(R.id.resultBinary);
        savedInstanceState.putString("Result", result.getText().toString());
        savedInstanceState.putString("Formula", editText.getText().toString());
        savedInstanceState.putInt("Start", start);
        savedInstanceState.putInt("End", end);
        savedInstanceState.putInt("Radix", radix);
        savedInstanceState.putString("StatusBinary", statusBinary.getText().toString());
        savedInstanceState.putString("ResultBinary", resultBinary.getText().toString());
        savedInstanceState.putString("Button", button.getText().toString());

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
        if (counts[0] >= 15) {
            Toast toast = Toast.makeText(this, "Не больше 15 цифр!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (counts[1] >= 10) {
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
        int start = editText.getSelectionStart();
        if (sb.length() == 0 || start == 0) {
            return;
        }


        int end = editText.getSelectionEnd();
        char znak = sb.charAt(start - 1);
        if (znak == ')') {
            countBracket++;
        } else if (znak == '(') {
            countBracket--;
        }

        if (start == end) {

            StringBuilder s = sb.deleteCharAt(--start);
            if (s.toString().contains(".")) {

            }
        } else {

            sb.delete(start, end);

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
        addOperation('\u00d7');
    }

    public void onClickDivide(View view) {
        addOperation('/');
    }


    public void onClickReset(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        editText.setText("");
        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText("");
        TextView resultBinary = (TextView) findViewById(R.id.resultBinary);
        resultBinary.setText("");
        countBracket = 0;

    }


    public void onClickRad(View view) {
        TextView rad = (TextView) findViewById(R.id.rad);

        if (isRad) {
            rad.setText("");
            isRad = false;
            Calculate.isRad = false;
        } else {
            rad.setText("Rad");
            isRad = true;
            Calculate.isRad = true;
        }
    }

    public void onClick2x(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start == end) {
            sb.insert(start, "2^(");
        } else {
            sb.delete(start, end);
            sb.insert(start, "2^(");
        }
        countBracket++;
        editText.setText(sb);
        editText.setSelection(start + 3);
    }

    public void onClickX2(View view) {
        addOperation('^');
        addDigit('2');

    }


    public void onClickX3(View view) {
        addOperation('^');
        addDigit('3');
    }

    public void onClickXy(View view) {
        addOperation('^');
    }

    public void onClickSin(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start == end) {
            sb.insert(start, "sin(");
        } else {
            sb.delete(start, end);
            sb.insert(start, "sin(");
        }
        countBracket++;
        editText.setText(sb);
        editText.setSelection(start + 4);
    }

    public void onClickCos(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start == end) {
            sb.insert(start, "cos(");
        } else {
            sb.delete(start, end);
            sb.insert(start, "cos(");
        }
        countBracket++;
        editText.setText(sb);
        editText.setSelection(start + 4);
    }

    public void onClickTg(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start == end) {
            sb.insert(start, "tg(");
        } else {
            sb.delete(start, end);
            sb.insert(start, "tg(");
        }
        countBracket++;
        editText.setText(sb);
        editText.setSelection(start + 3);
    }

    public void onClickSqrt(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start == end) {
            sb.insert(start, "\u221A(");
        } else {
            sb.delete(start, end);
            sb.insert(start, "\u221A(");
        }
        countBracket++;
        editText.setText(sb);
        editText.setSelection(start + 2);
    }

    public void onClickLn(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start == end) {
            sb.insert(start, "ln(");
        } else {
            sb.delete(start, end);
            sb.insert(start, "ln(");
        }
        countBracket++;
        editText.setText(sb);
        editText.setSelection(start + 3);
    }

    public void onClickLg(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start == end) {
            sb.insert(start, "lg(");
        } else {
            sb.delete(start, end);
            sb.insert(start, "lg(");
        }
        countBracket++;
        editText.setText(sb);
        editText.setSelection(start + 3);
    }

    public void onClickLogxY(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start == end) {
            sb.insert(start, "log:");
        } else {
            sb.delete(start, end);
            sb.insert(start, "log:");
        }

        editText.setText(sb);
        editText.setSelection(start + 3);
    }

    public void onClickXfac(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start == end) {
            sb.insert(start, "!");
        } else {
            sb.delete(start, end);
            sb.insert(start, "!");
        }

        editText.setText(sb);
        editText.setSelection(++start);
    }

    public void onClickPi(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start == end) {
            sb.insert(start, Calculate.PI);
        } else {
            sb.delete(start, end);
            sb.insert(start, Calculate.PI);
        }

        editText.setText(sb);
        editText.setSelection(start + 12);
    }

    public void onClickE(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        StringBuilder sb = new StringBuilder(editText.getText());
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start == end) {
            sb.insert(start, Calculate.E);
        } else {
            sb.delete(start, end);
            sb.insert(start, Calculate.E);
        }

        editText.setText(sb);
        editText.setSelection(start + 12);
    }

    public void addDigit(char c) {
        EditText editText = (EditText) findViewById(R.id.edit);

        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        StringBuilder sb = new StringBuilder(editText.getText());
        int[] counts = numberLength(sb, start);

        if (counts[0] >= 15) {
            Toast toast = Toast.makeText(this, "Не больше 15 цифр!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (counts[1] >= 10) {
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
            if (radix != 0) {
                TextView resultBinary = (TextView) findViewById(R.id.resultBinary);
                TextView statusBinary = (TextView) findViewById(R.id.statusBinary);

                Button button = (Button) findViewById(R.id.buttonBinary);
                try {
                    resultBinary.setText(Calculate.toRadix(res, radix));
                } catch (NumberFormatException e) {
                    radix = 0;
                    statusBinary.setText("");
                    resultBinary.setText("");
                    button.setText("->x₂");
                    show("Только с целыми числами");
                }
            }
        } catch (NumberFormatException e) {
            show();
            editText.setText("");
            result.setText("");
            if(radix != 0){
                TextView resultBinary = (TextView) findViewById(R.id.resultBinary);
                resultBinary.setText("");
            }

        }catch (RuntimeException e){
            show(e.getMessage());
            editText.setText("");

            result.setText("");
            if(radix != 0){
                TextView resultBinary = (TextView) findViewById(R.id.resultBinary);
                resultBinary.setText("");
            }
        }

    }

    public void onClickOrientation(View view){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        } else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }


    }

    public void onClickBinary(View view) {
        TextView result = (TextView) findViewById(R.id.result);
        String s = result.getText().toString();
        Button button = (Button) findViewById(R.id.buttonBinary);
        TextView statusBinary = (TextView) findViewById(R.id.statusBinary);
        TextView resultBinary = (TextView) findViewById(R.id.resultBinary);
        String resultBinaryString = "";




        switch (radix) {
            case 0: {
                radix = 2;
                statusBinary.setText("x₁₀->x₂:");
                button.setText("->x₈");
                break;
            }
            case 2: {
                radix = 8;
                statusBinary.setText("x₁₀->x₈:");
                button.setText("->x₁₆");
                break;
            }
            case 8: {
                radix = 16;
                statusBinary.setText("x₁₀->x₁₆:");
                button.setText("Off");
                break;
            }
            case 16: {
                radix = 0;
                statusBinary.setText("");
                resultBinary.setText("");
                button.setText("->x₂");
                return;
            }

        }

        if (s.isEmpty()) {
            return;
        }

        try {

            resultBinaryString = Calculate.toRadix(s, radix);
            resultBinary.setText(resultBinaryString);
        } catch (NumberFormatException e) {

            radix = 0;
            statusBinary.setText("");
            resultBinary.setText("");
            button.setText("->x₂");
            show("Только для целых чисел");

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
        int pointPosishion = 0;
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
        if (end == 0) {
            end = sb.length() - 1;
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
        if (c == '+' || c == '-' || c == '\u00d7' || c == '/' || c == '^') {
            return true;
        } else {
            return false;
        }
    }


    public void show() {
        Toast toast = Toast.makeText(this, "Неверный формат!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void show(String s) {
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        toast.show();
    }
}

