package com.example.proyectoc.activities;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoc.R;
import com.example.proyectoc.api.RestApiMethodsV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AgregarVehiculoActivity extends AppCompatActivity {
    private Spinner spinnerMarca, spinnerModelo, spinnerAnio, spinnerAceite;
    private Button btnGuardar;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_vehiculo);

        idUsuario = getIntent().getIntExtra("id_usuario", -1);
        Toast.makeText(this, "AgregarVehiculoActivity id_usuario: " + idUsuario, Toast.LENGTH_LONG).show();

        spinnerMarca = findViewById(R.id.spinnerMarca);
        spinnerModelo = findViewById(R.id.spinnerModelo);
        spinnerAnio = findViewById(R.id.spinnerAnio);
        spinnerAceite = findViewById(R.id.spinnerAceite);
        btnGuardar = findViewById(R.id.btnGuardar);

        cargarMarcas();

        spinnerMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String marca = (String) parent.getItemAtPosition(position);
                cargarModelos(marca);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerModelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String modelo = (String) parent.getItemAtPosition(position);
                String marca = (String) spinnerMarca.getSelectedItem();
                cargarAnios(marca, modelo);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerAnio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String anio = (String) parent.getItemAtPosition(position);
                String marca = (String) spinnerMarca.getSelectedItem();
                String modelo = (String) spinnerModelo.getSelectedItem();
                cargarAceites(marca, modelo, anio);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnGuardar.setOnClickListener(v -> guardarVehiculo());
    }

    private void cargarMarcas() {
        new Thread(() -> {
            try {
                URL url = new URL(RestApiMethodsV.GET_MARCAS);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = new BufferedReader(new InputStreamReader(in))
                        .lines().collect(Collectors.joining("\n"));
                JSONArray array = new JSONArray(result);
                List<String> marcas = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    marcas.add(array.getString(i));
                }
                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, marcas);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMarca.setAdapter(adapter);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error cargarMarcas: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void cargarModelos(String marca) {
        new Thread(() -> {
            try {
                URL url = new URL(RestApiMethodsV.GET_MODELOS + "?marca=" + URLEncoder.encode(marca, "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = new BufferedReader(new InputStreamReader(in))
                        .lines().collect(Collectors.joining("\n"));
                JSONArray array = new JSONArray(result);
                List<String> modelos = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    modelos.add(array.getString(i));
                }
                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modelos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerModelo.setAdapter(adapter);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error cargarModelos: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void cargarAnios(String marca, String modelo) {
        new Thread(() -> {
            try {
                URL url = new URL(RestApiMethodsV.GET_ANIOS + "?marca="
                        + URLEncoder.encode(marca, "UTF-8") + "&modelo=" + URLEncoder.encode(modelo, "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = new BufferedReader(new InputStreamReader(in))
                        .lines().collect(Collectors.joining("\n"));
                JSONArray array = new JSONArray(result);
                List<String> anios = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    anios.add(array.getString(i));
                }
                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, anios);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerAnio.setAdapter(adapter);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error cargarAnios: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void cargarAceites(String marca, String modelo, String anio) {
        new Thread(() -> {
            try {
                URL url = new URL(RestApiMethodsV.GET_ACEITES + "?marca="
                        + URLEncoder.encode(marca, "UTF-8")
                        + "&modelo=" + URLEncoder.encode(modelo, "UTF-8")
                        + "&anio=" + URLEncoder.encode(anio, "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = new BufferedReader(new InputStreamReader(in))
                        .lines().collect(Collectors.joining("\n"));
                JSONArray array = new JSONArray(result);
                List<String> aceites = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    aceites.add(array.getString(i));
                }
                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, aceites);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerAceite.setAdapter(adapter);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error cargarAceites: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void guardarVehiculo() {
        String marca = (String) spinnerMarca.getSelectedItem();
        String modelo = (String) spinnerModelo.getSelectedItem();
        String anioStr = (String) spinnerAnio.getSelectedItem();
        String aceite = (String) spinnerAceite.getSelectedItem();

        int anioInt;
        try {
            anioInt = Integer.parseInt(anioStr);
        } catch (Exception e) {
            Toast.makeText(this, "Año inválido", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject vehiculo = new JSONObject();
        try {
            vehiculo.put("id_usuario", idUsuario);
            vehiculo.put("marca", marca);
            vehiculo.put("modelo", modelo);
            vehiculo.put("anio", anioInt);
            vehiculo.put("Aceite", aceite);
        } catch (JSONException e) { e.printStackTrace(); }

        new Thread(() -> {
            try {
                URL url = new URL(RestApiMethodsV.ADD_VEHICULO);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(vehiculo.toString().getBytes("UTF-8"));
                os.close();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = new BufferedReader(new InputStreamReader(in))
                        .lines().collect(Collectors.joining("\n"));

                JSONObject response = new JSONObject(result);
                boolean success = response.optBoolean("success", false);

                runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(this, "Vehículo guardado correctamente", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        String msg = response.optString("message", "Error al guardar");
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error guardarVehiculo: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
