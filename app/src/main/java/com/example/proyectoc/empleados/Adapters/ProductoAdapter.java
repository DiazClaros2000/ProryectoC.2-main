package com.example.proyectoc.empleados.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoc.R;
import com.example.proyectoc.empleados.modelo.Producto;



import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private Context context;
    private List<Producto> listaProductos;
    private OnProductoChangeListener listener;

    public interface OnProductoChangeListener {
        void onPrecioChanged(Producto producto, double nuevoPrecio);
        void onEstadoChanged(Producto producto, boolean activo);
    }

    public ProductoAdapter(Context context, List<Producto> listaProductos, OnProductoChangeListener listener) {
        this.context = context;
        this.listaProductos = listaProductos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.tvNombre.setText(producto.getNombre());
        holder.etPrecio.setText(String.valueOf(producto.getPrecio()));
        holder.switchActivo.setChecked(producto.isActivo());

        holder.etPrecio.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                double nuevoPrecio;
                try {
                    nuevoPrecio = Double.parseDouble(holder.etPrecio.getText().toString());
                } catch (NumberFormatException e) {
                    nuevoPrecio = producto.getPrecio();
                }
                listener.onPrecioChanged(producto, nuevoPrecio);
            }
        });

        holder.switchActivo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onEstadoChanged(producto, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        EditText etPrecio;
        Switch switchActivo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            etPrecio = itemView.findViewById(R.id.etPrecioProducto);
            switchActivo = itemView.findViewById(R.id.switchActivo);
        }
    }
}
