package com.android.project.sigecah;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;


public class DetallesServicioActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_servicio);

        //Contenido del layout
        final TextView nombreEvento = (TextView) findViewById(R.id.nombreServicio);
        final ImageView logo = (ImageView) findViewById(R.id.logoServicio);
        final TextView nombreLugar = (TextView) findViewById(R.id.lugarServicio);
        final TextView fecha = (TextView) findViewById(R.id.fechaServicio);
        final TextView hora = (TextView) findViewById(R.id.horaServicio);
        final TextView precio = (TextView) findViewById(R.id.precioServicio);
        final TextView descripcion = (TextView) findViewById(R.id.descripcionServicio);

        //Recuperamos la informacion pasada por el Intent
        Bundle bundle = this.getIntent().getExtras();
        String objStr = bundle.getString("SERVICIO");

        try {
            JSONObject servicio = new JSONObject(objStr);
            if(servicio.length() != 0){
                nombreEvento.setText(servicio.getString("nombre"));
                String lugar = servicio.getString("nombreMapa");
                String imagenLugar = lugar.toLowerCase().replace(" ","");
                Integer imagen = getResources().getIdentifier("drawable/" + imagenLugar, null, getPackageName());
                logo.setImageResource(imagen);
                nombreLugar.setText(getResources().getString(R.string.realizara)+" "+lugar);
                fecha.setText(getResources().getString(R.string.fecha)+" "+ Utils.formatearFecha(servicio.getString("fecha")));
                hora.setText(getResources().getString(R.string.hora) + " " +getResources().getString(R.string.de)+" "+
                        servicio.getString("horaInicio")+" "+ getResources().getString(R.string.a)+" "+
                                servicio.getString("horaFin"));
                precio.setText(getResources().getString(R.string.precio_adicional)+" "+ servicio.getString("precioAdicional")+" "+getResources().getString(R.string.euros));
                descripcion.setText(getResources().getString(R.string.descripcion)+" "+ servicio.getString("descripcion"));
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
