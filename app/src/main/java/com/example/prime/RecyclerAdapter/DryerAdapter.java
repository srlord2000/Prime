package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.DryerModel;
import com.example.prime.Model.WasherModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.android.volley.VolleyLog.TAG;

public class DryerAdapter extends RecyclerView.Adapter<DryerAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<DryerModel> dryerModels;
    private Cursor cursor;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;

    public DryerAdapter(Context context, ArrayList<DryerModel> dryerModels) {
        this.context = context;
        this.dryerModels = dryerModels;
    }

    public void setDryerModels(ArrayList<DryerModel> dryerModels) {
        this.dryerModels = new ArrayList<>();
        this.dryerModels = dryerModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DryerAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dryerlayout, viewGroup, false);
        return new DryerAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DryerAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(dryerModels.get(position));
    }

    @Override
    public int getItemCount() {
        return dryerModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private Button cmd;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            cmd = itemView.findViewById(R.id.dryBtn);
        }
        void bind(final DryerModel dryerModel) {
            sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(context);
            apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
            id = sharedPrefsCookiePersistor.loadAll().get(0).value();
            cmd.setText(dryerModel.getServiceName());
            cmd.setTag(dryerModel.getId());
            if (cmd.getText().toString().equals("")){
                cmd.setVisibility(View.GONE);
            }
            cmd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(context);
                    apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
                    id = sharedPrefsCookiePersistor.loadAll().get(0).value();
                    String name, type, level;
                    name = ControlAdapter.n.toLowerCase();
                    type = dryerModel.getServiceType().toLowerCase();
                    level = dryerModel.getServiceLevel();
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("name", name);
                        obj.put("type", type);
                        obj.put("level", level);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(String.valueOf(obj), JSON);
                    retrofit2.Call<ResponseBody> call = apiInterface.postStationStart("ci_session="+id,body);
                    call.enqueue(new  retrofit2.Callback<ResponseBody>() {
                        @Override
                        public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.e(TAG, "onResponse: "+response );
                            try {
                                if(response.body()!= null) {
                                    String res = response.body().string();
                                    Log.e(TAG, "onResponse: "+res );
                                    JSONObject jsonObject = new JSONObject(res);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
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

    public ArrayList<DryerModel> getAll() {
        return dryerModels;
    }

}