package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.WashStationModel;
import com.example.prime.Model.WashSummaryModel;
import com.example.prime.R;

import java.util.ArrayList;

public class WashStationAdapter extends RecyclerView.Adapter<WashStationAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<WashStationModel> washStationModels;
    private boolean exist = false;
    public WashStationAdapter(Context context, ArrayList<WashStationModel> washStationModels) {
        this.context = context;
        this.washStationModels = washStationModels;
    }

    public void setStations(ArrayList<WashStationModel> washStationModels) {
        this.washStationModels = new ArrayList<>();
        this.washStationModels = washStationModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WashStationAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.childstationwashlayout, viewGroup, false);
        return new WashStationAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WashStationAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(washStationModels.get(position));
    }

    @Override
    public int getItemCount() {
        return washStationModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public LinearLayout linearLayout;


        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.serName);

        }

        void bind(final WashStationModel washStationModel) {
            name.setText(washStationModel.getStationName());
            if (name.getText().toString().equals("")){
                name.setVisibility(View.GONE);
            }



        }
    }

    public ArrayList<WashStationModel> getAll() {
        return washStationModels;
    }
}
