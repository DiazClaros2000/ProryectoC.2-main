package com.example.proyectoc.model;

public class LoginResponse {
    private boolean success;
    private String message;
    private UserData data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public UserData getData() { return data; }

    public static class UserData {
        public int id_usuario;
        public int id_admin;
        public String nombre;
        public String correo;
        public String rol;
    }
}