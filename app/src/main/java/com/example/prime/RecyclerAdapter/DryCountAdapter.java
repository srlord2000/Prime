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

import com.example.prime.Model.DryCountModel;
import com.example.prime.Model.DryStationCountModel;
import com.example.prime.Model.WashCountModel;
import com.example.prime.Model.WashStationCountModel;
import com.example.prime.R;

import java.util.ArrayList;

public class DryCountAdapter extends RecyclerView.Adapter<DryCountAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<DryStationCountModel> dryStationCountModels;
    private ArrayList<DryCountModel> dryCountModels;
    private DryStationCountAdapter dryStationCountAdapter;
    private boolean exist = false;
    public DryCountAdapter(Context context, ArrayList<DryStationCountModel> dryStationCountModels) {
        this.context = context;
        this.dryStationCountModels = dryStationCountModels;
    }

    public void setStations(ArrayList<DryStationCountModel> dryStationCountModels) {
        this.dryStationCountModels = new ArrayList<>();
        this.dryStationCountModels = dryStationCountModels;
        //notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DryCountAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.childstationdrylayout, viewGroup, false);
        return new DryCountAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DryCountAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(dryStationCountModels.get(position));
    }

    @Override
    public int getItemCount() {
        return dryStationCountModels.size();
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

        void bind(final DryStationCountModel dryStationCountModel) {
            name.setText(dryStationCountModel.getServiceName());
            dryCountModels = new ArrayList<>();
            dryStationCountAdapter = new DryStationCountAdapter(context,dryCountModels);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(dryStationCountAdapter);
            dryCountModels = dryStationCountModel.getDryCountModels();
            dryStationCountAdapter.setStations(dryCountModels);
            dryStationCountAdapter.notifyDataSetChanged();
            if (name.getText().toString().equals("")){
                name.setText("0");
            }

        }
    }

    public ArrayList<DryStationCountModel> getAll() {
        return dryStationCountModels;
    }
}