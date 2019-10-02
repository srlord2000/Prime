package com.example.prime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.prime.Model.MyModel;
import com.example.prime.Persistent.ClearableCookieJar;
import com.example.prime.Persistent.PersistentCookieJar;
import com.example.prime.Persistent.SetCookieCache;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    private Button btn,btn1;
    public static WebView wb;
    private String URLline = "http://192.168.0.100/auth/check";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String TAG;
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private long date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(this);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);

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
        if(sharedPrefsCookiePersistor.loadAll().isEmpty()){
            Load();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofit2.Call<ResponseBody> call = apiInterface.getJSONData1("ci_session="+id);
                call.enqueue(new  retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.e(TAG, "onResponse: "+response );
                    }

                    @Override
                    public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");

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
}
