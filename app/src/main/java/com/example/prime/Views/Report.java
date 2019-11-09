package com.example.prime.Views;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileBrowser;
import com.aditya.filebrowser.FolderChooser;
import com.example.prime.Model.DryStationModel;
import com.example.prime.Model.DrySummaryModel;
import com.example.prime.Model.SummaryModel;
import com.example.prime.Model.WashStationModel;
import com.example.prime.Model.WashSummaryModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.RecyclerAdapter.ControlAdapter;
import com.example.prime.RecyclerAdapter.SummaryAdapter;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.android.volley.VolleyLog.TAG;

public class Report extends Fragment {
    Context mContext;
    Button dateButton,downloadButton;
    public static Boolean running;
    public static Thread MyThread;
    private ArrayList<SummaryModel> summaryModels;
    private ArrayList<DrySummaryModel> drySummaryModels = new ArrayList<>();
    private ArrayList<WashSummaryModel> washSummaryModels = new ArrayList<>();
    private ArrayList<WashStationModel> washStationModels  = new ArrayList<>();
    private ArrayList<DryStationModel> dryStationModels  = new ArrayList<>();
    private List<String> strings;
    private ArrayList<String> unitname;
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
    private HashSet<String> hashSet1;
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private String TAG="Reports.java";
    private Button send, notif;
    private SummaryAdapter summaryAdapter;
    private retrofit2.Call<ResponseBody> call;
    SharedPreferences prefs ;
    SharedPreferences.Editor editor;

    final Calendar newCalendar = Calendar.getInstance();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = container.getContext();
        return inflater.inflate(R.layout.view_reports, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {



        dateButton = view.findViewById(R.id.dateReport);
        downloadButton = view.findViewById(R.id.btnDownload);

        final DatePickerDialog StartTime = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTime.show();
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginDownload();
            }
        });




        prefs = PreferenceManager.getDefaultSharedPreferences( mContext);
        editor = prefs.edit();
        strings = new ArrayList<>();
        unitname = new ArrayList<>();
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



    }

    private void beginDownload() {


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
                    editor.putString("Reports",responseData);
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

                        retrofit2.Call<ResponseBody> call2 = apiInterface.getSorts("ci_session="+id,unit_id);
                        call2.enqueue(new retrofit2.Callback<ResponseBody>() {
                            @Override
                            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                try {
                                    String res = response.body().string();
                                    Log.e(TAG, "onResponse: "+res );
                                    editor.putString(unit_id+"s",name);
                                    editor.putString(name, res);
                                    editor.commit();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                    washStationModels.clear();
                    dryStationModels.clear();
                    JSONArray object = new JSONArray(responseData);

                    for(int i = 0; i<object.length();i++) {
                        JSONObject json = null;
                        try {
                            json = object.getJSONObject(i);
                            strings.add(json.getString(GET_JSON_FROM_SERVER_NAME16));
                            unitname.add(json.getString(GET_JSON_FROM_SERVER_NAME17));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    hashSet = new HashSet<String>(strings);
                    strings.clear();
                    strings = new ArrayList<String>(hashSet);
                    Collections.sort(strings);

                    hashSet1 = new HashSet<String>(unitname);
                    unitname.clear();
                    unitname = new ArrayList<String>(hashSet1);
                    Collections.sort(unitname);

                    Log.e(TAG, "onResponse: "+strings +" "+ hashSet);

                    Log.e(TAG, "onResponse: "+unitname +" "+ hashSet1);
                    for (int i = 0; i < strings.size(); i++) {
                        washSummaryModels = new ArrayList<>();
                        drySummaryModels = new ArrayList<>();
                        washStationModels = new ArrayList<>();
                        dryStationModels = new ArrayList<>();


                        final String id = strings.get(i);
                        String data1 = prefs.getString(id, "");
                        Log.e(TAG, "onResponse: "+data1 );
                        JSONArray jsonArray = new JSONArray(data1);

                        final String id1 = prefs.getString(id+"s","");
                        String data2 = prefs.getString(id1, "");
                        Log.e(TAG, "onResponse: "+data2 );
                        JSONArray jsonArray1 = new JSONArray(data2);

                        SummaryModel parent = new SummaryModel();
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject json = jsonArray.getJSONObject(j);

                            String type = json.getString(GET_JSON_FROM_SERVER_NAME3);
                            if (type.toLowerCase().equals("wash")) {
                                washSummaryModels.add(new WashSummaryModel(json.getString(GET_JSON_FROM_SERVER_NAME1), json.getString(GET_JSON_FROM_SERVER_NAME2)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME3), json.getString(GET_JSON_FROM_SERVER_NAME4)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME5), json.getString(GET_JSON_FROM_SERVER_NAME6)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME7), json.getString(GET_JSON_FROM_SERVER_NAME8)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME9)));
                            } else if (type.toLowerCase().equals("dry")) {
                                drySummaryModels.add(new DrySummaryModel(json.getString(GET_JSON_FROM_SERVER_NAME1), json.getString(GET_JSON_FROM_SERVER_NAME2)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME3), json.getString(GET_JSON_FROM_SERVER_NAME4)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME5), json.getString(GET_JSON_FROM_SERVER_NAME6)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME7), json.getString(GET_JSON_FROM_SERVER_NAME8)
                                        , json.getString(GET_JSON_FROM_SERVER_NAME9)));
                            }
                            parent.setUnitname(json.getString(GET_JSON_FROM_SERVER_NAME8));
                            parent.setWashSummaryModels(washSummaryModels);
                            parent.setDrySummaryModels(drySummaryModels);
                        }
                        for (int j1 = 0; j1 < jsonArray1.length(); j1++) {
                            JSONObject json1 = jsonArray1.getJSONObject(j1);

                            washStationModels.add(new WashStationModel(json1.getString(GET_JSON_FROM_SERVER_NAME11), json1.getString(GET_JSON_FROM_SERVER_NAME12)
                                    , json1.getString(GET_JSON_FROM_SERVER_NAME13), json1.getString(GET_JSON_FROM_SERVER_NAME14)
                                    , json1.getString(GET_JSON_FROM_SERVER_NAME15), json1.getString(GET_JSON_FROM_SERVER_NAME16)
                                    , json1.getString(GET_JSON_FROM_SERVER_NAME17), json1.getString(GET_JSON_FROM_SERVER_NAME18)));

                            dryStationModels.add(new DryStationModel(json1.getString(GET_JSON_FROM_SERVER_NAME11), json1.getString(GET_JSON_FROM_SERVER_NAME12)
                                    , json1.getString(GET_JSON_FROM_SERVER_NAME13), json1.getString(GET_JSON_FROM_SERVER_NAME14)
                                    , json1.getString(GET_JSON_FROM_SERVER_NAME15), json1.getString(GET_JSON_FROM_SERVER_NAME16)
                                    , json1.getString(GET_JSON_FROM_SERVER_NAME17), json1.getString(GET_JSON_FROM_SERVER_NAME18)));

                            parent.setWashStationModels(washStationModels);
                            parent.setDryStationModels(dryStationModels);
                        }

                        parent.setUnitid(id);



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

}







