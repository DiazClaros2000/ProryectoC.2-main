package com.example.proyectoc;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etCorreo, etContraseña;
    Button btnLogin, btnRegistrarse;
    SignInButton btnGoogle;
    ApiService api;

    // Google Sign-In
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 100;

    // Tu Web Client ID del google-services.json (client_type 3)
    private static final String WEB_CLIENT_ID = "609523987112-70o0baup4sjie8h6qqegdogeg5ngesmm.apps.googleusercontent.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.etCorreo);
        etContraseña = findViewById(R.id.etContraseña);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnGoogle = findViewById(R.id.btnGoogleSignIn);

        api = ApiClient.getRetrofit().create(ApiService.class);

        // Configurar Google Sign-In con Web Client ID correcto
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(WEB_CLIENT_ID) // ¡Importante!
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Botón de Google
        btnGoogle.setOnClickListener(v -> signInWithGoogle());

        // Botón login normal
        btnLogin.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString().trim();
            String clave = etContraseña.getText().toString().trim();

            if (correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUsuario(new LoginRequest(correo, clave));
        });

        // Botón registrarse
        btnRegistrarse.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    // Iniciar sesión con Google
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                // Aquí tienes el email y el ID token de Google
                String correoGoogle = account.getEmail();
                String idToken = account.getIdToken(); // Por si lo quieres enviar a Firebase o tu backend

                // Si tu backend acepta login con correo Google
                loginUsuario(new LoginRequest(correoGoogle, ""));

            } catch (ApiException e) {
                Toast.makeText(this, "Error Google Sign-In: Código " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Login contra tu API
    private void loginUsuario(LoginRequest loginRequest) {
        api.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();
                    if (res.isSuccess()) {
                        String rol = res.getData().rol;
                        if (rol.equals("usuario")) {
                            Intent intent = new Intent(LoginActivity.this, UsuarioActivity.class);
                            intent.putExtra("id_usuario", res.getData().id_usuario);
                            startActivity(intent);
                            finish();
                        } else if (rol.equals("admin")) {
                            startActivity(new Intent(LoginActivity.this, AdminMenuActivity.class));
                            finish();
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
}