package com.example.proyectoc.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.proyectoc.R;
import com.example.proyectoc.model.Cotizacion;

public class ResponderCotizacionDialog extends Dialog {

    private Cotizacion cotizacion;
    private OnResponderListener listener;
    private EditText editTextPrecio, editTextNota;
    private TextView textViewInfoCotizacion, textViewPrecioSugerido;
    private Button buttonEnviar, buttonCancelar;

    public interface OnResponderListener {
        void onResponder(int idCotizacion, double precio, String nota);
    }

    public ResponderCotizacionDialog(@NonNull Context context, Cotizacion cotizacion, OnResponderListener listener) {
        super(context);
        this.cotizacion = cotizacion;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_responder_cotizacion);

        inicializarVistas();
        configurarInformacion();
        configurarListeners();
    }

    private void inicializarVistas() {
        textViewInfoCotizacion = findViewById(R.id.textViewInfoCotizacion);
        textViewPrecioSugerido = findViewById(R.id.textViewPrecioSugerido);
        editTextPrecio = findViewById(R.id.editTextPrecio);
        editTextNota = findViewById(R.id.editTextNota);
        buttonEnviar = findViewById(R.id.buttonEnviar);
        buttonCancelar = findViewById(R.id.buttonCancelar);
    }

    private void configurarInformacion() {
        String info = "Cliente: " + cotizacion.getNombreCompleto() + "\n" +
                "Email: " + cotizacion.getEmail_usuario() + "\n" +
                "Teléfono: " + cotizacion.getTelefono() + "\n" +
                "Servicio: " + cotizacion.getNombre_servicio() + "\n" +
                "Vehículo: " + cotizacion.getInfoVehiculo() + "\n" +
                "Ubicación: " + cotizacion.getUbicacion().toUpperCase() + "\n" +
                "Fecha: " + cotizacion.getFecha_servicio() + " " + cotizacion.getHora_servicio();

        textViewInfoCotizacion.setText(info);

        double precioSugerido = cotizacion.getPrecioSugerido();
        textViewPrecioSugerido.setText("Precio sugerido: L. " + String.format("%.2f", precioSugerido));

        editTextPrecio.setText(String.valueOf(precioSugerido));
    }

    private void configurarListeners() {
        buttonEnviar.setOnClickListener(v -> {
            String precioStr = editTextPrecio.getText().toString().trim();
            String nota = editTextNota.getText().toString().trim();

            if (precioStr.isEmpty()) {
                Toast.makeText(getContext(), "Ingrese un precio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nota.isEmpty()) {
                Toast.makeText(getContext(), "Ingrese una nota para el cliente", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double precio = Double.parseDouble(precioStr);
                if (precio <= 0) {
                    Toast.makeText(getContext(), "El precio debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (listener != null) {
                    listener.onResponder(cotizacion.getId_cotizacion(), precio, nota);
                }
                dismiss();

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Precio inválido", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCancelar.setOnClickListener(v -> dismiss());
    }
}