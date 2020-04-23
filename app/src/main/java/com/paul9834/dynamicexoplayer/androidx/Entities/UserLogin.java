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

package com.paul9834.dynamicexoplayer.androidx.Entities;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type User login.
 */
public class UserLogin {

    @SerializedName("fail")
    @Expose
    private Boolean fail;
    @SerializedName("errors")
    @Expose
    private String errors;

    /**
     * Gets fail.
     *
     * @return the fail
     */
    public Boolean getFail() {
        return fail;
    }

    /**
     * Sets fail.
     *
     * @param fail the fail
     */
    public void setFail(Boolean fail) {
        this.fail = fail;
    }

    /**
     * Gets errors.
     *
     * @return the errors
     */
    public String getErrors() {
        return errors;
    }

    /**
     * Sets errors.
     *
     * @param errors the errors
     */
    public void setErrors(String errors) {
        this.errors = errors;
    }

}