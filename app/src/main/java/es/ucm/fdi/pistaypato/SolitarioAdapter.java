package es.ucm.fdi.pistaypato;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SolitarioAdapter extends RecyclerView.Adapter<SolitarioAdapter.SolitarioViewHolder> {

    private List<Solitario> solitarioList;
    private OnItemClickListener listener;
    private int selectedPosition = -1; // Mantener el índice del ítem seleccionado

    // Constructor
    public SolitarioAdapter(List<Solitario> solitarioList, OnItemClickListener listener) {
        this.solitarioList = solitarioList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SolitarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.solitario, parent, false); // Asegúrate de tener un layout adecuado para cada item
        return new SolitarioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SolitarioViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Solitario solitario = solitarioList.get(position);

        holder.lugarText.setText("Pista: " + solitario.getLugar());
        holder.diaText.setText("Fecha: " + solitario.getFecha());
        holder.numPerfilesText.setText("Jugadores apuntados: " + String.valueOf(solitario.getNumPersonas()));

        // Cambiar el fondo si es el ítem seleccionado
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(Color.LTGRAY); // Cambiar el color del fondo del ítem seleccionado
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE); // Restaurar el fondo para los ítems no seleccionados
        }

        // Manejo del clic
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position; // Guardar la posición del ítem seleccionado
            notifyDataSetChanged(); // Notificar que los datos han cambiado para actualizar la vista
            listener.onItemClick(solitario);

        });
    }

    @Override
    public int getItemCount() {
        return solitarioList.size();
    }

    // Método para actualizar los datos
    public void updateData(List<Solitario> newList) {
        solitarioList = newList;
        notifyDataSetChanged();
    }

    // ViewHolder para cada ítem
    public static class SolitarioViewHolder extends RecyclerView.ViewHolder {
        TextView lugarText, numPerfilesText, diaText;
        Solitario sol;

        public SolitarioViewHolder(@NonNull View itemView) {
            super(itemView);

            lugarText = itemView.findViewById(R.id.lugar_solitario);
            diaText = itemView.findViewById(R.id.dia_sol);
            numPerfilesText = itemView.findViewById(R.id.numPerfiles);
        }
    }

    // Interfaz para manejar clics
    public interface OnItemClickListener {
        void onItemClick(Solitario solitario);
    }
}
