package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.ServiceModel;
import com.example.prime.Model.WashSummaryModel;
import com.example.prime.R;

import java.util.ArrayList;

public class WashSummaryAdapter extends RecyclerView.Adapter<WashSummaryAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<WashSummaryModel> washSummaryModels;
    private boolean exist = false;
    public WashSummaryAdapter(Context context, ArrayList<WashSummaryModel> washSummaryModels) {
        this.context = context;
        this.washSummaryModels = washSummaryModels;
    }

    public void setStations(ArrayList<WashSummaryModel> washSummaryModels) {
        this.washSummaryModels = new ArrayList<>();
        this.washSummaryModels = washSummaryModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WashSummaryAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.childwashlistlayout, viewGroup, false);
        return new WashSummaryAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WashSummaryAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(washSummaryModels.get(position));
    }

    @Override
    public int getItemCount() {
        return washSummaryModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public LinearLayout linearLayout;


        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.serName);

        }

        void bind(final WashSummaryModel washSummaryModel) {
            name.setText(washSummaryModel.getServiceName());
            if (name.getText().toString().equals("")){
                name.setVisibility(View.GONE);
            }



        }
    }

    public ArrayList<WashSummaryModel> getAll() {
        return washSummaryModels;
    }
}