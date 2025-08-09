package com.example.proyectoc.empleados.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoc.empleados.Adapters.ProductoAdapter;
import com.example.proyectoc.empleados.modelo.Producto;
import com.example.proyectoc.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GestionPreciosProductosActivity extends AppCompatActivity {

    private RecyclerView recyclerServicios;
    private ProductoAdapter adapter;
    private List<Producto> listaProductos;
    private FloatingActionButton btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_precios_productos);

        recyclerServicios = findViewById(R.id.recyclerServicios);
        btnAgregar = findViewById(R.id.btnAgregarProducto);

        listaProductos = new ArrayList<>();
        listaProductos.add(new Producto("1", "Lavado General", 100, true));
        listaProductos.add(new Producto("2", "Lavado Completo", 150, true));
        listaProductos.add(new Producto("3", "Lavado Motor", 400, false));

        adapter = new ProductoAdapter(this, listaProductos, new ProductoAdapter.OnProductoChangeListener() {
            @Override
            public void onPrecioChanged(Producto producto, double nuevoPrecio) {
                producto.setPrecio(nuevoPrecio);
                Toast.makeText(GestionPreciosProductosActivity.this, "Precio actualizado: " + producto.getNombre(), Toast.LENGTH_SHORT).show();
                // TODO: Aquí llamarías a tu API para actualizar en BD
            }

            @Override
            public void onEstadoChanged(Producto producto, boolean activo) {
                producto.setActivo(activo);
                Toast.makeText(GestionPreciosProductosActivity.this, (activo ? "Activado" : "Desactivado") + ": " + producto.getNombre(), Toast.LENGTH_SHORT).show();
                // TODO: Aquí llamarías a tu API para actualizar en BD
            }
        });

        recyclerServicios.setLayoutManager(new LinearLayoutManager(this));
        recyclerServicios.setAdapter(adapter);

        btnAgregar.setOnClickListener(v -> mostrarDialogoAgregarProducto());
    }

    private void mostrarDialogoAgregarProducto() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_agregar_producto, null);

        final EditText etNombre = dialogView.findViewById(R.id.etNombreNuevoProducto);
        final EditText etPrecio = dialogView.findViewById(R.id.etPrecioNuevoProducto);
        final Switch switchActivo = dialogView.findViewById(R.id.switchActivoNuevo);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar producto")
                .setView(dialogView)
                .setNegativeButton("Cancelar", (d, which) -> d.dismiss())
                .setPositiveButton("Agregar", null); // lo manejamos luego para validar

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button botonAgregar = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            botonAgregar.setOnClickListener(view -> {
                String nombre = etNombre.getText().toString().trim();
                String precioStr = etPrecio.getText().toString().trim();
                boolean activo = switchActivo.isChecked();

                if (nombre.isEmpty()) {
                    etNombre.setError("Ingresa el nombre");
                    etNombre.requestFocus();
                    return;
                }

                double precio;
                try {
                    precio = Double.parseDouble(precioStr);
                    if (precio < 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    etPrecio.setError("Precio inválido");
                    etPrecio.requestFocus();
                    return;
                }

                // Generar id único (puedes cambiar por lógica de tu BD)
                String id = UUID.randomUUID().toString();
                Producto nuevo = new Producto(id, nombre, precio, activo);

                // Agregar a la lista y notificar al adaptador
                listaProductos.add(nuevo);
                adapter.notifyItemInserted(listaProductos.size() - 1);
                recyclerServicios.scrollToPosition(listaProductos.size() - 1);

                Toast.makeText(GestionPreciosProductosActivity.this, "Producto agregado", Toast.LENGTH_SHORT).show();

                // TODO: aquí puedes llamar a tu API para persistir el nuevo producto en el servidor/BBDD

                dialog.dismiss();
            });
        });

        dialog.show();
    }
}
