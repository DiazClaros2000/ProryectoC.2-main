package com.example.proyectoc.empleados;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.proyectoc.R;
import com.example.proyectoc.api.ApiService;
import com.example.proyectoc.empleados.Adapters.EmpleadosAdapter;
import com.example.proyectoc.empleados.modelo.Empleado;
import com.example.proyectoc.empleados.modelo.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GestionarEmpleadosActivity extends AppCompatActivity implements EmpleadosAdapter.OnEmpleadoClickListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner spinnerFiltroEstado;

    private EmpleadosAdapter adapter;
    private List<Empleado> empleados;
    private List<Empleado> empleadosFiltrados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_empleados);

        // Configurar toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Gestionar Empleados");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        spinnerFiltroEstado = findViewById(R.id.spinnerFiltroEstado);

        // Configurar RecyclerView
        empleados = new ArrayList<>();
        empleadosFiltrados = new ArrayList<>();
        adapter = new EmpleadosAdapter(empleadosFiltrados, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Configurar SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarEmpleados();
            }
        });

        // Configurar filtros
        configurarFiltros();

        // Cargar empleados
        cargarEmpleados();
    }

    private void configurarFiltros() {
        // Configurar spinner de estados
        String[] estados = {"Todos", "Activo", "Inactivo"};
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, estados);
        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltroEstado.setAdapter(adapterEstados);

        // Configurar listener para filtro de estado
        spinnerFiltroEstado.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void cargarEmpleados() {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<List<Empleado>> call = apiService.getEmpleados();

        call.enqueue(new Callback<List<Empleado>>() {
            @Override
            public void onResponse(Call<List<Empleado>> call, Response<List<Empleado>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    empleados.clear();
                    empleados.addAll(response.body());
                    aplicarFiltros();
                } else {
                    Toast.makeText(GestionarEmpleadosActivity.this,
                            "Error al cargar empleados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Empleado>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(GestionarEmpleadosActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void aplicarFiltros() {
        empleadosFiltrados.clear();
        String estadoSeleccionado = spinnerFiltroEstado.getSelectedItem().toString();
        for (Empleado empleado : empleados) {
            boolean cumpleFiltroEstado = estadoSeleccionado.equals("Todos") ||
                    (estadoSeleccionado.equals("Activo") && empleado.isDisponible()) ||
                    (estadoSeleccionado.equals("Inactivo") && !empleado.isDisponible());
            if (cumpleFiltroEstado) {
                empleadosFiltrados.add(empleado);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEmpleadoClick(Empleado empleado) {
        // Mostrar opciones de editar, cambiar estado y eliminar
        String[] opciones = {"Editar", "Cambiar Estado", "Eliminar"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Opciones para " + empleado.getNombre());
        builder.setItems(opciones, (dialog, which) -> {
            if (which == 0) {
                // Editar
                android.content.Intent intent = new android.content.Intent(GestionarEmpleadosActivity.this, EditarEmpleadoActivity.class);
                intent.putExtra("empleado", empleado);
                startActivity(intent);
            } else if (which == 1) {
                // Cambiar estado
                cambiarEstadoEmpleado(empleado);
            } else if (which == 2) {
                // Eliminar
                confirmarEliminar(empleado);
            }
        });
        builder.show();
    }

    private void cambiarEstadoEmpleado(Empleado empleado) {
        empleado.setDisponible(!empleado.isDisponible());

        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<ApiService.ApiResponse> call = apiService.updateEmpleado(empleado);

        call.enqueue(new Callback<ApiService.ApiResponse>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponse> call, Response<ApiService.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String estado = empleado.isDisponible() ? "activado" : "desactivado";
                    Toast.makeText(GestionarEmpleadosActivity.this,
                            "Empleado " + estado + " exitosamente", Toast.LENGTH_SHORT).show();
                    cargarEmpleados(); // Recargar lista
                } else {
                    Toast.makeText(GestionarEmpleadosActivity.this,
                            "Error al cambiar estado del empleado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                Toast.makeText(GestionarEmpleadosActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmarEliminar(Empleado empleado) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Está seguro de que desea eliminar a " + empleado.getNombre() + "?");
        builder.setPositiveButton("Eliminar", (dialog, which) -> {
            eliminarEmpleado(empleado);
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void eliminarEmpleado(Empleado empleado) {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<ApiService.ApiResponse> call = apiService.deleteEmpleado(empleado);

        call.enqueue(new Callback<ApiService.ApiResponse>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponse> call, Response<ApiService.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(GestionarEmpleadosActivity.this,
                            "Empleado eliminado exitosamente", Toast.LENGTH_SHORT).show();
                    cargarEmpleados(); // Recargar lista
                } else {
                    Toast.makeText(GestionarEmpleadosActivity.this,
                            "Error al eliminar empleado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                Toast.makeText(GestionarEmpleadosActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarEmpleados(); // Recargar al volver a la actividad
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gestionar_empleados, menu);
        return true;
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
