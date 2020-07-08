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

import com.paul9834.dynamicexoplayer.androidx.Controllers.Channel;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type Retrofit client.
 */
public class Retrofit_Base {

    private static final String BASE_URL = "https://headendev.badala.software/";
    private static Retrofit_Base mInstance;
    private Retrofit retrofit;

    private Retrofit_Base() {
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
    public static synchronized Retrofit_Base getInstance() {
        if (mInstance == null) {
            mInstance = new Retrofit_Base();
        }
        return mInstance;
    }

    /**
     * Gets login.
     *
     * @return the login
     */
    public Channel getLogin() {
        return retrofit.create(Channel.class);
    }

}
