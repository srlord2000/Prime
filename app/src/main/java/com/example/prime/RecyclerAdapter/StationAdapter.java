package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.StationModel;
import com.example.prime.R;

import java.util.ArrayList;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<StationModel> station;
    private boolean exist = false;
    public StationAdapter(Context context, ArrayList<StationModel> stations) {
        this.context = context;
        this.station = stations;
    }

    public void setStations(ArrayList<StationModel> stations) {
        this.station = new ArrayList<>();
        this.station = stations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StationAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.stationlistlayout, viewGroup, false);
        return new StationAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StationAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(station.get(position));
    }

    @Override
    public int getItemCount() {
        return station.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextView name, unitname, ip, status;
        private LinearLayout linearLayout;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtStationName);
            unitname = itemView.findViewById(R.id.txtServiceType);
            ip = itemView.findViewById(R.id.txtIp);
            status = itemView.findViewById(R.id.txtStatus);
            linearLayout = itemView.findViewById(R.id.stationLayout);
        }

        void bind(final StationModel station) {
            linearLayout.setBackgroundColor(station.isChecked() ? Color.GRAY : Color.TRANSPARENT);
            name.setText(station.getStationName());
            unitname.setText(station.getUnitName());
            ip.setText(station.getIpaddress());
            status.setText(station.getStatus());
            ip.setVisibility(exist ? View.INVISIBLE:View.VISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    station.setChecked(!station.isChecked());
                    linearLayout.setBackgroundColor(station.isChecked() ? Color.GRAY : Color.TRANSPARENT);
                }
            });
        }
    }

    public ArrayList<StationModel> getAll() {
        return station;
    }

    public ArrayList<StationModel> getSelected() {
        ArrayList<StationModel> selected = new ArrayList<>();
        for (int i = 0; i < station.size(); i++) {
            if (station.get(i).isChecked()) {
                selected.add(station.get(i));
            }
        }
        return selected;
    }
}