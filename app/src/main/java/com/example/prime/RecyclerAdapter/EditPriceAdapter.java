package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.EditPriceModel;
import com.example.prime.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class EditPriceAdapter extends RecyclerView.Adapter<EditPriceAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<EditPriceModel> editPriceModels;
    private Cursor cursor;

    public EditPriceAdapter(Context context, ArrayList<EditPriceModel> editPriceModels) {
        this.context = context;
        this.editPriceModels = editPriceModels;
    }

    public void setPrice(ArrayList<EditPriceModel> editPriceModels) {
        this.editPriceModels = new ArrayList<>();
        this.editPriceModels = editPriceModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EditPriceAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.editpricelistlayout, viewGroup, false);

        return new EditPriceAdapter.MultiViewHolder(view);
    }

    public ArrayList<EditPriceModel> getData() {
        return editPriceModels;
    }

    @Override
    public void onBindViewHolder(@NonNull EditPriceAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(editPriceModels.get(position));
    }

    @Override
    public int getItemCount() {
        return editPriceModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextInputLayout unit_id, price, pulse;
        private EditText name;
        private LinearLayout linearLayout;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            unit_id = itemView.findViewById(R.id.unit);
            name = itemView.findViewById(R.id.prices);
        }

        void bind(final EditPriceModel priceModel) {
            unit_id.setHint(priceModel.getName());
            name.setText(priceModel.getPrice());
            name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    priceModel.setPrice(charSequence.toString());
                    Log.e(TAG, "onTextChanged: "+charSequence.toString() );
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }




    public ArrayList<EditPriceModel> getAll() {
        return editPriceModels;
    }

    public ArrayList<EditPriceModel> getSelected() {
        ArrayList<EditPriceModel> selected = new ArrayList<>();
        for (int i = 0; i < editPriceModels.size(); i++) {
            if (editPriceModels.get(i).isChecked()) {
                selected.add(editPriceModels.get(i));
            }
        }
        return selected;
    }
}