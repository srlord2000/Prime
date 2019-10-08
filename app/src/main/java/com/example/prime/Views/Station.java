package com.example.prime.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.prime.Model.CardsModel;
import com.example.prime.Model.StationModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.RecyclerAdapter.CardAdapter;
import com.example.prime.RecyclerAdapter.StationAdapter;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Station extends Fragment {

    public static Boolean running;
    public static Thread MyThread;
    private ArrayList<StationModel> stationModels;
    private String text1;
    private RecyclerView recyclerView;
    private Button add, delete;
    private StationAdapter stationAdapter;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String GET_JSON_FROM_SERVER_NAME1 = "id";
    String GET_JSON_FROM_SERVER_NAME2 = "host_id";
    String GET_JSON_FROM_SERVER_NAME3 = "station_name";
    String GET_JSON_FROM_SERVER_NAME4 = "hostname";
    String GET_JSON_FROM_SERVER_NAME5 = "status";
    String GET_JSON_FROM_SERVER_NAME6 = "unit_id";
    String GET_JSON_FROM_SERVER_NAME7 = "unit_name";
    String GET_JSON_FROM_SERVER_NAME8 = "ipaddress";
    JsonArrayRequest jsonArrayRequest;
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private String TAG="Station.java";

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = container.getContext();
        return inflater.inflate(R.layout.views_station, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        stationModels = new ArrayList<>();
        recyclerView = view.findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        stationAdapter = new StationAdapter(mContext, stationModels);
        recyclerView.setAdapter(stationAdapter);
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(mContext);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        id = sharedPrefsCookiePersistor.loadAll().get(0).value();

        data1();
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
                    data();

                }
                System.out.println("onEnd Thread");
            }
        };
        MyThread.start();


    }

    private void data(){
        retrofit2.Call<ResponseBody> call = apiInterface.getStation("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String result = prefs.getString("Station",null);
                try {
                    if(response.body() != null) {
                        String res = response.body().string();
                        if (res.equals(result)) {

                        } else {
                            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.remove("Station");
                            editor.putString("Station", res);
                            editor.apply();
                            stationModels = new ArrayList<>();
                            try {
                                stationModels.clear();
                                Log.e(TAG, "onResponse222: "+res );
                                JSONArray array = new JSONArray(res);
                                for (int i = 0; i < array.length(); i++) {

                                    StationModel station = new StationModel();
                                    JSONObject json = null;
                                    try {
                                        json = array.getJSONObject(i);
                                        station.setId(json.getString(GET_JSON_FROM_SERVER_NAME1));
                                        station.setHostId(json.getString(GET_JSON_FROM_SERVER_NAME2));
                                        station.setStationName(json.getString(GET_JSON_FROM_SERVER_NAME3));
                                        station.setHostname(json.getString(GET_JSON_FROM_SERVER_NAME4));
                                        station.setStatus(json.getString(GET_JSON_FROM_SERVER_NAME5));
                                        station.setUnitId(json.getString(GET_JSON_FROM_SERVER_NAME6));
                                        station.setUnitName(json.getString(GET_JSON_FROM_SERVER_NAME7));
                                        station.setIpaddress(json.getString(GET_JSON_FROM_SERVER_NAME8));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    stationModels.add(station);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            stationAdapter.setStations(stationModels);
                            stationAdapter.notifyDataSetChanged();

                        }
                    }
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

    private void data1(){
        retrofit2.Call<ResponseBody> call = apiInterface.getStation("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String result = prefs.getString("Station",null);
                try {
                    if(response.body() != null) {
                        String res = response.body().string();
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.remove("Station");
                        editor.putString("Station", res);
                        editor.apply();
                        stationModels = new ArrayList<>();
                        try {
                            stationModels.clear();
                            Log.e(TAG, "onResponse222: "+res );
                            JSONArray array = new JSONArray(res);
                            for (int i = 0; i < array.length(); i++) {
                                StationModel station = new StationModel();
                                JSONObject json = null;
                                try {
                                    json = array.getJSONObject(i);
                                    station.setId(json.getString(GET_JSON_FROM_SERVER_NAME1));
                                    station.setHostId(json.getString(GET_JSON_FROM_SERVER_NAME2));
                                    station.setStationName(json.getString(GET_JSON_FROM_SERVER_NAME3));
                                    station.setHostname(json.getString(GET_JSON_FROM_SERVER_NAME4));
                                    station.setStatus(json.getString(GET_JSON_FROM_SERVER_NAME5));
                                    station.setUnitId(json.getString(GET_JSON_FROM_SERVER_NAME6));
                                    station.setUnitName(json.getString(GET_JSON_FROM_SERVER_NAME7));
                                    station.setIpaddress(json.getString(GET_JSON_FROM_SERVER_NAME8));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                stationModels.add(station);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        stationAdapter.setStations(stationModels);
                        stationAdapter.notifyDataSetChanged();
                    }
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

}

