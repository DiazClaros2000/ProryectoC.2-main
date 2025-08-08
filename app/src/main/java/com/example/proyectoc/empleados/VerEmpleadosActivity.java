package com.example.proyectoc.empleados;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class VerEmpleadosActivity extends AppCompatActivity implements EmpleadosAdapter.OnEmpleadoClickListener {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EmpleadosAdapter adapter;
    private List<Empleado> empleados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_empleados);

        // Configurar toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Ver Empleados");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Configurar RecyclerView
        empleados = new ArrayList<>();
        adapter = new EmpleadosAdapter(empleados, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Configurar SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarEmpleados();
            }
        });

        // Cargar empleados
        cargarEmpleados();
    }

    private void cargarEmpleados() {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<List<Empleado>> call = apiService.getEmpleados();

        call.enqueue(new Callback<List<Empleado>>() {
            @Override
            public void onResponse(Call<List<Empleado>> call, Response<List<Empleado>> response) {
                swipeRefreshLayout.setRefreshing(false);

                // Log response details
                android.util.Log.d("VerEmpleados", "Response Code: " + response.code());
                android.util.Log.d("VerEmpleados", "Response Body: " + response.body());

                if (response.isSuccessful() && response.body() != null) {
                    empleados.clear();
                    empleados.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    android.util.Log.d("VerEmpleados", "Empleados cargados: " + empleados.size());
                    Toast.makeText(VerEmpleadosActivity.this,
                            "Empleados cargados: " + empleados.size(), Toast.LENGTH_SHORT).show();
                } else {
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        errorBody = "Error reading response body";
                    }

                    android.util.Log.e("VerEmpleados", "Error response: " + errorBody);
                    Toast.makeText(VerEmpleadosActivity.this,
                            "Error al cargar empleados: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Empleado>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                android.util.Log.e("VerEmpleados", "Network error: " + t.getMessage(), t);
                Toast.makeText(VerEmpleadosActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onEmpleadoClick(Empleado empleado) {
        // Mostrar opciones de editar o eliminar
        String[] opciones = {"Editar", "Eliminar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opciones para " + empleado.getNombre());
        builder.setItems(opciones, (dialog, which) -> {
            if (which == 0) {
                // Editar
                Intent intent = new Intent(VerEmpleadosActivity.this, EditarEmpleadoActivity.class);
                intent.putExtra("empleado", empleado);
                startActivity(intent);
            } else {
                // Eliminar
                confirmarEliminar(empleado);
            }
        });
        builder.show();
    }

    private void confirmarEliminar(Empleado empleado) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                    Toast.makeText(VerEmpleadosActivity.this,
                            "Empleado eliminado exitosamente", Toast.LENGTH_SHORT).show();
                    cargarEmpleados(); // Recargar lista
                } else {
                    Toast.makeText(VerEmpleadosActivity.this,
                            "Error al eliminar empleado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                Toast.makeText(VerEmpleadosActivity.this,
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
        getMenuInflater().inflate(R.menu.ver_empleados_menu, menu);
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
