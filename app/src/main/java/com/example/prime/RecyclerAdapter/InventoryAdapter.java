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

import com.example.prime.Model.CardsModel;
import com.example.prime.Model.InventoryModel;
import com.example.prime.R;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<InventoryModel> inventoryModels;
    private Cursor cursor;

    public InventoryAdapter(Context context, ArrayList<InventoryModel> inventoryModels) {
        this.context = context;
        this.inventoryModels = inventoryModels;
    }

    public void setInventory(ArrayList<InventoryModel> inventoryModels) {
        this.inventoryModels = new ArrayList<>();
        this.inventoryModels = inventoryModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InventoryAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.inventorylistlayout, viewGroup, false);

        return new InventoryAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(inventoryModels.get(position));
    }

    @Override
    public int getItemCount() {
        return inventoryModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextView itemname, stock, price;
        private LinearLayout linearLayout;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            itemname = itemView.findViewById(R.id.txtItemName);
            stock = itemView.findViewById(R.id.txtItemStock);
            price = itemView.findViewById(R.id.txtItemPrice);
            linearLayout = itemView.findViewById(R.id.inventoryLayout);
        }
        void bind(final InventoryModel inventoryModel) {
            linearLayout.setBackgroundColor(inventoryModel.isChecked() ? Color.GRAY : Color.TRANSPARENT);
            itemname.setText(inventoryModel.getItemName());
            stock.setText(inventoryModel.getStock());
            price.setText(inventoryModel.getPrice());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inventoryModel.setChecked(!inventoryModel.isChecked());
                    linearLayout.setBackgroundColor(inventoryModel.isChecked() ? Color.GRAY : Color.TRANSPARENT);

                }
            });
        }
    }

    public ArrayList<InventoryModel> getAll() {
        return inventoryModels;
    }

    public ArrayList<InventoryModel> getSelected() {
        ArrayList<InventoryModel> selected = new ArrayList<>();
        for (int i = 0; i < inventoryModels.size(); i++) {
            if (inventoryModels.get(i).isChecked()) {
                selected.add(inventoryModels.get(i));
            }
        }
        return selected;
    }
}