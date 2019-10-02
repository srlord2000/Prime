package com.example.prime.Retrofit;

import com.example.prime.Model.MyModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
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

}
