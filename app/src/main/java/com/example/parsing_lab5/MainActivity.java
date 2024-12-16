package com.example.parsing_lab5;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText searchFilter;
    private Spinner baseCurrencySpinner;
    private ProgressBar progressBar;
    private ArrayAdapter<String> adapter;
    private List<String> currencyList;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        searchFilter = findViewById(R.id.searchFilter);
        baseCurrencySpinner = findViewById(R.id.baseCurrencySpinner);
        progressBar = findViewById(R.id.progressBar);

        prefs = getSharedPreferences("currency_prefs", MODE_PRIVATE);

        currencyList = new ArrayList<>();

        String[] baseCurrencies = {"USD", "EUR"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, baseCurrencies);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        baseCurrencySpinner.setAdapter(spinnerAdapter);

        String lastCurrency = prefs.getString("base_currency", "USD");
        baseCurrencySpinner.setSelection(spinnerAdapter.getPosition(lastCurrency));

        baseCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCurrency = baseCurrencies[position];
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("base_currency", selectedCurrency);
                editor.apply();

                String url = "https://www.floatrates.com/daily/" + selectedCurrency.toLowerCase() + ".xml";
                new DataLoader(MainActivity.this).execute(url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (adapter != null) {
                    adapter.getFilter().filter(charSequence);
                }
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

    public void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
