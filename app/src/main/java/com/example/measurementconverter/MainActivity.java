package com.example.measurementconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener, TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener {

    private EditText measurementEditText;
    private TextView measurementConversionText;
    private Spinner conversionSpinner;
    private String measurementString = "";
    private SharedPreferences savedValues;
    private int conversionRate;
    private TextView measurement1;
    private TextView measurement2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        measurementEditText = (EditText) findViewById(R.id.measurementEditText);
        measurementConversionText = (TextView) findViewById(R.id.measurementConversionText);
        conversionSpinner = (Spinner) findViewById(R.id.conversionSpinner);
        measurement1 = (TextView) findViewById(R.id.measurement1);
        measurement2 = (TextView) findViewById(R.id.measurement2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.arrayMeasurement, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        conversionSpinner.setAdapter(adapter);

        savedValues = getSharedPreferences("savedValues", MODE_PRIVATE);

        measurementEditText.setOnKeyListener(this);
        measurementEditText.setOnEditorActionListener(this);
        conversionSpinner.setOnItemSelectedListener(this);


    }//end Oncreate

    public void calculateAndDisplay() {
        measurementString = measurementEditText.getText().toString();
        float measurement;
        if (measurementString.equals("")){
            measurement = 0;
        }
        else{
            measurement= Float.parseFloat(measurementString);
        }

        float conversionNum;

        if(conversionRate == 0){
            conversionNum = (float) 1.6093;
            measurement1.setText("Miles");
            measurement2.setText("Kilometers");
        }
        else if (conversionRate == 1){
            conversionNum = (float) 0.6214;
            measurement1.setText("Kilometers");
            measurement2.setText("Miles");
        }
        else if (conversionRate == 2){
            conversionNum = (float) 2.54;
            measurement1.setText("Inches");
            measurement2.setText("Centimeters");

        }
        else {
            conversionNum = (float) 0.3937;
            measurement1.setText("Centimeters");
            measurement2.setText("Inches");
        }

        float conversionFinal;

        conversionFinal = (measurement * conversionNum);


        NumberFormat instance = NumberFormat.getNumberInstance();
        measurementConversionText.setText(instance.format(conversionFinal));
    }


    @Override
    public void onPause() { //need to import shared preferences editor
        //save the instance variables
        super.onPause();//calls the superclass to complete the creation of the activity
        SharedPreferences.Editor editor = savedValues.edit();//needed unchanged check listener book used Editor editor = savedValuses
        editor.putString("measurementString", measurementString );
    }//end onPause

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:

                calculateAndDisplay();

                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        measurementEditText.getWindowToken(),0);
                return true;
        }
        return false;
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if(actionId== EditorInfo.IME_ACTION_DONE|| actionId==EditorInfo.IME_ACTION_UNSPECIFIED){
            calculateAndDisplay();
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        conversionRate = position;
        calculateAndDisplay();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
