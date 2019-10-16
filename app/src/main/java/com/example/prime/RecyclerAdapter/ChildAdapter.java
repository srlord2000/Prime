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
import com.example.prime.R;

import java.util.ArrayList;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<ServiceModel> serviceModels;
    private boolean exist = false;
    public ChildAdapter(Context context, ArrayList<ServiceModel> serviceModels) {
        this.context = context;
        this.serviceModels = serviceModels;
    }

    public void setStations(ArrayList<ServiceModel> serviceModels) {
        this.serviceModels = new ArrayList<>();
        this.serviceModels = serviceModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChildAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.childlayout, viewGroup, false);
        return new ChildAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(serviceModels.get(position));
    }

    @Override
    public int getItemCount() {
        return serviceModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextView id, price, pulse;
        private LinearLayout linearLayout;


        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.txtLevel);
            price = itemView.findViewById(R.id.txtPrice);
            pulse = itemView.findViewById(R.id.txtPulse);
        }

        void bind(final ServiceModel serviceModel) {
            id.setText(serviceModel.getServiceName());
            price.setText(serviceModel.getPrice());
            pulse.setText(serviceModel.getTapPulse());
        }
    }

    public ArrayList<ServiceModel> getAll() {
        return serviceModels;
    }
}