package com.example.prime.Views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.prime.Persistent.SharedPrefsCookiePersistor;
import com.example.prime.R;
import com.example.prime.Retrofit.ApiClient;
import com.example.prime.Retrofit.ApiClientBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Accounts extends Fragment {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    ApiClient apiInterface;
    public static SharedPrefsCookiePersistor sharedPrefsCookiePersistor;
    private String id;
    private String TAG="Accounts.java";
    private TextView userAdmin,userStaff;
    private Button editAdmin,editStaff;
    private RelativeLayout staff, admin;
    private String cpass;
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
        userAdmin = view.findViewById(R.id.userName);
        userStaff = view.findViewById(R.id.staffUserName);
        editAdmin = view.findViewById(R.id.editAdmin);
        editStaff = view.findViewById(R.id.editStaff);
        admin = view.findViewById(R.id.admin);
        staff = view.findViewById(R.id.staff);
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(mContext);
        apiInterface = ApiClientBuilder.getClient().create(ApiClient.class);
        id = sharedPrefsCookiePersistor.loadAll().get(0).value();
        data();
        editAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                final View mView = getLayoutInflater().inflate(R.layout.changeadminsettingsdialog, null);
                final EditText user = mView.findViewById(R.id.adminUsername);
                final EditText oldpass = mView.findViewById(R.id.adminOldpassword);
                final EditText newpass = mView.findViewById(R.id.adminNewpassword);
                final EditText confpass = mView.findViewById(R.id.adminConfirmpassword);
                final EditText newemail = mView.findViewById(R.id.adminNewemail);
                Button submit = mView.findViewById(R.id.btnChange);
                Button close = mView.findViewById(R.id.btnCancel);
                user.setText(userAdmin.getText().toString());
                mBuilder.setView(mView);
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

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject obj = new JSONObject();
                        try {
                            if (!oldpass.getText().toString().isEmpty()) {
                                if (user.getText().toString().equals(userAdmin.getText().toString())) {
                                    if (!newpass.getText().toString().equals("") && newemail.getText().toString().equals("")) {
                                        if (newpass.getText().toString().equals(confpass.getText().toString())) {
                                            obj = new JSONObject();
                                            obj.put("user_id", admin.getTag().toString());
                                            obj.put("c_username", userAdmin.getText().toString());
                                            obj.put("c_password", oldpass.getText().toString());
                                            obj.put("new_password", newpass.getText().toString());
                                        } else {
                                            Toast.makeText(mContext, "Password not Match!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (!newemail.getText().toString().equals("") && newpass.getText().toString().equals("")) {
                                        obj = new JSONObject();
                                        obj.put("user_id", admin.getTag().toString());
                                        obj.put("c_username", userAdmin.getText().toString());
                                        obj.put("c_password", oldpass.getText().toString());
                                        obj.put("new_email", newemail.getText().toString());
                                    } else {
                                        obj = new JSONObject();
                                        obj.put("user_id", admin.getTag().toString());
                                        obj.put("c_username", userAdmin.getText().toString());
                                        obj.put("c_password", oldpass.getText().toString());
                                        obj.put("new_password", newpass.getText().toString());
                                        obj.put("new_email", newemail.getText().toString());
                                    }

                                } else {
                                    if (!newpass.getText().toString().equals("") && newemail.getText().toString().equals("")) {
                                        if (newpass.getText().toString().equals(confpass.getText().toString())) {
                                            obj = new JSONObject();
                                            obj.put("user_id", admin.getTag().toString());
                                            obj.put("c_username", userAdmin.getText().toString());
                                            obj.put("c_password", oldpass.getText().toString());
                                            obj.put("new_password", newpass.getText().toString());
                                            obj.put("new_username", user.getText().toString());
                                        } else {
                                            Toast.makeText(mContext, "Password not Match!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (!newemail.getText().toString().equals("") && newpass.getText().toString().equals("")) {
                                        obj = new JSONObject();
                                        obj.put("user_id", admin.getTag().toString());
                                        obj.put("c_username", userAdmin.getText().toString());
                                        obj.put("c_password", oldpass.getText().toString());
                                        obj.put("new_email", newemail.getText().toString());
                                        obj.put("new_username", user.getText().toString());
                                    } else {
                                        obj = new JSONObject();
                                        obj.put("user_id", admin.getTag().toString());
                                        obj.put("c_username", userAdmin.getText().toString());
                                        obj.put("c_password", oldpass.getText().toString());
                                        obj.put("new_password", newpass.getText().toString());
                                        obj.put("new_email", newemail.getText().toString());
                                        obj.put("new_username", user.getText().toString());
                                    }
                                }
                            } else {
                                ((Activity) mView.getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        data();
                                        Toast.makeText(mContext, "Please Input Old Password!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (!obj.toString().isEmpty()) {
                            OkHttpClient client = new OkHttpClient();
                            Log.d("", "onClick: " + obj);
                            RequestBody body = RequestBody.create(String.valueOf(obj), JSON);
                            okhttp3.Request request = new okhttp3.Request.Builder()
                                    .url("http://192.168.0.100/users/edit")
                                    .addHeader("Cookie", "ci_session=" + id)
                                    .post(body)
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    call.cancel();
                                }

                                @Override
                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                    try {
                                        String responseData = response.body().string();
                                        JSONObject jsonObject = new JSONObject(responseData);
                                        if (jsonObject.getString("status").equals("0")) {
                                            ((Activity) mView.getContext()).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dialog.dismiss();
                                                    data();
                                                    Toast.makeText(mContext, "Changed Successful!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                        }
                    }
                });
            }
        });


        editStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                final View mView = getLayoutInflater().inflate(R.layout.changestaffsettingsdialog, null);
                final EditText adminpass = mView.findViewById(R.id.adminPassword);
                final LinearLayout linearLayout = mView.findViewById(R.id.adminAuth);
                final LinearLayout linearLayout1 = mView.findViewById(R.id.staffLayout);
                final EditText user = mView.findViewById(R.id.staffUsername);
                final EditText newpass = mView.findViewById(R.id.staffNewpassword);
                final EditText confpass = mView.findViewById(R.id.staffConfirmpassword);
                final EditText newemail = mView.findViewById(R.id.staffEmail);
                final Button submit = mView.findViewById(R.id.btnChange);
                final Button close = mView.findViewById(R.id.btnCancel);
                final Button submit1 = mView.findViewById(R.id.btnConfirm);
                final Button close1 = mView.findViewById(R.id.btnClose);
                user.setText(userStaff.getText().toString());
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
                close1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject obj = new JSONObject();
                        try {
                            if (user.getText().toString().equals(userStaff.getText().toString())) {
                                if (!newpass.getText().toString().equals("") && newemail.getText().toString().equals("")) {
                                    if (newpass.getText().toString().equals(confpass.getText().toString())) {
                                        obj = new JSONObject();
                                        obj.put("user_id", staff.getTag().toString());
                                        obj.put("c_username", userAdmin.getText().toString());
                                        obj.put("c_password", cpass);
                                        obj.put("new_password", newpass.getText().toString());
                                    } else {
                                        Toast.makeText(mContext, "Password not Match!", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (!newemail.getText().toString().equals("") && newpass.getText().toString().equals("")) {
                                    obj = new JSONObject();
                                    obj.put("user_id", staff.getTag().toString());
                                    obj.put("c_username", userAdmin.getText().toString());
                                    obj.put("c_password", cpass);
                                    obj.put("new_email", newemail.getText().toString());
                                } else {
                                    obj = new JSONObject();
                                    obj.put("user_id", staff.getTag().toString());
                                    obj.put("c_username", userAdmin.getText().toString());
                                    obj.put("c_password", cpass);
                                    obj.put("new_password", newpass.getText().toString());
                                    obj.put("new_email", newemail.getText().toString());
                                }

                            } else {
                                if (!newpass.getText().toString().equals("") && newemail.getText().toString().equals("")) {
                                    if (newpass.getText().toString().equals(confpass.getText().toString())) {
                                        obj = new JSONObject();
                                        obj.put("user_id", staff.getTag().toString());
                                        obj.put("c_username", userAdmin.getText().toString());
                                        obj.put("c_password", cpass);
                                        obj.put("new_password", newpass.getText().toString());
                                        obj.put("new_username", user.getText().toString());
                                    } else {
                                        Toast.makeText(mContext, "Password not Match!", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (!newemail.getText().toString().equals("") && newpass.getText().toString().equals("")) {
                                    obj = new JSONObject();
                                    obj.put("user_id", staff.getTag().toString());
                                    obj.put("c_username", userAdmin.getText().toString());
                                    obj.put("c_password", cpass);
                                    obj.put("new_email", newemail.getText().toString());
                                    obj.put("new_username", user.getText().toString());
                                } else {
                                    obj = new JSONObject();
                                    obj.put("user_id", staff.getTag().toString());
                                    obj.put("c_username", userAdmin.getText().toString());
                                    obj.put("c_password", cpass);
                                    obj.put("new_password", newpass.getText().toString());
                                    obj.put("new_email", newemail.getText().toString());
                                    obj.put("new_username", user.getText().toString());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (!obj.toString().isEmpty()) {
                            OkHttpClient client = new OkHttpClient();
                            Log.d("", "onClick: " + obj);
                            RequestBody body = RequestBody.create(String.valueOf(obj), JSON);
                            okhttp3.Request request = new okhttp3.Request.Builder()
                                    .url("http://192.168.0.100/users/edit")
                                    .addHeader("Cookie", "ci_session=" + id)
                                    .post(body)
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    call.cancel();
                                }

                                @Override
                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                    dialog.dismiss();
                                    try {
                                        String responseData = response.body().string();
                                        JSONObject jsonObject = new JSONObject(responseData);
                                        if (jsonObject.getString("status").equals("0")) {
                                            ((Activity) mView.getContext()).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    data();
                                                    Toast.makeText(mContext, "Changed Successful!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                        }
                    }
                });


                submit1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!adminpass.getText().toString().isEmpty()) {
                            JSONObject userJson = new JSONObject();
                            try {
                                userJson.put("username", userAdmin.getText().toString());
                                userJson.put("password", adminpass.getText().toString());
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                            OkHttpClient client = new OkHttpClient();

                            RequestBody body = RequestBody.create(String.valueOf(userJson), JSON);

                            okhttp3.Request request = new okhttp3.Request.Builder()
                                    .url("http://192.168.0.100/auth/check")
                                    .post(body)
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    call.cancel();
                                }

                                @Override
                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                    String res = response.body().string();
                                    final String msg;
                                    try {
                                        JSONObject object = new JSONObject(res);
                                        msg = object.getString("status");
                                        if(msg.equals("0")){
                                            ((Activity)mView.getContext()).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    linearLayout.setVisibility(View.GONE);
                                                    linearLayout1.setVisibility(View.VISIBLE);
                                                    cpass = adminpass.getText().toString();
                                                }
                                            });

                                        }else {
                                            ((Activity)mView.getContext()).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(mContext, "Wrong Password, Try Again!", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });


                        } else {
                            View contextView = mView.findViewById(android.R.id.content);
                            Snackbar.make(contextView, "Please Add Data", Snackbar.LENGTH_SHORT)
                                    .show();

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

        retrofit2.Call<ResponseBody> call = apiInterface.getUsers("ci_session="+id);
        call.enqueue(new  retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse( retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: "+response );

                try {
                    if(response.body()!= null) {
                        String res = response.body().string();
                        JSONArray jsonArray = new JSONArray(res);
                        userAdmin.setText(jsonArray.getJSONObject(0).getString("user_name"));
                        admin.setTag(jsonArray.getJSONObject(0).getString("user_id"));
                        userStaff.setText(jsonArray.getJSONObject(1).getString("user_name"));
                        staff.setTag(jsonArray.getJSONObject(1).getString("user_id"));
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

}