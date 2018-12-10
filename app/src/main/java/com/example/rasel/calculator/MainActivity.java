package com.example.rasel.calculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static double value1, value2, result;
    private EditText text;
    private boolean rplc, flag;
    private String sign = "";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.textView);

        value2 = value1 = result = 0.0;
        rplc = false;
        flag = false;
        sharedPreferences = getSharedPreferences("historyData", MODE_PRIVATE);
        saveValuetoSharedPreference(getString(R.string.memory), "0.0");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {  //saving the value when screen is retated
        super.onSaveInstanceState(outState);
        outState.putString("screenValue", text.getText().toString());
        outState.putDouble("valueOne", value1);
        outState.putDouble("valueTow", value2);
        outState.putDouble("Result", result);
        outState.putBoolean("replc", rplc);
        outState.putBoolean("flag", flag);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String temp = savedInstanceState.getString("screenValue");
        text.setText(temp);
        value1 = savedInstanceState.getDouble("valueOne");
        value2 = savedInstanceState.getDouble("valueTow");
        result = savedInstanceState.getDouble("Result");
        rplc = savedInstanceState.getBoolean("replc");
        flag = savedInstanceState.getBoolean("flag");
    }

    public void calculate(View view) { //Stroing the Scrren Value and showing Result
        String textboxCurrentValue = text.getText().toString();
        Button tmp = (Button) view;

        if (textboxCurrentValue.equals("")) {
            Toast.makeText(this, "Input is not valid", Toast.LENGTH_LONG).show();
            return;
        }
        if (tmp.getId() == R.id.buttonplus) {
            value1 = Double.parseDouble(text.getText().toString());
            sign = "+";
            text.setText("");
        } else if (tmp.getId() == R.id.buttonminus) {
            value1 = Double.parseDouble(text.getText().toString());
            sign = "-";
            text.setText("");
        } else if (tmp.getId() == R.id.buttonmul) {
            value1 = Double.parseDouble(text.getText().toString());
            sign = "*";
            text.setText("");
        } else if (tmp.getId() == R.id.buttondiv) {
            value1 = Double.parseDouble(text.getText().toString());
            sign = "/";
            text.setText("");
        } else if (tmp.getId() == R.id.button1eual) {
            value2 = Double.parseDouble(textboxCurrentValue);
            if (sign.equals("+")) {
                result = value1 + value2;
            } else if (sign.equals("-")) {
                result = value1 - value2;
            } else if (sign.equals("*")) {
                result = value1 * value2;
            } else if (sign.equals("/")) {
                result = value1 / value2;
            } else {
                Toast.makeText(this, "Please Input Properly", Toast.LENGTH_LONG).show();
            }
            rplc = true;
            text.setText(String.valueOf(result));
            text.setSelection(text.getText().length());

            String savedHistory = String.valueOf(value1) + sign + String.valueOf(value2) + "=" + result + "\n";
            String newValue = sharedPreferences.getString(getString(R.string.history), "");

            savedHistory = savedHistory.concat(newValue);

            saveValuetoSharedPreference(getString(R.string.history), savedHistory);
        }
    }

    public void valueCliked(View view) { // Putting Number in the text Box
        Button tmp = (Button) view;
        if ((tmp.getId() == R.id.buttonpoint) && (text.getText().toString().contains("."))) {
            return;
        }
        if (rplc) {
            text.setText("");
            rplc = false;
        }
        if (flag) {
            text.setText("");
            rplc = false;
        }
        text.append(tmp.getText().toString());
    }

    public void showHistory(View view) {      //show all the calculation previous Done
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    public void SingleOptionClicked(View view) {
        Button tmp = (Button) view;
        if (tmp.getId() == R.id.btnClear) {
            text.setText("");
        } else if (tmp.getId() == R.id.btnOnebyX) {
            rplc = true;
            value2 = Double.parseDouble(text.getText().toString());
            value2 = 1 / value2;
            text.setText(String.valueOf(value2));
            saveValuetoSharedPreference(getString(R.string.history), String.valueOf(value2));
        } else if (tmp.getId() == R.id.btnMemoryPlus) {
            rplc = true;
            Double newValue = Double.parseDouble(text.getText().toString());
            String oldValue = sharedPreferences.getString(getString(R.string.memory), "");
            value2 = Double.parseDouble(oldValue);
            value2 = newValue + value2;
            text.setText(String.valueOf(value2));
            saveValuetoSharedPreference(getString(R.string.memory), String.valueOf(value2));
        } else if (tmp.getId() == R.id.btnMemoryMinus) {
            rplc = true;
            Double newValue = Double.parseDouble(text.getText().toString());
            String oldValue = sharedPreferences.getString(getString(R.string.memory), "");
            value2 = Double.parseDouble(oldValue);
            value2 = value2 - newValue;
            text.setText(String.valueOf(value2));
            saveValuetoSharedPreference(getString(R.string.memory), String.valueOf(value2));
        }
        text.setSelection(text.getText().length());
    }

    public void SingleImageOptionClicked(View view) { // Performing Operation On the Current Available Text Value Screen
        String textboxCurrentValue = text.getText().toString();
        if (textboxCurrentValue.equals("")) {
            Toast.makeText(this, "Input is Empty", Toast.LENGTH_LONG).show();
            return;
        }
        ImageButton tmp = (ImageButton) view;
        if (tmp.getId() == R.id.btnEraze) {
            String str = text.getText().toString();
            if (str.length() > 0) {
                str = str.substring(0, str.length() - 1);
            }
            text.setText(str);
        } else if (tmp.getId() == R.id.btnSquare) {
            value2 = Double.parseDouble(textboxCurrentValue);
            value2 = value2 * value2;
            text.setText(String.valueOf(value2));
        } else if (tmp.getId() == R.id.btnSquareRoot) {
            value2 = Double.parseDouble(textboxCurrentValue);
            value2 = Math.sqrt(value2);
            text.setText(String.valueOf(value2));
        }
        text.setSelection(text.getText().length());
    }

    public void saveValuetoSharedPreference(String tag, String str) { // Saving value In Share Preferences Memory
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(tag, str);
        editor.apply();
    }
}
