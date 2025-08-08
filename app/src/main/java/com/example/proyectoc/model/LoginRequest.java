package com.example.proyectoc.model;

public class LoginRequest {
    private String correo;
    private String contraseña;

    public LoginRequest(String correo, String contraseña) {
        this.correo = correo;
        this.contraseña = contraseña;
    }
}
