package es.ucm.fdi.pistaypato;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

public class ReservaActivity extends Fragment {
    private int pistaId;
    private String horaInicio;
    private String horaFin;
    private boolean ocupado;
    private View view;

    PPAplication app;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String fecha;
    private String pista;

    private TextView nombre;
    private TextView ubicación;
    private TextView fech_a;
    //private ImageButton volver;


    @SuppressLint("WrongViewCast")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.activity_resultado, container, false);

        ImageButton volver = getActivity().findViewById(R.id.volver);
        volver.setVisibility(View.VISIBLE);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = getActivity().findViewById(R.id.middle_section);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.middle_section, new BusquedaActivity());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        app = (PPAplication) requireActivity().getApplication();
        try {
            ponerdatos();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return view;
    }

    private void ponerdatos() throws JSONException {
        nombre = view.findViewById(R.id.nombre);
        ubicación = view.findViewById(R.id.direccion);
        fech_a = view.findViewById(R.id.fecha);
        String ubi = "";

        nombre.setText(pista);
        fech_a.setText(fecha);

        for (int i = 0; i < app.jsonArray.length(); i++) {
            JSONObject field = app.jsonArray.getJSONObject(i);

            String nombre = field.optString("title"); // Obtiene el nombre del centro
            if(nombre.equals(pista)){
                JSONObject address = field.optJSONObject("address");
                String locality = address.optString("locality", ""); // Obtiene los servicios
                String postal_code = address.optString("postal-code", ""); // Obtiene los servicios
                String street = address.optString("street-address", ""); // Obtiene los servicios
                ubi = street + ", " + locality + ", " + postal_code;
            }
        }

        ubicación.setText(ubi);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fecha = getArguments().getString(ARG_PARAM1);
            pista = getArguments().getString(ARG_PARAM2);
        }
    }
    public static ReservaActivity newInstance(String param1, String param2) {
        ReservaActivity fragment = new ReservaActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
