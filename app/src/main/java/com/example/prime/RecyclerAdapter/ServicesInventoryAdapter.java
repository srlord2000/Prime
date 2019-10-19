package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.ConsumablesModel;
import com.example.prime.Model.ServicesInventoryModel;
import com.example.prime.R;

import java.util.ArrayList;

public class ServicesInventoryAdapter extends RecyclerView.Adapter<ServicesInventoryAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<ServicesInventoryModel> servicesInventoryModels;

    public ServicesInventoryAdapter(Context context, ArrayList<ServicesInventoryModel> servicesInventoryModels ) {
        this.context = context;
        this.servicesInventoryModels = servicesInventoryModels;
    }

    public void setServices(ArrayList<ServicesInventoryModel> servicesInventoryModels) {
        this.servicesInventoryModels = new ArrayList<>();
        this.servicesInventoryModels = servicesInventoryModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServicesInventoryAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.servicelistlayout, viewGroup, false);

        return new ServicesInventoryAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesInventoryAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(servicesInventoryModels.get(position));
    }

    @Override
    public int getItemCount() {
        return servicesInventoryModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextView itemname, time, price;
        private LinearLayout linearLayout;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            itemname = itemView.findViewById(R.id.txtServicesItem);
            time = itemView.findViewById(R.id.txtServicesTime);
            price = itemView.findViewById(R.id.txtServicesPrice);
            linearLayout = itemView.findViewById(R.id.servicesLayout);
        }
        void bind(final ServicesInventoryModel consumablesModel) {
            linearLayout.setBackgroundColor(consumablesModel.isChecked() ? Color.GRAY : Color.TRANSPARENT);
            itemname.setText(consumablesModel.getServiceName());
            time.setText(consumablesModel.getTimeAdded());
            price.setText(consumablesModel.getPrice());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    consumablesModel.setChecked(!consumablesModel.isChecked());
                    linearLayout.setBackgroundColor(consumablesModel.isChecked() ? Color.GRAY : Color.TRANSPARENT);

                }
            });
        }
    }

    public ArrayList<ServicesInventoryModel> getAll() {
        return servicesInventoryModels;
    }

    public ArrayList<ServicesInventoryModel> getSelected() {
        ArrayList<ServicesInventoryModel> selected = new ArrayList<>();
        for (int i = 0; i < servicesInventoryModels.size(); i++) {
            if (servicesInventoryModels.get(i).isChecked()) {
                selected.add(servicesInventoryModels.get(i));
            }
        }
        return selected;
    }
}