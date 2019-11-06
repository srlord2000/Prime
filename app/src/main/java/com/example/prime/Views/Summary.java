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

import com.example.prime.Model.DrySummaryModel;
import com.example.prime.Model.DryerModel;
import com.example.prime.Model.ServiceModel;
import com.example.prime.Model.StationModel;
import com.example.prime.Model.StringModel;
import com.example.prime.Model.SummaryModel;
import com.example.prime.Model.UnitModel;
import com.example.prime.Model.WashSummaryModel;
import com.example.prime.Model.WasherModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.RecyclerAdapter.CardAdapter;
import com.example.prime.RecyclerAdapter.ControlAdapter;
import com.example.prime.RecyclerAdapter.SummaryAdapter;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Summary extends Fragment {

    public static Boolean running;
    public static Thread MyThread;
    private ArrayList<SummaryModel> summaryModels;
    private ArrayList<DrySummaryModel> drySummaryModels = new ArrayList<>();
    private ArrayList<WashSummaryModel> washSummaryModels = new ArrayList<>();
    private List<String> strings;
    private String text1;
    private RecyclerView recyclerView;
    private ControlAdapter controlAdapter;
    private ArrayList<String> data = new ArrayList<>();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String GET_JSON_FROM_SERVER_NAME1 = "id";
    String GET_JSON_FROM_SERVER_NAME2 = "service_name";
    String GET_JSON_FROM_SERVER_NAME3 = "service_type";
    String GET_JSON_FROM_SERVER_NAME4 = "service_level";
    String GET_JSON_FROM_SERVER_NAME5 = "price";
    String GET_JSON_FROM_SERVER_NAME6 = "tap_pulse";
    String GET_JSON_FROM_SERVER_NAME7 = "time_added";
    String GET_JSON_FROM_SERVER_NAME8 = "unit_name";
    String GET_JSON_FROM_SERVER_NAME9 = "unit_id";

    String GET_JSON_FROM_SERVER_NAME11 = "id";
    String GET_JSON_FROM_SERVER_NAME12 = "host_id";
    String GET_JSON_FROM_SERVER_NAME13 = "station_name";
    String GET_JSON_FROM_SERVER_NAME14 = "hostname";
    String GET_JSON_FROM_SERVER_NAME15 = "status";
    String GET_JSON_FROM_SERVER_NAME16 = "unit_id";
    String GET_JSON_FROM_SERVER_NAME17 = "unit_name";
    String GET_JSON_FROM_SERVER_NAME18 = "ipaddress";
    private HashSet<String> hashSet;
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private String TAG="Summary.java";
    private Context mContext;
    private Button send, notif;
    private SummaryAdapter summaryAdapter;
    private retrofit2.Call<ResponseBody> call;
    SharedPreferences prefs ;
    SharedPreferences.Editor editor;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = container.getContext();
        return inflater.inflate(R.layout.views_summary, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences( mContext);
        editor = prefs.edit();
        strings = new ArrayList<>();
        summaryModels = new ArrayList<>();
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(mContext);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        id = sharedPrefsCookiePersistor.loadAll().get(0).value();
        send = view.findViewById(R.id.send);
        recyclerView = view.findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        summaryAdapter = new SummaryAdapter(mContext, summaryModels);
        recyclerView.setAdapter(summaryAdapter);
        load();
        //data();



    }
    private void load(){
        retrofit2.Call<ResponseBody> call = apiInterface.getUnits("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("TAG", response.headers().toString());
                try {
                    String responseData = response.body().string();
                    final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                    final SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("Summary",responseData);
                    editor.apply();
                    Log.e("TAGtry", responseData);
                    data.add(responseData);
                    JSONArray object = new JSONArray(responseData);
                    for(int i = 0; i<object.length();i++){
                        JSONObject jsonObject = object.getJSONObject(i);
                        final String unit_id = jsonObject.getString("id");
                        final String name = jsonObject.getString("unit_name");
                        retrofit2.Call<ResponseBody> call1 = apiInterface.getServiceId("ci_session="+id,unit_id);
                        call1.enqueue(new retrofit2.Callback<ResponseBody>() {
                            @Override
                            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                try {
                                    String res = response.body().string();
                                    Log.e(TAG, "onResponse: "+res );
                                    editor.putString(unit_id, res);
                                    editor.commit();

                                    load1();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.e("", "sharedtagtry: "+prefs.getString("15", ""));
                            }
                            @Override
                            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                                Log.d(TAG, "onFailuretagtry: ");

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailuretagtrt: ");

            }
        });

    }

    private void load1(){
        retrofit2.Call<ResponseBody> call = apiInterface.getStation("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("TAG", response.headers().toString());
                try {
                    String responseData = response.body().string();
                    Log.e("TAG", responseData);
                    summaryModels.clear();
                    drySummaryModels.clear();
                    washSummaryModels.clear();
                    JSONArray object = new JSONArray(responseData);

                    for(int i = 0; i<object.length();i++) {
                        JSONObject json = null;
                        try {
                            json = object.getJSONObject(i);
                            strings.add(json.getString(GET_JSON_FROM_SERVER_NAME16));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    hashSet = new HashSet<String>(strings);
                    strings.clear();
                    strings = new ArrayList<String>(hashSet);
                    Collections.sort(strings);
                    Log.e(TAG, "onResponse: "+strings +" "+ hashSet);
                    for (int i = 0; i < strings.size(); i++) {
                        washSummaryModels = new ArrayList<>();
                        drySummaryModels = new ArrayList<>();
                        final String id = strings.get(i);
                        String data1 = prefs.getString(id, "");
                        Log.e(TAG, "onResponse: "+data1 );
                        JSONArray jsonArray = new JSONArray(data1);
                        SummaryModel parent = new SummaryModel();
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject json = jsonArray.getJSONObject(j);
                            String type = json.getString(GET_JSON_FROM_SERVER_NAME3);
                            if(type.toLowerCase().equals("wash")) {
                                washSummaryModels.add(new WashSummaryModel(json.getString(GET_JSON_FROM_SERVER_NAME1), json.getString(GET_JSON_FROM_SERVER_NAME2)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME3), json.getString(GET_JSON_FROM_SERVER_NAME4)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME5), json.getString(GET_JSON_FROM_SERVER_NAME6)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME7), json.getString(GET_JSON_FROM_SERVER_NAME8)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME9)));
                            }else if (type.toLowerCase().equals("dry")){
                                drySummaryModels.add(new DrySummaryModel(json.getString(GET_JSON_FROM_SERVER_NAME1), json.getString(GET_JSON_FROM_SERVER_NAME2)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME3), json.getString(GET_JSON_FROM_SERVER_NAME4)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME5), json.getString(GET_JSON_FROM_SERVER_NAME6)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME7), json.getString(GET_JSON_FROM_SERVER_NAME8)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME9)));
                            }
                            parent.setUnitid(id);
                            parent.setUnitname(json.getString(GET_JSON_FROM_SERVER_NAME8));
                            parent.setWashSummaryModels(washSummaryModels);
                            parent.setDrySummaryModels(drySummaryModels);
                        }

                        summaryModels.add(parent);
                    }
                } catch (IOException | JSONException e1) {
                    e1.printStackTrace();
                }
                summaryAdapter.setPreset(summaryModels);
                summaryAdapter.notifyDataSetChanged();
            }

            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailuretagtrt: ");

            }
        });
    }

    private void data(){
        call = apiInterface.getStation("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.body() != null) {
                        String res = response.body().string();

                            strings = new ArrayList<>();
                            try {
                                Log.e(TAG, "onResponse222: "+res );
                                JSONArray array = new JSONArray(res);
                                for (int i = 0; i < array.length(); i++) {

                                    StringModel stringModel = new StringModel();
                                    JSONObject json = null;
                                    try {
                                        json = array.getJSONObject(i);
                                        strings.add(json.getString(GET_JSON_FROM_SERVER_NAME16));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                hashSet = new HashSet<String>(strings);
                                strings.clear();
                                strings = new ArrayList<String>(hashSet);
                                Collections.sort(strings);
                                Log.e(TAG, "onResponsedasdasdsad: "+strings );
                                str();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());

            }
        });
    }

    private void str(){
        for (int i = 0; i < strings.size(); i++) {
            final String in;
            in = strings.get(i);
            call = apiInterface.getServiceId("ci_session="+id,in);
            call.enqueue(new  retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if(response.body() != null) {
                                String res = response.body().string();
                                drySummaryModels = new ArrayList<>();
                                washSummaryModels = new ArrayList<>();
                                summaryModels = new ArrayList<>();
                                try {
                                    SummaryModel summaryModel = new SummaryModel();
                                    WashSummaryModel washerModel = new WashSummaryModel();
                                    DrySummaryModel dryerModel = new DrySummaryModel();
                                    Log.e(TAG, "onResponse222: "+res );
                                    JSONArray array = new JSONArray(res);
                                    for (int i = 0; i < array.length(); i++) {
                                        String type;

                                        JSONObject json = null;
                                        try {
                                            json = array.getJSONObject(i);
                                            type = json.getString(GET_JSON_FROM_SERVER_NAME3);
                                            summaryModel.setUnitname(json.getString(GET_JSON_FROM_SERVER_NAME2));
                                            if(type.toLowerCase().equals("wash")) {
                                                washerModel.setId(json.getString(GET_JSON_FROM_SERVER_NAME1));
                                                washerModel.setServiceName(json.getString(GET_JSON_FROM_SERVER_NAME2));
                                                washerModel.setServiceType(json.getString(GET_JSON_FROM_SERVER_NAME3));
                                                washerModel.setServiceLevel(json.getString(GET_JSON_FROM_SERVER_NAME4));
                                                washerModel.setPrice(json.getString(GET_JSON_FROM_SERVER_NAME5));
                                                washerModel.setTapPulse(json.getString(GET_JSON_FROM_SERVER_NAME6));
                                                washerModel.setTimeAdded(json.getString(GET_JSON_FROM_SERVER_NAME7));
                                                washerModel.setUnitName(json.getString(GET_JSON_FROM_SERVER_NAME8));
                                                washerModel.setUnitId(json.getString(GET_JSON_FROM_SERVER_NAME9));
                                                washSummaryModels.add(washerModel);
                                            }else if (type.toLowerCase().equals("dry")){
                                                dryerModel.setId(json.getString(GET_JSON_FROM_SERVER_NAME1));
                                                dryerModel.setServiceName(json.getString(GET_JSON_FROM_SERVER_NAME2));
                                                dryerModel.setServiceType(json.getString(GET_JSON_FROM_SERVER_NAME3));
                                                dryerModel.setServiceLevel(json.getString(GET_JSON_FROM_SERVER_NAME4));
                                                dryerModel.setPrice(json.getString(GET_JSON_FROM_SERVER_NAME5));
                                                dryerModel.setTapPulse(json.getString(GET_JSON_FROM_SERVER_NAME6));
                                                dryerModel.setTimeAdded(json.getString(GET_JSON_FROM_SERVER_NAME7));
                                                dryerModel.setUnitName(json.getString(GET_JSON_FROM_SERVER_NAME8));
                                                dryerModel.setUnitId(json.getString(GET_JSON_FROM_SERVER_NAME9));
                                                drySummaryModels.add(dryerModel);

                                            }
                                            summaryModel.setUnitid(in);
                                            summaryModel.setDrySummaryModels(drySummaryModels);
                                            summaryModel.setWashSummaryModels(washSummaryModels);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    summaryModels.add(summaryModel);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        summaryAdapter.setPreset(summaryModels);
                        summaryAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());

                    }
                });
        }


    }
}
