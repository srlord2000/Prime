package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.AddDataModel;
import com.example.prime.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AddDataAdapter extends RecyclerView.Adapter<AddDataAdapter.MultiViewHolder> {
    private String name1;
    private Context context;
    private ArrayList<AddDataModel> presets;
    private Cursor cursor;
    private int lastCheckedPosition = -1;
    public AddDataAdapter(Context context, ArrayList<AddDataModel> cards) {
        this.context = context;
        this.presets = cards;
    }

    public void setPreset(ArrayList<AddDataModel> cards) {
        this.presets = new ArrayList<>();
        this.presets = cards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddDataAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.addnewdatalayout, viewGroup, false);
        return new AddDataAdapter.MultiViewHolder(view);
    }

    public List<AddDataModel> getData() {
        return presets;
    }

    @Override
    public void onBindViewHolder(@NonNull AddDataAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(presets.get(position));
    }

    @Override
    public int getItemCount() {
        return presets.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextView unit_id, price, pulse, type, level;
        private LinearLayout linearLayout;
        private Button delete,edit;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            unit_id = itemView.findViewById(R.id.txtName);
            type = itemView.findViewById(R.id.txtType);
            level = itemView.findViewById(R.id.txtLevel);
            price = itemView.findViewById(R.id.txtPrice);
            pulse = itemView.findViewById(R.id.txtPulse);
            linearLayout = itemView.findViewById(R.id.presetLayout);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
        }

        void bind(final AddDataModel preset) {
            unit_id.setText(preset.getName());
            type.setText(preset.getType());
            level.setText(preset.getLevel());
            price.setText(preset.getPrice());
            pulse.setText(preset.getPulse());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presets.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });




            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAdapterPosition();
                    Log.e(TAG, "onClick: "+pos);
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                    View mView =  LayoutInflater.from(context).inflate(R.layout.addnewpresetdialog, null);
                    final EditText name = mView.findViewById(R.id.etServiceName);
                    final Spinner type = mView.findViewById(R.id.etServiceType);
                    final EditText level = mView.findViewById(R.id.etLevel);
                    final EditText price = mView.findViewById(R.id.etPrice);
                    final EditText pulse = mView.findViewById(R.id.etPulse);
                    Button submit = mView.findViewById(R.id.btnSubmit);
                    Button close = mView.findViewById(R.id.btnCancel1);

                    name.setText(presets.get(pos).getName());
                    int index;
                    for(int i = 0; i<type.getCount(); i++) {
                        if (type.getItemAtPosition(i).toString().equalsIgnoreCase(presets.get(pos).getType())){
                            index = i;
                            type.setSelection(index);
                            break;
                        }
                    }
                    level.setText(presets.get(pos).getLevel());
                    price.setText(presets.get(pos).getPrice());
                    pulse.setText(presets.get(pos).getPulse());

                    type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            int id = adapterView.getSelectedItemPosition();
                            name1 = adapterView.getSelectedItem().toString();
                            Log.e(TAG, "onNothingSelected: "+name1 );
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            int id = adapterView.getFirstVisiblePosition();
                            name1 = adapterView.getSelectedItem().toString();
                            Log.e(TAG, "onNothingSelected: " + name1 );

                        }
                    });

                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AddDataModel add = new AddDataModel();
                            add.setName(name.getText().toString());
                            add.setType(name1);
                            add.setLevel(level.getText().toString());
                            add.setPrice(price.getText().toString());
                            add.setPulse(pulse.getText().toString());
                            presets.set(pos, add);
                            notifyItemChanged(pos);
                            dialog.dismiss();
                        }
                    });
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });




            linearLayout.setBackgroundColor(preset.isChecked() ? Color.GRAY : Color.TRANSPARENT);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    preset.setChecked(!preset.isChecked());
                    preset.setPos(getAdapterPosition());
                    linearLayout.setBackgroundColor(preset.isChecked() ? Color.GRAY : Color.TRANSPARENT);

                }
            });
        }
    }


    public void deleteAtIndex(int index){
        presets.remove(index);
        notifyDataSetChanged();
    }

    public ArrayList<AddDataModel> getAll() {
        return presets;
    }

    public ArrayList<AddDataModel> getSelected() {
        ArrayList<AddDataModel> selected = new ArrayList<>();
        for (int i = 0; i < presets.size(); i++) {
            if (presets.get(i).isChecked()) {
                selected.add(presets.get(i));
            }
        }
        return selected;
    }

}