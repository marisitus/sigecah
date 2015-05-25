package com.android.project.sigecah;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;


public class DetallesSitioActivity extends ActionBarActivity {

    private LatLng posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_sitio);

        //Variables que vamos a utilizar
        String coordenadasJSON = null;
        String direccion = null;
        String nombre = null;

        //Contenido del layout
        final TextView nombreSitio = (TextView) findViewById(R.id.nombreSitio);
        final TextView descripcion = (TextView) findViewById(R.id.descripcionSitio);

        //Recuperamos la informacion pasada por el Intent
        Bundle bundle = this.getIntent().getExtras();
        String objStr = bundle.getString("SITIO");
        try {
            JSONObject sitioInteres = new JSONObject(objStr);
            if(sitioInteres.length() != 0){
                nombre = sitioInteres.getString("nombre");
                nombreSitio.setText(nombre);
                descripcion.setText(sitioInteres.getString("descripcion"));
                coordenadasJSON = sitioInteres.getString("coordenadas");
                direccion = sitioInteres.getString("direccion");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(coordenadasJSON != null){
            String[]coord = coordenadasJSON.split(",|\\(|\\)");
            posicion = new LatLng(Double.parseDouble(coord[1]), Double.parseDouble(coord[2]));
        }

        // Mostramos el mapa
        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicion, 15);
        map.animateCamera(update);

        // Mostramos el marcador de color rojo
        Marker marker = map.addMarker(new MarkerOptions()
                .position(posicion)
                .title(nombre)
                .snippet(direccion)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        marker.showInfoWindow();

        map.getUiSettings().setZoomControlsEnabled(true);

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