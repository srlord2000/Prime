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
import com.example.prime.Model.ClientsModel;
import com.example.prime.R;

import java.util.ArrayList;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<ClientsModel> clientsModels;
    private Cursor cursor;

    public ClientsAdapter(Context context, ArrayList<ClientsModel> clientsModels) {
        this.context = context;
        this.clientsModels = clientsModels;
    }

    public void setClients(ArrayList<ClientsModel> clientsModels) {
        this.clientsModels = new ArrayList<>();
        this.clientsModels = clientsModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClientsAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.clientlistlayout, viewGroup, false);
        return new ClientsAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientsAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(clientsModels.get(position));
    }

    @Override
    public int getItemCount() {
        return clientsModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextView email;
        private LinearLayout linearLayout;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.txtEmail);
            linearLayout = itemView.findViewById(R.id.clientLayout);
        }
        void bind(final ClientsModel clientsModel) {
            linearLayout.setBackgroundColor(clientsModel.isChecked() ? Color.GRAY : Color.TRANSPARENT);
            email.setText(clientsModel.getEmail());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clientsModel.setChecked(!clientsModel.isChecked());
                    linearLayout.setBackgroundColor(clientsModel.isChecked() ? Color.GRAY : Color.TRANSPARENT);

                }
            });
        }
    }

    public ArrayList<ClientsModel> getAll() {
        return clientsModels;
    }

    public ArrayList<ClientsModel> getSelected() {
        ArrayList<ClientsModel> selected = new ArrayList<>();
        for (int i = 0; i < clientsModels.size(); i++) {
            if (clientsModels.get(i).isChecked()) {
                selected.add(clientsModels.get(i));
            }
        }
        return selected;
    }
}