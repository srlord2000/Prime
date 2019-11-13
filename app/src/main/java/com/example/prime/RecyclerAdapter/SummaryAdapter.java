package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.DryStationModel;
import com.example.prime.Model.DrySummaryModel;
import com.example.prime.Model.SummaryModel;
import com.example.prime.Model.WashStationModel;
import com.example.prime.Model.WashSummaryModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;

import static com.android.volley.VolleyLog.TAG;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.MultiViewHolder> {

    public static Boolean running;
    public static Thread MyThread;
    private DrySummaryAdapter drySummaryAdapter;
    private WashSummaryAdapter washSummaryAdapter;
    private DryStationAdapter dryStationAdapter;
    private WashStationAdapter washStationAdapter;
    private ArrayList<SummaryModel> summaryModels;
    private Context context;
    private ArrayList<WashSummaryModel> washSummaryModels;
    private ArrayList<DrySummaryModel> drySummaryModels;
    private ArrayList<WashStationModel> washStationModels;
    private ArrayList<DryStationModel> dryStationModels;
    private ArrayList<WashStationModel> stationTotalModels;
    private ArrayList<DryStationModel> stationTotalModels1;
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id, res;
    private ArrayList<String> data = new ArrayList<>();
    SharedPreferences prefs ;
    SharedPreferences.Editor editor;
    private HashSet<String> hashSet;
    private HashSet<String> hashSet1;
    private List<String> strings;
    private ArrayList<String> unitname;
    private String t;
    private String formattedDate;
    private ArrayList<Double> integers;
    private ArrayList<Double> sums;
    private int sum=0;
    private WashStationTotalAdapter washStationTotalAdapter;
    private DryStationTotalAdapter dryStationTotalAdapter;

//    String GET_JSON_FROM_SERVER_NAME1 = "id";
//    String GET_JSON_FROM_SERVER_NAME2 = "service_name";
//    String GET_JSON_FROM_SERVER_NAME3 = "service_type";
//    String GET_JSON_FROM_SERVER_NAME4 = "service_level";
//    String GET_JSON_FROM_SERVER_NAME5 = "price";
//    String GET_JSON_FROM_SERVER_NAME6 = "tap_pulse";
//    String GET_JSON_FROM_SERVER_NAME7 = "time_added";
//    String GET_JSON_FROM_SERVER_NAME8 = "unit_name";
//    String GET_JSON_FROM_SERVER_NAME9 = "unit_id";
//
//    String GET_JSON_FROM_SERVER_NAME11 = "id";
//    String GET_JSON_FROM_SERVER_NAME12 = "host_id";
//    String GET_JSON_FROM_SERVER_NAME13 = "station_name";
//    String GET_JSON_FROM_SERVER_NAME14 = "hostname";
//    String GET_JSON_FROM_SERVER_NAME15 = "status";
//    String GET_JSON_FROM_SERVER_NAME16 = "unit_id";
//    String GET_JSON_FROM_SERVER_NAME17 = "unit_name";
//    String GET_JSON_FROM_SERVER_NAME18 = "ipaddress";

    public SummaryAdapter(Context context, ArrayList<SummaryModel> summaryModels) {
        this.context = context;
        this.summaryModels = summaryModels;
    }

    public void setPreset(ArrayList<SummaryModel> summaryModels) {
        this.summaryModels = new ArrayList<>();
        this.summaryModels = summaryModels;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public SummaryAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.summarylistlayout, viewGroup, false);
        return new SummaryAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(summaryModels.get(position));
    }

    @Override
    public int getItemCount() {
        return summaryModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder{

        private TextView wash, dry, subtotal1, amount1, subtotal2, amount2, washtotal, drytotal;
        private LinearLayout washLayout, dryLayout;
        private RecyclerView recyclerView, recyclerView1, recyclerView2, recyclerView3, recyclerView5, recyclerView6;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            wash = itemView.findViewById(R.id.serviceNameWash);
            dry = itemView.findViewById(R.id.serviceNameDry);
            recyclerView = itemView.findViewById(R.id.listView);
            recyclerView1 = itemView.findViewById(R.id.listView1);
            recyclerView2 = itemView.findViewById(R.id.listView2);
            recyclerView3 = itemView.findViewById(R.id.listView3);
            recyclerView5 = itemView.findViewById(R.id.listView5);
            recyclerView6 = itemView.findViewById(R.id.listView6);
            washLayout = itemView.findViewById(R.id.washLayout);
            dryLayout = itemView.findViewById(R.id.dryLayout);
            subtotal1 = itemView.findViewById(R.id.subTotal1);
            amount1 = itemView.findViewById(R.id.amount1);
            subtotal2 = itemView.findViewById(R.id.subTotal2);
            amount2 = itemView.findViewById(R.id.amount2);
            washtotal = itemView.findViewById(R.id.washtotal);
            drytotal = itemView.findViewById(R.id.drytotal);
        }

        void bind(final SummaryModel summaryModel) {
            setdate();
            date();
            prefs = PreferenceManager.getDefaultSharedPreferences( context);
            editor = prefs.edit();
            strings = new ArrayList<>();
            unitname = new ArrayList<>();
            stationTotalModels = new ArrayList<>();
            washStationModels = new ArrayList<>();
            washSummaryModels = new ArrayList<>();
            dryStationModels = new ArrayList<>();
            drySummaryModels = new ArrayList<>();
            sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(context);
            apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
            id = sharedPrefsCookiePersistor.loadAll().get(0).value();

            if(summaryModel.getWashSummaryModels().size() > 0){
                String name = summaryModel.getUnitname()+" Wash";
                wash.setText(name);
                wash.setTag(summaryModel.getUnitid());
                washSummaryAdapter = new WashSummaryAdapter(context,washSummaryModels);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(washSummaryAdapter);
                washSummaryModels = summaryModel.getWashSummaryModels();
                washSummaryAdapter.setStations(washSummaryModels);
                washSummaryAdapter.notifyDataSetChanged();
                recyclerView.setHasFixedSize(true);

                washStationAdapter = new WashStationAdapter(context,washStationModels);
                recyclerView2.setLayoutManager(new LinearLayoutManager(context));
                recyclerView2.setAdapter(washStationAdapter);
                washStationModels = summaryModel.getWashStationModels();
                washStationAdapter.setStations(washStationModels);
                washStationAdapter.notifyDataSetChanged();
                recyclerView2.setHasFixedSize(true);

                washStationTotalAdapter = new WashStationTotalAdapter(context,stationTotalModels);
                recyclerView5.setLayoutManager(new LinearLayoutManager(context));
                recyclerView5.setAdapter(washStationTotalAdapter);
                stationTotalModels = summaryModel.getWashStationModels();
                washStationTotalAdapter.setStations(stationTotalModels);
                washStationTotalAdapter.notifyDataSetChanged();
                recyclerView5.setHasFixedSize(true);
                Log.e(TAG, "SORTSs!: "+summaryModel.getWashStationModels().size());

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

                            for(int j=0;j<summaryModel.getWashStationModels().size();j++){
                                final String name = summaryModel.getWashStationModels().get(j).getStationName();
                                retrofit2.Call<ResponseBody> call = apiInterface.getTally3("ci_session="+id,t,"0",name,"Wash");
                                call.enqueue(new retrofit2.Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                        try {
                                            String res = response.body().string();
                                            if(!prefs.getString(name+"total","").equals(res)) {
                                                editor.remove(name + "total");
                                                editor.putString(name + "total", res);
                                                editor.commit();
                                            }

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

                        }
                        System.out.println("onEnd Thread");
                    }
                };
                MyThread.start();



                washLayout.setVisibility(View.VISIBLE);
                subtotal1.setText("Sub Total");
                amount1.setText("Amount");

            }else {
                washLayout.setVisibility(View.GONE);
            }


            if(summaryModel.getDrySummaryModels().size() > 0){
                final String name = summaryModel.getUnitname()+" Dry";
                dry.setText(name);
                dry.setTag(summaryModel.getUnitid());
                drySummaryAdapter = new DrySummaryAdapter(context,drySummaryModels);
                recyclerView1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                recyclerView1.setAdapter(drySummaryAdapter);
                drySummaryModels = summaryModel.getDrySummaryModels();
                drySummaryAdapter.setStations(drySummaryModels);
                drySummaryAdapter.notifyDataSetChanged();
                recyclerView1.setHasFixedSize(true);

                dryStationAdapter = new DryStationAdapter(context,dryStationModels);
                recyclerView3.setLayoutManager(new LinearLayoutManager(context));
                recyclerView3.setAdapter(dryStationAdapter);
                dryStationModels = summaryModel.getDryStationModels();
                dryStationAdapter.setStations(dryStationModels);
                dryStationAdapter.notifyDataSetChanged();
                recyclerView3.setHasFixedSize(true);

                dryStationTotalAdapter = new DryStationTotalAdapter(context,stationTotalModels1);
                recyclerView6.setLayoutManager(new LinearLayoutManager(context));
                recyclerView6.setAdapter(dryStationTotalAdapter);
                stationTotalModels1 = summaryModel.getDryStationModels();
                dryStationTotalAdapter.setStations(stationTotalModels1);
                dryStationTotalAdapter.notifyDataSetChanged();
                recyclerView6.setHasFixedSize(true);
                Log.e(TAG, "SORTSs!: "+summaryModel.getWashStationModels().size());

                load();
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
                            for(int j=0;j<summaryModel.getDryStationModels().size();j++){
                                final String name = summaryModel.getDryStationModels().get(j).getStationName();
                                retrofit2.Call<ResponseBody> call1 = apiInterface.getTally4("ci_session="+id,t,"0",name,"Dry");
                                call1.enqueue(new retrofit2.Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                        try {
                                            String res = response.body().string();
                                            if(!prefs.getString(name+"totald","").equals(res)) {
                                                editor.remove(name + "totald");
                                                editor.putString(name + "totald", res);
                                                editor.commit();
                                                load();

                                            }

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

                        }
                        System.out.println("onEnd Thread");
                    }
                };
                MyThread.start();



                dryLayout.setVisibility(View.VISIBLE);
                subtotal2.setText("Sub Total");
                amount2.setText("Amount");
            }else {
                dryLayout.setVisibility(View.GONE);
            }




        }
        private void load(){
            integers = new ArrayList<>();
            integers.clear();
            for (int i=0;i<summaryModels.get(getAdapterPosition()).getWashStationModels().size();i++) {
                String tx = stationTotalModels.get(i).getTotal();
                //double txx = Double.parseDouble(tx);
                Log.e(TAG, "load: "+tx);
                //integers.add(txx);
            }
            if (integers.size() != 0) {
                for (int s = 0; s < integers.size(); s++) {
                    sum += integers.get(s);
                }
                drytotal.setText(String.valueOf(sum));
            }
        }




    }

