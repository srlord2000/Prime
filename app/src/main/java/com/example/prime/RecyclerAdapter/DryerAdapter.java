package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.DryerModel;
import com.example.prime.Model.WasherModel;
import com.example.prime.R;

import java.util.ArrayList;

public class DryerAdapter extends RecyclerView.Adapter<DryerAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<DryerModel> dryerModels;
    private Cursor cursor;

    public DryerAdapter(Context context, ArrayList<DryerModel> dryerModels) {
        this.context = context;
        this.dryerModels = dryerModels;
    }

    public void setDryerModels(ArrayList<DryerModel> dryerModels) {
        this.dryerModels = new ArrayList<>();
        this.dryerModels = dryerModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DryerAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dryerlayout, viewGroup, false);
        return new DryerAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DryerAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(dryerModels.get(position));
    }

    @Override
    public int getItemCount() {
        return dryerModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private Button cmd;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            cmd = itemView.findViewById(R.id.dryBtn);
        }
        void bind(final DryerModel washerModel) {
            cmd.setText(washerModel.getServiceName());
            cmd.setTag(washerModel.getId());
            if (cmd.getText().toString().equals("")){
                cmd.setVisibility(View.GONE);
            }

        }
    }

    public ArrayList<DryerModel> getAll() {
        return dryerModels;
    }

}