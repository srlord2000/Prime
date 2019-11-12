package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.DryStationModel;
import com.example.prime.Model.DrySummaryModel;
import com.example.prime.R;

import java.util.ArrayList;

public class DryStationAdapter extends RecyclerView.Adapter<DryStationAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<DryStationModel> dryStationModels;
    private boolean exist = false;
    public DryStationAdapter(Context context, ArrayList<DryStationModel> dryStationModels) {
        this.context = context;
        this.dryStationModels = dryStationModels;
    }

    public void setStations(ArrayList<DryStationModel> dryStationModels) {
        this.dryStationModels = new ArrayList<>();
        this.dryStationModels = dryStationModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DryStationAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.childstationdry, viewGroup, false);
        return new DryStationAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DryStationAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(dryStationModels.get(position));
    }

    @Override
    public int getItemCount() {
        return dryStationModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public LinearLayout linearLayout;


        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.serName);
        }

        void bind(final DryStationModel dryStationModel) {
            name.setText(dryStationModel.getStationName());
            if (name.getText().toString().equals("")){
                name.setVisibility(View.GONE);
            }

        }
    }

    public ArrayList<DryStationModel> getAll() {
        return dryStationModels;
    }
}
