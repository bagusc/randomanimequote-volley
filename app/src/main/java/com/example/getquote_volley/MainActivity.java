package com.example.getquote_volley;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private Button btnGenerate;
    private TextView tvChar, tvAnime, tvQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Menginisialisasi RequestQueue dari MyApplication
        requestQueue = ((MyApplication) this.getApplication()).getRequestQueue();


        btnGenerate = findViewById(R.id.btnGenerate);
        tvChar = findViewById(R.id.tvChar);
        tvAnime = findViewById(R.id.tvAnime);
        tvQuote = findViewById(R.id.tvQuote);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQuoteFromAPI();
            }
        });
    }

    private void getQuoteFromAPI() {
        String baseUrl = "https://katanime.vercel.app";
        String endpoint = "/api/getrandom";
        String url = baseUrl + endpoint;
        RequestQueue requestQueue = MyApplication.getInstance().getRequestQueue();

    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Menangani respons di sini
                    // 'response' berisi kutipan-kutipan anime yang diambil dari API

                    try {
                             // Parse the JSON response
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray resultArray = jsonObject.getJSONArray("result");

                            if (resultArray.length() > 0) {
                                // Get the first result
                                JSONObject firstResult = resultArray.getJSONObject(0);
                                String indoQuote = firstResult.getString("indo");
                                String character = firstResult.getString("character");
                                String anime = firstResult.getString("anime");

                            // Show kutipan dalam TextView
                            tvChar.setText("Character : " + character);
                            tvAnime.setText("Anime : " + anime);
                            tvQuote.setText("Quote : " + indoQuote);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    tvQuote.setText(response);
                },
                error -> {
                    // Menangani kesalahan di sini
                    // 'error' berisi detail kesalahan
                    tvQuote.setText("Gagal mengambil kutipan.");
                });

        requestQueue.add(stringRequest);
    }
}
