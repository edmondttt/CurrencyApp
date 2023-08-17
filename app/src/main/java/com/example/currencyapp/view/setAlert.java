package com.example.currencyapp.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.currencyapp.R;

public class setAlert extends AppCompatActivity {

    private Spinner addCurrency;
    private Button setBtn;
    private EditText editText;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alert);

        addCurrency = findViewById(R.id.addSpinner);
        setBtn = findViewById(R.id.setBtn);
        editText = findViewById(R.id.editText);


        String[] currencyList = {"USD", "INR", "CAD", "CNY", "GBP"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, currencyList);
        addCurrency.setAdapter(adapter);

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currency = addCurrency.getSelectedItem().toString();
                Double rate = Double.parseDouble(editText.getText().toString());
                Intent data = new Intent();

                data.putExtra("Currency", currency);
                data.putExtra("Rate", rate);
                setResult(RESULT_OK, data);

                finish();


            }
        });
    }
}