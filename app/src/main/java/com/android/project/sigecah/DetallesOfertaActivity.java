package com.android.project.sigecah;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;


public class DetallesOfertaActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_oferta);

        //Contenido del layout
        final TextView nombreOferta = (TextView) findViewById(R.id.nombreOferta);
        final TextView descripcion = (TextView) findViewById(R.id.descripcionOferta);
        final TextView fechaInicio = (TextView) findViewById(R.id.fecha1Oferta);
        final TextView fechaFin = (TextView) findViewById(R.id.fecha2Oferta);
        final TextView precio = (TextView) findViewById(R.id.precioOferta);
        final TextView tiendaOferta = (TextView) findViewById(R.id.tiendaOferta);

        //Recuperamos la informacion pasada por el Intent
        Bundle bundle = this.getIntent().getExtras();
        String objStr = bundle.getString("OFERTA");

        try {
            JSONObject oferta = new JSONObject(objStr);
            if(oferta.length() != 0){
                nombreOferta.setText(oferta.getString("nombre"));
                fechaInicio.setText(getResources().getString(R.string.fecha_inicio)+" "+ Utils.formatearFecha(oferta.getString("fechaInicio")));
                fechaFin.setText(getResources().getString(R.string.fecha_fin)+" "+ Utils.formatearFecha(oferta.getString("fechaFin")));
                precio.setText(getResources().getString(R.string.precio)+" "+ oferta.getString("precio")+" "+ getResources().getString(R.string.euros));
                descripcion.setText(getResources().getString(R.string.descripcion) +" "+ oferta.getString("descripcion"));
                JSONObject tienda = oferta.getJSONObject("tiendaidTienda");
                tiendaOferta.setText(getResources().getString(R.string.tienda) +" "+ tienda.getString("nombre"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Boton que permite volver atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalles_animacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
