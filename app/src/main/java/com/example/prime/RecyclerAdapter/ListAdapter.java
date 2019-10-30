package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.CardsModel;
import com.example.prime.Model.ListModel;
import com.example.prime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.android.volley.VolleyLog.TAG;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<ListModel> listModels;
    private Cursor cursor;

    public ListAdapter(Context context, ArrayList<ListModel> listModels) {
        this.context = context;
        this.listModels = listModels;
    }

    public void setListModels(ArrayList<ListModel> listModels) {
        this.listModels = new ArrayList<>();
        this.listModels = listModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.listlayout, viewGroup, false);

        return new ListAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(listModels.get(position));
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextView time, station, type, service;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.timeList);
            station = itemView.findViewById(R.id.stationList);
            type = itemView.findViewById(R.id.typeList);
            service = itemView.findViewById(R.id.serviceList);
        }
        void bind(final ListModel listModel) {
            final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.US);
            Date d = null;
            String curtime = listModel.getTimeAdded();
            try {
                d = sdf1.parse(curtime);
                Log.i(TAG, "bind: "+sdf2.format(d));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(d != null) {
                time.setText(sdf2.format(d));
            }
            station.setText(listModel.getStationName());
            type.setText(listModel.getServiceType());
            service.setText(listModel.getServiceName());
        }
    }

    public ArrayList<ListModel> getAll() {
        return listModels;
    }

    public ArrayList<ListModel> getSelected() {
        ArrayList<ListModel> selected = new ArrayList<>();
        for (int i = 0; i < listModels.size(); i++) {
            if (listModels.get(i).isChecked()) {
                selected.add(listModels.get(i));
            }
        }
        return selected;
    }
}