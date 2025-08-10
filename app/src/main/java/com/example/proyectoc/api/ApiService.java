package com.example.proyectoc.api;

import com.example.proyectoc.empleados.modelo.Empleado;
import com.example.proyectoc.empleados.modelo.Servicio;
import com.example.proyectoc.model.LoginRequest;
import com.example.proyectoc.model.LoginResponse;
import com.example.proyectoc.model.Usuario;
import com.example.proyectoc.model.Cotizacion;

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

    // Endpoints para Cotizaciones
    @GET("cotizacion/listar_admin.php")
    Call<ApiResponseWithData<List<Cotizacion>>> listarCotizacionesAdmin();

    @POST("cotizacion/responder.php")
    Call<ApiResponse> responderCotizacion(@Body ResponderCotizacionRequest request);






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
    // Agregar estas clases al final de ApiService.java, después de ApiResponse:

    class ResponderCotizacionRequest {
        public int id_cotizacion;
        public double precio_ofrecido;
        public String nota_admin;

        public ResponderCotizacionRequest(int id_cotizacion, double precio_ofrecido, String nota_admin) {
            this.id_cotizacion = id_cotizacion;
            this.precio_ofrecido = precio_ofrecido;
            this.nota_admin = nota_admin;
        }
    }

    class ApiResponseWithData<T> {
        private boolean success;
        private String message;
        private T data;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public T getData() { return data; }
        public void setData(T data) { this.data = data; }
    }
}