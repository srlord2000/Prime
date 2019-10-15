package com.example.prime.Views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prime.Model.CardsModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.RecyclerAdapter.CardAdapter;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static com.android.volley.VolleyLog.TAG;

public class Card extends Fragment {

    public static Boolean running;
    public static Thread MyThread;
    public static Boolean running1;
    public static Thread MyThread1;
    private ArrayList<CardsModel> cardList;
    private String text1;
    private RecyclerView recyclerView;
    private Button scan, delete, push;
    private CardAdapter cardAdapter;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String GET_JSON_FROM_SERVER_NAME1 = "id";
    String GET_JSON_FROM_SERVER_NAME2 = "service_type";
    String GET_JSON_FROM_SERVER_NAME3 = "service_level";
    String GET_JSON_FROM_SERVER_NAME4 = "time_added";
    JsonArrayRequest jsonArrayRequest;
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private String TAG = "Card.java";

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = container.getContext();
        return inflater.inflate(R.layout.views_card, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        cardList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.listView);
        scan = view.findViewById(R.id.scan);
        delete = view.findViewById(R.id.btnCardRemove);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        cardAdapter = new CardAdapter(mContext, cardList);
        recyclerView.setAdapter(cardAdapter);
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(mContext);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        id = sharedPrefsCookiePersistor.loadAll().get(0).value();
        data1();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> sad = new ArrayList<>();
                if (cardAdapter.getSelected().size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < cardAdapter.getSelected().size(); i++) {
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("id", cardAdapter.getSelected().get(i).getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        stringBuilder.append(cardAdapter.getSelected().get(i).getId());
                        stringBuilder.append("\n");
                        sad.add(obj.toString());

                        OkHttpClient client = new OkHttpClient();

                        RequestBody body = RequestBody.create(String.valueOf(obj), JSON);

                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url("http://192.168.0.100/cards/remove")
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
                                Log.d("TAG", response.body().string());
                            }
                        });
                        Log.e("", "onClick: " + sad);
                    }
                } else {
                    Toast.makeText(mContext, "No Selection", Toast.LENGTH_SHORT).show();
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

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = getLayoutInflater().inflate(R.layout.cardscandialog, null);
                final LinearLayout registered, unregistered, scan, retry;
                registered = mView.findViewById(R.id.registeredLayout);
                unregistered = mView.findViewById(R.id.unregisteredLayout);
                scan = mView.findViewById(R.id.scan);
                retry = mView.findViewById(R.id.retry);
                final TextView card = mView.findViewById(R.id.cardId);
                final TextView card1 = mView.findViewById(R.id.cardId1);
                final TextView level = mView.findViewById(R.id.serviceLevel);
                final TextView time = mView.findViewById(R.id.timeAdded);
                final Spinner type = mView.findViewById(R.id.serviceType);
                final EditText level1 = mView.findViewById(R.id.serviceLevel1);
                final TextInputLayout box = mView.findViewById(R.id.serviceLevelBox);
                Button submit = mView.findViewById(R.id.btnSubmit);
                Button remove = mView.findViewById(R.id.btnRemove);
                Button closeReg = mView.findViewById(R.id.btnCancel);
                Button closeUnReg = mView.findViewById(R.id.btnCancel1);
                Button cancel = mView.findViewById(R.id.btnClose1);
                Button btnRetry = mView.findViewById(R.id.buttonRetry);
                Button btnClose = mView.findViewById(R.id.btnClose3);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

//                dialog.setOnKeyListener(new Dialog.OnKeyListener() {
//
//                    @Override
//                    public boolean onKey(DialogInterface arg0, int keyCode,
//                                         KeyEvent event) {
//                        // TODO Auto-generated method stub
//                        if (keyCode == KeyEvent.KEYCODE_BACK) {
//                            running1 = false;
//                            MyThread1.interrupt();
//                            dialog.dismiss();
//                        }
//                        return true;
//                    }
//                });

                closeReg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        running1 = false;
//                        MyThread1.interrupt();
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        running1 = false;
//                        MyThread1.interrupt();
                        dialog.dismiss();
                    }
                });

                closeUnReg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        running1 = false;
