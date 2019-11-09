package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.CardsModel;
import com.example.prime.Model.DryStationModel;
import com.example.prime.Model.DrySummaryModel;
import com.example.prime.Model.ListModel;
import com.example.prime.Model.ServiceModel;
import com.example.prime.Model.StationModel;
import com.example.prime.Model.SummaryModel;
import com.example.prime.Model.UnitModel;
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
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.android.volley.VolleyLog.TAG;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.MultiViewHolder> {
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
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    String GET_JSON_FROM_SERVER_NAME1 = "id";
    String GET_JSON_FROM_SERVER_NAME2 = "host_id";
    String GET_JSON_FROM_SERVER_NAME3 = "station_name";
    String GET_JSON_FROM_SERVER_NAME4 = "hostname";
    String GET_JSON_FROM_SERVER_NAME5 = "status";
    String GET_JSON_FROM_SERVER_NAME6 = "unit_id";
    String GET_JSON_FROM_SERVER_NAME7 = "unit_name";
    String GET_JSON_FROM_SERVER_NAME8 = "ipaddress";

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

        private TextView wash, dry;
        private LinearLayout washLayout, dryLayout;
        private RecyclerView recyclerView, recyclerView1, recyclerView2, recyclerView3;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            wash = itemView.findViewById(R.id.serviceNameWash);
            dry = itemView.findViewById(R.id.serviceNameDry);
            recyclerView = itemView.findViewById(R.id.listView);
            recyclerView1 = itemView.findViewById(R.id.listView1);
            recyclerView2 = itemView.findViewById(R.id.listView2);
            recyclerView3 = itemView.findViewById(R.id.listView3);
            washLayout = itemView.findViewById(R.id.washLayout);
            dryLayout = itemView.findViewById(R.id.dryLayout);
        }

        void bind(final SummaryModel summaryModel) {


            sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(context);
            apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
            id = sharedPrefsCookiePersistor.loadAll().get(0).value();



            if(summaryModel.getWashSummaryModels().size() > 0){
                String name = summaryModel.getUnitname()+" Wash";
                wash.setText(name);
                wash.setTag(summaryModel.getUnitid());
                washSummaryAdapter = new WashSummaryAdapter(context,washSummaryModels);
                washSummaryModels = summaryModel.getWashSummaryModels();
                washSummaryAdapter.setStations(washSummaryModels);
                washSummaryAdapter.notifyDataSetChanged();
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(washSummaryAdapter);
                recyclerView.setHasFixedSize(true);

                washStationAdapter = new WashStationAdapter(context,washStationModels);
                washStationModels = summaryModel.getWashStationModels();
                washStationAdapter.setStations(washStationModels);
                washStationAdapter.notifyDataSetChanged();
                recyclerView2.setLayoutManager(new LinearLayoutManager(context));
                recyclerView2.setAdapter(washStationAdapter);
                recyclerView2.setHasFixedSize(true);




                washLayout.setVisibility(View.VISIBLE);

            }else {
                washLayout.setVisibility(View.GONE);
            }


            if(summaryModel.getDrySummaryModels().size() > 0){
                String name = summaryModel.getUnitname()+" Dry";
                dry.setText(name);
                dry.setTag(summaryModel.getUnitid());
                drySummaryAdapter = new DrySummaryAdapter(context,drySummaryModels);
                drySummaryModels = summaryModel.getDrySummaryModels();
                drySummaryAdapter.setStations(drySummaryModels);
                drySummaryAdapter.notifyDataSetChanged();
                recyclerView1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                recyclerView1.setAdapter(drySummaryAdapter);
                recyclerView1.setHasFixedSize(true);

                dryStationAdapter = new DryStationAdapter(context,dryStationModels);
                dryStationModels = summaryModel.getDryStationModels();
                dryStationAdapter.setStations(dryStationModels);
                dryStationAdapter.notifyDataSetChanged();
                recyclerView3.setLayoutManager(new LinearLayoutManager(context));
                recyclerView3.setAdapter(dryStationAdapter);
                recyclerView3.setHasFixedSize(true);


                dryLayout.setVisibility(View.VISIBLE);
            }else {
                dryLayout.setVisibility(View.GONE);
            }




        }

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