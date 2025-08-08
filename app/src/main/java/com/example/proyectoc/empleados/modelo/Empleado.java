package com.example.proyectoc.empleados.modelo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Empleado implements Serializable {

    @SerializedName("idEmpleado")
    private int idEmpleado;

    @SerializedName("id_servicio")
    private Integer id_servicio;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("disponible")
    private boolean disponible;

    @SerializedName("zona")
    private String zona;

    @SerializedName("nombre_servicio")
    private String nombre_servicio;

    // Constructor
    public Empleado() {}

    public Empleado(int idEmpleado, Integer id_servicio, String nombre, boolean disponible, String zona, String nombre_servicio) {
        this.idEmpleado = idEmpleado;
        this.id_servicio = id_servicio;
        this.nombre = nombre;
        this.disponible = disponible;
        this.zona = zona;
        this.nombre_servicio = nombre_servicio;
    }

    // Getters y Setters
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Integer getIdServicio() {
        return id_servicio;
    }

    public void setIdServicio(Integer id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getNombreServicio() {
        return nombre_servicio;
    }

    public void setNombreServicio(String nombre_servicio) {
        this.nombre_servicio = nombre_servicio;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "idEmpleado=" + idEmpleado +
                ", id_servicio=" + id_servicio +
                ", nombre='" + nombre + '\'' +
                ", disponible=" + disponible +
                ", zona='" + zona + '\'' +
                ", nombre_servicio='" + nombre_servicio + '\'' +
                '}';
    }
}
