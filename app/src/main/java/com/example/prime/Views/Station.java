package com.example.prime.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.prime.Model.SpinnerModel;
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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Station extends Fragment {

    public static Boolean running;
    public static Thread MyThread;
    private ArrayList<StationModel> stationModels;
    private String text1;
    private RecyclerView recyclerView;
    private Button add, delete, scan;
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
    protected List<SpinnerModel> spinnerData;

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
        delete = view.findViewById(R.id.btnStationRemove);
        add = view.findViewById(R.id.btnStationAdd);
        scan = view.findViewById(R.id.btnStationScan);
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> sad = new ArrayList<>();
                if (stationAdapter.getSelected().size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < stationAdapter.getSelected().size(); i++) {
                        JSONObject obj = new JSONObject();
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(stationAdapter.getSelected().get(i).getIpaddress());
                        try {

                            obj.put("ipaddress", jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        stringBuilder.append(stationAdapter.getSelected().get(i).getId());
                        stringBuilder.append("\n");
                        sad.add(obj.toString());
                        Log.i(TAG, "onClick: "+obj);

                        OkHttpClient client = new OkHttpClient();

                        RequestBody body = RequestBody.create(String.valueOf(obj), JSON);

                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url("http://192.168.0.100/stations/remove/ip")
                                .addHeader("Cookie","ci_session="+id)
                                .post(body)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                call.cancel();
                            }

                            @Override
                            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                Log.e("TAG", response.message());
                                Log.e(TAG, "onResponse: "+response.body().string() );
                            }
                        });
                        Log.e("", "onClick: " + sad);
                    }
                } else {
                    Toast.makeText(mContext, "No Selection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = getLayoutInflater().inflate(R.layout.stationhostscandialog, null);
                final TextView host12;
                final EditText ip11 = mView.findViewById(R.id.ipAddress1);
                //final EditText station11 = mView.findViewById(R.id.stationName1);
                final Spinner unit1 = (Spinner)mView.findViewById(R.id.unitType1);
                host12 = mView.findViewById(R.id.hostType1);
                Button submit1 = mView.findViewById(R.id.btnSubmit1);
                //Button close12 = mView.findViewById(R.id.btnCancel12);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

//                close12.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });

                unit1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected( AdapterView<?> adapterView, View view,  int i, long l) {
                        int id = adapterView.getSelectedItemPosition();
                        text1 = spinnerData.get(id).getId();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        int id = adapterView.getFirstVisiblePosition();
                        text1 = spinnerData.get(id).getId();
                    }
                });

//                submit1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (!station11.getText().toString().isEmpty()) {
//
//                            Toast.makeText(mContext, text1, Toast.LENGTH_SHORT).show();
//
//                            String ip,station, host1,type;
//                            type = text1;
//                            host1 = host12.getText().toString();
//                            ip = ip11.getText().toString();
//                            station =  station11.getText().toString();
//                            JSONObject obj = new JSONObject();
//                            try {
//                                obj.put("ipaddress", ip);
//                                obj.put("unit_id", type);
//                                obj.put("station_name",station);
//                                obj.put("hostname",host1);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            Toast.makeText(mContext, text1, Toast.LENGTH_SHORT).show();
//
//                            OkHttpClient client = new OkHttpClient();
//
//
//
//                            RequestBody body = RequestBody.create(String.valueOf(obj), JSON);
//
//                            Log.e(TAG, "onClick: "+obj );
//                            okhttp3.Request request = new okhttp3.Request.Builder()
//                                    .url("http://192.168.0.100/stations/add/cc")
//                                    .post(body)
//                                    .build();
//
//                            client.newCall(request).enqueue(new Callback() {
//                                @Override
//                                public void onFailure(Call call, IOException e) {
//                                    call.cancel();
//                                    Log.e(TAG, "onFailure: error");
//                                }
//
//                                @Override
//                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
//                                    Log.e("TAG", response.body().string());
//                                }
//                            });
//
//                            Toast.makeText(mContext,
//                                    text1,
//                                    Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//
//
//                        } else {
//                            Toast.makeText(mContext,
//                                    R.string.error_msg,
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

                //retro

            }
        });

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

