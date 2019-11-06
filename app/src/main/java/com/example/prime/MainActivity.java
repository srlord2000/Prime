package com.example.prime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.prime.Model.MyModel;
import com.example.prime.Persistent.ClearableCookieJar;
import com.example.prime.Persistent.PersistentCookieJar;
import com.example.prime.Persistent.SetCookieCache;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    RelativeLayout rellay1, rellay2;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(!sharedPrefsCookiePersistor.loadAll().isEmpty()){
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }else {
                rellay1.setVisibility(View.VISIBLE);
                rellay2.setVisibility(View.VISIBLE);
            }
        }
    };

    private Button btn,o,forgot;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String TAG;
    ApiClient apiInterface;
    SharedPreferences prefs ;
    SharedPreferences.Editor editor;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private EditText username,password;
    private long date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Rellay();
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(this);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        btn = findViewById(R.id.btnLogin);
        forgot = findViewById(R.id.btnforgot);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        List<Cookie> list=sharedPrefsCookiePersistor.loadAll();
        for(int i = 0; i<list.size();i++){
            Log.e(TAG, "onCreate: "+new Date(list.get(i).expiresAt()));
            date = list.get(i).expiresAt();
            id = list.get(i).value();
        }
        long dat2= (long) Long.parseLong(String.valueOf(new Date().getTime()));
        if(date <= dat2){
            sharedPrefsCookiePersistor.clear();
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username",username.getText());
                    jsonObject.put("password",password.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
                ClearableCookieJar cookieJar =
                        new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .cookieJar(cookieJar)
                        .build();
                RequestBody body = RequestBody.create(String.valueOf(jsonObject), JSON);
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("http://192.168.0.100/auth/check")
                        .post(body)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        call.cancel();
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        Log.d("TAG", response.headers().toString());
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if(object.getString("status").equals("0")) {
                                JSONObject object1 = object.getJSONObject("data");
                                String level, name, email;
                                boolean loggedin;
                                level = object1.getString("level");
                                name = object1.getString("username");
                                email = object1.getString("email");
                                loggedin = object1.getBoolean("logged_in");
                                Log.e(TAG, "onResponse: " + level + name + email + loggedin);
                                editor.putString("level", level);
                                editor.putString("username", name);
                                editor.putString("email", email);
                                editor.putBoolean("logged_in", loggedin);
                                editor.apply();
                                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }else if (object.getString("status").equals("1")) {
                                Log.e(TAG, "onResponse: "+object.getString("status") );
                                sharedPrefsCookiePersistor.clear();
                                View contextView = findViewById(android.R.id.content);
                                Snackbar.make(contextView, "Wrong Password or Username", Snackbar.LENGTH_SHORT)
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        });

    }

    private void Load(){
        String user="admin",pass="secret";
        String postData = null;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username","admin");
            jsonObject.put("password","secret");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        RequestBody body = RequestBody.create(String.valueOf(jsonObject), JSON);

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://192.168.0.100/auth/check")
                .post(body)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.d("TAG", response.body().string());
                Log.d("TAG", response.headers().toString());
            }
        });
    }
    public void Rellay(){
        rellay1 =  findViewById(R.id.rellay1);
        rellay2 =  findViewById(R.id.rellay2);
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash
    }


}
