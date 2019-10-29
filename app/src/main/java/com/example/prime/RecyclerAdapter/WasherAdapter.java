package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.ClientsModel;
import com.example.prime.Model.WasherModel;
import com.example.prime.R;

import java.util.ArrayList;

public class WasherAdapter extends RecyclerView.Adapter<WasherAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<WasherModel> washerModels;
    private Cursor cursor;

    public WasherAdapter(Context context, ArrayList<WasherModel> washerModels) {
        this.context = context;
        this.washerModels = washerModels;
    }

    public void setWasherModels(ArrayList<WasherModel> washerModels) {
        this.washerModels = new ArrayList<>();
        this.washerModels = washerModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WasherAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.washerlayout, viewGroup, false);
        return new WasherAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WasherAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(washerModels.get(position));
    }

    @Override
    public int getItemCount() {
        return washerModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private Button cmd;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            cmd = itemView.findViewById(R.id.washerBtn);
        }
        void bind(final WasherModel washerModel) {
            cmd.setText(washerModel.getServiceName());
            cmd.setTag(washerModel.getId());
            if (cmd.getText().toString().equals("")){
                cmd.setVisibility(View.GONE);
            }

        }
    }

    public ArrayList<WasherModel> getAll() {
        return washerModels;
    }

}