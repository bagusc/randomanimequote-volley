package com.example.getquote_volley;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    // Handler dan Runnable untuk menjalankan getQuoteFromAPI() setiap 30 detik
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Menginisialisasi RequestQueue dari MyApplication
        requestQueue = ((MyApplication) this.getApplication()).getRequestQueue();
//
//        btnGenerate = findViewById(R.id.btnGenerate);
//        tvChar = findViewById(R.id.tvChar);
//        tvAnime = findViewById(R.id.tvAnime);
//        tvQuote = findViewById(R.id.tvQuote);

//        btnGenerate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getQuoteFromAPI();
//            }
//        });

        // Membuat tugas yang diulang setiap 30 detik
        runnable = new Runnable() {
            @Override
            public void run() {
                getQuoteFromAPI();
                // Jalankan lagi setelah 30 detik
                handler.postDelayed(this, 5000);
            }
        };

        // Jalankan tugas pertama kali
        handler.post(runnable);
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
//                            tvChar.setText("Character : " + character);
//                            tvAnime.setText("Anime : " + anime);
//                            tvQuote.setText("Quote : " + indoQuote);

                            String newText ="\""+indoQuote.toString()+"\"  "+"-"+character;

                            // Save the text to SharedPreferences
                            saveWidgetTextToPrefs(newText);

                            // Update all widgets
                            updateAllWidgets();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    tvQuote.setText(response);
                },
                error -> {
                    // Menangani kesalahan di sini
                    // 'error' berisi detail kesalahan
//                    tvQuote.setText("Gagal mengambil kutipan.");
                });

        requestQueue.add(stringRequest);

    }



    private void saveWidgetTextToPrefs(String newText) {
        SharedPreferences prefs = getSharedPreferences(NewAppWidget.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NewAppWidget.KEY_WIDGET_TEXT, newText);
        editor.apply();
    }

    private void updateAllWidgets() {
        Intent intent = new Intent(this, NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hentikan tugas saat Activity dihancurkan
        handler.removeCallbacks(runnable);
    }
}


