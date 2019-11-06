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
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.ConsumablesModel;
import com.example.prime.Model.ServicesInventoryModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.RecyclerAdapter.ConsumablesAdapter;
import com.example.prime.RecyclerAdapter.ServicesInventoryAdapter;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Services extends Fragment {

    public static Boolean running;
    public static Thread MyThread;
    private ArrayList<ServicesInventoryModel> servicesInventoryModels;
    private String text1;
    private RecyclerView recyclerView;
    private Button add, edit, delete;
    private ServicesInventoryAdapter servicesInventoryAdapter;
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
        return inflater.inflate(R.layout.views_service, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        servicesInventoryModels = new ArrayList<>();
        recyclerView = view.findViewById(R.id.listView);
        add = view.findViewById(R.id.btnServiceAdd);
        edit = view.findViewById(R.id.btnServiceEdit);
        delete = view.findViewById(R.id.btnServiceDelete);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        servicesInventoryAdapter = new ServicesInventoryAdapter(mContext, servicesInventoryModels);
        recyclerView.setAdapter(servicesInventoryAdapter);
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
                if (servicesInventoryAdapter.getSelected().size() > 0) {
                    for (int i = 0; i < servicesInventoryAdapter.getSelected().size(); i++) {
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("id", servicesInventoryAdapter.getSelected().get(i).getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestBody body = RequestBody.create(String.valueOf(obj), JSON);
//                        okhttp3.Request request = new okhttp3.Request.Builder()
//                                .url("http://192.168.0.100/inventory/services/remove")
//                                .addHeader("Cookie","ci_session="+id)
//                                .post(body)
//                                .build();
//
//                        client.newCall(request).enqueue(new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                call.cancel();
//                            }
//
//                            @Override
//                            public void onResponse(Call call, okhttp3.Response response) throws IOException {
//                                Log.d("TAG", response.body().string());
//                                if (getActivity() != null) {
//                                    View contextView = getActivity().findViewById(android.R.id.content);
//                                    Snackbar.make(contextView, "Deleted Successfully!", Snackbar.LENGTH_SHORT)
//                                            .show();
//                                }
//                            }
//                        });
                        retrofit2.Call<ResponseBody> call = apiInterface.postServiceRemove("ci_session="+id,body);
                        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
                            @Override
                            public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                                Log.e(TAG, "onResponse: "+response );
                                try {
                                    if(response.body()!= null) {
                                        String res = response.body().string();
                                        Log.e(TAG, "onResponse: "+res );
                                        JSONObject jsonObject = new JSONObject(res);
                                        if (jsonObject.getString("status").equals("0")) {
                                            if (getActivity() != null) {
                                                View contextView = getActivity().findViewById(android.R.id.content);
                                                Snackbar.make(contextView, "Deleted Successfully!", Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                                Log.d(TAG, "onFailure: ");
                            }
                        });
                        Log.e("", "onClick: " + sad);
                    }
                } else {
                    if (getActivity() != null) {
                        View contextView = getActivity().findViewById(android.R.id.content);
                        Snackbar.make(contextView, "No Selection!", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }
            }

        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                final View mView = getLayoutInflater().inflate(R.layout.serviceadddialog, null);
                final EditText item = mView.findViewById(R.id.serviceName);
                final EditText price = mView.findViewById(R.id.servicesPrice);
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
                        if (!item.getText().toString().isEmpty()) {
                            ArrayList<String> sad = new ArrayList<>();
                            StringBuilder stringBuilder = new StringBuilder();
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("name", item.getText().toString());
                                obj.put("price",price.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            stringBuilder.append("\n");
                            sad.add(obj.toString());
                            RequestBody body = RequestBody.create(String.valueOf(obj), JSON);
//                            okhttp3.Request request = new okhttp3.Request.Builder()
//                                    .url("http://192.168.0.100/inventory/services/add")
//                                    .addHeader("Cookie", "ci_session=" + id)
//                                    .post(body)
//                                    .build();
//
//                            client.newCall(request).enqueue(new Callback() {
//                                @Override
//                                public void onFailure(Call call, IOException e) {
//                                    call.cancel();
//                                }
//
//                                @Override
//                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
//                                    Log.d("TAG", response.body().string());
//                                    dialog.dismiss();
//                                }
//                            });
                            retrofit2.Call<ResponseBody> call = apiInterface.postServiceAdd("ci_session="+id,body);
                            call.enqueue(new  retrofit2.Callback<ResponseBody>() {
                                @Override
                                public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Log.e(TAG, "onResponse: "+response );
                                    try {
                                        if(response.body()!= null) {
                                            String res = response.body().string();
                                            Log.e(TAG, "onResponse: "+res );
                                            JSONObject jsonObject = new JSONObject(res);
                                            if (jsonObject.getString("status").equals("0")) {
                                                dialog.dismiss();
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                                    Log.d(TAG, "onFailure: ");
                                }
                            });
                            Log.e("", "onClick: " + sad);
                            Log.e("", "onClick: " + sad);



                        } else {
                            if (getActivity() != null) {
                                View contextView = getActivity().findViewById(android.R.id.content);
                                Snackbar.make(contextView, "Complete Details!", Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        }
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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(servicesInventoryAdapter.getSelected().size() != 1){
                    if (getActivity() != null) {
                        View contextView = getActivity().findViewById(android.R.id.content);
                        Snackbar.make(contextView, "1 Selection Only!", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }else {
                    for (int i = 0; i < servicesInventoryAdapter.getSelected().size(); i++) {
                        final String ids, name, prices;
                        name = servicesInventoryAdapter.getSelected().get(i).getServiceName();
                        ids = servicesInventoryAdapter.getSelected().get(i).getId();
                        prices = servicesInventoryAdapter.getSelected().get(i).getPrice();

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                        final View mView = getLayoutInflater().inflate(R.layout.serviceseditdialog, null);
                        final EditText item = mView.findViewById(R.id.serviceName);
                        final EditText price = mView.findViewById(R.id.servicesPrice);
                        Button submit = mView.findViewById(R.id.dialog_submit);
                        Button close = mView.findViewById(R.id.dialog_close);
                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        if (dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                        }
                        item.setText(name);
                        price.setText(prices);

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                OkHttpClient client = new OkHttpClient();
                                JSONObject userJson = new JSONObject();
                                try {
                                    userJson.put("id", ids);
                                    userJson.put("name", item.getText().toString());
                                    userJson.put("price", price.getText().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.e("", "onClick: " + userJson);

                                RequestBody body = RequestBody.create(String.valueOf(userJson), JSON);

//                                okhttp3.Request request = new okhttp3.Request.Builder()
//                                        .url("http://192.168.0.100/inventory/services/edit")
//                                        .addHeader("Cookie", "ci_session=" + id)
//                                        .post(body)
//                                        .build();
//
//                                client.newCall(request).enqueue(new Callback() {
//                                    @Override
//                                    public void onFailure(Call call, IOException e) {
//                                        call.cancel();
//                                    }
//
//                                    @Override
//                                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
//                                        Log.d("TAG", response.body().toString());
//                                        dialog.dismiss();
//
//                                    }
//                                });
                                retrofit2.Call<ResponseBody> call = apiInterface.postServiceEdit("ci_session="+id,body);
                                call.enqueue(new  retrofit2.Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Log.e(TAG, "onResponse: "+response );
                                        try {
                                            if(response.body()!= null) {
                                                String res = response.body().string();
                                                Log.e(TAG, "onResponse: "+res );
                                                JSONObject jsonObject = new JSONObject(res);
                                                if (jsonObject.getString("status").equals("0")) {
                                                    dialog.dismiss();
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    @Override
                                    public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                                        Log.d(TAG, "onFailure: ");
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });

    }

    private void data(){
        retrofit2.Call<ResponseBody> call = apiInterface.getServicesItem("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String result = prefs.getString("ServicesInventory",null);
                try {
                    if(response.body() != null) {
                        String res = response.body().string();
                        if (res.equals(result)) {

                        } else {
                            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.remove("ServicesInventory");
                            editor.putString("ServicesInventory", res);
                            editor.apply();
                            servicesInventoryModels = new ArrayList<>();
                            try {
                                servicesInventoryModels.clear();
                                Log.e(TAG, "onResponse222: "+res );
                                JSONArray array = new JSONArray(res);
                                for (int i = 0; i < array.length(); i++) {

                                    ServicesInventoryModel servicesInventoryModel = new ServicesInventoryModel();
                                    JSONObject json = null;
                                    try {
                                        json = array.getJSONObject(i);
                                        servicesInventoryModel.setId(json.getString("id"));
                                        servicesInventoryModel.setServiceName(json.getString("service_name"));
                                        servicesInventoryModel.setPrice(json.getString("price"));
                                        servicesInventoryModel.setTimeAdded(json.getString("time_added"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    servicesInventoryModels.add(servicesInventoryModel);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            servicesInventoryAdapter.setServices(servicesInventoryModels);
                            servicesInventoryAdapter.notifyDataSetChanged();
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
        retrofit2.Call<ResponseBody> call = apiInterface.getServicesItem("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String result = prefs.getString("ServicesInventory",null);
                try {
                    if(response.body() != null) {
                        String res = response.body().string();
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.remove("ServicesInventory");
                        editor.putString("ServicesInventory", res);
                        editor.apply();
                        servicesInventoryModels = new ArrayList<>();
                        try {
                            servicesInventoryModels.clear();
                            Log.e(TAG, "onResponse222: "+res );
                            JSONArray array = new JSONArray(res);
                            for (int i = 0; i < array.length(); i++) {

                                ServicesInventoryModel servicesInventoryModel = new ServicesInventoryModel();
                                JSONObject json = null;
                                try {
                                    json = array.getJSONObject(i);
                                    servicesInventoryModel.setId(json.getString("id"));
                                    servicesInventoryModel.setServiceName(json.getString("service_name"));
                                    servicesInventoryModel.setPrice(json.getString("price"));
                                    servicesInventoryModel.setTimeAdded(json.getString("time_added"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                servicesInventoryModels.add(servicesInventoryModel);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        servicesInventoryAdapter.setServices(servicesInventoryModels);
                        servicesInventoryAdapter.notifyDataSetChanged();
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
