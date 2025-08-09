package com.example.proyectoc.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoc.LoginActivity;
import com.example.proyectoc.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class UsuarioActivity extends AppCompatActivity {

    Button btnMisVehiculos, btnHistorial, btnSolicitarServicio, btnCerrarSesion, btnVerPerfil;
    int idUsuario;

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
        btnVerPerfil = findViewById(R.id.btnVerPerfil);

        // Recibir ID de usuario desde Login o Register
        idUsuario = getIntent().getIntExtra("id_usuario", -1);

        // Si no recibimos id_usuario por Intent, buscarlo en SharedPreferences
        if (idUsuario == -1) {
            SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);
            idUsuario = prefs.getInt("id_usuario", -1);
        }

        if (idUsuario == -1) {
            Toast.makeText(this, "Error: ID de usuario no recibido", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Bienvenido usuario ID: " + idUsuario, Toast.LENGTH_SHORT).show();
            Log.d("UsuarioActivity", "ID de usuario recibido: " + idUsuario);
        }

        // Configurar Google SignIn
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

        btnVerPerfil.setOnClickListener(v -> {
            if (idUsuario != -1) {
                // Leer datos guardados del usuario
                SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);
                Intent intent = new Intent(this, PerfilActivity.class);
                intent.putExtra("id_usuario", idUsuario);
                intent.putExtra("nombre", prefs.getString("nombre", ""));
                intent.putExtra("apellido", prefs.getString("apellido", ""));
                intent.putExtra("correo", prefs.getString("correo", ""));
                intent.putExtra("pais", prefs.getString("pais", ""));
                intent.putExtra("foto_perfil", prefs.getString("foto_perfil", ""));
                startActivity(intent);
            } else {
                Toast.makeText(this, "No se puede abrir perfil: ID no válido", Toast.LENGTH_LONG).show();
            }
        });

        btnCerrarSesion.setOnClickListener(v -> cerrarSesionGoogle());
    }

    private void cerrarSesionGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            mGoogleSignInClient.revokeAccess().addOnCompleteListener(this, revokeTask -> {
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

                // Borrar datos del usuario guardados
                getSharedPreferences("usuario", MODE_PRIVATE).edit().clear().apply();

                Intent intent = new Intent(UsuarioActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        });
    }
}
