package com.tony.conversosdedivisas;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    // 1. Declaramos las variables para los elementos de la pantalla
    private EditText etMonto;
    private Spinner spinnerDe, spinnerA;
    private Button btnConvertir;
    private TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // <--- Línea corregida
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Los "findViewById" y el resto del código que ya tienes van aquí abajo...
        etMonto = findViewById(R.id.etMonto);
        spinnerDe = findViewById(R.id.spinnerDe);
        spinnerA = findViewById(R.id.spinnerA);
        btnConvertir = findViewById(R.id.btnConvertir);
        tvResultado = findViewById(R.id.tvResultado);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.monedas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDe.setAdapter(adapter);
        spinnerA.setAdapter(adapter);

        btnConvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarConversion();
            }
        });
    }

    private void realizarConversion() {
        String montoStr = etMonto.getText().toString();
        if (montoStr.isEmpty()) return;

        String de = spinnerDe.getSelectedItem().toString();
        String a = spinnerA.getSelectedItem().toString();

        // La URL del servidor (Sustituye TU_API_KEY por tu código real)
        String url = "https://v6.exchangerate-api.com/v6/354c6b845ea8212ff65ce891/pair/" + de + "/" + a + "/" + montoStr;

        // Creamos la petición de datos
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Extraemos el resultado del JSON que nos envía el servidor
                        double resultado = response.getDouble("conversion_result");
                        tvResultado.setText(String.format("Resultado: %.2f %s", resultado, a));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Error de red", Toast.LENGTH_SHORT).show()
        );

        // Enviamos la petición
        Volley.newRequestQueue(this).add(request);
    }
}