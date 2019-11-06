package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.DrySummaryModel;
import com.example.prime.Model.ServiceModel;
import com.example.prime.Model.SummaryModel;
import com.example.prime.Model.UnitModel;
import com.example.prime.Model.WashSummaryModel;
import com.example.prime.R;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.MultiViewHolder> {
    private ChildAdapter childAdapter;
    private DrySummaryAdapter drySummaryAdapter;
    private WashSummaryAdapter washSummaryAdapter;
    private ArrayList<SummaryModel> summaryModels;
    private Context context;
    private ArrayList<WashSummaryModel> washSummaryModels;
    private ArrayList<DrySummaryModel> drySummaryModels;

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
            if(summaryModel.getWashSummaryModels().size() > 0){
                String name = summaryModel.getUnitname()+" Wash";
                wash.setText(name);
                washSummaryAdapter = new WashSummaryAdapter(context,washSummaryModels);
                washSummaryModels = summaryModel.getWashSummaryModels();
                washSummaryAdapter.setStations(washSummaryModels);
                washSummaryAdapter.notifyDataSetChanged();
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(washSummaryAdapter);
                recyclerView.setHasFixedSize(true);

                recyclerView2.setLayoutManager(new LinearLayoutManager(context));
                recyclerView2.setAdapter(washSummaryAdapter);
                recyclerView2.setHasFixedSize(true);
                washLayout.setVisibility(View.VISIBLE);

            }else {
                washLayout.setVisibility(View.GONE);
            }
            if(summaryModel.getDrySummaryModels().size() > 0){
                String name = summaryModel.getUnitname()+" Dry";
                dry.setText(name);
                drySummaryAdapter = new DrySummaryAdapter(context,drySummaryModels);
                drySummaryModels = summaryModel.getDrySummaryModels();
                drySummaryAdapter.setStations(drySummaryModels);
                drySummaryAdapter.notifyDataSetChanged();
                recyclerView1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                recyclerView1.setAdapter(drySummaryAdapter);
                recyclerView1.setHasFixedSize(true);
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