package com.example.listafacil.Controlador;

public class Utility {

    public static final String TABLA_USUARIO = "usuario";
    public static final String CAMPO_CORREO = "correo";
    public static final String CAMPO_CONTRASENA = "contrasena";

    public static final String CREAR_TABLA_USUARIO = "CREATE TABLE " +
            "" + TABLA_USUARIO + " (" +
            CAMPO_CORREO + " TEXT PRIMARY KEY, " +
            CAMPO_CONTRASENA + " TEXT)";
}

