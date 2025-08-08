package com.example.proyectoc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectoc.R;

public class UsuarioActivity extends AppCompatActivity {

    Button btnMisVehiculos, btnHistorial, btnSolicitarServicio, btnCerrarSesion;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        btnMisVehiculos = findViewById(R.id.btnMisVehiculos);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnSolicitarServicio = findViewById(R.id.btnSolicitarServicio);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Obtener el id_usuario del intent (ajusta esto según cómo lo manejes en tu login)
        idUsuario = getIntent().getIntExtra("id_usuario", -1);
        Toast.makeText(this, "UsuarioActivity id_usuario: " + idUsuario, Toast.LENGTH_LONG).show();

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
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
