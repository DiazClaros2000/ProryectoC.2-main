package com.example.proyectoc.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.proyectoc.R;
import com.example.proyectoc.api.ApiClient;
import com.example.proyectoc.api.ApiService;
import com.example.proyectoc.model.Cotizacion;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GestionCotizacionesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCotizaciones;
    private CotizacionesAdapter cotizacionesAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private ApiService apiService;
    private List<Cotizacion> listaCotizaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cotizaciones);

        inicializarVistas();
        configurarRecyclerView();
        cargarCotizaciones();
    }

    private void inicializarVistas() {
        recyclerViewCotizaciones = findViewById(R.id.recyclerViewCotizaciones);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        apiService = ApiClient.getRetrofit().create(ApiService.class);
        listaCotizaciones = new ArrayList<>();

        swipeRefresh.setOnRefreshListener(this::cargarCotizaciones);
    }

    private void configurarRecyclerView() {
        cotizacionesAdapter = new CotizacionesAdapter(listaCotizaciones, new CotizacionesAdapter.OnCotizacionClickListener() {
            @Override
            public void onResponderClick(Cotizacion cotizacion) {
                mostrarDialogResponder(cotizacion);
            }

            @Override
            public void onVerDetallesClick(Cotizacion cotizacion) {
                // Implementar si quieres mostrar más detalles
                Toast.makeText(GestionCotizacionesActivity.this,
                        "Detalles de " + cotizacion.getNombreCompleto(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerViewCotizaciones.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCotizaciones.setAdapter(cotizacionesAdapter);
    }

    private void cargarCotizaciones() {
        swipeRefresh.setRefreshing(true);

        Call<ApiService.ApiResponseWithData<List<Cotizacion>>> call = apiService.listarCotizacionesAdmin();

        call.enqueue(new Callback<ApiService.ApiResponseWithData<List<Cotizacion>>>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponseWithData<List<Cotizacion>>> call,
                                   Response<ApiService.ApiResponseWithData<List<Cotizacion>>> response) {
                swipeRefresh.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        listaCotizaciones.clear();
                        listaCotizaciones.addAll(response.body().getData());
                        cotizacionesAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(GestionCotizacionesActivity.this,
                                response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GestionCotizacionesActivity.this,
                            "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponseWithData<List<Cotizacion>>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(GestionCotizacionesActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogResponder(Cotizacion cotizacion) {
        ResponderCotizacionDialog dialog = new ResponderCotizacionDialog(this, cotizacion,
                new ResponderCotizacionDialog.OnResponderListener() {
                    @Override
                    public void onResponder(int idCotizacion, double precio, String nota) {
                        responderCotizacion(idCotizacion, precio, nota);
                    }
                });
        dialog.show();
    }

    private void responderCotizacion(int idCotizacion, double precio, String nota) {
        ApiService.ResponderCotizacionRequest request =
                new ApiService.ResponderCotizacionRequest(idCotizacion, precio, nota);

        Call<ApiService.ApiResponse> call = apiService.responderCotizacion(request);

        call.enqueue(new Callback<ApiService.ApiResponse>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponse> call,
                                   Response<ApiService.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isIssuccess()) {
                        Toast.makeText(GestionCotizacionesActivity.this,
                                "Cotización respondida exitosamente", Toast.LENGTH_SHORT).show();
                        cargarCotizaciones(); // Recargar la lista
                    } else {
                        Toast.makeText(GestionCotizacionesActivity.this,
                                response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GestionCotizacionesActivity.this,
                            "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                Toast.makeText(GestionCotizacionesActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}