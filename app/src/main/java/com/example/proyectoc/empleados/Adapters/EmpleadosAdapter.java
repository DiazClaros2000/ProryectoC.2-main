package com.example.proyectoc.empleados.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoc.R;
import com.example.proyectoc.empleados.modelo.Empleado;

import java.util.List;

public class EmpleadosAdapter extends RecyclerView.Adapter<EmpleadosAdapter.EmpleadoViewHolder> {
    private List<Empleado> empleados;
    private OnEmpleadoClickListener listener;

    public interface OnEmpleadoClickListener {
        void onEmpleadoClick(Empleado empleado);
    }

    public EmpleadosAdapter(List<Empleado> empleados, OnEmpleadoClickListener listener) {
        this.empleados = empleados;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmpleadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_empleado, parent, false);
        return new EmpleadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpleadoViewHolder holder, int position) {
        Empleado empleado = empleados.get(position);
        holder.bind(empleado);
    }

    @Override
    public int getItemCount() {
        return empleados.size();
    }

    class EmpleadoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre;
        private TextView tvServicio;
        private TextView tvZona;
        private TextView tvEstado;
        private CardView cardView;

        public EmpleadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvServicio = itemView.findViewById(R.id.tvServicio);
            tvZona = itemView.findViewById(R.id.tvZona);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onEmpleadoClick(empleados.get(position));
                    }
                }
            });
        }

        public void bind(Empleado empleado) {
            tvNombre.setText(empleado.getNombre());
            tvServicio.setText(empleado.getNombreServicio() != null ? empleado.getNombreServicio() : "Sin servicio");
            tvZona.setText(empleado.getZona());

            if (empleado.isDisponible()) {
                tvEstado.setText("Activo");
                tvEstado.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
            } else {
                tvEstado.setText("Inactivo");
                tvEstado.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            }
        }
    }
}
