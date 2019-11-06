package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.DrySummaryModel;
import com.example.prime.Model.ServiceModel;
import com.example.prime.R;

import java.util.ArrayList;

public class DrySummaryAdapter extends RecyclerView.Adapter<DrySummaryAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<DrySummaryModel> drySummaryModels;
    private boolean exist = false;
    public DrySummaryAdapter(Context context, ArrayList<DrySummaryModel> drySummaryModels) {
        this.context = context;
        this.drySummaryModels = drySummaryModels;
    }

    public void setStations(ArrayList<DrySummaryModel> drySummaryModels) {
        this.drySummaryModels = new ArrayList<>();
        this.drySummaryModels = drySummaryModels;
        notifyDataSetChanged();
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


        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.serName);
        }

        void bind(final DrySummaryModel drySummaryModel) {
            name.setText(drySummaryModel.getServiceName());
            if (name.getText().toString().equals("")){
                name.setVisibility(View.GONE);
            }

        }
    }

    public ArrayList<DrySummaryModel> getAll() {
        return drySummaryModels;
    }
}