package com.rachad.wildprecision;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SplashActivity extends AppCompatActivity {
    WebView webView;
    TextView textView;
    ProgressBar progressBar;
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        webView = findViewById(R.id.webView);
        textView = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBar);
        mQueue = Volley.newRequestQueue(this);


        if (connected()) {
            jsonParse();
            /*WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
            webView.loadUrl("http://rachad.epizy.com//Wildprecisiontrack/");
            new CountDownTimer(10000, 10000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    try {
                        textView.setText(R.string.no_internet_connection);
                        progressBar.setVisibility(View.INVISIBLE);
                    } catch (Exception ignored) {
                    }
                }
            }.start();*/

        } else {
            textView.setText(R.string.no_internet_connection);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public boolean connected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    private void jsonParse() {
        String url = "https://s0sa.000webhostapp.com/test/checkconnection.json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject employee = jsonArray.getJSONObject(i);
                            String string1 = employee.getString("Wildprecisiontrack");
                            if (string1.equals("1")) {
                                 startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            } else {
                                textView.setText(R.string.no_internet_connection);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "126", Toast.LENGTH_SHORT).show();
                        textView.setText(R.string.no_internet_connection);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, error -> {
            Toast.makeText(this, "032", Toast.LENGTH_SHORT).show();
            textView.setText(R.string.no_internet_connection);
            progressBar.setVisibility(View.INVISIBLE);
        });


        mQueue.add(request);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SplashActivity.super.finish();
    }
}

/*
class JavaScriptInterface {
    Context mContext;

    JavaScriptInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void showToast() {
        mContext.startActivity(new Intent(mContext, MainActivity.class));
    }
}*/
