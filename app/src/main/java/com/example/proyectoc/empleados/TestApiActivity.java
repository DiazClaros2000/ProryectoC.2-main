package com.example.proyectoc.empleados;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoc.R;
import com.example.proyectoc.api.ApiService;
import com.example.proyectoc.empleados.modelo.Empleado;
import com.example.proyectoc.empleados.modelo.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestApiActivity extends AppCompatActivity {

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);

        tvResult = findViewById(R.id.tvResult);

        // Test API connection
        testApiConnection();
    }

    private void testApiConnection() {
        tvResult.setText("Probando conexión...");

        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<List<Empleado>> call = apiService.getEmpleados();

        call.enqueue(new Callback<List<Empleado>>() {
            @Override
            public void onResponse(Call<List<Empleado>> call, Response<List<Empleado>> response) {
                Log.d("TestApi", "Response Code: " + response.code());
                Log.d("TestApi", "Response Body: " + response.body());

                String result = "Código de respuesta: " + response.code() + "\n\n";

                if (response.isSuccessful() && response.body() != null) {
                    result += "Empleados encontrados: " + response.body().size() + "\n\n";
                    for (Empleado empleado : response.body()) {
                        result += "- " + empleado.getNombre() + " (" + empleado.getZona() + ")\n";
                    }
                } else {
                    result += "Error en la respuesta\n";
                    try {
                        if (response.errorBody() != null) {
                            result += "Error body: " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        result += "Error leyendo respuesta: " + e.getMessage();
                    }
                }

                tvResult.setText(result);
            }

            @Override
            public void onFailure(Call<List<Empleado>> call, Throwable t) {
                Log.e("TestApi", "Network error: " + t.getMessage(), t);
                String result = "Error de conexión:\n" + t.getMessage();
                tvResult.setText(result);
                Toast.makeText(TestApiActivity.this, "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });
    }
}
