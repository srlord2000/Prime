package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.DryCountModel;
import com.example.prime.Model.WashCountModel;
import com.example.prime.R;

import java.util.ArrayList;

public class DryStationCountAdapter extends RecyclerView.Adapter<DryStationCountAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<DryCountModel> dryCountModels;
    private boolean exist = false;
    public DryStationCountAdapter(Context context, ArrayList<DryCountModel> dryCountModels) {
        this.context = context;
        this.dryCountModels = dryCountModels;
    }

    public void setStations(ArrayList<DryCountModel> dryCountModels) {
        this.dryCountModels = new ArrayList<>();
        this.dryCountModels = dryCountModels;
        //notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DryStationCountAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.childstationdrycount, viewGroup, false);
        return new DryStationCountAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DryStationCountAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(dryCountModels.get(position));
    }

    @Override
    public int getItemCount() {
        return dryCountModels.size();
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

        void bind(final DryCountModel dryCountModel) {
            name.setText(dryCountModel.getCount());
            if (name.getText().toString().equals("")){
                name.setText("0");
            }

        }
    }

    public ArrayList<DryCountModel> getAll() {
        return dryCountModels;
    }

    public ArrayList<DryCountModel> getSelected() {
        ArrayList<DryCountModel> selected = new ArrayList<>();
        for (int i = 0; i < dryCountModels.size(); i++) {
            if (dryCountModels.get(i).isChecked()) {
                selected.add(dryCountModels.get(i));
            }
        }
        return selected;
    }
}