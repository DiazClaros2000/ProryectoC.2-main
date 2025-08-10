package com.example.proyectoc.model;

public class Cotizacion {
    private int id_cotizacion;
    private String fecha_solicitud;
    private String fecha_servicio;
    private String hora_servicio;
    private String ubicacion;
    private String latitud;
    private String longitud;
    private String estado;
    private double precio_ofrecido;
    private String nota_admin;
    private String fecha_respuesta;
    private String telefono;
    private String nombre_usuario;
    private String apellido_usuario;
    private String email_usuario;
    private String marca;
    private String modelo;
    private int anio;
    private String nombre_servicio;
    private double precio_en_sitio;
    private double precio_a_domicilio;

    // Constructor vacío
    public Cotizacion() {}

    // Getters y Setters
    public int getId_cotizacion() { return id_cotizacion; }
    public void setId_cotizacion(int id_cotizacion) { this.id_cotizacion = id_cotizacion; }

    public String getFecha_solicitud() { return fecha_solicitud; }
    public void setFecha_solicitud(String fecha_solicitud) { this.fecha_solicitud = fecha_solicitud; }

    public String getFecha_servicio() { return fecha_servicio; }
    public void setFecha_servicio(String fecha_servicio) { this.fecha_servicio = fecha_servicio; }

    public String getHora_servicio() { return hora_servicio; }
    public void setHora_servicio(String hora_servicio) { this.hora_servicio = hora_servicio; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getPrecio_ofrecido() { return precio_ofrecido; }
    public void setPrecio_ofrecido(double precio_ofrecido) { this.precio_ofrecido = precio_ofrecido; }

    public String getNota_admin() { return nota_admin; }
    public void setNota_admin(String nota_admin) { this.nota_admin = nota_admin; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getNombre_usuario() { return nombre_usuario; }
    public void setNombre_usuario(String nombre_usuario) { this.nombre_usuario = nombre_usuario; }

    public String getApellido_usuario() { return apellido_usuario; }
    public void setApellido_usuario(String apellido_usuario) { this.apellido_usuario = apellido_usuario; }

    public String getEmail_usuario() { return email_usuario; }
    public void setEmail_usuario(String email_usuario) { this.email_usuario = email_usuario; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getNombre_servicio() { return nombre_servicio; }
    public void setNombre_servicio(String nombre_servicio) { this.nombre_servicio = nombre_servicio; }

    public double getPrecio_en_sitio() { return precio_en_sitio; }
    public void setPrecio_en_sitio(double precio_en_sitio) { this.precio_en_sitio = precio_en_sitio; }

    public double getPrecio_a_domicilio() { return precio_a_domicilio; }
    public void setPrecio_a_domicilio(double precio_a_domicilio) { this.precio_a_domicilio = precio_a_domicilio; }

    // Métodos helper
    public String getNombreCompleto() {
        return nombre_usuario + " " + apellido_usuario;
    }

    public String getInfoVehiculo() {
        if (marca != null && modelo != null) {
            return marca + " " + modelo + " (" + anio + ")";
        }
        return "Vehículo no especificado";
    }

    public double getPrecioSugerido() {
        if ("domicilio".equals(ubicacion)) {
            return precio_a_domicilio;
        } else {
            return precio_en_sitio;
        }
    }
}