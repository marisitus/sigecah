package com.android.project.sigecah;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {

    private final Context context;

    boolean gpsActivado = false;
    boolean redActivada = false;

    protected LocationManager locationManager;

    Location location;

    public MyLocationListener (Context context){
        this.context = context;
    }

    public Location getLocation(){
        try{
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            gpsActivado = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            redActivada = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gpsActivado && !redActivada) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle(context.getResources().getString((R.string.importante)));
                alertDialog.setMessage(context.getResources().getString(R.string.cuerpo_importante));
                alertDialog.setPositiveButton(context.getResources().getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setIcon(R.mipmap.ic_gps_disabled);
                alertDialog.show();

            } else {
                if(redActivada){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                if(gpsActivado){
                    if(location == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                        if(locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Este metodo se ejecuta cada vez que se recibe un cambio de ubicacion
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        /*
        * Este metodo se ejecuta cada vez que se detecta un cambio en el
		* status del proveedor de localizacion (GPS)
		* Los diferentes Status son:
		* OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
		* TEMPORARILY_UNAVAILABLE -> Temporalmente no disponible pero se espera que
		* este disponible en breve
		* AVAILABLE -> Disponible
        * */
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Este metodo se ejecuta cuando el GPS esta activado
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Este metodo se ejecuta cuando el GPS esta desactivado
    }
}
