package com.example.proyectoc.empleados.modelo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Servicio implements Serializable {

    @SerializedName("id_servicio")
    private int id_servicio;

    @SerializedName("nombre_servicio")
    private String nombre_servicio;

    // Constructor
    public Servicio() {}

    public Servicio(int id_servicio, String nombre_servicio) {
        this.id_servicio = id_servicio;
        this.nombre_servicio = nombre_servicio;
    }

    // Getters y Setters
    public int getIdServicio() {
        return id_servicio;
    }

    public void setIdServicio(int id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getNombreServicio() {
        return nombre_servicio;
    }

    public void setNombreServicio(String nombre_servicio) {
        this.nombre_servicio = nombre_servicio;
    }

    @Override
    public String toString() {
        return nombre_servicio;
    }
}
