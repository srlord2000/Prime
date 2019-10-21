package com.example.prime.Views;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prime.Model.CardsModel;
import com.example.prime.Model.ClientsModel;
import com.example.prime.Model.StationModel;
import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.RecyclerAdapter.ClientsAdapter;
import com.example.prime.RecyclerAdapter.StationAdapter;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Email extends Fragment {
    public static String result1;
    public static Boolean running;
    public static Thread MyThread;
    private ArrayList<ClientsModel> clientsModels;
    private String text1;
    private RecyclerView recyclerView;
    private Button add, delete, sender, sendtime;
    private ClientsAdapter clientsAdapter;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private String TAG="Email.java";
    Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = container.getContext();
        return inflater.inflate(R.layout.view_emails, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        clientsModels = new ArrayList<>();
        recyclerView = view.findViewById(R.id.listView);
        delete = view.findViewById(R.id.btnDeleteEmail);
        add = view.findViewById(R.id.btnAddEmail);
        sender = view.findViewById(R.id.btnSenderEdit);
        sendtime = view.findViewById(R.id.btnSetTime);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        clientsAdapter = new ClientsAdapter(mContext, clientsModels);
        recyclerView.setAdapter(clientsAdapter);
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(mContext);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        id = sharedPrefsCookiePersistor.loadAll().get(0).value();
        data1();
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
                    data();
                }
                System.out.println("onEnd Thread");
            }
        };
        MyThread.start();

        sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                final View mView = getLayoutInflater().inflate(R.layout.sendereditlayout, null);
                final EditText email = mView.findViewById(R.id.sender_email);
                final EditText password = mView.findViewById(R.id.sender_password);
                final TextView settings = mView.findViewById(R.id.link_settings);
                Spanned policy = Html.fromHtml(getString(R.string.settingstext), HtmlCompat.FROM_HTML_MODE_LEGACY);
                Button submit = mView.findViewById(R.id.dialog_submit);
                Button close = mView.findViewById(R.id.dialog_close);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
                settings.setText(policy);
                settings.setMovementMethod(LinkMovementMethod.getInstance());

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                            String email1, pass;
                            email1 = email.getText().toString();
                            pass = password.getText().toString();
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("gmail", email1);
                                obj.put("passwd", pass);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(mContext, text1, Toast.LENGTH_SHORT).show();

                            OkHttpClient client = new OkHttpClient();
                            RequestBody body = RequestBody.create(String.valueOf(obj), JSON);
                            okhttp3.Request request = new okhttp3.Request.Builder()
                                    .url("http://192.168.0.100/info/edit/sender")
                                    .post(body)
                                    .addHeader("Cookie", "ci_session=" + id)
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

                            Toast.makeText(mContext,
                                    text1,
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();


                        } else {
                            Toast.makeText(mContext,
                                    R.string.error_msg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                retrofit2.Call<ResponseBody> call = apiInterface.getSender("ci_session="+id);
                call.enqueue(new  retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse( retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        try {
                            String res = response.body().string();
                            JSONObject object = new JSONObject(res);

                            email.setText(object.getString("gmail"));
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ArrayList<String> sad = new ArrayList<>();
                if (clientsAdapter.getSelected().size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < clientsAdapter.getSelected().size(); i++) {
                        JSONObject obj = new JSONObject();
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(clientsAdapter.getSelected().get(i).getEmail());
                        try {
                            obj.put("email", jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        stringBuilder.append(clientsAdapter.getSelected().get(i).getEmail());
                        stringBuilder.append("\n");
                        sad.add(obj.toString());
                        Log.i(TAG, "onClick: "+obj);

                        OkHttpClient client = new OkHttpClient();

                        RequestBody body = RequestBody.create(String.valueOf(obj), JSON);

                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url("http://192.168.0.100/clients/remove")
                                .addHeader("Cookie","ci_session="+id)
                                .post(body)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                call.cancel();
                                View contextView = view.findViewById(android.R.id.content);
                                Snackbar.make(contextView, "Failed!", Snackbar.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                Log.e("TAG", response.message());
                                if (getActivity() != null) {
                                    View contextView = getActivity().findViewById(android.R.id.content);
                                    Snackbar.make(contextView, "Delete Successful!", Snackbar.LENGTH_SHORT)
                                            .show();
                                }
                                Log.e(TAG, "onResponse: "+response.body().string() );
                            }
                        });
                    }
                } else {
                    if (getActivity() != null) {
                        View contextView = getActivity().findViewById(android.R.id.content);
                        Snackbar.make(contextView, "No Selection!", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                final View mView = getLayoutInflater().inflate(R.layout.addclientslayout, null);
                final EditText email = mView.findViewById(R.id.clientsEmail);
                final EditText first_name = mView.findViewById(R.id.first_name);
                final EditText last_name = mView.findViewById(R.id.last_name);
                Button submit = mView.findViewById(R.id.dialog_submit);
                Button close = mView.findViewById(R.id.dialog_close);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        if (!email.getText().toString().isEmpty() && !first_name.getText().toString().isEmpty() && !last_name.getText().toString().isEmpty()) {
                            Toast.makeText(mContext, text1, Toast.LENGTH_SHORT).show();
                            final String emails,firstn,lastn;
                            emails = email.getText().toString();
                            firstn = first_name.getText().toString();
                            lastn = last_name.getText().toString();
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("first_name", firstn);
                                obj.put("last_name", lastn);
                                obj.put("email",emails);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            OkHttpClient client = new OkHttpClient();
                            RequestBody body = RequestBody.create(String.valueOf(obj), JSON);
                            Log.e(TAG, "onClick: "+obj );
                            okhttp3.Request request = new okhttp3.Request.Builder()
                                    .url("http://192.168.0.100/clients/add")
                                    .addHeader("Cookie","ci_session="+id)
                                    .post(body)
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    if (getActivity() != null) {
                                        View contextView = getActivity().findViewById(android.R.id.content);
                                        Snackbar.make(contextView, "Failed, Try Again!", Snackbar.LENGTH_SHORT)
                                                .show();
                                    }
                                    call.cancel();
                                    Log.e(TAG, "onFailure: error");
                                }

                                @Override
                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                    dialog.dismiss();
                                    if (getActivity() != null) {
                                        View contextView = getActivity().findViewById(android.R.id.content);
                                        Snackbar.make(contextView, "Add Successfully!", Snackbar.LENGTH_SHORT)
                                                .show();
                                    }
                                    Log.e("TAG", response.body().string());
                                }
                            });




                        } else {
                            if (getActivity() != null) {
                                View contextView = getActivity().findViewById(android.R.id.content);
                                Snackbar.make(contextView, "Please Input Complete Details!", Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        }
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


        sendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                final View mView = getLayoutInflater().inflate(R.layout.settimelayout, null);
                final Spinner shift = mView.findViewById(R.id.shiftsSpinner);
                final Spinner day = mView.findViewById(R.id.daysSpinner);
                final TextInputEditText clock = mView.findViewById(R.id.senderTime);
                final LinearLayout oneShift = mView.findViewById(R.id.shift1);
                final LinearLayout twoShift = mView.findViewById(R.id.shift2);
                final TextInputLayout textInputLayout = mView.findViewById(R.id.layout);
                clock.setFocusable(false);
                clock.setKeyListener(null);
                Spanned policy = Html.fromHtml(getString(R.string.settingstext), HtmlCompat.FROM_HTML_MODE_LEGACY);
                Button submit = mView.findViewById(R.id.dialog_submit);
                Button test = mView.findViewById(R.id.testSender);
                Button close = mView.findViewById(R.id.dialog_close);
                clock.setInputType(InputType.TYPE_NULL);
                final int text = 1;
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
                ArrayAdapter<String> shifts = new ArrayAdapter<>(mContext,R.layout.spinnershiftlist,getResources().getStringArray(R.array.spinnerShifts));
                shift.setAdapter(shifts);
                ArrayAdapter<String> days = new ArrayAdapter<>(mContext,R.layout.spinnerdayslist,getResources().getStringArray(R.array.spinnerDays));
                day.setAdapter(days);

                shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected( AdapterView<?> adapterView, View view,  int i, long l) {
                        int id = adapterView.getSelectedItemPosition();
                        if(id == 0) {
                            text1 = "1";
                            oneShift.setVisibility(View.VISIBLE);
                            twoShift.setVisibility(View.GONE);
                        }else if (id == 1){
                            text1 = "2";
                            oneShift.setVisibility(View.GONE);
                            twoShift.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        int id = adapterView.getSelectedItemPosition();
                        if(id == 0) {
                            text1 = "1";
                            oneShift.setVisibility(View.VISIBLE);
                            twoShift.setVisibility(View.GONE);
                        }else if (id == 1){
                            text1 = "2";
                            oneShift.setVisibility(View.GONE);
                            twoShift.setVisibility(View.VISIBLE);
                        }
                    }
                });

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.US);
                Date date = null;
                try {
                    date = sdf.parse("16:30");
                } catch (ParseException e) {
                }
                final Calendar c = Calendar.getInstance();
                c.setTime(date);
//                clock.setHour(c.get(Calendar.HOUR_OF_DAY));
//                clock.setMinute(c.get(Calendar.MINUTE));

                final SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a", Locale.US);
                String re = sdf1.format(c.getTime());
                clock.setText(re);
                clock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Date d = null;
                        Calendar cal = Calendar.getInstance();
                        String curtime = clock.getText().toString();
                        try {
                            d = sdf1.parse(curtime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        cal.setTime(d);


                        int hour = cal.get(Calendar.HOUR_OF_DAY);
                        int minute = cal.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(mContext,R.style.TimePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                String time;
                                time = selectedHour+":"+selectedMinute;
                                SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm", Locale.US);
                                Date date2 = null;
                                try {
                                    date2 = sdf2.parse(time);
                                } catch (ParseException e) {
                                }
                                final Calendar c2 = Calendar.getInstance();
                                c2.setTime(date2);

                                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a", Locale.US);
                                String re = sdf1.format(c2.getTime());
                                clock.setText(re);
                            }
                        }, hour, minute, false);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.RIGHT;
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params1.gravity = Gravity.LEFT;
                        mTimePicker.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                        mTimePicker.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                        mTimePicker.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.TRANSPARENT);
                        mTimePicker.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(Color.TRANSPARENT);
                    }
                });

                textInputLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Date d = null;
                        Calendar cal = Calendar.getInstance();
                        String curtime = clock.getText().toString();
                        try {
                            d = sdf1.parse(curtime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        cal.setTime(d);


                        int hour = cal.get(Calendar.HOUR_OF_DAY);
                        int minute = cal.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(mContext,R.style.TimePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                String time;
                                time = selectedHour+":"+selectedMinute;
                                SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm", Locale.US);
                                Date date2 = null;
                                try {
                                    date2 = sdf2.parse(time);
                                } catch (ParseException e) {
                                }
                                final Calendar c2 = Calendar.getInstance();
                                c2.setTime(date2);

                                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a", Locale.US);
                                String re = sdf1.format(c2.getTime());
                                clock.setText(re);
                            }
                        }, hour, minute, false);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.RIGHT;

                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params1.gravity = Gravity.LEFT;

                        mTimePicker.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                        mTimePicker.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                        mTimePicker.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.TRANSPARENT);
                        mTimePicker.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(Color.TRANSPARENT);
                    }
                });


                test.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int hour , minute;
