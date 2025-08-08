package com.example.proyectoc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoc.R;
import com.example.proyectoc.api.RestApiMethodsV;
import com.example.proyectoc.model.Vehiculo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MisVehiculosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VehiculoAdapter adapter;
    private List<Vehiculo> vehiculos = new ArrayList<>();
    private Button btnAgregarVehiculo;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_vehiculos);

        recyclerView = findViewById(R.id.recyclerVehiculos);
        btnAgregarVehiculo = findViewById(R.id.btnAgregarVehiculo);

        adapter = new VehiculoAdapter(vehiculos, this::eliminarVehiculo);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        idUsuario = getIntent().getIntExtra("id_usuario", -1);
        Toast.makeText(this, "MisVehiculosActivity id_usuario: " + idUsuario, Toast.LENGTH_LONG).show();

        btnAgregarVehiculo.setOnClickListener(v -> {
            if (vehiculos.size() < 3) {
                Intent intent = new Intent(this, AgregarVehiculoActivity.class);
                intent.putExtra("id_usuario", idUsuario);
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(this, "Solo puedes registrar hasta 3 vehículos", Toast.LENGTH_SHORT).show();
            }
        });

        cargarVehiculos();
    }

    private void cargarVehiculos() {
        String url = RestApiMethodsV.GET_VEHICULOS_USUARIO + "?id_usuario=" + idUsuario;
        new Thread(() -> {
            try {
                URL urlObj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = new BufferedReader(new InputStreamReader(in))
                        .lines().collect(Collectors.joining("\n"));
                JSONArray array = new JSONArray(result);
                vehiculos.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Vehiculo v = new Vehiculo();
                    v.setId_vehiculo(obj.getInt("id_vehiculo"));
                    v.setMarca(obj.getString("marca"));
                    v.setModelo(obj.getString("modelo"));
                    v.setAnio(obj.getInt("anio"));
                    v.setTipo_aceite(obj.getString("Aceite"));
                    vehiculos.add(v);
                }
                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                    btnAgregarVehiculo.setEnabled(vehiculos.size() < 3);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void eliminarVehiculo(Vehiculo vehiculo) {
        new Thread(() -> {
            try {
                URL url = new URL(RestApiMethodsV.DELETE_VEHICULO);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                JSONObject obj = new JSONObject();
                obj.put("id_vehiculo", vehiculo.getId_vehiculo());

                OutputStream os = conn.getOutputStream();
                os.write(obj.toString().getBytes("UTF-8"));
                os.close();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = new BufferedReader(new InputStreamReader(in))
                        .lines().collect(Collectors.joining("\n"));

                JSONObject response = new JSONObject(result);
                boolean success = response.optBoolean("success", false);

                runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(this, "Vehículo eliminado", Toast.LENGTH_SHORT).show();
                        cargarVehiculos();
                    } else {
                        Toast.makeText(this, "Error al eliminar", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error eliminar: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            cargarVehiculos();
        }
    }
}
