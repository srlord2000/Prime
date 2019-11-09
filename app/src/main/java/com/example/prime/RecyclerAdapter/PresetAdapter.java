package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.ServiceModel;
import com.example.prime.Model.UnitModel;
import com.example.prime.R;

import java.util.ArrayList;

public class PresetAdapter extends RecyclerView.Adapter<PresetAdapter.MultiViewHolder> {
    private ChildAdapter childAdapter;
    private ArrayList<ServiceModel> arrayList;
    private Context context;
    private ArrayList<UnitModel> unitModels;
    private boolean exist = false;
    private static int sPosition;
    private static SparseBooleanArray sSelectedItems;
    private static AdapterClickListener sClickListener;
    private static final int MULTIPLE = 0;
    private static final int SINGLE = 1;
    private static int sModo = 1;

    public PresetAdapter(Context context, ArrayList<UnitModel> stations) {
        this.context = context;
        this.unitModels = stations;
    }

    public void setPreset(ArrayList<UnitModel> stations) {
        this.unitModels = new ArrayList<>();
        this.unitModels = stations;
        sSelectedItems = new SparseBooleanArray();
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public PresetAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.presetlistlayout, viewGroup, false);
        return new PresetAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PresetAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(unitModels.get(position));
    }

    @Override
    public int getItemCount() {
        return unitModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener  {

        private TextView name, unitname, ip, status;
        private LinearLayout linearLayout;
        private RecyclerView recyclerView ;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtUnitName);
            recyclerView = itemView.findViewById(R.id.listView);
            linearLayout = itemView.findViewById(R.id.presetLayout);
        }

        void bind(final UnitModel unitModel) {
//            linearLayout.setBackgroundColor(station.isChecked() ? Color.GRAY : Color.TRANSPARENT);
//            recyclerView.setBackgroundColor(station.isChecked() ? Color.GRAY : Color.TRANSPARENT);

            name.setText(unitModel.getUnitName());
            childAdapter = new ChildAdapter(context,arrayList);
            arrayList = unitModel.getServiceModels();
            childAdapter.setStations(arrayList);
            childAdapter.notifyDataSetChanged();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(childAdapter);
            recyclerView.setHasFixedSize(true);
            itemView.setOnClickListener(this);


            if (sSelectedItems.get(getAdapterPosition())) {
                linearLayout.setBackgroundColor(Color.parseColor("#707070"));
                recyclerView.setBackgroundColor(Color.parseColor("#707070"));
            } else {
                linearLayout.setBackgroundColor(Color.parseColor("#ededed"));
                recyclerView.setBackgroundColor(Color.parseColor("#ededed"));
            }

            linearLayout.setSelected(sSelectedItems.get(getAdapterPosition(), false));
//            ip.setVisibility(exist ? View.INVISIBLE:View.VISIBLE);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    station.setChecked(!station.isChecked());
//                    linearLayout.setBackgroundColor(station.isChecked() ? Color.GRAY : Color.TRANSPARENT);
//                    recyclerView.setBackgroundColor(station.isChecked() ? Color.GRAY : Color.TRANSPARENT);
//                }
//            });
        }

        @Override
        public void onClick(View v) {
            if (sSelectedItems.get(getAdapterPosition(), false)) {
                sSelectedItems.delete(getAdapterPosition());
                linearLayout.setSelected(false);
                linearLayout.setBackgroundColor(Color.parseColor("#ededed"));
                recyclerView.setBackgroundColor(Color.parseColor("#ededed"));
            } else {
                switch (sModo) {
                    case SINGLE:
                        sSelectedItems.put(sPosition, false);
                        break;
                    case MULTIPLE:
                    default:
                        break;
                }
                linearLayout.setBackgroundColor(Color.parseColor("#707070"));
                recyclerView.setBackgroundColor(Color.parseColor("#707070"));
                sSelectedItems.put(getAdapterPosition(), true);
                linearLayout.setSelected(true);
            }
            sClickListener.onItemClick(getAdapterPosition());
        }
    }


    public void setOnItemClickListener(AdapterClickListener clickListener) {
        sClickListener = clickListener;
    }



    public void selected(int position) {
        switch (sModo) {
            case SINGLE:
                sPosition = position;
                notifyDataSetChanged();
                break;
            case MULTIPLE:
            default:
                break;
        }
    }

    public ArrayList<UnitModel> getAll() {
        return unitModels;
    }

//    public ArrayList<StationModel> getSelected() {
//        ArrayList<StationModel> selected = new ArrayList<>();
//        for (int i = 0; i < station.size(); i++) {
//            if (station.get(i).isChecked()) {
//                selected.add(station.get(i));
//            }
//        }
//        return selected;
//    }

    public interface AdapterClickListener {
        void onItemClick(int position);
    }
}