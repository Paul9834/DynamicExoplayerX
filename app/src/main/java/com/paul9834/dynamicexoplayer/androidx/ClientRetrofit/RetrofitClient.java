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

package com.paul9834.dynamicexoplayer.androidx.ClientRetrofit;

import com.paul9834.dynamicexoplayer.androidx.Controllers.CanalesInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type Retrofit client.
 */
public class RetrofitClient {

    private static final String BASE_URL = "http://ott.terraformed.services/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    /**
     * Gets login.
     *
     * @return the login
     */
    public CanalesInterface getLogin() {
        return retrofit.create(CanalesInterface.class);
    }

}
