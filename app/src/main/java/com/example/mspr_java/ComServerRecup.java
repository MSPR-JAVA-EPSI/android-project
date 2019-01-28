package com.example.mspr_java;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.EquipementItemComponent;

public class ComServerRecup {
    public String get() throws IOException {
        InputStream is = null;
        String url = "??";
        try {
            final HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(10000 /* milliseconds */); // temps laisser pour la lecture
            conn.setConnectTimeout(15000 /* milliseconds */); // temps laisser pour l'utilisation
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            is = conn.getInputStream();
            // Lecture de l'InputStream and sauvegarde en string
            return readIt(is);
        } finally {
            // Fermer l'InputStream
            if (is != null) {
                is.close();
            }
        }
    }

    private String readIt(InputStream is) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            response.append(line).append('\n');
        }
        return response.toString();
    }
    public List<EquipementItemComponent> parse(final String json) {
        try {
            final List<EquipementItemComponent> products = new ArrayList<>();
            final JSONArray jProductArray = new JSONArray(json);
            for (int i = 0; i < jProductArray.length(); i++) {
                products.add(new EquipementItemComponent(jProductArray.optJSONObject(i)));
            }
            return products;
        } catch (JSONException e) {
            Log.v("sheh","error 404");
        }
        return null;
    }
}
