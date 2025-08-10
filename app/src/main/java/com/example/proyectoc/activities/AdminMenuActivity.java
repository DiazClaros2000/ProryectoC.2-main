package com.example.proyectoc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectoc.R;
import com.example.proyectoc.empleados.MainEmpleados;
import com.example.proyectoc.empleados.actividades.GestionPreciosProductosActivity;

public class AdminMenuActivity extends AppCompatActivity {

    Button btnCotizaciones, btnTecnicos, btnUsuarios, btnCerrarSesion, btnGestionPrecios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        btnCotizaciones = findViewById(R.id.btnCotizaciones);
        btnTecnicos = findViewById(R.id.btnTecnicos);
        btnUsuarios = findViewById(R.id.btnUsuarios);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnGestionPrecios = findViewById(R.id.btnGestionPrecios);

        // BOTÓN ACTUALIZADO - Ya no muestra toast, abre la actividad de gestión
        btnCotizaciones.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMenuActivity.this, GestionCotizacionesActivity.class);
            startActivity(intent);
        });

        btnTecnicos.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMenuActivity.this, MainEmpleados.class);
            startActivity(intent);
        });

        btnUsuarios.setOnClickListener(v ->
                Toast.makeText(this, "Usuarios (falta pantalla)", Toast.LENGTH_SHORT).show());

        btnGestionPrecios.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMenuActivity.this, GestionPreciosProductosActivity.class);
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            finish(); // Termina esta actividad y regresa a la anterior (login por ejemplo)
        });
    }
}