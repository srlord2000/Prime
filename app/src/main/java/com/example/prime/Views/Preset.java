package com.example.prime.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.prime.AddPreset;
import com.example.prime.AdvancedOption;
import com.example.prime.Model.CardsModel;
import com.example.prime.Model.EditPriceModel;
import com.example.prime.Model.ServiceModel;
import com.example.prime.Model.UnitModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.RecyclerAdapter.CardAdapter;
import com.example.prime.RecyclerAdapter.ChildAdapter;
import com.example.prime.RecyclerAdapter.EditPriceAdapter;
import com.example.prime.RecyclerAdapter.PresetAdapter;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

public class Preset extends Fragment implements PresetAdapter.AdapterClickListener{

    public static Boolean running1;
    public static Thread MyThread1;
    public static Boolean running;
    public static Thread MyThread;
    private RecyclerView recyclerView;
    public static ArrayList<EditPriceModel> editPrices;
    private Button add, edit, delete;
    private String nameselect,nameid,groupid;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private String TAG = "Card.java";
    private ArrayList<ServiceModel> childDataItems;
    private ArrayList<UnitModel> arrDummyData = new ArrayList<>();
    private ArrayList<String> data = new ArrayList<>();
    EditPriceAdapter editPriceAdapter;
    PresetAdapter presetAdapter;
    SharedPreferences prefs ;
    SharedPreferences.Editor editor;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = container.getContext();
        return inflater.inflate(R.layout.views_preset, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        nameselect = "";
        nameid = "";
        groupid = "";
        childDataItems = new ArrayList<>();
        recyclerView = view.findViewById(R.id.listView);
        presetAdapter = new PresetAdapter(mContext,arrDummyData);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(presetAdapter);
        recyclerView.setHasFixedSize(true);
        add = view.findViewById(R.id.btnAddPreset);
        edit = view.findViewById(R.id.btnEditPrice);
        prefs = PreferenceManager.getDefaultSharedPreferences( mContext);
        editor = prefs.edit();
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(mContext);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        id = sharedPrefsCookiePersistor.loadAll().get(0).value();
        load();
        load1();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPrices = new ArrayList<>();
                editPriceAdapter = new EditPriceAdapter(mContext ,editPrices);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = getLayoutInflater().inflate(R.layout.editprice, null);
                RecyclerView recyclerView = mView.findViewById(R.id.recycler);
                Button submit = mView.findViewById(R.id.btnSubmit);
                Button advanced = mView.findViewById(R.id.btnAdvanced);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(editPriceAdapter);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                if(!nameselect.equals("")){
                    dialog.show();
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                    advanced.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity().getBaseContext(),
                                    AdvancedOption.class);
                            intent.putExtra("Key", nameselect);
                            intent.putExtra("Id", nameid);
                            intent.putExtra("GroupId", groupid);
                            getActivity().startActivity(intent);
                        }
                    });

                    String json = prefs.getString(nameselect, "");
                    Log.i("TAGedit", String.valueOf(json));
                    editPrices = new ArrayList<>();
                    try {
                        editPrices.clear();
                        JSONArray array = new JSONArray(json);
                        for (int i = 0; i < array.length(); i++) {
                            EditPriceModel editPrice = new EditPriceModel();
                            JSONObject json1 = null;
                            try {
                                json1 = array.getJSONObject(i);
                                editPrice.setPrice(json1.getString("price"));
                                editPrice.setName(json1.getString("service_name"));
                                editPrice.setId(json1.getString("id"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            editPrices.add(editPrice);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editPriceAdapter.setPrice(editPrices);
                    editPriceAdapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(mContext, "No Selection!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(),
                        AddPreset.class);
                getActivity().startActivity(intent);
            }
        });
        presetAdapter.setOnItemClickListener(this);
        running1 = true;
        MyThread1 = new Thread() {//create thread
            @Override
            public void run() {
                int i=0;
                while(running1){
                    System.out.println("counter: "+i);
                    i++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Sleep interrupted");
                    }
                    check();
                    services();
                    //check1();
                }
                System.out.println("onEnd Thread");
            }
        };
        MyThread1.start();


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
                    editor.putString("Preset",responseData);
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
                                    editor.remove(name);
                                    editor.putString(name, res);
                                    editor.commit();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.e("", "sharedtagtry: "+prefs.getString(name, ""));
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
        retrofit2.Call<ResponseBody> call = apiInterface.getUnits("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("TAG", response.headers().toString());
                try {
                    String responseData = response.body().string();
                    Log.e("TAG", responseData);
                    childDataItems.clear();
                    arrDummyData.clear();
                    JSONArray object = new JSONArray(responseData);
                    for(int i = 0; i<object.length();i++){
                        childDataItems = new ArrayList<>();
                        JSONObject jsonObject = object.getJSONObject(i);
                        final String id = jsonObject.getString("id");
                        String name = jsonObject.getString("unit_name");
                        String group = jsonObject.getString("group_id");
                        String time = jsonObject.getString("time_added");
                        String data1 = prefs.getString(name,"");
                        JSONArray jsonArray = new JSONArray(data1);
                        UnitModel parent = new UnitModel();
                        for(int j = 0; j<jsonArray.length();j++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            childDataItems.add(new ServiceModel(jsonObject1.getString("id"),jsonObject1.getString("service_name"),jsonObject1.getString("service_type"),jsonObject1.getString("service_level"),jsonObject1.getString("price"),jsonObject1.getString("tap_pulse"),jsonObject1.getString("time_added"),jsonObject1.getString("unit_name")));
                            parent.setUnitName(name);
                            parent.setId(id);
                            parent.setGroupId(group);
                            parent.setTimeAdded(time);
                            parent.setServiceModels(childDataItems);
                            String s = jsonObject1.getString("unit_name")+jsonObject1.getString("service_type")+jsonObject1.getString("service_name");

                        }

                        arrDummyData.add(parent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                presetAdapter.setPreset(arrDummyData);
                presetAdapter.notifyDataSetChanged();
            }

            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailuretagtrt: ");

            }
        });
    }

    private void services(){
        retrofit2.Call<ResponseBody> call = apiInterface.getServices("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("TAG", response.headers().toString());
                try {
                    String responseData = response.body().string();
                    Log.e("services", responseData);
                    if(responseData.equals(prefs.getString("Services", ""))){

                    }else {
                        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        final SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putString("Services",responseData);
                        editor.apply();
                        running = true;
                        MyThread = new Thread() {//create thread
                            @Override
                            public void run() {
                                int i=0;
                                while(running && i <= 3){
                                    System.out.println("counter231: "+i);
                                    i++;
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        System.out.println("Sleep interrupted");
                                    }
                                    load();
                                    load1();

                                }
                                System.out.println("onEnd Thread");
                            }
                        };
                        MyThread.start();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailuretagtrt: ");

            }
        });
    }

    private void check(){
        retrofit2.Call<ResponseBody> call = apiInterface.getUnits("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("TAG", response.headers().toString());
                try {
                    String responseData = response.body().string();
                    String preset = prefs.getString("Preset", "");
                    Log.e("checkpreset", responseData);
                    Log.e(TAG, "presershared: "+preset );
                    if(preset.equals(responseData)){

                    }else{
                        services();
                        load();
                        load1();
//                        running = true;
//                        MyThread = new Thread() {//create thread
//                            @Override
//                            public void run() {
//                                int i=0;
//                                while(running){
//                                    System.out.println("counter231: "+i);
//                                    i++;
//                                    try {
//                                        Thread.sleep(1000);
//                                    } catch (InterruptedException e) {
//                                        System.out.println("Sleep interrupted");
//                                    }
//                                    load();
//                                    load1();
//                                    if(i == 3){
//                                        MyThread.interrupt();
//                                    }
//
//                                }
//                                System.out.println("onEnd Thread");
//                            }
//                        };
//                        MyThread.start();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailuretagtrt: ");

            }
        });
    }

    private void check1(){
        retrofit2.Call<ResponseBody> call = apiInterface.getServices("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("TAG", response.headers().toString());
                try {
                    String responseData = response.body().string();
                    String preset = prefs.getString("Services", "");
                    if(preset.equals(responseData)){
                    }else{
                        services();
                        load();
                        load1();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailuretagtry: ");

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        presetAdapter.selected(position);
        Drawable background = Objects.requireNonNull(recyclerView.findViewHolderForLayoutPosition(position)).itemView.getBackground();
        if (((ColorDrawable) background).getColor() == Color.parseColor("#707070")) {
            nameselect = arrDummyData.get(position).getUnitName();
            nameid = arrDummyData.get(position).getId();
            groupid = arrDummyData.get(position).getGroupId();
            Log.e("", "onItemClick: " + arrDummyData.get(position).getUnitName());

        } else {
            nameselect = "";
            nameid = "";
            groupid = "";
            Log.e("", "onItemClick: sad");
        }
    }
}
