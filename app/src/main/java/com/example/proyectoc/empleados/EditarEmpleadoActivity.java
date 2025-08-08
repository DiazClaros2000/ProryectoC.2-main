package com.example.proyectoc.empleados;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class EditarEmpleadoActivity extends AppCompatActivity {
    private EditText etNombre;
    private Spinner spinnerServicio;
    private Switch switchDisponible;
    private EditText etZona;
    private Button btnActualizar;
    private Button btnCancelar;

    private Empleado empleado;
    private List<Servicio> servicios;
    private ArrayAdapter<Servicio> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_empleado);

        // Configurar toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Editar Empleado");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Obtener empleado de la intent
        empleado = (Empleado) getIntent().getSerializableExtra("empleado");
        if (empleado == null) {
            Toast.makeText(this, "Error: No se pudo cargar el empleado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre);
        spinnerServicio = findViewById(R.id.spinnerServicio);
        switchDisponible = findViewById(R.id.switchDisponible);
        etZona = findViewById(R.id.etZona);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnCancelar = findViewById(R.id.btnCancelar);

        // Cargar datos del empleado
        cargarDatosEmpleado();

        // Cargar servicios
        cargarServicios();

        // Configurar listeners
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarEmpleado();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cargarDatosEmpleado() {
        etNombre.setText(empleado.getNombre());
        etZona.setText(empleado.getZona());
        switchDisponible.setChecked(empleado.isDisponible());
    }

    private void cargarServicios() {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<List<Servicio>> call = apiService.getServicios();

        call.enqueue(new Callback<List<Servicio>>() {
            @Override
            public void onResponse(Call<List<Servicio>> call, Response<List<Servicio>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    servicios = response.body();
                    adapter = new ArrayAdapter<>(EditarEmpleadoActivity.this,
                            android.R.layout.simple_spinner_item, servicios);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerServicio.setAdapter(adapter);

                    // Seleccionar el servicio actual del empleado
                    Integer idServicioEmpleado = empleado.getIdServicio();
                    if (idServicioEmpleado != null) {
                        for (int i = 0; i < servicios.size(); i++) {
                            if (servicios.get(i).getIdServicio() == idServicioEmpleado.intValue()) {
                                spinnerServicio.setSelection(i);
                                break;
                            }
                        }
                    } else {
                        // Si es null, selecciona el primer servicio por defecto
                        spinnerServicio.setSelection(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Servicio>> call, Throwable t) {
                Toast.makeText(EditarEmpleadoActivity.this,
                        "Error al cargar servicios: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarEmpleado() {
        String nombre = etNombre.getText().toString().trim();
        String zona = etZona.getText().toString().trim();

        if (nombre.isEmpty() || zona.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Servicio servicioSeleccionado = (Servicio) spinnerServicio.getSelectedItem();
        boolean disponible = switchDisponible.isChecked();

        // Actualizar datos del empleado
        empleado.setNombre(nombre);
        empleado.setIdServicio(servicioSeleccionado.getIdServicio());
        empleado.setDisponible(disponible);
        empleado.setZona(zona);

        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<ApiService.ApiResponse> call = apiService.updateEmpleado(empleado);

        call.enqueue(new Callback<ApiService.ApiResponse>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponse> call, Response<ApiService.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditarEmpleadoActivity.this,
                            "Empleado actualizado exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditarEmpleadoActivity.this,
                            "Error al actualizar empleado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                Toast.makeText(EditarEmpleadoActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
