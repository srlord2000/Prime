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

import com.example.prime.Model.DryCountModel;
import com.example.prime.Model.DryStationCountModel;
import com.example.prime.Model.DrySummaryModel;
import com.example.prime.Model.ServiceModel;
import com.example.prime.Model.WashCountModel;
import com.example.prime.Model.WashStationCountModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;

import static com.android.volley.VolleyLog.TAG;

public class DrySummaryAdapter extends RecyclerView.Adapter<DrySummaryAdapter.MultiViewHolder> {

    private Context context;
    public static Boolean running;
    public static Thread MyThread;
    private ArrayList<DrySummaryModel> drySummaryModels;
    private ArrayList<DryCountModel> dryCountModels = new ArrayList<>();
    private String formattedDate;
    private DryStationCountAdapter dryStationCountAdapter;
    private String t;

    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    public DrySummaryAdapter(Context context, ArrayList<DrySummaryModel> drySummaryModels) {
        this.context = context;
        this.drySummaryModels = drySummaryModels;
    }

    public void setStations(ArrayList<DrySummaryModel> drySummaryModels) {
        this.drySummaryModels = new ArrayList<>();
        this.drySummaryModels = drySummaryModels;
        //notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DrySummaryAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.childdrylistlayout, viewGroup, false);
        return new DrySummaryAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrySummaryAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(drySummaryModels.get(position));
    }

    @Override
    public int getItemCount() {
        return drySummaryModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public LinearLayout linearLayout;
        final private RecyclerView recyclerView;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.serName);
            recyclerView = itemView.findViewById(R.id.recycler);
        }

        void bind(final DrySummaryModel drySummaryModel) {
            setdate();
            date();
            recyclerView.setTag(drySummaryModel.getUnitId());
            name.setTag(drySummaryModel.getServiceName());
            sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(context);
            apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
            id = sharedPrefsCookiePersistor.loadAll().get(0).value();
            name.setText(drySummaryModel.getServiceName());
            if (name.getText().toString().equals("")){
                name.setVisibility(View.GONE);
            }

            running = true;
            MyThread = new Thread() {//create thread
                @Override
                public void run() {
                    int i = 0;
                    while (running) {
                        System.out.println("counter: " + i);
                        i++;
                        try {
                            Thread.sleep(500);
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
            retrofit2.Call<ResponseBody> call = apiInterface.getSorts2("ci_session="+id, recyclerView.getTag().toString());
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
                            retrofit2.Call<ResponseBody> call1 = apiInterface.getTally1("ci_session="+id,t,"0",name1,name.getTag().toString());
                            call1.enqueue(new  retrofit2.Callback<ResponseBody>() {
                                @Override
                                public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                    Log.d("TAG", response.headers().toString());

                                    String responseData1 = null;


                                    try {
                                        responseData1 = response.body().string();
                                        dryCountModels = new ArrayList<>();
                                        JSONArray jsonArray1 = new JSONArray(responseData1);
                                        for (int j=0; j<jsonArray1.length();j++){

                                            JSONObject jsonObject1= jsonArray1.getJSONObject(j);
                                            DryCountModel dryCountModel1 = new DryCountModel() ;
                                            try {
                                                dryCountModel1.setServiceName(jsonObject1.getString("service_name"));
                                                dryCountModel1.setCount(jsonObject1.getString("count"));
                                                dryCountModel1.setGroupName(jsonObject1.getString("group_name"));
                                                dryCountModel1.setServiceLevel(jsonObject1.getString("service_level"));
                                                dryCountModel1.setServiceType(jsonObject1.getString("service_type"));
                                                dryCountModel1.setStationName(jsonObject1.getString("station_name"));
                                            }catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            dryCountModels.add(dryCountModel1);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    dryStationCountAdapter = new DryStationCountAdapter(context,dryCountModels);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    recyclerView.setAdapter(dryStationCountAdapter);
                                    recyclerView.setHasFixedSize(true);
                                    dryStationCountAdapter.setStations(dryCountModels);
                                    dryStationCountAdapter.notifyDataSetChanged();


                                    Log.e("TAG", responseData1);


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

    public ArrayList<DrySummaryModel> getAll() {
        return drySummaryModels;
    }
}