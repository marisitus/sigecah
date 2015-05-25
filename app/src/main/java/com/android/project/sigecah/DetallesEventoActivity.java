package com.android.project.sigecah;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;


public class DetallesEventoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_evento);

        //Contenido del layout
        final TextView nombreEvento = (TextView) findViewById(R.id.nombreEvento);
        final ImageView logo = (ImageView) findViewById(R.id.logoEvento);
        final TextView nombreLugar = (TextView) findViewById(R.id.lugarEvento);
        final TextView tipo = (TextView) findViewById(R.id.tipoEvento);
        final TextView fecha = (TextView) findViewById(R.id.fechaEvento);
        final TextView hora = (TextView) findViewById(R.id.horaEvento);
        final TextView descripcion = (TextView) findViewById(R.id.descripcionEvento);

        //Recuperamos la informacion pasada por el Intent
        Bundle bundle = this.getIntent().getExtras();
        String objStr = bundle.getString("EVENTO");

        try {
            JSONObject evento = new JSONObject(objStr);
            if(evento.length() != 0){
                nombreEvento.setText(evento.getString("nombre"));
                String lugar = evento.getString("nombreMapa");
                String imagenLugar = lugar.toLowerCase();
                Integer imagen = getResources().getIdentifier("drawable/" + imagenLugar, null, getPackageName());
                logo.setImageResource(imagen);
                nombreLugar.setText(getResources().getString(R.string.realizara) + " " + lugar);
                tipo.setText(getResources().getString(R.string.tipo)+" "+ evento.getString("tipoEvento"));
                fecha.setText(getResources().getString(R.string.fecha)+" "+Utils.formatearFecha(evento.getString("fecha")));
                String hora1 = evento.getString("horaInicio");
                String hora2 = evento.getString("horaFin");
                hora.setText(getResources().getString(R.string.hora)+" "+getResources().getString(R.string.de)+" "+hora1+" "+
                        getResources().getString(R.string.a)+" "+ hora2);
                descripcion.setText(getResources().getString(R.string.descripcion)+" "+ evento.getString("descripcion"));
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