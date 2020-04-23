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

package com.paul9834.dynamicexoplayer.androidx.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.paul9834.dynamicexoplayer.androidx.ClientRetrofit.RetrofitClient;
import com.paul9834.dynamicexoplayer.androidx.Entities.UserLogin;
import com.paul9834.dynamicexoplayer.androidx.R;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * The type Login activity.
 */
public class  LoginActivity extends AppCompatActivity {

    /**
     * The Correo.
     */
    EditText correo;
    /**
     * The Password.
     */
    EditText password;
    /**
     * The Boton.
     */
    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userSharedPreference();

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Inicio de Sesión");

        Login();
    }


    private void Login () {

        correo = findViewById(R.id.email);
        password = findViewById(R.id.password);
        boton = findViewById(R.id.boton);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Callback();
            }
        });

    }

    /**
     * Callback.
     */
    public void Callback () {

        String correoCall = correo.getText().toString();
        String passwordCall = password.getText().toString();

        Call<List<UserLogin>> call = RetrofitClient.getInstance().getLogin().login(correoCall,passwordCall);


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this);

        progressDoalog.setTitle("Iniciando sesión..");
        progressDoalog.setMessage("Por favor espere...");
        progressDoalog.show();


        call.enqueue(new Callback<List<UserLogin>>() {
                    @Override
                    public void onResponse(Call<List<UserLogin>> call, retrofit2.Response<List<UserLogin>> response) {
                        progressDoalog.dismiss();

                Log.e("TAG", response.code() + "");

                List<UserLogin> lista = response.body();

                Boolean auth=true;

                int id = 0;

                for (UserLogin e : lista) {
                    auth = e.getFail();
                    id = e.getIdUser();
                }

                if (auth) {
                    Toast.makeText(LoginActivity.this, "Hay un error en el Inicio de Sesión.", Toast.LENGTH_LONG).show();
                }

                else {


                    SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor idUser = sp.edit();
                    idUser.putInt("your_int_key", id);
                    idUser.apply();




                    SharedPreferences.Editor editor = getSharedPreferences("Hola", MODE_PRIVATE).edit();
                    editor.putBoolean("isRegistered", true).apply();





                    Toast.makeText(LoginActivity.this, "Ha iniciado sesión correctamente..", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(LoginActivity.this, RecyclerViewHome.class);
                    startActivity(i);
                    finish();


                }
            }
            @Override
            public void onFailure(Call<List<UserLogin>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(LoginActivity.this, "Error.", Toast.LENGTH_LONG).show();
            }
        });



    }




    private void userSharedPreference(){


        SharedPreferences prefs = getSharedPreferences("Hola", MODE_PRIVATE);
        boolean isRegistered = prefs.getBoolean("isRegistered", false);

        if(isRegistered){
            Intent i = new Intent(LoginActivity.this, RecyclerViewHome.class);
            startActivity(i);
            finish();
        }

    }
}
