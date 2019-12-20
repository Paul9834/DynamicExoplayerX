package com.paul9834.dynamicexoplayer.androidx.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.paul9834.dynamicexoplayer.androidx.ClientRetrofit.RetrofitClient;
import com.paul9834.dynamicexoplayer.androidx.Entities.UserLogin;
import com.paul9834.dynamicexoplayer.androidx.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class  LoginActivity extends AppCompatActivity {

    EditText correo;
    EditText password;
    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("Inicio de Sesión");

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

                for (UserLogin e : lista) {
                    auth = e.getFail();
                }

                if (auth) {
                    Toast.makeText(LoginActivity.this, "Hay un error en el Inicio de Sesión.", Toast.LENGTH_LONG).show();
                }

                else {
                    Toast.makeText(LoginActivity.this, "Ha iniciado sesión correctamente..", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(LoginActivity.this, RecyclerViewHome.class);
                    startActivity(i);
                }
            }
            @Override
            public void onFailure(Call<List<UserLogin>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(LoginActivity.this, "Error.", Toast.LENGTH_LONG).show();
            }
        });



    }
}
