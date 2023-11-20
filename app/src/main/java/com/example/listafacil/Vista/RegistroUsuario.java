package com.example.listafacil.Vista;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.listafacil.Controlador.ConexionHelper;
import com.example.listafacil.Controlador.Utility;
import com.example.listafacil.R;

public class RegistroUsuario extends Activity {

    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    public void register(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (isValidInput(username, password)) {
            // Guardar usuario en la base de datos
            guardarUsuarioEnBD(username, password);

            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

            // Redirigir a la pantalla de inicio de sesión automáticamente
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Por favor, ingrese un nombre de usuario y una contraseña válidos", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarUsuarioEnBD(String correo, String contrasena) {
        ConexionHelper conn = new ConexionHelper(this, "TABLA_USUARIO", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Utility.CAMPO_CORREO, correo);
        contentValues.put(Utility.CAMPO_CONTRASENA, contrasena);  // Aquí estaba el error

        long idResultante = db.insert(Utility.TABLA_USUARIO, null, contentValues);
        Toast.makeText(getApplicationContext(), "ATENCIÓN, id Registrado..." + idResultante,
                Toast.LENGTH_SHORT).show();

        db.close();
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

        // Verificar si el usuario ya existe en la base de datos
        ConexionHelper conn = new ConexionHelper(this, "TABLA_USUARIO", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Utility.TABLA_USUARIO + " WHERE " + Utility.CAMPO_CORREO + "=?", new String[]{username});
        boolean userExists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        if (userExists) {
            Toast.makeText(this, "Este usuario ya está registrado", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
