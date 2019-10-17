package com.example.prime;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.prime.Model.AddDataModel;
import com.example.prime.Model.GroupModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.RecyclerAdapter.AddDataAdapter;
import com.example.prime.RecyclerAdapter.GroupAdapter;
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

public class AdvancedOption extends AppCompatActivity {
    private String name,name2;
    private Button add,bypass,submit;
    private RecyclerView recyclerView;
    private ArrayList<AddDataModel> adds;
    private ArrayList<String> set;
    private AddDataAdapter addAdapter;
    private String TAG;
    private Spinner spinner;
    private String name1,id1,groupid;
    private EditText text;
    protected List<GroupModel> spinnerData;
    SharedPreferences prefs ;
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_option);
        Intent intent = getIntent();
        recyclerView = findViewById(R.id.recycler);
        add = findViewById(R.id.btnAdd);
        bypass = findViewById(R.id.btnBypass);
        submit = findViewById(R.id.btnSubmit);
        spinner = findViewById(R.id.spinner);
        text = findViewById(R.id.txtName);
        adds = new ArrayList<>();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        addAdapter = new AddDataAdapter(AdvancedOption.this,adds);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdvancedOption.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(addAdapter);
        name1 = intent.getStringExtra("Key");
        id1 = intent.getStringExtra("Id");
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(this);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        id = sharedPrefsCookiePersistor.loadAll().get(0).value();
        groupid = intent.getStringExtra("GroupId");

        if(!name1.equals("")){
            data();
            text.setText(name1);


        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int id = adapterView.getSelectedItemPosition();
                name = spinnerData.get(id).getId();
                Log.e(TAG, "onNothingSelected: "+name );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                int id = adapterView.getFirstVisiblePosition();
                name = spinnerData.get(id).getId();
                Log.e(TAG, "onNothingSelected: "+name );

            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList e = addAdapter.getAll();
                set = new ArrayList<>();
                JSONArray array = new JSONArray();

                Log.e(TAG, "onClick: "+id1 );
                try {
                    for(int i = 0; i < e.size(); i++) {
                        String name = adds.get(i).getName();
                        String type = adds.get(i).getType();
                        String level = adds.get(i).getLevel();
                        String price = adds.get(i).getPrice();
                        String pulse = adds.get(i).getPulse();
                        JSONObject cust= new JSONObject();
                        cust.put("service_name",name);
                        cust.put("service_type",type);
                        cust.put("service_level",level);
                        cust.put("price",price);
                        cust.put("tap_pulse",pulse);
                        array.put(cust);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                JSONObject userJson = new JSONObject();
                try {
                    userJson.put("unit_name",text.getText().toString());
                    userJson.put("group_id",name);
                    userJson.put("unit_id",id1);
                    userJson.put("services",array);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                Log.e(TAG, "onClick: "+userJson);
                Log.e(TAG, "onClick: "+name );
                OkHttpClient client = new OkHttpClient();


                RequestBody body = RequestBody.create(String.valueOf(userJson), JSON);

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("http://192.168.0.100/units/edit")
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


            }
        });

//        remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                adds.clear();
//                addAdapter.notifyDataSetChanged();
//
//            }
//        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AdvancedOption.this);
                View mView = getLayoutInflater().inflate(R.layout.addnewpresetdialog, null);
                final EditText name = mView.findViewById(R.id.etServiceName);
                final Spinner type = mView.findViewById(R.id.etServiceType);
                final EditText level = mView.findViewById(R.id.etLevel);
                final EditText price = mView.findViewById(R.id.etPrice);
                final EditText pulse = mView.findViewById(R.id.etPulse);
                Button submit = mView.findViewById(R.id.btnSubmit);

                type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        int id = adapterView.getSelectedItemPosition();
                        name2 = adapterView.getSelectedItem().toString();
                        Log.e(TAG, "onNothingSelected: "+name2 );
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        int id = adapterView.getFirstVisiblePosition();
                        name2 = adapterView.getSelectedItem().toString();
                        Log.e(TAG, "onNothingSelected: "+name2 );

                    }
                });

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddDataModel add = new AddDataModel();
                        add.setName(name.getText().toString());
                        add.setType(name2);
                        add.setLevel(level.getText().toString());
                        add.setPrice(price.getText().toString());
                        add.setPulse(pulse.getText().toString());
                        adds.add(add);
                        addAdapter.notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });
            }
        });
        retrofit2.Call<ResponseBody> call = apiInterface.getGroup("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {

                JSONArray array = null;
                try {
                    String responseData = response.body().string();
                    spinnerData = new ArrayList<>();
                    array = new JSONArray(responseData);
                    for(int i = 0; i<array.length(); i++) {
                        GroupModel dataObject = new GroupModel();
                        JSONObject json = null;
                        try {
                            json = array.getJSONObject(i);
                            dataObject.setGroupName(json.getString("group_name"));
                            dataObject.setId(json.getString("id"));
                            dataObject.setGroupDescription("group_description");
                            dataObject.setDisplayNumber("display_number");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        spinnerData.add(dataObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(null != spinnerData){
                    GroupAdapter spinnerAdapter = new GroupAdapter(getApplicationContext(), R.layout.groupspinnerlayout,spinnerData);
                    spinner.setAdapter(spinnerAdapter);
                    int index;
                    for (int i = 0; i < spinnerData.size(); i++) {
                        if (spinnerData.get(i).getId().equalsIgnoreCase(groupid)) {
                            index = i;
                            spinner.setSelection(index);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: ");

            }
        });
    }


    private void data(){
        Intent in = getIntent();
        String key = in.getStringExtra("Key");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String json = prefs.getString(key, "");
        Log.e("TAG", String.valueOf(json));
        adds = new ArrayList<>();
        try {

            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {

                AddDataModel editPrice = new AddDataModel();
                JSONObject json1 = null;
                try {
                    json1 = array.getJSONObject(i);
                    editPrice.setName(json1.getString("service_name"));
                    editPrice.setType(json1.getString("service_type"));
                    editPrice.setLevel(json1.getString("service_level"));
                    editPrice.setPrice(json1.getString("price"));
                    editPrice.setPulse(json1.getString("tap_pulse"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adds.add(editPrice);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        addAdapter.setPreset(adds);
        addAdapter.notifyDataSetChanged();
    }
}
