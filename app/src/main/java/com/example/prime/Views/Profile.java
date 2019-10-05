package com.example.prime.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;



import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.android.volley.VolleyLog.TAG;

public class Profile extends Fragment {

    public static Boolean running;
    public static Thread MyThread;
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private String TAG="Profile.java";
    private TextView shopname,branchname,date,time,timezone,auto;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.views_profile, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        shopname = view.findViewById(R.id.shopName);
        branchname = view.findViewById(R.id.branchName);
        time = view.findViewById(R.id.serverTime);
        date = view.findViewById(R.id.serverDate);
        timezone = view.findViewById(R.id.serverTimeZone);
        auto = view.findViewById(R.id.autoShutdown);
        data();
        auto.setText("DISABLED");
        running = true;
        MyThread = new Thread() {//create thread
            @Override
            public void run() {
                int i=0;
                while(running){
                    System.out.println("counter: "+i);
                    i++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Sleep interrupted");
                    }
                    time();

                }
                System.out.println("onEnd Thread");
            }
        };
        MyThread.start();

    }

    private void data(){
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(mContext);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        id = sharedPrefsCookiePersistor.loadAll().get(0).value();

        retrofit2.Call<ResponseBody> call = apiInterface.getInfo("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: "+response );
                try {

                    if(response.body()!= null) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        shopname.setText(jsonObject.getString("shop_name"));
                        branchname.setText(jsonObject.getString("branch_name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: ");

            }
        });
    }

    private void time(){
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(mContext);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        id = sharedPrefsCookiePersistor.loadAll().get(0).value();

//        running = true;
//        MyThread = new Thread(){//create thread
//            @Override
//            public void run() {
                retrofit2.Call<ResponseBody> call = apiInterface.getServerTime("ci_session="+id);
                call.enqueue(new  retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.e(TAG, "onResponse: "+response );
                        try {
                            if(response.body()!= null) {
                                String res = response.body().string();
                                JSONObject jsonObject = new JSONObject(res);
                                long result = jsonObject.getLong("timestamp") * 1000L;
                                SimpleDateFormat formatTime = new SimpleDateFormat("h:mm:ss a", Locale.US);
                                SimpleDateFormat formatDate = new SimpleDateFormat("EEE MMMM dd, yyyy  ", Locale.US);
                                String time1 = formatTime.format(new Date(result));
                                String date1 = formatDate.format(new Date(result));
                                time.setText(time1);
                                date.setText(date1);
                                timezone.setText("GMT +08:00");
                                Log.e(TAG, "onResponse: "+date1 );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");

                    }
                });
//                int i=0;
//                while(running){
//                    System.out.println("counter: "+i);
//                    i++;
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        System.out.println("Sleep interrupted");
//                    }
//                    call.request();
//                    Log.e(TAG, "run: "+call.request());
//
//                }
//                System.out.println("onEnd Thread");
//            }
//        };
//        MyThread.start();
    }

}
