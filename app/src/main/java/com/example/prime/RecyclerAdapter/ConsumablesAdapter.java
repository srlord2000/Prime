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
import com.example.prime.Model.InventoryModel;
import com.example.prime.R;

import java.util.ArrayList;

public class ConsumablesAdapter extends RecyclerView.Adapter<ConsumablesAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<ConsumablesModel> consumablesModels;
    private Cursor cursor;

    public ConsumablesAdapter(Context context, ArrayList<ConsumablesModel> consumablesModels ) {
        this.context = context;
        this.consumablesModels = consumablesModels;
    }

    public void setConsumables(ArrayList<ConsumablesModel> consumablesModels) {
        this.consumablesModels = new ArrayList<>();
        this.consumablesModels = consumablesModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConsumablesAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.consumableslistlayout, viewGroup, false);

        return new ConsumablesAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsumablesAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(consumablesModels.get(position));
    }

    @Override
    public int getItemCount() {
        return consumablesModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextView itemname, time, price;
        private LinearLayout linearLayout;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            itemname = itemView.findViewById(R.id.txtConsumablesItem);
            time = itemView.findViewById(R.id.txtConsumablesTime);
            price = itemView.findViewById(R.id.txtConsumablesPrice);
            linearLayout = itemView.findViewById(R.id.consumablesLayout);
        }
        void bind(final ConsumablesModel consumablesModel) {
            linearLayout.setBackgroundColor(consumablesModel.isChecked() ? Color.GRAY : Color.TRANSPARENT);
            itemname.setText(consumablesModel.getItemName());
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

    public ArrayList<ConsumablesModel> getAll() {
        return consumablesModels;
    }

    public ArrayList<ConsumablesModel> getSelected() {
        ArrayList<ConsumablesModel> selected = new ArrayList<>();
        for (int i = 0; i < consumablesModels.size(); i++) {
            if (consumablesModels.get(i).isChecked()) {
                selected.add(consumablesModels.get(i));
            }
        }
        return selected;
    }
}