//                        hour =clock.getHour();
//                        minute =clock.getMinute();

                    }
                });



            }
        });


    }


    private void data(){
        retrofit2.Call<ResponseBody> call = apiInterface.getClients("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String result = prefs.getString("Clients",null);
                try {
                    if(response.body() != null) {
                        String res = response.body().string();
                        if (res.equals(result)) {

                        } else {
                            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.remove("Clients");
                            editor.putString("Clients", res);
                            editor.apply();
                            clientsModels = new ArrayList<>();
                            try {
                                clientsModels.clear();
                                Log.e(TAG, "onResponse222: "+res );
                                JSONArray array = new JSONArray(res);
                                for (int i = 0; i < array.length(); i++) {

                                    ClientsModel clientsModel = new ClientsModel();
                                    JSONObject json = null;
                                    try {
                                        json = array.getJSONObject(i);
                                        clientsModel.setEmail(json.getString("email"));
                                        clientsModel.setFirstName(json.getString("first_name"));
                                        clientsModel.setLastName(json.getString("last_name"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    clientsModels.add(clientsModel);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            clientsAdapter.setClients(clientsModels);
                            clientsAdapter.notifyDataSetChanged();

                        }
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

    private void data1(){
        retrofit2.Call<ResponseBody> call = apiInterface.getClients("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String result = prefs.getString("Clients",null);
                try {
                    if(response.body() != null) {
                        String res = response.body().string();
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.remove("Clients");
                        editor.putString("Clients", res);
                        editor.apply();
                        clientsModels = new ArrayList<>();
                        try {
                            clientsModels.clear();
                            Log.e(TAG, "onResponse222: "+res );
                            JSONArray array = new JSONArray(res);
                            for (int i = 0; i < array.length(); i++) {

                                ClientsModel clientsModel = new ClientsModel();
                                JSONObject json = null;
                                try {
                                    json = array.getJSONObject(i);
                                    clientsModel.setEmail(json.getString("email"));
                                    clientsModel.setFirstName(json.getString("first_name"));
                                    clientsModel.setLastName(json.getString("last_name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                clientsModels.add(clientsModel);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        clientsAdapter.setClients(clientsModels);
                        clientsAdapter.notifyDataSetChanged();

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

}

