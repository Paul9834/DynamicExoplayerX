package com.paul9834.dynamicexoplayer.androidx.Controllers;

import com.paul9834.dynamicexoplayer.androidx.Entities.Canales;
import com.paul9834.dynamicexoplayer.androidx.Entities.UserLogin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CanalesInterface {

    @GET("getCanales/3")
    Call<List<Canales>> getCanales();

    @FormUrlEncoded
    @POST("api/mobile/login")
    Call<List<UserLogin>> login(
            @Field("email") String email,
            @Field("password") String password);
}


