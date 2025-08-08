package com.example.proyectoc.model;

public class Vehiculo {
    private int id_vehiculo;
    private int id_usuario;
    private String marca;
    private String modelo;
    private int anio;
    private String tipo_aceite;

    public int getId_vehiculo() { return id_vehiculo; }
    public void setId_vehiculo(int id_vehiculo) { this.id_vehiculo = id_vehiculo; }
    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public String getTipo_aceite() { return tipo_aceite; }
    public void setTipo_aceite(String tipo_aceite) { this.tipo_aceite = tipo_aceite; }
}