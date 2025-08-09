package com.example.proyectoc.api;

import com.example.proyectoc.empleados.modelo.Empleado;
import com.example.proyectoc.empleados.modelo.Servicio;
import com.example.proyectoc.model.LoginRequest;
import com.example.proyectoc.model.LoginResponse;
import com.example.proyectoc.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {
    @POST("routes/login_general.php")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // Perfil de usuario
    @GET("routes/usuarios/perfil.php")
    Call<Usuario> obtenerPerfil(@Query("id_usuario") int idUsuario);

    @PUT("routes/usuarios/actualizar.php")
    Call<LoginResponse> actualizarPerfil(@Body Usuario usuario);

    // Endpoints para Empleados
    @GET("GetEmpleados.php")
    Call<List<Empleado>> getEmpleados();

    @POST("CreateEmpleado.php")
    Call<ApiResponse> createEmpleado(@Body Empleado empleado);

    @PUT("UpdateEmpleado.php")
    Call<ApiResponse> updateEmpleado(@Body Empleado empleado);

    @POST("DeleteEmpleado.php")
    Call<ApiResponse> deleteEmpleado(@Body Empleado empleado);





    // Endpoints para Servicios
    @GET("GetServicios.php")
    Call<List<Servicio>> getServicios();

    // Clase para respuestas de la API
    class ApiResponse {
        private String message;
        private boolean issuccess;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isIssuccess() {
            return issuccess;
        }

        public void setIssuccess(boolean issuccess) {
            this.issuccess = issuccess;
        }
    }
}