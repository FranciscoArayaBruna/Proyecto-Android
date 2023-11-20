package com.example.listafacil.Vista;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.listafacil.R;

import org.json.JSONArray;

import java.util.List;

public class TareaAdapter extends ArrayAdapter<String> {

    public TareaAdapter(Context context, List<String> tareas) {
        super(context, 0, tareas);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String tarea = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView txtTarea = convertView.findViewById(R.id.txtTarea);
        Button btnEditar = convertView.findViewById(R.id.btnEditar);
        Button btnEliminar = convertView.findViewById(R.id.btnEliminar);

        txtTarea.setText(tarea);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarTarea(position, tarea);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarTarea(position);
            }
        });

        return convertView;
    }

    private void eliminarTarea(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Eliminar Tarea");
        builder.setMessage("¿Estás seguro de que deseas eliminar esta tarea?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtiene la tarea que se va a eliminar
                String tarea = getItem(position);
                // Elimina la tarea de la lista
                remove(tarea);
                // Notifica al adaptador que los datos han cambiado
                notifyDataSetChanged();
                // Guarda la lista actualizada en las preferencias compartidas
                guardarTareasEnPrefs();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Añade este método para guardar la lista actualizada en las preferencias compartidas
    private void guardarTareasEnPrefs() {
        SharedPreferences prefs = getContext().getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < getCount(); i++) {
            jsonArray.put(getItem(i));
        }

        editor.putString("tareasGuardadas", jsonArray.toString());
        editor.apply();
    }



    private void editarTarea(final int position, String tareaActual) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Editar Tarea");

        final EditText input = new EditText(getContext());
        input.setText(tareaActual); // Muestra la tarea actual en el cuadro de texto de edición
        builder.setView(input);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tareaEditada = input.getText().toString();
                // Actualiza la tarea en la lista
                remove(getItem(position));
                insert(tareaEditada, position);
                // Notifica al adaptador que los datos han cambiado
                notifyDataSetChanged();
                // Guarda la lista actualizada en las preferencias compartidas
                guardarTareasEnPrefs();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


}

