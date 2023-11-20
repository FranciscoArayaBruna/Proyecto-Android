package com.example.semana12_sqliteapp.Modelo;

public class Usuario {

    // Atributos de la clase
    private String correo;
    private String contrasena;

    // Constructor por defecto
    public Usuario() {
    }

    // Constructor con parámetros
    public Usuario(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
    }

    // Métodos de acceso (getters y setters) para cada atributo
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    // Método toString para obtener una representación de cadena del objeto
    @Override
    public String toString() {
        return "Usuario{" +
                "correo='" + correo + '\'' +
                ", contrasena='" + contrasena + '\'' +
                '}';
    }
}
