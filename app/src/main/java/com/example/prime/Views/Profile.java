package com.example.prime.Views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;



import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.android.volley.VolleyLog.TAG;

public class Profile extends Fragment {

    public static Boolean running;
    private Button btnEdit, btnServerTimeEdit;
    public static Thread MyThread;
    ApiClient apiInterface;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private String TAG="Profile.java";
    private TextView shopname,branchname,date,time,timezone,auto;

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
        return inflater.inflate(R.layout.views_profile, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        shopname = view.findViewById(R.id.shopName);
        btnEdit = view.findViewById(R.id.editAccount);
        btnServerTimeEdit = view.findViewById(R.id.editDateTime);
        branchname = view.findViewById(R.id.branchName);
        time = view.findViewById(R.id.serverTime);
        date = view.findViewById(R.id.serverDate);
        timezone = view.findViewById(R.id.serverTimeZone);
        auto = view.findViewById(R.id.autoShutdown);
        auto.setText("DISABLED");
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(mContext);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        id = sharedPrefsCookiePersistor.loadAll().get(0).value();
        data();
        running = true;
        MyThread = new Thread() {//create thread
            @Override
            public void run() {
                int i=0;
                while(running){
                    System.out.println("counter: "+i);
                    i++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Sleep interrupted");
                    }
                    time();

                }
                System.out.println("onEnd Thread");
            }
        };
        MyThread.start();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = getLayoutInflater().inflate(R.layout.changegeneralsettingsdialog, null);
                Button close = mView.findViewById(R.id.btnCancel);
                Button submit = mView.findViewById(R.id.btnChange);
                final EditText shop = mView.findViewById(R.id.changeShopName);
                final EditText branch = mView.findViewById(R.id.changeBranchName);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
                shop.setText(shopname.getText().toString());
                branch.setText(branchname.getText().toString());

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!shop.getText().toString().isEmpty() && !branch.getText().toString().isEmpty()) {
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("shop_name",shop.getText().toString());
                                obj.put("branch_name",branch.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            OkHttpClient client = new OkHttpClient();

                            RequestBody body = RequestBody.create(String.valueOf(obj),JSON);

                            Request request = new Request.Builder()
                                    .url("http://192.168.0.100/info/edit/shop")
                                    .addHeader("Cookie","ci_session="+id)
                                    .post(body)
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    call.cancel();
                                }

                                @Override
                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                    Log.d("TAG", response.body().string());
                                }
                            });
                            data();
                            Toast.makeText(mContext,
                                    R.string.success_msg,
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();


                        } else {
                            Toast.makeText(mContext,
                                    R.string.error_msg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });

        btnServerTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = getLayoutInflater().inflate(R.layout.timeeditdialog, null);
                Button close = mView.findViewById(R.id.dialog_close);
                Button submit = mView.findViewById(R.id.dialog_submit);
                final EditText time = mView.findViewById(R.id.setTime);
                final EditText date = mView.findViewById(R.id.setDate);
                time.setFocusable(false);
                time.setKeyListener(null);
                date.setFocusable(false);
                date.setKeyListener(null);
                mBuilder.setView(mView);
                time.setText("12:12 PM");
                date.setText("Date");
                final AlertDialog dialog = mBuilder.create();
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                final SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a", Locale.US);
                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!time.getText().toString().isEmpty()) {
                            Date d = null;
                            Calendar cal = Calendar.getInstance();
                            String curtime = time.getText().toString();
                            try {
                                d = sdf1.parse(curtime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            cal.setTime(d);
                            int hour = cal.get(Calendar.HOUR_OF_DAY);
                            int minute = cal.get(Calendar.MINUTE);
                            TimePickerDialog mTimePicker;
                            mTimePicker = new TimePickerDialog(mContext, R.style.TimePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    String times;
                                    times = selectedHour + ":" + selectedMinute;
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.US);
                                    Date date2 = null;
                                    try {
                                        date2 = sdf2.parse(times);
                                    } catch (ParseException e) {
                                    }
                                    final Calendar c2 = Calendar.getInstance();
                                    c2.setTime(date2);

                                    SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a", Locale.US);
                                    String re = sdf1.format(c2.getTime());
                                    time.setText(re);
                                }
                            }, hour, minute, false);//Yes 24 hour time
                            mTimePicker.setTitle("Select Time");
                            mTimePicker.show();
                            mTimePicker.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                            mTimePicker.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                            mTimePicker.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.TRANSPARENT);
                            mTimePicker.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                });

                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!date.getText().toString().isEmpty()) {
                            Date d = null;
                            int mYear,mMonth,mDay;
                            Calendar cal = Calendar.getInstance();
                            mYear = cal.get(Calendar.YEAR);
                            mMonth = cal.get(Calendar.MONTH);
                            mDay = cal.get(Calendar.DAY_OF_MONTH);
                            DatePickerDialog mDate;
                            mDate = new DatePickerDialog(mContext, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                                    String dates;
                                    dates = mMonth + " " + mDay +" ,"+mYear;
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.US);
                                    Date date2 = null;
                                    try {
                                        date2 = sdf2.parse(dates);
                                    } catch (ParseException e) {
                                    }
                                    final Calendar c2 = Calendar.getInstance();
                                    c2.setTime(date2);

                                    SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a", Locale.US);
                                    String re = sdf1.format(c2.getTime());
                                    date.setText(re);
                                }
                            }, mYear, mMonth, mDay);
                                    mDate.setTitle("Select Time");
                            mDate.show();
                            mDate.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                            mDate.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                            mDate.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.TRANSPARENT);
                            mDate.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                });
            }
        });

    }

    private void data(){
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(mContext);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        id = sharedPrefsCookiePersistor.loadAll().get(0).value();

        retrofit2.Call<ResponseBody> call = apiInterface.getInfo("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: "+response );
                try {

                    if(response.body()!= null) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        shopname.setText(jsonObject.getString("shop_name"));
                        branchname.setText(jsonObject.getString("branch_name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void time(){
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(mContext);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        id = sharedPrefsCookiePersistor.loadAll().get(0).value();

//        running = true;
//        MyThread = new Thread(){//create thread
//            @Override
//            public void run() {
        if(!id.isEmpty()) {
            retrofit2.Call<ResponseBody> call = apiInterface.getServerTime("ci_session=" + id);
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e(TAG, "onResponse: " + response);
                    try {
                        if (response.body() != null) {
                            String res = response.body().string();
                            JSONObject jsonObject = new JSONObject(res);
                            long result = jsonObject.getLong("timestamp") * 1000L;
                            SimpleDateFormat formatTime = new SimpleDateFormat("h:mm:ss a", Locale.US);
                            SimpleDateFormat formatDate = new SimpleDateFormat("EEE MMMM dd, yyyy  ", Locale.US);
                            String time1 = formatTime.format(new Date(result));
                            String date1 = formatDate.format(new Date(result));
                            time.setText(time1);
                            date.setText(date1);
                            timezone.setText("GMT +08:00");
                            Log.e(TAG, "onResponse: " + date1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "onFailure: ");

                }
            });
        }
//                int i=0;
//                while(running){
//                    System.out.println("counter: "+i);
//                    i++;
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        System.out.println("Sleep interrupted");
//                    }
//                    call.request();
//                    Log.e(TAG, "run: "+call.request());
//
//                }
//                System.out.println("onEnd Thread");
//            }
//        };
//        MyThread.start();
    }

}
