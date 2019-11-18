package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.WashCountModel;
import com.example.prime.Model.WashStationCountModel;
import com.example.prime.R;

import java.util.ArrayList;

public class WashStationCountAdapter extends RecyclerView.Adapter<WashStationCountAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<WashCountModel> washCountModels;
    private boolean exist = false;
    public WashStationCountAdapter(Context context, ArrayList<WashCountModel> washCountModels) {
        this.context = context;
        this.washCountModels = washCountModels;
    }

    public void setStations(ArrayList<WashCountModel> washCountModels) {
        this.washCountModels = new ArrayList<>();
        this.washCountModels = washCountModels;
        //notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WashStationCountAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.childstationwashcount, viewGroup, false);
        return new WashStationCountAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WashStationCountAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(washCountModels.get(position));
    }

    @Override
    public int getItemCount() {
        return washCountModels.size();
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

        void bind(final WashCountModel washCountModel) {
            if (!washCountModel.getCount().equals("")){
                name.setText(washCountModel.getCount());
            }else {
                name.setText("0");
            }
        }
    }

    public ArrayList<WashCountModel> getAll() {
        return washCountModels;
    }

    public ArrayList<WashCountModel> getSelected() {
        ArrayList<WashCountModel> selected = new ArrayList<>();
        for (int i = 0; i < washCountModels.size(); i++) {
            if (washCountModels.get(i).isChecked()) {
                selected.add(washCountModels.get(i));
            }
        }
        return selected;
    }
}