package com.example.listafacil.Vista;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.listafacil.Controlador.ConexionHelper;
import com.example.listafacil.Controlador.Utility;
import com.example.listafacil.R;

public class LoginActivity extends Activity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        // Borrar cualquier texto existente en los EditText
        editTextUsername.setText("");
        editTextPassword.setText("");
    }

    private void login() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (userExists(username)) {
            if (isValidUser(username, password)) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                guardarCorreoUsuario(username);
                Intent i = new Intent(LoginActivity.this, VistaPrincipal.class);
                i.putExtra("USERNAME", username);
                startActivity(i);
            } else {
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        String correo = editTextUsername.getText().toString();
        String contrasena = editTextPassword.getText().toString();

        if (isValidInput(correo, contrasena)) {
            // Verificar si el usuario ya está registrado antes de abrir la base de datos
            if (userExists(correo)) {
                Toast.makeText(this, "Este correo ya está registrado", Toast.LENGTH_SHORT).show();
            } else {
                // Guardar usuario en la base de datos
                guardarUsuarioEnBD(correo, contrasena);

                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                // Realizar el inicio de sesión automáticamente después del registro
                guardarCorreoUsuario(correo);
                Intent intent = new Intent(this, VistaPrincipal.class);
                intent.putExtra("USERNAME", correo);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "Por favor, ingrese un correo y una contraseña válidos", Toast.LENGTH_SHORT).show();
        }
    }


    private void guardarUsuarioEnBD(String correo, String contrasena) {
        ConexionHelper conn = new ConexionHelper(this, "TABLA_USUARIO", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        // Verificar si el usuario ya existe en la base de datos
        if (userExists(correo)) {
            Toast.makeText(this, "Este correo ya está registrado", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Utility.CAMPO_CORREO, correo);
            contentValues.put(Utility.CAMPO_CONTRASENA, contrasena);

            // Insertar usuario en la base de datos
            long idResultante = db.insert(Utility.TABLA_USUARIO, null, contentValues);

            // Mostrar un mensaje indicando que el registro fue exitoso
            Toast.makeText(getApplicationContext(), "Registro exitoso. ID: " + idResultante, Toast.LENGTH_SHORT).show();
        }

        // Cerrar la conexión a la base de datos después de realizar la operación
        db.close();
    }




    private void guardarCorreoUsuario(String correo) {
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userEmail", correo);
        editor.apply();
    }

    private boolean userExists(String correo) {
        ConexionHelper conn = new ConexionHelper(this, "TABLA_USUARIO", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + Utility.TABLA_USUARIO + " WHERE " + Utility.CAMPO_CORREO + "=?", new String[]{correo});
            return cursor.getCount() > 0;
        } finally {
            db.close();
        }
    }

    private boolean isValidUser(String username, String password) {
        ConexionHelper conn = new ConexionHelper(this, "TABLA_USUARIO", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + Utility.TABLA_USUARIO + " WHERE " + Utility.CAMPO_CORREO + "=? AND " + Utility.CAMPO_CONTRASENA + "=?", new String[]{username, password});
            return cursor.getCount() > 0;
        } finally {
            db.close();
        }
    }

    private boolean isValidInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un nombre de usuario y una contraseña válidos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
