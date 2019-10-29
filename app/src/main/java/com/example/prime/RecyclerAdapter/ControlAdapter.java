package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.ControlModel;
import com.example.prime.Model.DryerModel;
import com.example.prime.Model.StationModel;
import com.example.prime.Model.WasherModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ControlAdapter extends RecyclerView.Adapter<ControlAdapter.MultiViewHolder> {

    private DryerAdapter dryerAdapter;
    private WasherAdapter washerAdapter;
    private Context context;
    private ArrayList<DryerModel> dryerModels;
    private ArrayList<WasherModel> washerModels;
    private ArrayList<StationModel> stationModels;
    String GET_JSON_FROM_SERVER_NAME1 = "id";
    String GET_JSON_FROM_SERVER_NAME2 = "service_name";
    String GET_JSON_FROM_SERVER_NAME3 = "service_type";
    String GET_JSON_FROM_SERVER_NAME4 = "service_level";
    String GET_JSON_FROM_SERVER_NAME5 = "price";
    String GET_JSON_FROM_SERVER_NAME6 = "tap_pulse";
    String GET_JSON_FROM_SERVER_NAME7 = "time_added";
    String GET_JSON_FROM_SERVER_NAME8 = "unit_name";
    String GET_JSON_FROM_SERVER_NAME9 = "unit_id";
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private String TAG="ControlAdapter.java";
    private boolean exist = false;
    public ControlAdapter(Context context, ArrayList<StationModel> stationModels) {
        this.context = context;
        this.stationModels = stationModels;
    }

    public void setControl(ArrayList<StationModel> stationModels) {
        this.stationModels = new ArrayList<>();
        this.stationModels = stationModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ControlAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.listcontrollayout, viewGroup, false);
        return new ControlAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ControlAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(stationModels.get(position));
    }

    @Override
    public int getItemCount() {
        return stationModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private Button cmd;
        private LinearLayout linearLayout;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.stationNames);
            cmd = itemView.findViewById(R.id.btnCmd);
        }

        void bind(final StationModel controlModel) {
            dryerModels = new ArrayList<>();
            washerModels = new ArrayList<>();
            sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(context);
            apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
            id = sharedPrefsCookiePersistor.loadAll().get(0).value();
            name.setText(controlModel.getStationName());
            cmd.setTag(controlModel.getUnitId());
            cmd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dryerModels = new ArrayList<>();
                    washerModels = new ArrayList<>();
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                    final View mView = LayoutInflater.from(context).inflate(R.layout.controllist, null);
                    final RecyclerView recyclerViewWasher = mView.findViewById(R.id.recyclerWash);
                    final RecyclerView recyclerViewDryer = mView.findViewById(R.id.recyclerDryer);
                    final CardView cardViewWash = mView.findViewById(R.id.cardWash);
                    final CardView cardViewDry = mView.findViewById(R.id.cardDry);
                    Button abort = mView.findViewById(R.id.abort);
                    recyclerViewDryer.setLayoutManager(new GridLayoutManager(context,3));
                    recyclerViewDryer.setHasFixedSize(true);
                    recyclerViewWasher.setLayoutManager(new GridLayoutManager(context,3));
                    recyclerViewWasher.setHasFixedSize(true);
                    dryerAdapter = new DryerAdapter(context, dryerModels);
                    washerAdapter = new WasherAdapter(context, washerModels);
                    recyclerViewWasher.setAdapter(washerAdapter);
                    recyclerViewDryer.setAdapter(dryerAdapter);
                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    if (dialog.getWindow() != null){
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }

                    retrofit2.Call<ResponseBody> call = apiInterface.getServiceId("ci_session="+id,cmd.getTag().toString());
                    call.enqueue(new  retrofit2.Callback<ResponseBody>() {
                        @Override
                        public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                if(response.body() != null) {
                                    String res = response.body().string();
                                    dryerModels = new ArrayList<>();
                                    washerModels = new ArrayList<>();
                                    try {
                                        dryerModels.clear();
                                        washerModels.clear();
                                        Log.e(TAG, "onResponse222: "+res );
                                        JSONArray array = new JSONArray(res);
                                        for (int i = 0; i < array.length(); i++) {
                                            String type;
                                            WasherModel washerModel = new WasherModel();
                                            DryerModel dryerModel = new DryerModel();
                                            JSONObject json = null;
                                            try {
                                                json = array.getJSONObject(i);
                                                type = json.getString(GET_JSON_FROM_SERVER_NAME3);
                                                if(type.toLowerCase().equals("wash")) {
                                                    cardViewWash.setVisibility(View.VISIBLE);
                                                    washerModel.setId(json.getString(GET_JSON_FROM_SERVER_NAME1));
                                                    washerModel.setServiceName(json.getString(GET_JSON_FROM_SERVER_NAME2));
                                                    washerModel.setServiceType(json.getString(GET_JSON_FROM_SERVER_NAME3));
                                                    washerModel.setServiceLevel(json.getString(GET_JSON_FROM_SERVER_NAME4));
                                                    washerModel.setPrice(json.getString(GET_JSON_FROM_SERVER_NAME5));
                                                    washerModel.setTapPulse(json.getString(GET_JSON_FROM_SERVER_NAME6));
                                                    washerModel.setTimeAdded(json.getString(GET_JSON_FROM_SERVER_NAME7));
                                                    washerModel.setUnitName(json.getString(GET_JSON_FROM_SERVER_NAME8));
                                                    washerModel.setUnitId(json.getString(GET_JSON_FROM_SERVER_NAME9));
                                                }else if (type.toLowerCase().equals("dry")){
                                                    cardViewDry.setVisibility(View.VISIBLE);
                                                    dryerModel.setId(json.getString(GET_JSON_FROM_SERVER_NAME1));
                                                    dryerModel.setServiceName(json.getString(GET_JSON_FROM_SERVER_NAME2));
                                                    dryerModel.setServiceType(json.getString(GET_JSON_FROM_SERVER_NAME3));
                                                    dryerModel.setServiceLevel(json.getString(GET_JSON_FROM_SERVER_NAME4));
                                                    dryerModel.setPrice(json.getString(GET_JSON_FROM_SERVER_NAME5));
                                                    dryerModel.setTapPulse(json.getString(GET_JSON_FROM_SERVER_NAME6));
                                                    dryerModel.setTimeAdded(json.getString(GET_JSON_FROM_SERVER_NAME7));
                                                    dryerModel.setUnitName(json.getString(GET_JSON_FROM_SERVER_NAME8));
                                                    dryerModel.setUnitId(json.getString(GET_JSON_FROM_SERVER_NAME9));

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            dryerModels.add(dryerModel);
                                            washerModels.add(washerModel);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    washerAdapter.setWasherModels(washerModels);
                                    washerAdapter.notifyDataSetChanged();
                                    dryerAdapter.setDryerModels(dryerModels);
                                    dryerAdapter.notifyDataSetChanged();
                                    }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure( retrofit2.Call<ResponseBody> call, Throwable t) {
                            Log.d(TAG, "onFailure: ");
                        }
                    });
                }
            });
        }
    }

    public ArrayList<StationModel> getAll() {
        return stationModels;
    }

    public ArrayList<StationModel> getSelected() {
        ArrayList<StationModel> selected = new ArrayList<>();
        for (int i = 0; i < stationModels.size(); i++) {
            if (stationModels.get(i).isChecked()) {
                selected.add(stationModels.get(i));
            }
        }
        return selected;
    }
}