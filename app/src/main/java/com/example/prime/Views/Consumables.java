package com.example.prime.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.ConsumablesModel;
import com.example.prime.Model.InventoryModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.RecyclerAdapter.ConsumablesAdapter;
import com.example.prime.RecyclerAdapter.InventoryAdapter;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.ResponseBody;

public class Consumables extends Fragment {
    public static Boolean running;
    public static Thread MyThread;
    private ArrayList<ConsumablesModel> consumablesModels;
    private String text1;
    private RecyclerView recyclerView;
    private Button add, edit, delete;
    private ConsumablesAdapter consumablesAdapter;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private String TAG = "Inventory.java";
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = container.getContext();
        return inflater.inflate(R.layout.views_consumable, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        consumablesModels = new ArrayList<>();
        recyclerView = view.findViewById(R.id.listView);
        add = view.findViewById(R.id.btnConsumablesAdd);
        edit = view.findViewById(R.id.btnConsumablesEdit);
        delete = view.findViewById(R.id.btnConsumablesDelete);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        consumablesAdapter = new ConsumablesAdapter(mContext, consumablesModels);
        recyclerView.setAdapter(consumablesAdapter);
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(mContext);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        id = sharedPrefsCookiePersistor.loadAll().get(0).value();
        data1();

        running = true;
        MyThread = new Thread() {//create thread
            @Override
            public void run() {
                int i = 0;
                while (running) {
                    System.out.println("counter: " + i);
                    i++;
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        System.out.println("Sleep interrupted");
                    }
                    data();
                }
                System.out.println("onEnd Thread");
            }
        };
        MyThread.start();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                final View mView = getLayoutInflater().inflate(R.layout.addconsumablesdialog, null);
                final EditText item = mView.findViewById(R.id.consumablesItem);
                final EditText price = mView.findViewById(R.id.consumablesPrice);
                Button submit = mView.findViewById(R.id.dialog_submit);
                Button close = mView.findViewById(R.id.dialog_close);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private void data(){
        retrofit2.Call<ResponseBody> call = apiInterface.getConsumables("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String result = prefs.getString("Consumables",null);
                try {
                    if(response.body() != null) {
                        String res = response.body().string();
                        if (res.equals(result)) {

                        } else {
                            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.remove("Consumables");
                            editor.putString("Consumables", res);
                            editor.apply();
                            consumablesModels = new ArrayList<>();
                            try {
                                consumablesModels.clear();
                                Log.e(TAG, "onResponse222: "+res );
                                JSONArray array = new JSONArray(res);
                                for (int i = 0; i < array.length(); i++) {

                                    ConsumablesModel consumablesModel = new ConsumablesModel();
                                    JSONObject json = null;
                                    try {
                                        json = array.getJSONObject(i);
                                        consumablesModel.setId(json.getString("id"));
                                        consumablesModel.setItemName(json.getString("item_name"));
                                        consumablesModel.setPrice(json.getString("price"));
                                        consumablesModel.setTimeAdded(json.getString("time_added"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    consumablesModels.add(consumablesModel);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            consumablesAdapter.setConsumables(consumablesModels);
                            consumablesAdapter.notifyDataSetChanged();

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
        retrofit2.Call<ResponseBody> call = apiInterface.getConsumables("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String result = prefs.getString("Consumables",null);
                try {
                    if(response.body() != null) {
                        String res = response.body().string();
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.remove("Consumables");
                        editor.putString("Consumables", res);
                        editor.apply();
                        consumablesModels = new ArrayList<>();
                        try {
                            consumablesModels.clear();
                            Log.e(TAG, "onResponse222: "+res );
                            JSONArray array = new JSONArray(res);
                            for (int i = 0; i < array.length(); i++) {

                                ConsumablesModel consumablesModel = new ConsumablesModel();
                                JSONObject json = null;
                                try {
                                    json = array.getJSONObject(i);
                                    consumablesModel.setId(json.getString("id"));
                                    consumablesModel.setItemName(json.getString("item_name"));
                                    consumablesModel.setPrice(json.getString("price"));
                                    consumablesModel.setTimeAdded(json.getString("time_added"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                consumablesModels.add(consumablesModel);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        consumablesAdapter.setConsumables(consumablesModels);
                        consumablesAdapter.notifyDataSetChanged();
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
