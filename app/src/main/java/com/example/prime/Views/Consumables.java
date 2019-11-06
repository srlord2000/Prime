package com.example.prime.Views;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.ConsumablesModel;
import com.example.prime.Model.EditPriceModel;
import com.example.prime.Model.InventoryModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.RecyclerAdapter.ConsumablesAdapter;
import com.example.prime.RecyclerAdapter.InventoryAdapter;
import com.example.prime.RecyclerAdapter.PresetAdapter;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;
import com.google.android.material.snackbar.Snackbar;

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
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Consumables extends Fragment{
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


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ArrayList<String> sad = new ArrayList<>();
                if (consumablesAdapter.getSelected().size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < consumablesAdapter.getSelected().size(); i++) {
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("id", consumablesAdapter.getSelected().get(i).getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        stringBuilder.append(consumablesAdapter.getSelected().get(i).getId());
                        stringBuilder.append("\n");
                        sad.add(obj.toString());

                        RequestBody body = RequestBody.create(String.valueOf(obj), JSON);
//                        okhttp3.Request request = new okhttp3.Request.Builder()
//                                .url("http://192.168.0.100/inventory/item/remove")
//                                .addHeader("Cookie","ci_session="+id)
//                                .post(body)
//                                .build();
//                        client.newCall(request).enqueue(new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                call.cancel();
//                            }
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
                        retrofit2.Call<ResponseBody> call = apiInterface.postItemRemove("ci_session="+id,body);
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
//                                    .url("http://192.168.0.100/inventory/item/add")
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

                            retrofit2.Call<ResponseBody> call = apiInterface.postItemAdd("ci_session="+id,body);
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
                if (consumablesAdapter.getSelected().size() != 1) {
                    if (getActivity() != null) {
                        View contextView = getActivity().findViewById(android.R.id.content);
                        Snackbar.make(contextView, "1 Selection Only!", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    for (int i = 0; i < consumablesAdapter.getSelected().size(); i++) {
                        final String ids, name, prices;
                        name = consumablesAdapter.getSelected().get(i).getItemName();
                        ids = consumablesAdapter.getSelected().get(i).getId();
                        prices = consumablesAdapter.getSelected().get(i).getPrice();

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                        final View mView = getLayoutInflater().inflate(R.layout.editconsumablesdialog, null);
                        final EditText item = mView.findViewById(R.id.consumablesItem);
                        final EditText price = mView.findViewById(R.id.consumablesPrice);
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
//                                        .url("http://192.168.0.100/inventory/item/edit")
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
                                retrofit2.Call<ResponseBody> call = apiInterface.postItemEdit("ci_session="+id,body);
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
