package com.example.listafacil;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

public class VistaPrincipal extends AppCompatActivity {

    private EditText edtNuevaTarea;
    private Button btnAgregarTarea;
    private ListView lstTareas;
    private ArrayList<String> listaTareas;
    private TareaAdapter adaptadorTareas;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal);



        edtNuevaTarea = findViewById(R.id.edtNuevaTarea);
        btnAgregarTarea = findViewById(R.id.btnAgregarTarea);
        lstTareas = findViewById(R.id.lstTareas);

        listaTareas = new ArrayList<>();
        adaptadorTareas = new TareaAdapter(this, listaTareas);
        lstTareas.setAdapter(adaptadorTareas);

        btnAgregarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevaTarea = edtNuevaTarea.getText().toString();

                if (!nuevaTarea.isEmpty()) {
                    listaTareas.add(nuevaTarea);

                    adaptadorTareas.notifyDataSetChanged();

                    edtNuevaTarea.setText("");
                }
            }
        });
    }
}