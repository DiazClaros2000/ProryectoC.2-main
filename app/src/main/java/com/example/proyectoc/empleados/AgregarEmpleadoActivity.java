package com.example.proyectoc.empleados;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoc.R;
import com.example.proyectoc.api.ApiService;
import com.example.proyectoc.empleados.modelo.Empleado;
import com.example.proyectoc.empleados.modelo.RetrofitClient;
import com.example.proyectoc.empleados.modelo.Servicio;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarEmpleadoActivity extends AppCompatActivity {

    private EditText etNombre;
    private Spinner spinnerServicio;
    private Switch switchDisponible;
    private EditText etZona;
    private Button btnGuardar;
    private Button btnCancelar;

    private List<Servicio> servicios;
    private ArrayAdapter<Servicio> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_empleado);

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre);
        spinnerServicio = findViewById(R.id.spinnerServicio);
        switchDisponible = findViewById(R.id.switchDisponible);
        etZona = findViewById(R.id.etZona);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        // Cargar servicios
        cargarServicios();

        // Configurar listeners
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarEmpleado();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cargarServicios() {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<List<Servicio>> call = apiService.getServicios();

        call.enqueue(new Callback<List<Servicio>>() {
            @Override
            public void onResponse(Call<List<Servicio>> call, Response<List<Servicio>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    servicios = response.body();
                    adapter = new ArrayAdapter<>(AgregarEmpleadoActivity.this,
                            android.R.layout.simple_spinner_item, servicios);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerServicio.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Servicio>> call, Throwable t) {
                Toast.makeText(AgregarEmpleadoActivity.this,
                        "Error al cargar servicios: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarEmpleado() {
        String nombre = etNombre.getText().toString().trim();
        String zona = etZona.getText().toString().trim();

        if (nombre.isEmpty() || zona.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Servicio servicioSeleccionado = (Servicio) spinnerServicio.getSelectedItem();
        boolean disponible = switchDisponible.isChecked();

        Empleado empleado = new Empleado();
        empleado.setNombre(nombre);
        empleado.setIdServicio(servicioSeleccionado.getIdServicio());
        empleado.setDisponible(disponible);
        empleado.setZona(zona);

        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<ApiService.ApiResponse> call = apiService.createEmpleado(empleado);

        call.enqueue(new Callback<ApiService.ApiResponse>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponse> call, Response<ApiService.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AgregarEmpleadoActivity.this,
                            "Empleado agregado exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AgregarEmpleadoActivity.this,
                            "Error al agregar empleado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                Toast.makeText(AgregarEmpleadoActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
