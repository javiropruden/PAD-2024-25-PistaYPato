package es.ucm.fdi.pistaypato;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class ReservaActivity extends Fragment {
    private int pistaId;
    private String horaInicio;
    private String horaFin;
    private boolean ocupado;
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.activity_resultado, container, false);


        return view;
    }

    //meter getters y setters

    /*codigo util
    * List<Reserva> reservas = new ArrayList<>();
// Ejemplo de reservas ocupadas
reservas.add(new Reserva(1, "09:00", "10:00", true));
reservas.add(new Reserva(1, "11:00", "12:00", true));
// Ejemplo de reservas libres
reservas.add(new Reserva(1, "10:00", "11:00", false));
    *
    *
    * public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder> {

    private List<Reserva> reservas;

    public HorarioAdapter(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    @Override
    public HorarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horario, parent, false);
        return new HorarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorarioViewHolder holder, int position) {
        Reserva reserva = reservas.get(position);
        holder.horaTextView.setText(reserva.getHoraInicio() + " - " + reserva.getHoraFin());
        holder.itemView.setBackgroundColor(reserva.isOcupado() ? Color.RED : Color.GREEN);
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    public class HorarioViewHolder extends RecyclerView.ViewHolder {
        TextView horaTextView;

        public HorarioViewHolder(View itemView) {
            super(itemView);
            horaTextView = itemView.findViewById(R.id.horaTextView);
        }
    }
}

*
*
*
* RecyclerView recyclerView = findViewById(R.id.recyclerView);
HorarioAdapter adapter = new HorarioAdapter(reservas);
recyclerView.setLayoutManager(new LinearLayoutManager(this));
recyclerView.setAdapter(adapter);

*
*
*
* public void actualizarReservas(List<Reserva> nuevasReservas) {
    reservas.clear();
    reservas.addAll(nuevasReservas);
    adapter.notifyDataSetChanged();
}

*
* @Override
public void onBindViewHolder(HorarioViewHolder holder, int position) {
    Reserva reserva = reservas.get(position);
    holder.horaTextView.setText(reserva.getHoraInicio() + " - " + reserva.getHoraFin());
    holder.itemView.setBackgroundColor(reserva.isOcupado() ? Color.RED : Color.GREEN);

    // Listener para reservar
    holder.itemView.setOnClickListener(v -> {
        if (!reserva.isOcupado()) {
            // Lógica para hacer la reserva
            reserva.setOcupado(true);
            notifyDataSetChanged();
        }
    });
}

    * */
}