//                        MyThread1.interrupt();
                        dialog.dismiss();
                    }
                });

                type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String text = (String) adapterView.getItemAtPosition(i);
                        text1 = text;
                        if (text.equals("Abort")) {
                            level1.setText("0");
                            level1.setVisibility(View.GONE);
                            box.setVisibility(View.GONE);
                        } else {
                            level1.setText("");
                            level1.setVisibility(View.VISIBLE);
                            box.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText
                                (mContext, "Selected : " + text, Toast.LENGTH_SHORT)
                                .show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
//                        running1 = false;
//                        MyThread1.interrupt();
                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
//                        running1 = false;
//                        MyThread1.interrupt();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!level1.getText().toString().isEmpty()) {
                            type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    final String text = (String) adapterView.getItemAtPosition(i);
                                    text1 = text;
                                    if (text.equals("Abort")) {
                                        level1.setText("0");
                                        level1.setVisibility(View.GONE);
                                    } else {
                                        level1.setText("");
                                        level1.setVisibility(View.VISIBLE);
                                    }
                                    Toast.makeText(mContext, text1, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            text1 = type.getSelectedItem().toString();
                            String card11, level11;
                            card11 = card1.getText().toString();
                            level11 = level1.getText().toString();
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("id", card11);
                                obj.put("type", text1);
                                obj.put("level", level11);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(mContext, text1, Toast.LENGTH_SHORT).show();

                            OkHttpClient client = new OkHttpClient();


                            RequestBody body = RequestBody.create(String.valueOf(obj), JSON);

                            okhttp3.Request request = new okhttp3.Request.Builder()
                                    .url("http://192.168.0.100/cards/add")
                                    .post(body)
                                    .addHeader("Cookie", "ci_session=" + id)
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    call.cancel();
                                }

                                @Override
                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                    Log.d("TAG", response.body().string());
                                }
                            });

                            Toast.makeText(mContext,
                                    text1,
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();


                        } else {
                            Toast.makeText(mContext,
                                    R.string.error_msg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!card.getText().toString().isEmpty()) {
                            JSONObject obj = new JSONObject();
                            JSONArray jsonObject = new JSONArray();
                            try {
                                jsonObject.put(card.getText().toString());
                                obj.put("id",jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.e(TAG, "onClick: "+jsonObject );

                            retrofit2.Call<ResponseBody> call = apiInterface.deleteCard("ci_session="+id, jsonObject);
                            call.enqueue(new  retrofit2.Callback<ResponseBody>() {
                                @Override
                                public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                    try {
                                        Log.e(TAG, "remove: "+response.body().string() );
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                                    Log.d(TAG, "onFailure: ");
                                }
                            });

                            Toast.makeText(mContext,
                                    R.string.success_msg,
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();


                        } else {
                            Toast.makeText(mContext,
                                    R.string.error_msg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                    final retrofit2.Call<ResponseBody> call = apiInterface.cardScan("ci_session=" + id);
                    call.enqueue(new retrofit2.Callback<ResponseBody>() {
                        @Override
                        public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            try {
                                String res = response.body().string();
                                Log.e(TAG, "onResponse: "+res );

                                try {
                                    if (!res.contains("Could not read server response")){
                                        JSONObject jsonObject = new JSONObject(res);
                                        if (!jsonObject.getString("service_type").equals("")) {

                                            if (jsonObject.getString("service_type").equals("Unregistered")) {
                                                scan.setVisibility(View.GONE);
                                                unregistered.setVisibility(View.VISIBLE);
                                                card1.setText(jsonObject.getString("id"));
                                                Toast.makeText(mContext, text1, Toast.LENGTH_SHORT).show();

                                            } else {
                                                scan.setVisibility(View.GONE);
                                                registered.setVisibility(View.VISIBLE);
                                                card.setText(jsonObject.getString("id"));
                                                level.setText(jsonObject.getString("service_level"));
                                                time.setText(jsonObject.getString("time_added"));

                                            }
                                        }
                                    }else if (res.contains("Could not read server response")){
                                        scan.setVisibility(View.GONE);
                                        retry.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                            Log.d(TAG, "onFailure: ");

                        }
                    });


                btnRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        scan.setVisibility(View.VISIBLE);
                        retry.setVisibility(View.GONE);

                        call.clone().enqueue(new retrofit2.Callback<ResponseBody>() {
                            @Override
                            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                try {
                                    String res = response.body().string();
                                    Log.e(TAG, "onResponse: "+res );

                                    try {
                                        if (!res.contains("Could not read server response")){
                                            JSONObject jsonObject = new JSONObject(res);
                                            if (!jsonObject.getString("service_type").equals("")) {
                                                if (jsonObject.getString("service_type").equals("Unregistered")) {
                                                    scan.setVisibility(View.GONE);
                                                    unregistered.setVisibility(View.VISIBLE);
                                                    card1.setText(jsonObject.getString("id"));
                                                    Toast.makeText(mContext, text1, Toast.LENGTH_SHORT).show();

                                                } else {
                                                    scan.setVisibility(View.GONE);
                                                    registered.setVisibility(View.VISIBLE);
                                                    card.setText(jsonObject.getString("id"));
                                                    level.setText(jsonObject.getString("service_level"));
                                                    time.setText(jsonObject.getString("time_added"));

                                                }
                                            }
                                        }else if (res.contains("Could not read server response")){
                                            scan.setVisibility(View.GONE);
                                            retry.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                                Log.d(TAG, "onFailure: ");

                            }
                        });


                    }
                });

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        running1 = false;
                        MyThread1.interrupt();
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void data(){
        retrofit2.Call<ResponseBody> call = apiInterface.getCards("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String result = prefs.getString("Card",null);
                    try {
                        if(response.body() != null) {
                            String res = response.body().string();
                            if (res.equals(result)) {

                            } else {
                                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                                SharedPreferences.Editor editor = sharedPrefs.edit();
                                editor.remove("Card");
                                editor.putString("Card", res);
                                editor.apply();
                                cardList = new ArrayList<>();
                                try {
                                    cardList.clear();
                                    Log.e(TAG, "onResponse222: "+res );
                                    JSONArray array = new JSONArray(res);
                                    for (int i = 0; i < array.length(); i++) {

                                        CardsModel GetDataAdapter3 = new CardsModel();
                                        JSONObject json = null;
                                        try {
                                            json = array.getJSONObject(i);
                                            GetDataAdapter3.setId(json.getString(GET_JSON_FROM_SERVER_NAME1));
                                            GetDataAdapter3.setServiceType(json.getString(GET_JSON_FROM_SERVER_NAME2));
                                            GetDataAdapter3.setServiceLevel(json.getString(GET_JSON_FROM_SERVER_NAME3));
                                            GetDataAdapter3.setTimeAdded(json.getString(GET_JSON_FROM_SERVER_NAME4));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        cardList.add(GetDataAdapter3);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                cardAdapter.setCards(cardList);
                                cardAdapter.notifyDataSetChanged();

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
        retrofit2.Call<ResponseBody> call = apiInterface.getCards("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String result = prefs.getString("Card",null);
                try {
                    if(response.body() != null) {
                        String res = response.body().string();
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.remove("Card");
                        editor.putString("Card", res);
                        editor.apply();
                        cardList = new ArrayList<>();
                        try {
                            cardList.clear();
                            Log.e(TAG, "onResponse222: "+res );
                            JSONArray array = new JSONArray(res);
                            for (int i = 0; i < array.length(); i++) {
                                CardsModel GetDataAdapter3 = new CardsModel();
                                JSONObject json = null;
                                try {
                                    json = array.getJSONObject(i);
                                    GetDataAdapter3.setId(json.getString(GET_JSON_FROM_SERVER_NAME1));
                                    GetDataAdapter3.setServiceType(json.getString(GET_JSON_FROM_SERVER_NAME2));
                                    GetDataAdapter3.setServiceLevel(json.getString(GET_JSON_FROM_SERVER_NAME3));
                                    GetDataAdapter3.setTimeAdded(json.getString(GET_JSON_FROM_SERVER_NAME4));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                cardList.add(GetDataAdapter3);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cardAdapter.setCards(cardList);
                        cardAdapter.notifyDataSetChanged();
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
