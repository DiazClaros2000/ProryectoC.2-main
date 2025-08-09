package com.example.proyectoc.activities;

import android.content.Intent;
import android.content.SharedPreferences; // AGREGADO
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoc.LoginActivity;
import com.example.proyectoc.R;
import com.example.proyectoc.api.ApiClient;
import com.example.proyectoc.api.ApiService;
import com.example.proyectoc.model.LoginRequest;
import com.example.proyectoc.model.LoginResponse;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText nombreInput, apellidoInput, correoInput, contrasenaInput, paisInput;
    Button btnRegistrar, btnVolver;

    // Si true: después del register intenta hacer login automático para obtener id_usuario
    // Si false: después del register vuelve al LoginActivity
    private final boolean autoLoginAfterRegister = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nombreInput = findViewById(R.id.nombreInput);
        apellidoInput = findViewById(R.id.apellidoInput);
        correoInput = findViewById(R.id.correoInput);
        contrasenaInput = findViewById(R.id.contraseñaInput); // id del layout original
        paisInput = findViewById(R.id.paisInput);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolver = findViewById(R.id.btnVolver);

        btnRegistrar.setOnClickListener(v -> registrarUsuario());

        btnVolver.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registrarUsuario() {
        String nombre = nombreInput.getText().toString().trim();
        String apellido = apellidoInput.getText().toString().trim();
        String correo = correoInput.getText().toString().trim();
        String contrasena = contrasenaInput.getText().toString().trim();
        String pais = paisInput.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || pais.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://16.16.0.14/car_wash_api/routes/register.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("nombre", nombre);
                jsonBody.put("apellido", apellido);
                jsonBody.put("correo", correo);
                jsonBody.put("contraseña", contrasena); // la clave del JSON sigue siendo "contraseña" si tu backend la espera así:
                jsonBody.put("pais", pais);

                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    InputStream is = conn.getInputStream();
                    String respuesta = streamToString(is);

                    int idUsuario = parseIdFromResponse(respuesta);

                    if (idUsuario > 0) {
                        // Guardar datos localmente
                        guardarDatosUsuario(idUsuario, nombre, apellido, correo, pais);

                        runOnUiThread(() -> {
                            Toast.makeText(RegisterActivity.this, "Registro exitoso (id: " + idUsuario + ")", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, com.example.proyectoc.activities.UsuarioActivity.class);
                            intent.putExtra("id_usuario", idUsuario);
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        if (autoLoginAfterRegister) {
                            runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Registro OK. Obteniendo ID... (autologin)", Toast.LENGTH_SHORT).show());
                            doAutoLoginAndStart(correo, contrasena);
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(RegisterActivity.this, "Registro exitoso. Por favor inicia sesión.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            });
                        }
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Error al registrar (codigo: " + responseCode + ")", Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                Log.e("RegisterError", e.toString());
                runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Error de red: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }

    // Guardar datos del usuario localmente
    private void guardarDatosUsuario(int idUsuario, String nombre, String apellido, String correo, String pais) {
        SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("id_usuario", idUsuario);
        editor.putString("nombre", nombre);
        editor.putString("apellido", apellido);
        editor.putString("correo", correo);
        editor.putString("pais", pais);
        editor.apply();
    }

    // Convierte InputStream a String
    private String streamToString(InputStream is) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();
            return sb.toString();
        } catch (Exception e) {
            Log.e("StreamError", e.toString());
            return "";
        }
    }

    // Intentamos extraer id_usuario desde varios posibles formatos JSON
    private int parseIdFromResponse(String resp) {
        try {
            if (resp == null || resp.isEmpty()) return -1;
            JSONObject o = new JSONObject(resp);

            if (o.has("id_usuario")) return o.getInt("id_usuario");
            if (o.has("id")) return o.getInt("id");
            if (o.has("data")) {
                Object data = o.get("data");
                if (data instanceof JSONObject) {
                    JSONObject d = (JSONObject) data;
                    if (d.has("id_usuario")) return d.getInt("id_usuario");
                    if (d.has("id")) return d.getInt("id");
                }
            }
            return -1;
        } catch (Exception e) {
            Log.e("ParseError", e.toString());
            return -1;
        }
    }

    // Llamada a la API de login mediante Retrofit para obtener id y role
    private void doAutoLoginAndStart(String correo, String contrasena) {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(correo, contrasena);
        api.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();
                    if (res.isSuccess()) {
                        int idUsuario = res.getData().id_usuario;
                        String rol = res.getData().rol;

                        // Guardar datos también en autologin
                        guardarDatosUsuario(idUsuario,
                                nombreInput.getText().toString().trim(),
                                apellidoInput.getText().toString().trim(),
                                correoInput.getText().toString().trim(),
                                paisInput.getText().toString().trim());

                        runOnUiThread(() -> {
                            Toast.makeText(RegisterActivity.this, "Bienvenido (id: " + idUsuario + ")", Toast.LENGTH_SHORT).show();
                            if ("usuario".equals(rol)) {
                                Intent intent = new Intent(RegisterActivity.this, com.example.proyectoc.activities.UsuarioActivity.class);
                                intent.putExtra("id_usuario", idUsuario);
                                startActivity(intent);
                            } else if ("admin".equals(rol)) {
                                startActivity(new Intent(RegisterActivity.this, com.example.proyectoc.activities.AdminMenuActivity.class));
                            }
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(RegisterActivity.this, "Registro OK, pero no se pudo loguear automáticamente: " + res.getMessage(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(RegisterActivity.this, "Registro OK. Falló al obtener ID (respuesta login inválida)", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    });
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "Registro OK. Error al autologin: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                });
            }
        });
    }
}
