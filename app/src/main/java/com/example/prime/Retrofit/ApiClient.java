package com.example.prime.Retrofit;

import com.example.prime.Model.MyModel;
import com.example.prime.Model.ServerResponseModel;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiClient {

        @GET("cards/show")
        Call<MyModel> getJSONData();

        @GET("cards/show")
        Call<ResponseBody> getJSONData1(@Header("Cookie") String token);

        @GET("services/show")
        Call<ResponseBody> getServices(@Header("Cookie") String token);

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

        @GET("stations/show/all")
        Call<ResponseBody> getStation1(@Header("Cookie") String token);

        @GET("stations/show/all")
        Call<ResponseBody> getStation2(@Header("Cookie") String token);

        @GET("stations/show/all")
        Call<ResponseBody> getStation3(@Header("Cookie") String token);

        @GET("stations/show/all")
        Call<ResponseBody> getStation4(@Header("Cookie") String token);

        @GET("stations/show/all")
        Call<ResponseBody> getStation5(@Header("Cookie") String token);

        @GET("stations/show/all")
        Call<ResponseBody> getStation6(@Header("Cookie") String token);

        @GET("cards/scan")
        Call<ResponseBody> cardScan(@Header("Cookie") String token);

        @GET("units/show")
        Call<ResponseBody> getUnits(@Header("Cookie") String token);

        @GET("units/show")
        Call<ResponseBody> getUnits1(@Header("Cookie") String token);

        @GET("units/show")
        Call<ResponseBody> getUnits2(@Header("Cookie") String token);

        @GET("units/show")
        Call<ResponseBody> getUnits3(@Header("Cookie") String token);

        @GET("units/show")
        Call<ResponseBody> getUnits4(@Header("Cookie") String token);

        @GET("units/show")
        Call<ResponseBody> getUnits5(@Header("Cookie") String token);

        @GET("groups/show")
        Call<ResponseBody> getGroup(@Header("Cookie") String token);

        @GET("clients/show")
        Call<ResponseBody> getClients(@Header("Cookie") String token);

        @GET("services/show")
        Call<ResponseBody> getServiceId(@Header("Cookie") String token, @Query("unitid") String query);

        @GET("services/show")
        Call<ResponseBody> getServiceId1(@Header("Cookie") String token, @Query("unitid") String query);

        @GET("services/show")
        Call<ResponseBody> getServiceId2(@Header("Cookie") String token, @Query("unitid") String query);

        @GET("stations/sorts")
        Call<ResponseBody> getSorts(@Header("Cookie") String token, @Query("unitid") String query);

        @GET("stations/sorts")
        Call<ResponseBody> getSorts1(@Header("Cookie") String token, @Query("unitid") String query);

        @GET("tally/showsort")
        Call<ResponseBody> getTally(@Header("Cookie") String token, @Query("to") String to, @Query("from") String from, @Query("sort") String sort, @Query("name") String name);

        @GET("stations/sorts")
        Call<ResponseBody> getSorts2(@Header("Cookie") String token, @Query("unitid") String query);

        @GET("tally/showsort")
        Call<ResponseBody> getTally1(@Header("Cookie") String token, @Query("to") String to, @Query("from") String from, @Query("sort") String sort, @Query("name") String name);

        @GET("inventory/show")
        Call<ResponseBody> getInventory(@Header("Cookie") String token);

        @GET("inventory/item/show")
        Call<ResponseBody> getConsumables(@Header("Cookie") String token);

        @GET("inventory/services/show")
        Call<ResponseBody> getServicesItem(@Header("Cookie") String token);

        @GET("info/show/sender")
        Call<ResponseBody> getSender(@Header("Cookie") String token);

        @GET("info/show/cron")
        Call<ResponseBody> getCron(@Header("Cookie") String token);

        @GET("tally/sortstation")
        Call<ResponseBody> getTally3(@Header("Cookie") String token, @Query("to") String to, @Query("from") String from, @Query("sort") String sort, @Query("name") String name);

        @GET("tally/sortstation")
        Call<ResponseBody> getTally4(@Header("Cookie") String token, @Query("to") String to, @Query("from") String from, @Query("sort") String sort, @Query("name") String name);

        @GET("tally/sortid")
        Call<ResponseBody> getTotal(@Header("Cookie") String token, @Query("to") String to, @Query("from") String from, @Query("sort") String sort, @Query("name") String name);

        @GET("tally/show")
        Call<ResponseBody> getOverAll(@Header("Cookie") String token, @Query("to") String to, @Query("from") String from);

        @GET("timectl/sync/http")
        Call<ResponseBody> getSync(@Header("Cookie") String token);

        @GET("host/scan")
        Call<ResponseBody> getHost(@Header("Cookie") String token);

        @GET("/tally/breakdown")
        Call<ResponseBody> getList(@Header("Cookie") String token, @Query("to") String to, @Query("from") String from );

        @Multipart
        @POST("upload/logo")
        Call<ResponseBody> uploadFile(@Header("Cookie") String token, @Part MultipartBody.Part file);


        @Headers("Content-Type: application/json")
        @POST("info/edit/shop")
        Call<ResponseBody> postShop(@Header("Cookie")String token, @Body RequestBody body);

        @POST("timectl/sync/set")
        Call<ResponseBody> postSetTime(@Header("Cookie")String token, @Body RequestBody body);

        @POST("users/edit")
        Call<ResponseBody> postUserEdit(@Header("Cookie")String token, @Body RequestBody body);

        @POST("auth/check")
        Call<ResponseBody> postAuth(@Header("Cookie") String token, @Body RequestBody body);

        @POST("cards/remove")
        Call<ResponseBody> deleteCard(@Header("Cookie") String token,@Field("id") JSONArray body);

        @POST("inventory/add")
        Call<ResponseBody> postAddInventory(@Header("Cookie")String token, @Body RequestBody body);

        @POST("inventory/item/remove")
        Call<ResponseBody> postItemRemove(@Header("Cookie")String token, @Body RequestBody body);

        @POST("inventory/item/add")
        Call<ResponseBody> postItemAdd(@Header("Cookie")String token, @Body RequestBody body);

        @POST("inventory/item/edit")
        Call<ResponseBody> postItemEdit(@Header("Cookie")String token, @Body RequestBody body);

        @POST("inventory/services/remove")
        Call<ResponseBody> postServiceRemove(@Header("Cookie")String token, @Body RequestBody body);

        @POST("inventory/services/add")
        Call<ResponseBody> postServiceAdd(@Header("Cookie")String token, @Body RequestBody body);

        @POST("inventory/services/edit")
        Call<ResponseBody> postServiceEdit(@Header("Cookie")String token, @Body RequestBody body);

        @POST("stations/start")
        Call<ResponseBody> postStationStart(@Header("Cookie")String token, @Body RequestBody body);

        @Streaming
        @GET("assets/img/refe.doc")
        Call<ResponseBody> downloadFileWithFixedUrl();

        @GET
        @Streaming
        Call<ResponseBody> downloadFileWithFixedUrl1(@Url String url);


}
