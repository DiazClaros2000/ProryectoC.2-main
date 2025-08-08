package com.example.proyectoc.activities;

import android.content.Intent;
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
                URL url = new URL("http://192.168.100.174/car_wash_api/routes/register.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("nombre", nombre);
                jsonBody.put("apellido", apellido);
                jsonBody.put("correo", correo);
                // la clave del JSON sigue siendo "contraseña" si tu backend la espera así:
                jsonBody.put("contraseña", contrasena);
                jsonBody.put("pais", pais);

                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    // Leer respuesta
                    InputStream is = conn.getInputStream();
                    String respuesta = streamToString(is);

                    // Intentar extraer id_usuario de la respuesta JSON
                    int idUsuario = parseIdFromResponse(respuesta);

                    if (idUsuario > 0) {
                        // Si encontramos el id en la respuesta, vamos directo a UsuarioActivity con el id correcto
                        runOnUiThread(() -> {
                            Toast.makeText(RegisterActivity.this, "Registro exitoso (id: " + idUsuario + ")", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, com.example.proyectoc.activities.UsuarioActivity.class);
                            intent.putExtra("id_usuario", idUsuario);
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        // No se encontró id en la respuesta
                        if (autoLoginAfterRegister) {
                            // Intentar login automático para obtener el id (usa tu API de login existente via Retrofit)
                            runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Registro OK. Obteniendo ID... (autologin)", Toast.LENGTH_SHORT).show());
                            doAutoLoginAndStart(correo, contrasena);
                        } else {
                            // Volver al login y pedir que inicie sesión manualmente
                            runOnUiThread(() -> {
                                Toast.makeText(RegisterActivity.this, "Registro exitoso. Por favor inicia sesión.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            });
                        }
                    }
                } else {
                    // Error del servidor al registrar
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

            // Posibles lugares donde el backend puede devolver el id:
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
            // Si no encontramos, retornar -1
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
                        // No se pudo loguear con las credenciales nuevas
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
