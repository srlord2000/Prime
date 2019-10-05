package com.example.prime.Views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.prime.Model.CardsModel;
import com.example.prime.R;
import com.example.prime.RecyclerAdapter.CardAdapter;

import java.util.ArrayList;

import okhttp3.MediaType;

public class Card extends Fragment {


    private ArrayList<CardsModel> cardList;
    private String text1;
    private RecyclerView recyclerView;
    private Button add, delete;
    private CardAdapter cardAdapter;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String HTTP_JSON_URL = "http://192.168.0.100/cards/show";
    String GET_JSON_FROM_SERVER_NAME1 = "id";
    String GET_JSON_FROM_SERVER_NAME2 = "service_type";
    String GET_JSON_FROM_SERVER_NAME3 = "service_level";
    String GET_JSON_FROM_SERVER_NAME4 = "time_added";
    JsonArrayRequest jsonArrayRequest;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.views_account, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {


    }

}
