package com.example.proyectoc.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectoc.R;
import com.example.proyectoc.model.Vehiculo;
import java.util.List;

public class VehiculoAdapter extends RecyclerView.Adapter<VehiculoAdapter.ViewHolder> {
    private List<Vehiculo> vehiculos;
    private OnEliminarClickListener eliminarClickListener;

    public interface OnEliminarClickListener {
        void onEliminarClick(Vehiculo vehiculo);
    }

    public VehiculoAdapter(List<Vehiculo> vehiculos, OnEliminarClickListener listener) {
        this.vehiculos = vehiculos;
        this.eliminarClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehiculo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehiculo v = vehiculos.get(position);
        holder.txtMarca.setText(v.getMarca());
        holder.txtModelo.setText(v.getModelo());
        holder.txtAnio.setText(String.valueOf(v.getAnio()));
        holder.txtAceite.setText(v.getTipo_aceite());
        holder.btnEliminar.setOnClickListener(view -> {
            if (eliminarClickListener != null) {
                eliminarClickListener.onEliminarClick(v);
            }
        });
    }

    @Override
    public int getItemCount() { return vehiculos.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMarca, txtModelo, txtAnio, txtAceite;
        Button btnEliminar;
        public ViewHolder(View itemView) {
            super(itemView);
            txtMarca = itemView.findViewById(R.id.txtMarca);
            txtModelo = itemView.findViewById(R.id.txtModelo);
            txtAnio = itemView.findViewById(R.id.txtAnio);
            txtAceite = itemView.findViewById(R.id.txtAceite);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}