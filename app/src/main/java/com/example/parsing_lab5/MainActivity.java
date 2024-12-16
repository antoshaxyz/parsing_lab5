package com.example.parsing_lab5;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText searchFilter;
    private ArrayAdapter<String> adapter;
    private List<String> currencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        searchFilter = findViewById(R.id.searchFilter);

        currencyList = new ArrayList<>();

        // Start data loading
        new DataLoader(this).execute("https://www.floatrates.com/daily/usd.xml");

        // Filter functionality
        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    public void updateCurrencyList(List<String> rates) {
        currencyList = rates;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currencyList);
        listView.setAdapter(adapter);
    }

    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
