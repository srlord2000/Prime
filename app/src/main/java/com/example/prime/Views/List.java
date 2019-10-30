package com.example.prime.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.ListModel;
import com.example.prime.Model.StationModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.RecyclerAdapter.ControlAdapter;
import com.example.prime.RecyclerAdapter.ListAdapter;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class List extends Fragment {

    public static Boolean running;
    public static Thread MyThread;
    private ArrayList<ListModel> listModels;
    private String text1;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String GET_JSON_FROM_SERVER_NAME1 = "station_name";
    String GET_JSON_FROM_SERVER_NAME2 = "service_type";
    String GET_JSON_FROM_SERVER_NAME3 = "service_level";
    String GET_JSON_FROM_SERVER_NAME4 = "service_name";
    String GET_JSON_FROM_SERVER_NAME5 = "time_added";
    String GET_JSON_FROM_SERVER_NAME6 = "group_name";
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private String TAG="List.java";
    private Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = container.getContext();
        return inflater.inflate(R.layout.views_list, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        listModels = new ArrayList<>();
        recyclerView = view.findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        listAdapter = new ListAdapter(mContext, listModels);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setHasFixedSize(true);
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
        final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        String from = sdf1.format(today);
        Log.i(TAG, "data: " +from);

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        String to = sdf1.format(tomorrow);
        Log.i(TAG, "data: " +to);
        retrofit2.Call<ResponseBody> call = apiInterface.getList("ci_session="+id,to,from);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String result = prefs.getString("Breakdown",null);
                try {
                    if(response.body() != null) {
                        String res = response.body().string();
                        if (res.equals(result)) {

                        } else {
                            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.remove("Breakdown");
                            editor.putString("Breakdown", res);
                            editor.apply();
                            listModels = new ArrayList<>();
                            try {
                                listModels.clear();
                                Log.e(TAG, "onResponse222: "+res );
                                JSONArray array = new JSONArray(res);
                                for (int i = 0; i < array.length(); i++) {

                                    ListModel listModel = new ListModel();
                                    JSONObject json = null;
                                    try {
                                        json = array.getJSONObject(i);
                                        listModel.setStationName(json.getString(GET_JSON_FROM_SERVER_NAME1));
                                        listModel.setServiceType(json.getString(GET_JSON_FROM_SERVER_NAME2));
                                        listModel.setServiceLevel(json.getString(GET_JSON_FROM_SERVER_NAME3));
                                        listModel.setServiceName(json.getString(GET_JSON_FROM_SERVER_NAME4));
                                        listModel.setTimeAdded(json.getString(GET_JSON_FROM_SERVER_NAME5));
                                        listModel.setGroupName(json.getString(GET_JSON_FROM_SERVER_NAME6));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    listModels.add(listModel);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            listAdapter.setListModels(listModels);
                            listAdapter.notifyDataSetChanged();

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
        retrofit2.Call<ResponseBody> call = apiInterface.getList("ci_session="+id,"2019-09-02","2019-08-28");
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String result = prefs.getString("Breakdown",null);
                try {
                    if(response.body() != null) {
                        String res = response.body().string();
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.remove("Breakdown");
                        editor.putString("Breakdown", res);
                        editor.apply();
                        listModels = new ArrayList<>();
                        try {
                            listModels.clear();
                            Log.e(TAG, "onResponse222: "+res );
                            JSONArray array = new JSONArray(res);
                            for (int i = 0; i < array.length(); i++) {
                                ListModel listModel = new ListModel();
                                JSONObject json = null;
                                try {
                                    json = array.getJSONObject(i);
                                    listModel.setStationName(json.getString(GET_JSON_FROM_SERVER_NAME1));
                                    listModel.setServiceType(json.getString(GET_JSON_FROM_SERVER_NAME2));
                                    listModel.setServiceLevel(json.getString(GET_JSON_FROM_SERVER_NAME3));
                                    listModel.setServiceName(json.getString(GET_JSON_FROM_SERVER_NAME4));
                                    listModel.setTimeAdded(json.getString(GET_JSON_FROM_SERVER_NAME5));
                                    listModel.setGroupName(json.getString(GET_JSON_FROM_SERVER_NAME6));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                listModels.add(listModel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listAdapter.setListModels(listModels);
                        listAdapter.notifyDataSetChanged();
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
