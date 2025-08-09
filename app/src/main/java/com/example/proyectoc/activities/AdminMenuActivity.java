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

    Button btnCotizaciones, btnTecnicos, btnUsuarios, btnCerrarSesion, btnGestionPrecios, btnReportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        btnCotizaciones = findViewById(R.id.btnCotizaciones);
        btnTecnicos = findViewById(R.id.btnTecnicos);
        btnUsuarios = findViewById(R.id.btnUsuarios);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnGestionPrecios = findViewById(R.id.btnGestionPrecios);
        btnReportes = findViewById(R.id.btnReportes);

        btnCotizaciones.setOnClickListener(v ->
                Toast.makeText(this, "Cotizaciones (falta pantalla)", Toast.LENGTH_SHORT).show());

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

        // Nuevo listener para Reportes
        btnReportes.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMenuActivity.this, ReportesActivity.class);
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
