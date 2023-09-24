package com.example.listafacil;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
                // Lógica para editar la tarea
                // Abre un cuadro de diálogo de edición aquí
                editarTarea(position, tarea);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para eliminar la tarea
                remove(tarea); // Elimina la tarea de la lista
                notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
            }
        });

        return convertView;
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
                getItem(position);
                // Actualiza la tarea en la lista
                remove(tareaActual);
                insert(tareaEditada, position);
                // Notifica al adaptador que los datos han cambiado
                notifyDataSetChanged();
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

