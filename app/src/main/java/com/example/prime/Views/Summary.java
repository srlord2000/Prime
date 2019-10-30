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

import com.example.prime.Model.StationModel;
import com.example.prime.Model.StringModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.RecyclerAdapter.ControlAdapter;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Summary extends Fragment {

    public static Boolean running;
    public static Thread MyThread;
    private ArrayList<StationModel> stationModels;
    private ArrayList<String> strings;
    private String text1;
    private RecyclerView recyclerView;
    private ControlAdapter controlAdapter;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String GET_JSON_FROM_SERVER_NAME1 = "id";
    String GET_JSON_FROM_SERVER_NAME2 = "host_id";
    String GET_JSON_FROM_SERVER_NAME3 = "station_name";
    String GET_JSON_FROM_SERVER_NAME4 = "hostname";
    String GET_JSON_FROM_SERVER_NAME5 = "status";
    String GET_JSON_FROM_SERVER_NAME6 = "unit_id";
    String GET_JSON_FROM_SERVER_NAME7 = "unit_name";
    String GET_JSON_FROM_SERVER_NAME8 = "ipaddress";
    private HashSet<String> hashSet;
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private String TAG="Control.java";
    private Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = container.getContext();
        return inflater.inflate(R.layout.views_control, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        strings = new ArrayList<>();
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(mContext);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        id = sharedPrefsCookiePersistor.loadAll().get(0).value();
        data();


    }

    private void data(){
        retrofit2.Call<ResponseBody> call = apiInterface.getStation("ci_session="+id);
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
                                        strings.add(json.getString(GET_JSON_FROM_SERVER_NAME6));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                hashSet = new HashSet<String>(strings);
                                strings.clear();
                                strings.addAll(hashSet);
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
                Log.d(TAG, "onFailure: ");

            }
        });
    }

    private void str(){
        for (int i = 0; i < strings.size(); i++) {
            final String in;
            try {
                 in = strings.get(i).toString();

                 Log.e(TAG, "str: "+in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
