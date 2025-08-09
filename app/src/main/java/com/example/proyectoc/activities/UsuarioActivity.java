package com.example.proyectoc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoc.LoginActivity;
import com.example.proyectoc.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class UsuarioActivity extends AppCompatActivity {

    Button btnMisVehiculos, btnHistorial, btnSolicitarServicio, btnCerrarSesion;
    int idUsuario;

    // Web Client ID de tu google-services.json (client_type 3)
    private static final String WEB_CLIENT_ID = "609523987112-70o0baup4sjie8h6qqegdogeg5ngesmm.apps.googleusercontent.com";
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        btnMisVehiculos = findViewById(R.id.btnMisVehiculos);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnSolicitarServicio = findViewById(R.id.btnSolicitarServicio);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        idUsuario = getIntent().getIntExtra("id_usuario", -1);
        Toast.makeText(this, "UsuarioActivity id_usuario: " + idUsuario, Toast.LENGTH_LONG).show();

        // Configurar GoogleSignInClient (igual que en LoginActivity)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(WEB_CLIENT_ID)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnMisVehiculos.setOnClickListener(v -> {
            Intent intent = new Intent(this, MisVehiculosActivity.class);
            intent.putExtra("id_usuario", idUsuario);
            startActivity(intent);
        });

        btnHistorial.setOnClickListener(v ->
                Toast.makeText(this, "Historial (falta pantalla)", Toast.LENGTH_SHORT).show());

        btnSolicitarServicio.setOnClickListener(v ->
                Toast.makeText(this, "Servicio (falta pantalla)", Toast.LENGTH_SHORT).show());

        btnCerrarSesion.setOnClickListener(v -> {
            cerrarSesionGoogle();
        });
    }

    private void cerrarSesionGoogle() {
        // Cierra la sesión y borra credenciales
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // Opcional: Revocar acceso para que pida cuenta siempre
            mGoogleSignInClient.revokeAccess().addOnCompleteListener(this, revokeTask -> {
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

                // Regresar al login
                Intent intent = new Intent(UsuarioActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        });
    }
}