package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.CardsModel;
import com.example.prime.Model.DrySummaryModel;
import com.example.prime.Model.WashCountModel;
import com.example.prime.Model.WashStationCountModel;
import com.example.prime.R;

import java.util.ArrayList;

public class WashCountAdapter extends RecyclerView.Adapter<WashCountAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<WashStationCountModel> washStationCountModels;
    private ArrayList<WashCountModel> washCountModels;
    private WashStationCountAdapter washStationCountAdapter;
    private boolean exist = false;
    public WashCountAdapter(Context context, ArrayList<WashStationCountModel> washStationCountModels) {
        this.context = context;
        this.washStationCountModels = washStationCountModels;
    }

    public void setStations(ArrayList<WashStationCountModel> washStationCountModels) {
        this.washCountModels = new ArrayList<>();
        this.washStationCountModels = washStationCountModels;
        //notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WashCountAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.childstationwashlayout, viewGroup, false);
        return new WashCountAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WashCountAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(washStationCountModels.get(position));
    }

    @Override
    public int getItemCount() {
        return washStationCountModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public LinearLayout linearLayout;
        private RecyclerView recyclerView;


        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.serName);
            recyclerView = itemView.findViewById(R.id.recycler);
        }

        void bind(final WashStationCountModel washCountModel) {
            name.setText(washCountModel.getServiceName());
            washCountModels = new ArrayList<>();
            washStationCountAdapter = new WashStationCountAdapter(context,washCountModels);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(washStationCountAdapter);
            washCountModels = washCountModel.getWashCountModels();
            washStationCountAdapter.setStations(washCountModels);
            washStationCountAdapter.notifyDataSetChanged();
            if (name.getText().toString().equals("")){
                name.setText("0");
            }

        }
    }

    public ArrayList<WashStationCountModel> getAll() {
        return washStationCountModels;
    }


}