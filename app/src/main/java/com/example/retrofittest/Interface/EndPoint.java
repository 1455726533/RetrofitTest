package com.example.retrofittest.Interface;

import com.example.retrofittest.Bean.BaseData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by kevin on 2016/3/6.
 */
public interface EndPoint {

    String apikey="ce15e55ff10c90252fa7f7b542f437a8";


    @Headers("apikey:"+apikey)
    @GET("apistore/mobilenumber/mobilenumber")
    Call<BaseData> getData(@Query("phone") String phone);

}
