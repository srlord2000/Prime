package com.example.prime.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prime.Model.GroupModel;
import com.example.prime.Model.InventorySpinnerModel;
import com.example.prime.R;

import java.util.List;

public class InventorySpinnerAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<InventorySpinnerModel> items;
    private final int mResource;

    public InventorySpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                        @NonNull List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView unit = (TextView) view.findViewById(R.id.spinner_list_item);
        TextView id = (TextView) view.findViewById(R.id.spinner_list_id);
        InventorySpinnerModel dataObject = items.get(position);

        unit.setText(dataObject.getItemName());
        id.setText(dataObject.getId());


        return view;
    }
}