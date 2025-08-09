package com.example.proyectoc.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proyectoc.R;
import com.example.proyectoc.api.ApiClient;
import com.example.proyectoc.api.ApiService;
import com.example.proyectoc.model.LoginResponse;
import com.example.proyectoc.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etPais, etCorreo, etFoto;
    private ImageView ivFoto;
    private Button btnEditar, btnGuardar;
    private ApiService apiService;
    private int idUsuario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        idUsuario = getIntent().getIntExtra("id_usuario", -1);
        apiService = ApiClient.getRetrofit().create(ApiService.class);

        etNombre = findViewById(R.id.etNombrePerfil);
        etApellido = findViewById(R.id.etApellidoPerfil);
        etPais = findViewById(R.id.etPaisPerfil);
        etCorreo = findViewById(R.id.etCorreoPerfil);
        etFoto = findViewById(R.id.etFotoPerfil);
        ivFoto = findViewById(R.id.ivFotoPerfil);
        btnEditar = findViewById(R.id.btnEditarPerfil);
        btnGuardar = findViewById(R.id.btnGuardarPerfil);

        setEditable(false);
        cargarPerfil();

        btnEditar.setOnClickListener(v -> setEditable(true));
        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void setEditable(boolean editable) {
        etNombre.setEnabled(editable);
        etApellido.setEnabled(editable);
        etPais.setEnabled(editable);
        etCorreo.setEnabled(false); // correo no editable normalmente
        etFoto.setEnabled(editable);
        btnGuardar.setVisibility(editable ? View.VISIBLE : View.GONE);
    }

    private void cargarPerfil() {
        apiService.obtenerPerfil(idUsuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario u = response.body();
                    etNombre.setText(u.getNombre());
                    etApellido.setText(u.getApellido());
                    etPais.setText(u.getPais());
                    etCorreo.setText(u.getCorreo());
                    etFoto.setText(u.getFoto_perfil());
                    if (u.getFoto_perfil() != null && !u.getFoto_perfil().isEmpty()) {
                        Glide.with(PerfilActivity.this).load(u.getFoto_perfil()).into(ivFoto);
                    } else {
                        ivFoto.setImageResource(R.drawable.ic_launcher_foreground);
                    }
                } else {
                    Toast.makeText(PerfilActivity.this, "No se pudo obtener el perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(PerfilActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarCambios() {
        Usuario u = new Usuario();
        u.setId_usuario(idUsuario);
        u.setNombre(etNombre.getText().toString().trim());
        u.setApellido(etApellido.getText().toString().trim());
        u.setPais(etPais.getText().toString().trim());
        u.setCorreo(etCorreo.getText().toString().trim());
        u.setFoto_perfil(etFoto.getText().toString().trim());

        apiService.actualizarPerfil(u).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(PerfilActivity.this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                    setEditable(false);
                    cargarPerfil();
                } else {
                    Toast.makeText(PerfilActivity.this, "No se pudo actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(PerfilActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}