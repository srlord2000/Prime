package com.example.prime.RecyclerAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.DryStationModel;
import com.example.prime.Model.WashStationModel;
import com.example.prime.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class DryStationTotalAdapter extends RecyclerView.Adapter<DryStationTotalAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<DryStationModel> stationTotalModels;
    private ArrayList<Double> integers;
    SharedPreferences prefs ;
    SharedPreferences.Editor editor;
    public static Boolean running;
    public static Thread MyThread;

    public DryStationTotalAdapter(Context context, ArrayList<DryStationModel> stationTotalModels) {
        this.context = context;
        this.stationTotalModels = stationTotalModels;
    }

    public void setStations(ArrayList<DryStationModel> stationTotalModels) {
        this.stationTotalModels = new ArrayList<>();
        this.stationTotalModels = stationTotalModels;
        //notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DryStationTotalAdapter.MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.washstationtotallayout, viewGroup, false);
        return new DryStationTotalAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DryStationTotalAdapter.MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(stationTotalModels.get(position));
    }

    @Override
    public int getItemCount() {
        return stationTotalModels.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public LinearLayout linearLayout;
        private double sum,prod;
        private String res;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.serName);
        }

        void bind(final DryStationModel stationTotalModel) {
            integers = new ArrayList<>();
            prefs = PreferenceManager.getDefaultSharedPreferences( context);
            editor = prefs.edit();
            name.setTag(stationTotalModel.getStationName());
            res = prefs.getString(name.getTag()+"totald","");
            running = true;
            MyThread = new Thread() {//create thread
                @Override
                public void run() {
                    int i = 0;
                    while (running) {
                        System.out.println("counter: " + i);
                        i++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            System.out.println("Sleep interrupted");
                        }
                        String re = prefs.getString(name.getTag()+"totald","");
                        if(!re.equals(res)){
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(re);
                                integers.clear();
                                for (int s = 0; s < jsonArray.length(); s++) {
                                    int price, count;
                                    JSONObject object = jsonArray.getJSONObject(s);
                                    prod = 0;
                                    price = object.getInt("price");
                                    count = object.getInt("count");
                                    prod = price * count;
                                    integers.add(prod);
                                    Log.e("", "bind: " + res + " " + count + " " + price);
                                }
                                sum = 0;
                                for (int j = 0; j < integers.size(); j++) {
                                    sum += integers.get(j);
                                }
                                DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                                DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                                dfs.setCurrencySymbol("");
                                dfs.setMonetaryDecimalSeparator('.');
                                dfs.setGroupingSeparator(',');
                                df.setDecimalFormatSymbols(dfs);
                                String k = df.format(sum);

                                name.setText(k);
                                stationTotalModel.setTotal(k);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                    System.out.println("onEnd Thread");
                }
            };
            MyThread.start();

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(res);
                for (int s = 0; s < jsonArray.length(); s++) {
                    int price, count;
                    JSONObject object = jsonArray.getJSONObject(s);
                    prod = 0;
                    price = object.getInt("price");
                    count = object.getInt("count");
                    prod = price * count;
                    integers.add(prod);
                    Log.e("", "bind: " + res + " " + count + " " + price);
                }
                sum = 0;
                for (int j = 0; j < integers.size(); j++) {
                    sum += integers.get(j);
                }
                DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                dfs.setCurrencySymbol("");
                dfs.setMonetaryDecimalSeparator('.');
                dfs.setGroupingSeparator(',');
                df.setDecimalFormatSymbols(dfs);
                String k = df.format(sum);

                name.setText(k);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<DryStationModel> getAll() {
        return stationTotalModels;
    }


}