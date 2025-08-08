package com.example.proyectoc.empleados;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoc.R;

public class MainEmpleados extends AppCompatActivity {

    private Button btnAgregarEmpleado;
    private Button btnGestionarEmpleados;
    private Button btnVerEmpleados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_empleados);

        // Inicializar vistas
        btnAgregarEmpleado = findViewById(R.id.btnAgregarEmpleado);
        btnGestionarEmpleados = findViewById(R.id.btnGestionarEmpleados);
        btnVerEmpleados = findViewById(R.id.btnVerEmpleados);

        // Configurar listeners
        btnAgregarEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainEmpleados.this, AgregarEmpleadoActivity.class);
                startActivity(intent);
            }
        });

        btnGestionarEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainEmpleados.this, GestionarEmpleadosActivity.class);
                startActivity(intent);
            }
        });

        btnVerEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainEmpleados.this, VerEmpleadosActivity.class);
                startActivity(intent);
            }
        });
    }
}
