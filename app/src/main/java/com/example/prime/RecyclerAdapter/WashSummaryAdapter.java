package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.DryStationModel;
import com.example.prime.Model.DrySummaryModel;
import com.example.prime.Model.ServiceModel;
import com.example.prime.Model.SummaryModel;
import com.example.prime.Model.WashCountModel;
import com.example.prime.Model.WashStationCountModel;
import com.example.prime.Model.WashStationModel;
import com.example.prime.Model.WashSummaryModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import okhttp3.ResponseBody;

import static com.android.volley.VolleyLog.TAG;

public class WashSummaryAdapter extends RecyclerView.Adapter<WashSummaryAdapter.MultiViewHolder> {

    private Context context;
    public static Boolean running;
    public static Thread MyThread;

    private ArrayList<WashSummaryModel> washSummaryModels;
    private ArrayList<WashCountModel> washCountModels = new ArrayList<>();
    private String formattedDate;
    public static WashStationCountAdapter washCountAdapter;
    private String t;
    public static int add = 0, sum = 0;
    public static ArrayList<Integer> addd;

    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    public WashSummaryAdapter(Context context, ArrayList<WashSummaryModel> washSummaryModels) {
        this.context = context;
        this.washSummaryModels = washSummaryModels;
    }

    public void setStations(ArrayList<WashSummaryModel> washSummaryModels) {
        this.washSummaryModels = new ArrayList<>();
        this.washSummaryModels = washSummaryModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WashSummaryAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.childwashlistlayout, viewGroup, false);
        return new WashSummaryAdapter.MultiViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WashSummaryAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(washSummaryModels.get(position));

    }

    @Override
    public int getItemCount() {
        return washSummaryModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {


        public LinearLayout linearLayout;
        public TextView name, subtotal, amount;
        private RecyclerView recyclerView;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.serName);
            recyclerView = itemView.findViewById(R.id.recycler);
            subtotal = itemView.findViewById(R.id.subTotal);
            amount = itemView.findViewById(R.id.amount);

        }

        void bind(final WashSummaryModel washSummaryModel) {
            setdate();
            date();
            amount.setTag(washSummaryModel.getPrice());
            recyclerView.setTag(washSummaryModel.getUnitId());
            name.setTag(washSummaryModel.getServiceName());
            sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(context);
            apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
            id = sharedPrefsCookiePersistor.loadAll().get(0).value();
            name.setText(washSummaryModel.getServiceName());
            if (name.getText().toString().equals("")){
                name.setVisibility(View.GONE);
            }
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
                        load();

                    }
                    System.out.println("onEnd Thread");
                }
            };
            MyThread.start();




        }
        private void load(){
            retrofit2.Call<ResponseBody> call = apiInterface.getSorts1("ci_session="+id, recyclerView.getTag().toString());
            call.enqueue(new  retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    Log.d("TAG", response.headers().toString());

                    String responseData = null;
                    try {
                        responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);
                        Log.e(TAG, "onResponse: "+responseData );

                        for (int i=0; i<jsonArray.length();i++){

                            final JSONObject jsonObject= jsonArray.getJSONObject(i);
                            String name1 = jsonObject.getString("unit_id");
                            retrofit2.Call<ResponseBody> call1 = apiInterface.getTally("ci_session="+id,t,"0",name1,name.getTag().toString());
                            call1.enqueue(new  retrofit2.Callback<ResponseBody>() {
                                @Override
                                public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                    Log.d("TAG", response.headers().toString());

                                    String responseData1 = null;

                                    try {
                                        responseData1 = response.body().string();
                                        washCountModels = new ArrayList<>();
                                        JSONArray jsonArray1 = new JSONArray(responseData1);
                                        for (int j=0; j<jsonArray1.length();j++){
                                            washCountAdapter = new WashStationCountAdapter(context,washCountModels);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                            recyclerView.setAdapter(washCountAdapter);
                                            recyclerView.setItemViewCacheSize(20);
                                            recyclerView.setHasFixedSize(true);
                                            JSONObject jsonObject1= jsonArray1.getJSONObject(j);
                                            WashCountModel washCountModel1 = new WashCountModel();
                                            try {
                                                washCountModel1.setServiceName(jsonObject1.getString("service_name"));
                                                washCountModel1.setCount(jsonObject1.getString("count"));
                                                washCountModel1.setGroupName(jsonObject1.getString("group_name"));
                                                washCountModel1.setServiceLevel(jsonObject1.getString("service_level"));
                                                washCountModel1.setServiceType(jsonObject1.getString("service_type"));
                                                washCountModel1.setStationName(jsonObject1.getString("station_name"));
                                            }catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            washCountModels.add(washCountModel1);
                                        }

                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                    }
                                    washCountAdapter.setStations(washCountModels);
                                    washCountAdapter.notifyDataSetChanged();

                                    addd = new ArrayList<>();
                                    for(int i=0;i<washCountAdapter.getSelected().size();i++)
                                    {
                                        addd.add(Integer.parseInt(washCountAdapter.getSelected().get(i).getCount()));
                                    }
                                    Log.e(TAG, "Add: "+addd);
                                    sum = 0;
                                    for(int i=0; i<addd.size(); i++){
                                        sum += addd.get(i);
                                    }
                                    Log.e(TAG, "ADDS1: "+sum );
                                    subtotal.setText(String.valueOf(sum));
                                    double harga = Double.parseDouble(amount.getTag().toString());
                                    double product = harga * sum;
                                    DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                                    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                                    dfs.setCurrencySymbol("");
                                    dfs.setMonetaryDecimalSeparator('.');
                                    dfs.setGroupingSeparator(',');
                                    df.setDecimalFormatSymbols(dfs);
                                    String k = df.format(product);
                                    add = sum;

                                    amount.setText(k);

                                }

                                public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                                    Log.d(TAG, "onFailuretagtrt: ");

                                }
                            });


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("TAG", responseData);


                }

                public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "onFailuretagtrt: ");

                }
            });


        }
    }



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

    public ArrayList<WashSummaryModel> getAll() {
        return washSummaryModels;
    }
}