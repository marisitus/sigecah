package com.android.project.sigecah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONObject;


public class LoginActivity extends ActionBarActivity {

    String dni;
    String nhabitacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

        // Contenido del Layout
        final EditText dniText =         (EditText) findViewById(R.id.dni);
        final EditText nhabitacionText = (EditText) findViewById(R.id.nhabitacion);
        final Button botonAceptar =      (Button) findViewById(R.id.botonAceptar);
        final TextView mensajeText =     (TextView) findViewById(R.id.mensaje);

        // Metodo de pulsar el boton
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cogemos los valores de los campos dni y nhabitacion
                dni = dniText.getText().toString();
                nhabitacion = nhabitacionText.getText().toString();

                // Si el DNI o HABITACION esta en blanco
                if (dni.isEmpty() || nhabitacion.isEmpty()) {
                    mensajeText.setText(getResources().getString(R.string.rellene_campos));
                } else new ComprobacionAsyncTask(LoginActivity.this).execute();
            }
        });
    }

    private class ComprobacionAsyncTask extends AsyncTask<Void, Void, Integer> {



        private TextView mensajeText;

        private LoginActivity activity;
        private ProgressDialog dialog;

        public ComprobacionAsyncTask(LoginActivity a){
            this.activity = a;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialog = new ProgressDialog(activity);
            dialog.setMessage(getResources().getString(R.string.iniciando_sesion));
            dialog.setIndeterminate(false);
            dialog.show();
            // Mensaje para mostrar los errores
            mensajeText = (TextView) findViewById(R.id.mensaje);
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            Integer comprobacion;

            try{
                //Comprobamos si con su dni y nhabitacion el cliente esta en el hotel.
                String jsonComprobacion = Utils.peticionGet("http://150.214.188.71:8080/HotelesAplication2/webresources/com.tfg.cliente/buscar/" + dni + "/" + nhabitacion);
                comprobacion = Integer.parseInt(jsonComprobacion);

            }catch (Exception ex){
                ex.printStackTrace();
                comprobacion = null;
            }
            return comprobacion;
        }

        @Override
        protected  void onPostExecute (Integer comprobacion) {
            super.onPostExecute(comprobacion);
            if (comprobacion != null) {
                // Si devuelve 1, el cliente esta en el sistema
                if (comprobacion == 1) {
                    new GetDatosAsyncTask(dialog).execute();
                } else {
                    if (this.dialog.isShowing()) {
                        this.dialog.dismiss();
                    }
                    mensajeText.setText(getResources().getString(R.string.no_registrado));
                }
            } else {
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                    mensajeText.setText(getResources().getString(R.string.problema_servidor));
                }
            }
        }
    }

    private class GetDatosAsyncTask extends AsyncTask<Void, Void, Void>{
        private JSONObject jsonCliente;
        private JSONObject jsonHotel;

        private Integer idCliente = null;
        private String nombreCliente = null;
        private Integer idHotel = null;
        private String nombreHotel = null;

        private ProgressDialog dialog;

        public GetDatosAsyncTask(ProgressDialog d) {
            this.dialog = d;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                //Cogemos el cliente según su DNI
                String cliente = Utils.peticionGet("http://150.214.188.71:8080/HotelesAplication2/webresources/com.tfg.cliente/clienteDni/" + dni);

                //Cogemos el hotel con el DNI y numero de habitacion del cliente
                String hotel = Utils.peticionGet("http://150.214.188.71:8080/HotelesAplication2/webresources/com.tfg.cliente/getHotel/" + dni + "/" + nhabitacion);

                jsonCliente = new JSONObject(cliente);
                jsonHotel = new JSONObject(hotel);

                // Cogemos el nombre del cliente y el id del hotel
                idCliente = jsonCliente.getInt("idCliente");
                nombreCliente = jsonCliente.getString("nombre");
                idHotel = jsonHotel.getInt("idHotel");
                nombreHotel = jsonHotel.getString("nombre");

            }catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(this.dialog.isShowing()){
                this.dialog.dismiss();
            }

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            // Pasamos los atributos a la siguiente vista
            Bundle b = new Bundle();
            b.putString("NOMBRE", nombreCliente);
            b.putInt("IDHOTEL", idHotel);
            b.putString("NOMBREHOTEL", nombreHotel);
            b.putInt("IDCLIENTE", idCliente);
            intent.putExtras(b);

            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
