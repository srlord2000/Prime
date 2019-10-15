package com.example.prime.Retrofit;

import com.example.prime.Model.MyModel;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiClient {

        @GET("cards/show")
        Call<MyModel> getJSONData();

        @GET("cards/show")
        Call<ResponseBody> getJSONData1(@Header("Cookie") String token);

        @GET("services/show")
        Call<ResponseBody> getJSONData2(@Header("Cookie") String token);

        @POST("auth/check")
        Call<ResponseBody> getData(@Body Object body);

        @GET("info/show/shop")
        Call<ResponseBody> getInfo(@Header("Cookie") String token);

        @GET("users/show/shop")
        Call<ResponseBody> getUsers(@Header("Cookie") String token);

        @GET("timectl/show")
        Call<ResponseBody> getServerTime(@Header("Cookie") String token);

        @GET("cards/show")
        Call<ResponseBody> getCards(@Header("Cookie") String token);

        @GET("stations/show/all")
        Call<ResponseBody> getStation(@Header("Cookie") String token);

        @GET("cards/scan")
        Call<ResponseBody> cardScan(@Header("Cookie") String token);

        @GET("units/show")
        Call<ResponseBody> getUnits(@Header("Cookie") String token);

        @Headers({"Accept:application/json", "Content-Type:application/json;"})
        @FormUrlEncoded
        @POST("cards/remove")
        Call<ResponseBody> deleteCard(@Header("Cookie") String token,@Field("id") JSONArray body);


}
