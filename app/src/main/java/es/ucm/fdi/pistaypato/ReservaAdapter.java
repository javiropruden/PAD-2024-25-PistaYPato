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

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder> {

    private List<Reserva> reservaList;
    private OnItemClickListener listener;
    private int selectedPosition = -1; // Mantener el índice del ítem seleccionado

    // Constructor
    public ReservaAdapter(List<Reserva> list, OnItemClickListener listener) {
        this.reservaList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reserva, parent, false); // Asegúrate de tener un layout adecuado para cada item
        return new ReservaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Reserva reserva = reservaList.get(position);

        holder.lugarText.setText("Pista: " + reserva.getPista());
        holder.diaText.setText("Fecha: " + reserva.getFecha());
        holder.horaText.setText("Hora: " + reserva.getHora());

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
            listener.onItemClick(reserva);
        });
    }

    @Override
    public int getItemCount() {

        Log.d("Jiayun ", "jia " + reservaList.size());
        return reservaList.size();
    }

    // Método para actualizar los datos
    public void updateData(List<Reserva> newList) {
        reservaList = newList;
        notifyDataSetChanged();
    }

    // ViewHolder para cada ítem
    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView lugarText, horaText, diaText;
        Reserva sol;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);

            lugarText = itemView.findViewById(R.id.lugar_reserva);
            diaText = itemView.findViewById(R.id.dia_reserva);
            horaText = itemView.findViewById(R.id.hora_reserva);

        }
    }

    // Interfaz para manejar clics
    public interface OnItemClickListener {
        void onItemClick(Reserva reserva);
    }
}
