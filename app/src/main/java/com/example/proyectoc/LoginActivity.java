package com.example.proyectoc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoc.activities.AdminMenuActivity;
import com.example.proyectoc.activities.RegisterActivity;
import com.example.proyectoc.activities.UsuarioActivity;
import com.example.proyectoc.api.ApiClient;
import com.example.proyectoc.api.ApiService;
import com.example.proyectoc.model.LoginRequest;
import com.example.proyectoc.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etCorreo, etContraseña;
    Button btnLogin, btnRegistrarse;
    ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Referencias a los campos
        etCorreo = findViewById(R.id.etCorreo);
        etContraseña = findViewById(R.id.etContraseña);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        // Instancia Retrofit
        api = ApiClient.getRetrofit().create(ApiService.class);

        // Evento para iniciar sesión
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = etCorreo.getText().toString().trim();
                String clave = etContraseña.getText().toString().trim();

                if (correo.isEmpty() || clave.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Llamar al login de la API
                LoginRequest loginRequest = new LoginRequest(correo, clave);

                api.login(loginRequest).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse res = response.body();
                            if (res.isSuccess()) {
                                String rol = res.getData().rol;

                                if (rol.equals("usuario")) {
                                    int idUsuario = res.getData().id_usuario;
                                    Toast.makeText(LoginActivity.this, "Login OK, id_usuario: " + idUsuario, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, UsuarioActivity.class);
                                    intent.putExtra("id_usuario", idUsuario);
                                    startActivity(intent);
                                } else if (rol.equals("admin")) {
                                    startActivity(new Intent(LoginActivity.this, AdminMenuActivity.class));
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Evento para abrir la pantalla de registro
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
