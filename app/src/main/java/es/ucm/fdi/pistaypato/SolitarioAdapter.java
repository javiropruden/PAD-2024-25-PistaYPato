package es.ucm.fdi.pistaypato;

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

    public SolitarioAdapter(List<Solitario> solitarioList) {
        this.solitarioList = solitarioList;
    }

    @NonNull
    @Override
    public SolitarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.solitario, parent, false);
        return new SolitarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolitarioViewHolder holder, int position) {
        Solitario solitario = solitarioList.get(position);
        holder.tvNombreSolitario.setText(solitario.getLugar());
        holder.tvCantidadPerfiles.setText("Cantidad de perfiles: " + solitario.getNumPersonas());
    }

    @Override
    public int getItemCount() {
        return solitarioList.size();
    }

    public static class SolitarioViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreSolitario, tvCantidadPerfiles;

        public SolitarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreSolitario = itemView.findViewById(R.id.lugar_solitario);
            tvCantidadPerfiles = itemView.findViewById(R.id.numPerfiles);
        }
    }
    // En el adaptador
    public void updateData(List<Solitario> nuevosSolitarios) {
        if (nuevosSolitarios != null && !nuevosSolitarios.isEmpty()) {
            this.solitarioList.clear();  // Limpiamos la lista actual
            this.solitarioList.addAll(nuevosSolitarios);  // Agregamos los nuevos solitarios
            notifyDataSetChanged();  // Notificamos al RecyclerView que los datos han cambiado
        } else {
            Log.e("RecyclerView", "La lista de solitarios está vacía.");
        }
    }
}