//    private void load(){
//        retrofit2.Call<ResponseBody> call = apiInterface.getUnits("ci_session="+id);
//        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
//            @Override
//            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                Log.d("TAG", response.headers().toString());
//                try {
//                    String responseData = response.body().string();
//                    final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
//                    final SharedPreferences.Editor editor = sharedPrefs.edit();
//                    editor.putString("Summary",responseData);
//                    editor.apply();
//                    Log.e("TAGtry", responseData);
//                    data.add(responseData);
//                    JSONArray object = new JSONArray(responseData);
//                    for(int i = 0; i<object.length();i++){
//                        JSONObject jsonObject = object.getJSONObject(i);
//                        final String unit_id = jsonObject.getString("id");
//                        final String name = jsonObject.getString("unit_name");
//
//                        retrofit2.Call<ResponseBody> call1 = apiInterface.getServiceId("ci_session="+id,unit_id);
//                        call1.enqueue(new retrofit2.Callback<ResponseBody>() {
//                            @Override
//                            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                                try {
//                                    String res = response.body().string();
//                                    Log.e(TAG, "onResponse: "+res );
//                                    editor.putString(unit_id, res);
//                                    editor.commit();
//                                    load1();
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                Log.e("", "sharedtagtry: "+prefs.getString("15", ""));
//                            }
//                            @Override
//                            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
//                                Log.d(TAG, "onFailuretagtry: ");
//
//                            }
//                        });
//
//                        retrofit2.Call<ResponseBody> call2 = apiInterface.getSorts("ci_session="+id,unit_id);
//                        call2.enqueue(new retrofit2.Callback<ResponseBody>() {
//                            @Override
//                            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                                try {
//                                    String res = response.body().string();
//                                    Log.e(TAG, "onResponse: "+res );
//                                    editor.putString(unit_id+"s",name);
//                                    editor.putString(name, res);
//                                    editor.commit();
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            @Override
//                            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
//                                Log.d(TAG, "onFailuretagtry: ");
//
//                            }
//                        });
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
//                Log.d(TAG, "onFailuretagtrt: ");
//
//            }
//        });
//
//    }
//
//    private void load1(){
//        retrofit2.Call<ResponseBody> call = apiInterface.getStation("ci_session="+id);
//        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
//            @Override
//            public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                Log.d("TAG", response.headers().toString());
//                try {
//                    String responseData = response.body().string();
//                    Log.e("TAG", responseData);
//                    summaryModels.clear();
//                    drySummaryModels.clear();
//                    washSummaryModels.clear();
//                    washStationModels.clear();
//                    dryStationModels.clear();
//                    JSONArray object = new JSONArray(responseData);
//
//                    for(int i = 0; i<object.length();i++) {
//                        JSONObject json = null;
//                        try {
//                            json = object.getJSONObject(i);
//                            strings.add(json.getString(GET_JSON_FROM_SERVER_NAME16));
//                            unitname.add(json.getString(GET_JSON_FROM_SERVER_NAME17));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    hashSet = new HashSet<String>(strings);
//                    strings.clear();
//                    strings = new ArrayList<String>(hashSet);
//                    Collections.sort(strings);
//
//                    hashSet1 = new HashSet<String>(unitname);
//                    unitname.clear();
//                    unitname = new ArrayList<String>(hashSet1);
//                    Collections.sort(unitname);
//
//                    Log.e(TAG, "onResponse: "+strings +" "+ hashSet);
//
//                    Log.e(TAG, "onResponse: "+unitname +" "+ hashSet1);
//                    for (int i = 0; i < strings.size(); i++) {
//                        washSummaryModels = new ArrayList<>();
//                        drySummaryModels = new ArrayList<>();
//                        washStationModels = new ArrayList<>();
//                        dryStationModels = new ArrayList<>();
//
//
//                        final String id = strings.get(i);
//                        String data1 = prefs.getString(id, "");
//                        Log.e(TAG, "onResponse: "+data1 );
//                        JSONArray jsonArray = new JSONArray(data1);
//
//                        final String id1 = prefs.getString(id+"s","");
//                        String data2 = prefs.getString(id1, "");
//                        Log.e(TAG, "onResponse: "+data2 );
//                        JSONArray jsonArray1 = new JSONArray(data2);
//
//                        SummaryModel parent = new SummaryModel();
//                        for (int j = 0; j < jsonArray.length(); j++) {
//                            JSONObject json = jsonArray.getJSONObject(j);
//
//                            String type = json.getString(GET_JSON_FROM_SERVER_NAME3);
//                            if (type.toLowerCase().equals("wash")) {
//                                washSummaryModels.add(new WashSummaryModel(json.getString(GET_JSON_FROM_SERVER_NAME1), json.getString(GET_JSON_FROM_SERVER_NAME2)
//                                        , json.getString(GET_JSON_FROM_SERVER_NAME3), json.getString(GET_JSON_FROM_SERVER_NAME4)
//                                        , json.getString(GET_JSON_FROM_SERVER_NAME5), json.getString(GET_JSON_FROM_SERVER_NAME6)
//                                        , json.getString(GET_JSON_FROM_SERVER_NAME7), json.getString(GET_JSON_FROM_SERVER_NAME8)
//                                        , json.getString(GET_JSON_FROM_SERVER_NAME9)));
//                            } else if (type.toLowerCase().equals("dry")) {
//                                drySummaryModels.add(new DrySummaryModel(json.getString(GET_JSON_FROM_SERVER_NAME1), json.getString(GET_JSON_FROM_SERVER_NAME2)
//                                        , json.getString(GET_JSON_FROM_SERVER_NAME3), json.getString(GET_JSON_FROM_SERVER_NAME4)
//                                        , json.getString(GET_JSON_FROM_SERVER_NAME5), json.getString(GET_JSON_FROM_SERVER_NAME6)
//                                        , json.getString(GET_JSON_FROM_SERVER_NAME7), json.getString(GET_JSON_FROM_SERVER_NAME8)
//                                        , json.getString(GET_JSON_FROM_SERVER_NAME9)));
//                            }
//                            parent.setUnitname(json.getString(GET_JSON_FROM_SERVER_NAME8));
//                            parent.setWashSummaryModels(washSummaryModels);
//                            parent.setDrySummaryModels(drySummaryModels);
//                        }
//                        for (int j1 = 0; j1 < jsonArray1.length(); j1++) {
//                            JSONObject json1 = jsonArray1.getJSONObject(j1);
//
//                            washStationModels.add(new WashStationModel(json1.getString(GET_JSON_FROM_SERVER_NAME11), json1.getString(GET_JSON_FROM_SERVER_NAME12)
//                                    , json1.getString(GET_JSON_FROM_SERVER_NAME13), json1.getString(GET_JSON_FROM_SERVER_NAME14)
//                                    , json1.getString(GET_JSON_FROM_SERVER_NAME15), json1.getString(GET_JSON_FROM_SERVER_NAME16)
//                                    , json1.getString(GET_JSON_FROM_SERVER_NAME17), json1.getString(GET_JSON_FROM_SERVER_NAME18)));
//
//                            dryStationModels.add(new DryStationModel(json1.getString(GET_JSON_FROM_SERVER_NAME11), json1.getString(GET_JSON_FROM_SERVER_NAME12)
//                                    , json1.getString(GET_JSON_FROM_SERVER_NAME13), json1.getString(GET_JSON_FROM_SERVER_NAME14)
//                                    , json1.getString(GET_JSON_FROM_SERVER_NAME15), json1.getString(GET_JSON_FROM_SERVER_NAME16)
//                                    , json1.getString(GET_JSON_FROM_SERVER_NAME17), json1.getString(GET_JSON_FROM_SERVER_NAME18)));
//
//                            parent.setWashStationModels(washStationModels);
//                            parent.setDryStationModels(dryStationModels);
//                        }
//
//                        parent.setUnitid(id);
//
//
//
//                        summaryModels.add(parent);
//
//                    }
//
////                    for (int i = 0; i < unitname.size(); i++) {
////                        washStationModels = new ArrayList<>();
////                        dryStationModels = new ArrayList<>();
////                        final String id = unitname.get(i);
////                        String data1 = prefs.getString(id, "");
////                        Log.e(TAG, "onResponse: "+data1 );
////                        JSONArray jsonArray = new JSONArray(data1);
////
////                        for (int j = 0; j < jsonArray.length(); j++) {
////                            JSONObject json = jsonArray.getJSONObject(j);
////
////                            washStationModels.add(new WashStationModel(json.getString(GET_JSON_FROM_SERVER_NAME11), json.getString(GET_JSON_FROM_SERVER_NAME12)
////                                        , json.getString(GET_JSON_FROM_SERVER_NAME13), json.getString(GET_JSON_FROM_SERVER_NAME14)
////                                        , json.getString(GET_JSON_FROM_SERVER_NAME15), json.getString(GET_JSON_FROM_SERVER_NAME16)
////                                        , json.getString(GET_JSON_FROM_SERVER_NAME17), json.getString(GET_JSON_FROM_SERVER_NAME18)));
////
////                            dryStationModels.add(new DryStationModel(json.getString(GET_JSON_FROM_SERVER_NAME11), json.getString(GET_JSON_FROM_SERVER_NAME12)
////                                    , json.getString(GET_JSON_FROM_SERVER_NAME13), json.getString(GET_JSON_FROM_SERVER_NAME14)
////                                    , json.getString(GET_JSON_FROM_SERVER_NAME15), json.getString(GET_JSON_FROM_SERVER_NAME16)
////                                    , json.getString(GET_JSON_FROM_SERVER_NAME17), json.getString(GET_JSON_FROM_SERVER_NAME18)));
////
////                            parent.setDryStationModels(dryStationModels);
////                            parent.setWashStationModels(washStationModels);
////                        }
////
////                        summaryModels.add(parent);
////                    }
//                } catch (IOException | JSONException e1) {
//                    e1.printStackTrace();
//                }
//                summaryAdapter.setPreset(summaryModels);
//                summaryAdapter.notifyDataSetChanged();
//            }
//
//            public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
//                Log.d(TAG, "onFailuretagtrt: ");
//
//            }
//        });
//    }

    private void setdate(){
        final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        String from = sdf1.format(today);
        long unix1 = today.getTime()/1000;
        String f = String.valueOf(unix1);

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        String to = sdf1.format(tomorrow);
        long unix = tomorrow.getTime()/1000;
        t = String.valueOf(unix);
    }

    private void date(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        formattedDate = df.format(c);
    }

    public ArrayList<SummaryModel> getAll() {
        return summaryModels;
    }

//    public ArrayList<StationModel> getSelected() {
//        ArrayList<StationModel> selected = new ArrayList<>();
//        for (int i = 0; i < station.size(); i++) {
//            if (station.get(i).isChecked()) {
//                selected.add(station.get(i));
//            }
//        }
//        return selected;
//    }

}