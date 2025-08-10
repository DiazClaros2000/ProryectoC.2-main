package com.example.proyectoc.activities;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectoc.R;
import com.example.proyectoc.model.Cotizacion;
import java.util.List;

public class CotizacionesAdapter extends RecyclerView.Adapter<CotizacionesAdapter.CotizacionViewHolder> {

    private List<Cotizacion> cotizaciones;
    private OnCotizacionClickListener listener;

    public interface OnCotizacionClickListener {
        void onResponderClick(Cotizacion cotizacion);
        void onVerDetallesClick(Cotizacion cotizacion);
    }

    public CotizacionesAdapter(List<Cotizacion> cotizaciones, OnCotizacionClickListener listener) {
        this.cotizaciones = cotizaciones;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CotizacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cotizacion_admin, parent, false);
        return new CotizacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CotizacionViewHolder holder, int position) {
        Cotizacion cotizacion = cotizaciones.get(position);

        holder.textViewCliente.setText(cotizacion.getNombreCompleto());
        holder.textViewEmail.setText(cotizacion.getEmail_usuario());
        holder.textViewTelefono.setText(cotizacion.getTelefono());
        holder.textViewServicio.setText(cotizacion.getNombre_servicio());
        holder.textViewVehiculo.setText(cotizacion.getInfoVehiculo());
        holder.textViewFecha.setText(cotizacion.getFecha_servicio() + " " + cotizacion.getHora_servicio());
        holder.textViewUbicacion.setText(cotizacion.getUbicacion().toUpperCase());
        holder.textViewEstado.setText(cotizacion.getEstado().toUpperCase());

        double precioSugerido = cotizacion.getPrecioSugerido();
        holder.textViewPrecioSugerido.setText("Precio sugerido: L. " + String.format("%.2f", precioSugerido));

        configurarEstado(holder, cotizacion);

        holder.buttonResponder.setOnClickListener(v -> {
            if (listener != null) {
                listener.onResponderClick(cotizacion);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onVerDetallesClick(cotizacion);
            }
        });
    }

    private void configurarEstado(CotizacionViewHolder holder, Cotizacion cotizacion) {
        switch (cotizacion.getEstado()) {
            case "pendiente":
                holder.textViewEstado.setTextColor(Color.parseColor("#FF9800"));
                holder.buttonResponder.setVisibility(View.VISIBLE);
                holder.textViewRespuesta.setVisibility(View.GONE);
                break;

            case "respondida":
                holder.textViewEstado.setTextColor(Color.parseColor("#4CAF50"));
                holder.buttonResponder.setVisibility(View.GONE);
                holder.textViewRespuesta.setVisibility(View.VISIBLE);

                String respuesta = "PRECIO OFRECIDO: L. " + String.format("%.2f", cotizacion.getPrecio_ofrecido());
                if (cotizacion.getNota_admin() != null && !cotizacion.getNota_admin().trim().isEmpty()) {
                    respuesta += "\nNOTA: " + cotizacion.getNota_admin();
                }
                holder.textViewRespuesta.setText(respuesta);
                break;

            default:
                holder.textViewEstado.setTextColor(Color.parseColor("#666666"));
                holder.buttonResponder.setVisibility(View.GONE);
                holder.textViewRespuesta.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return cotizaciones.size();
    }

    static class CotizacionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCliente, textViewEmail, textViewTelefono, textViewServicio, textViewVehiculo;
        TextView textViewFecha, textViewUbicacion, textViewEstado, textViewRespuesta;
        TextView textViewPrecioSugerido;
        Button buttonResponder;

        public CotizacionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCliente = itemView.findViewById(R.id.textViewCliente);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewTelefono = itemView.findViewById(R.id.textViewTelefono);
            textViewServicio = itemView.findViewById(R.id.textViewServicio);
            textViewVehiculo = itemView.findViewById(R.id.textViewVehiculo);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
            textViewUbicacion = itemView.findViewById(R.id.textViewUbicacion);
            textViewEstado = itemView.findViewById(R.id.textViewEstado);
            textViewRespuesta = itemView.findViewById(R.id.textViewRespuesta);
            textViewPrecioSugerido = itemView.findViewById(R.id.textViewPrecioSugerido);
            buttonResponder = itemView.findViewById(R.id.buttonResponder);
        }
    }
}