package com.android.project.sigecah;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;


public class InicioFragment extends Fragment {

    private String nombre;
    private String hotel;

    private TextView elTiempo;
    private ImageView logo;
    private TextView temperatura;

    // Constructor estatico al que pasaremos los argumentos
    public static InicioFragment newInstance(Bundle arguments){
        InicioFragment f = new InicioFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public InicioFragment(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nombre = getArguments().getString("NOMBRE");
        hotel = getArguments().getString("NOMBREHOTEL");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        if(view != null) {
            final TextView mensajeBienvenida = (TextView) view.findViewById(R.id.textWelcome);
            elTiempo = (TextView) view.findViewById(R.id.textElTiempo);
            logo = (ImageView) view.findViewById(R.id.logoTiempo);
            temperatura = (TextView) view.findViewById(R.id.temperatura);
            mensajeBienvenida.setText(getResources().getString(R.string.hola) +" "+ nombre +
                    getResources().getString(R.string.disfrute_estancia) +" "+ hotel);

            MyLocationListener myLocationListener = new MyLocationListener(getActivity());
            Location location = myLocationListener.getLocation();

            if(location != null) {
                new TiempoAsyncTask(location).execute();
            }else {
                elTiempo.setVisibility(View.GONE);
                logo.setImageResource(R.drawable.eltiempo);
                ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(logo.getLayoutParams());
                marginParams.setMargins(0, 400, 0, 0);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                logo.setLayoutParams(layoutParams);
                temperatura.setText(getResources().getString(R.string.active_gps));
            }
        }
        return view;
    }

    private class TiempoAsyncTask extends AsyncTask<Void, Void, Void> {

        private Location location;
        private String ciudad;
        private String icon;
        private String grados;
        private Bitmap imagen;

        public TiempoAsyncTask(Location l) {
            this.location = l;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

                if(location != null) {

                    Double latitud = location.getLatitude();
                    Double longitud = location.getLongitude();

                    try {
                        String respStr = Utils.peticionGet("http://api.openweathermap.org/data/2.5/weather?lat=" + latitud + "&lon=" + longitud + "&units=metric&lang=sp");

                        JSONObject obj = new JSONObject(respStr);

                        ciudad = obj.getString("name");

                        JSONArray jsonArray = obj.getJSONArray("weather");
                        JSONObject jsonWeather = jsonArray.getJSONObject(0);
                        icon = jsonWeather.getString("icon");
                        grados = obj.getJSONObject("main").getString("temp");

                        InputStream inputStream = Utils.peticionGetImage("http://openweathermap.org/img/w/" + icon + ".png");
                        imagen = BitmapFactory.decodeStream(inputStream);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }else{
                    Log.e("Localizacion es", "nula");
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            elTiempo.setText(getResources().getString(R.string.tiempo) +" "+ ciudad);
            logo.setImageBitmap(imagen);
            String grad = String.valueOf(grados).split("\\.")[0];
            temperatura.setText(getResources().getString(R.string.temperatura) + " " + grad + " \u00B0C");

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}