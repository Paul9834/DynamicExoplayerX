/*
 *
 *  * Copyright (c) 2020. [Kevin Paul Montealegre Melo]
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in
 *  * all copies or substantial portions of the Software.
 *
 */

package com.paul9834.dynamicexoplayer.androidx.Controllers;

import com.paul9834.dynamicexoplayer.androidx.Entities.Channel_info;
import com.paul9834.dynamicexoplayer.androidx.Entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * The interface Canales interface.
 */
public interface Channel {

    /**
     * Gets canales.
     *
     * @return the canales
     */
    @GET("getCanales/{id}")
    Call<List<Channel_info>> getCanales(@Path("id") int id);

    /**
     * Login call.
     *
     * @param email    the email
     * @param password the password
     * @return the call
     */
    @FormUrlEncoded
    @POST("api/mobile/login")
    Call<List<User>> login(
            @Field("email") String email,
            @Field("password") String password);
}


