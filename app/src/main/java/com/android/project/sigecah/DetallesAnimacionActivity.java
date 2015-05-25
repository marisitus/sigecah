package com.android.project.sigecah;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;


public class DetallesAnimacionActivity extends ActionBarActivity {

    private Integer idCliente;
    private Integer idAnimacion;

    private TextView nPlazas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_animacion);

        //Boton que permite volver atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Contenido del layout
        final TextView nombreAnimacion = (TextView) findViewById(R.id.nombreAnimacion);
        final ImageView logo = (ImageView) findViewById(R.id.logoAnimacion);
        final TextView nombreLugar = (TextView) findViewById(R.id.lugarAnimacion);
        final TextView tipo = (TextView) findViewById(R.id.tipoAnimacion);
        final TextView descripcion = (TextView) findViewById(R.id.descripcionAnimacion);
        final TextView inscripcionText = (TextView) findViewById(R.id.inscripciontext);
        final NumberPicker numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        nPlazas = (TextView) findViewById(R.id.plazas);
        inscripcionText.setVisibility(View.GONE);
        numberPicker.setVisibility(View.GONE);
        numberPicker.setMaxValue(5);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(true);

        //Recuperamos la informacion pasada por el Intent
        Bundle bundle = this.getIntent().getExtras();
        idCliente = bundle.getInt("IDCLIENTE");
        String animacionStr = bundle.getString("ANIMACION");
        Integer aforo = null;
        try {
            JSONObject animacion = new JSONObject(animacionStr);
            nombreAnimacion.setText(animacion.getString("nombre"));
            String lugar = animacion.getString("nombreMapa");
            String imagenLugar = lugar.toLowerCase();
            Integer imagen = getResources().getIdentifier("drawable/" + imagenLugar, null, getPackageName());
            logo.setImageResource(imagen);
            nombreLugar.setText(getResources().getString(R.string.realizara) + " " + lugar);
            tipo.setText(getResources().getString(R.string.tipo)+" "+ animacion.getString("tipoAnimacion"));
            descripcion.setText(getResources().getString(R.string.descripcion) + " " + animacion.getString("descripcion"));
            aforo = animacion.getInt("aforo");
            nPlazas.setText(getResources().getString(R.string.plazas_disponibles)+" "+ aforo);
            idAnimacion = animacion.getInt("idAnimacion");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ComprobarInscripcionAsyncTask(this, inscripcionText, numberPicker, aforo).execute();
    }

    private class ComprobarInscripcionAsyncTask extends AsyncTask<Void, Void, Integer> {

        private  DetallesAnimacionActivity activity;
        private TextView inscripcionText;
        private NumberPicker numberPicker;
        private Integer aforo;

        private Button inscribirse;

        public ComprobarInscripcionAsyncTask(Integer newAforo){
            this.aforo = newAforo;
        }

        public ComprobarInscripcionAsyncTask(DetallesAnimacionActivity a, TextView i, NumberPicker np, Integer af) {
            this.activity = a;
            this.inscripcionText = i;
            this.numberPicker = np;
            this.aforo = af;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inscribirse = (Button) findViewById(R.id.button);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            Integer comprobacion;
            String respStr = Utils.peticionGet("http://150.214.188.71:8080/HotelesAplication2/webresources/com.tfg.asistencia/getInscripcion/" + idAnimacion + "/" + idCliente);
            comprobacion = Integer.parseInt(respStr);
            return comprobacion;
        }

        @Override
        protected void onPostExecute(Integer comprobacion) {
            super.onPostExecute(comprobacion);

            if(comprobacion == 1 && aforo == 0){
                animacionCompletaInscrita(inscribirse);
            }else if(comprobacion == 1) {
                animacionInscrita(inscribirse);
            }else if(aforo == 0){
                animacionCompleta(inscribirse);
            }else if(comprobacion == 0) {
                inscripcionText.setVisibility(View.VISIBLE);
                numberPicker.setVisibility(View.VISIBLE);
                inscribirse.setText(getResources().getString(R.string.inscribir));
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        final Integer nPersonas = newVal;
                        inscribirse.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(aforo - nPersonas < 0){
                                    Toast toast1 =
                                            Toast.makeText(getApplicationContext(),
                                                    getResources().getString(R.string.excedeNumero1) +" "+
                                                            aforo +" "+ getResources().getString(R.string.excedeNumero2), Toast.LENGTH_LONG);
                                    toast1.show();
                                }else {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                                    dialog.setTitle(getResources().getString(R.string.title_dialog));
                                    dialog.setMessage(getResources().getString(R.string.body_dialog));
                                    dialog.setCancelable(false);
                                    dialog.setPositiveButton(getResources().getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new InscripcionAsyncTask(nPersonas).execute();
                                        }
                                    });
                                    dialog.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    final TextView t_view = new TextView(getApplicationContext());
                                    t_view.setTextSize(14);
                                    dialog.setIcon(R.mipmap.alert_icon);
                                    dialog.show();
                                }
                            }
                        });
                    }
                });

            }

        }
    }

    private class InscripcionAsyncTask extends AsyncTask<Void, Void, Boolean>{
        private Integer nPersonas;
        private String newAforo = null;

        public InscripcionAsyncTask(Integer np) {
            this.nPersonas = np;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean result = true;

            try {
                //Construimos el objeto JSON
                JSONObject asistencia = new JSONObject();
                JSONObject animacion = new JSONObject();
                JSONObject asistenciaPK = new JSONObject();
                JSONObject cliente = new JSONObject();
                String fecha = "";

                asistencia.put("animacion", animacion);
                asistenciaPK.put("animacionidAnimacion", idAnimacion);
                asistenciaPK.put("clienteidCliente", idCliente);
                asistencia.put("asistenciaPK", asistenciaPK);
                asistencia.put("cantidad", nPersonas);
                asistencia.put("cliente", cliente);
                asistencia.put("fecha", fecha);

                Utils.peticionPost("http://150.214.188.71:8080/HotelesAplication2/webresources/com.tfg.asistencia", asistencia.toString());
                newAforo = Utils.peticionGet("http://150.214.188.71:8080/HotelesAplication2/webresources/com.tfg.animacion/getAforo/" + idAnimacion);

            } catch (JSONException e) {
                e.printStackTrace();
                result = false;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            // Si se ha realizado correctamente la inscripcion
            if(result) {
                Toast toast1 =
                                Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.body_confirmacion), Toast.LENGTH_LONG);
                toast1.show();
                new ComprobarInscripcionAsyncTask(Integer.valueOf(newAforo)).execute();
                nPlazas.setText(getResources().getString(R.string.plazas_disponibles) + " " + newAforo);
            // Si no se ha realizado correctamente la inscripcion
            }else{
                Toast toast2 =
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.body_no_confirmacion), Toast.LENGTH_LONG);
                toast2.show();
            }
        }
    }

    public void animacionCompletaInscrita(Button inscribirse){
        inscribirse.setText(getResources().getString(R.string.animacion_completa)+". "+
                getResources().getString(R.string.inscrito));
        inscribirse.setEnabled(false);
    }

    public void animacionInscrita (Button inscribirse){
        inscribirse.setText(getResources().getString(R.string.inscrito));
        inscribirse.setEnabled(false);
    }

    public void animacionCompleta (Button inscribirse){
        inscribirse.setText(getResources().getString(R.string.animacion_completa));
        inscribirse.setEnabled(false);
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