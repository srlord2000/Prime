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
import com.example.prime.R;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<CardsModel> card;
    private Cursor cursor;

    public CardAdapter(Context context, ArrayList<CardsModel> cards) {
        this.context = context;
        this.card = cards;
    }

    public void setCards(ArrayList<CardsModel> cards) {
        this.card = new ArrayList<>();
        this.card = cards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardlistlayout, viewGroup, false);

        return new MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(card.get(position));
    }

    @Override
    public int getItemCount() {
        return card.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextView id, type, level, time;
        private LinearLayout linearLayout;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.txtId);
            type = itemView.findViewById(R.id.txtServiceType);
            level = itemView.findViewById(R.id.txtServiceLevel);
            time = itemView.findViewById(R.id.txtTime);
            linearLayout = itemView.findViewById(R.id.cardLayout);
        }
        void bind(final CardsModel card) {
            linearLayout.setBackgroundColor(card.isChecked() ? Color.GRAY : Color.TRANSPARENT);
            id.setText(card.getId());
            type.setText(card.getServiceType());
            level.setText(card.getServiceLevel());
            time.setText(card.getTimeAdded());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    card.setChecked(!card.isChecked());
                    linearLayout.setBackgroundColor(card.isChecked() ? Color.GRAY : Color.TRANSPARENT);

                }
            });
        }
    }

    public ArrayList<CardsModel> getAll() {
        return card;
    }

    public ArrayList<CardsModel> getSelected() {
        ArrayList<CardsModel> selected = new ArrayList<>();
        for (int i = 0; i < card.size(); i++) {
            if (card.get(i).isChecked()) {
                selected.add(card.get(i));
            }
        }
        return selected;
    }
}