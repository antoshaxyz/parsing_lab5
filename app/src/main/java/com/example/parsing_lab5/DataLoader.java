package com.example.parsing_lab5;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DataLoader extends AsyncTask<String, Void, String> {

    private MainActivity activity;

    public DataLoader(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        activity.showLoading(true);
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        activity.showLoading(false);
        if (result != null) {
            List<String> parsedData = Parser.parseXML(result);
            activity.updateCurrencyList(parsedData);
        } else {
            activity.showError("Failed to fetch data. Please check your internet connection.");
        }
    }
}
