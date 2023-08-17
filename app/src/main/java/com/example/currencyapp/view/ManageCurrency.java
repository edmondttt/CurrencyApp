package com.example.currencyapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.currencyapp.R;

public class ManageCurrency extends AppCompatActivity {
    private Spinner addCurrency;
    private Button addBtn,deleteALLBtn, deleteBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_currency);

        addCurrency = findViewById(R.id.addSpinner);
        addBtn = findViewById(R.id.addBtn);
        deleteALLBtn = findViewById(R.id.deleteALLBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        String[] currencyList = {"USD", "INR", "CAD", "CNY", "GBP"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, currencyList);
        addCurrency.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currency = addCurrency.getSelectedItem().toString();

                Intent data = new Intent();

                data.putExtra("Currency", currency);

                setResult(RESULT_OK, data);

                finish();


            }
        });

        deleteALLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2);

                finish();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currency = addCurrency.getSelectedItem().toString();

                Intent data = new Intent();

                data.putExtra("Currency", currency);

                setResult(3, data);

                finish();
            }
        });
    }
}