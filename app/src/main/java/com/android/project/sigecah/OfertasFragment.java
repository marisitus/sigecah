package com.android.project.sigecah;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class OfertasFragment extends Fragment {

    private JSONArray itemsJSON;
    private Integer idHotel;

    // Constructor estatico al que pasaremos los argumentos
    public static OfertasFragment newInstance(Bundle arguments){
        OfertasFragment f = new OfertasFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public OfertasFragment(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idHotel = getArguments().getInt("IDHOTEL");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new OfertasAsyncTask(this).execute();

    }

    private class OfertasAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

        private Fragment fragment;
        private ProgressDialog dialog;


        public OfertasAsyncTask(Fragment f){
            this.fragment = f;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(fragment.getActivity());
            dialog.setMessage("Cargando...");
            dialog.setIndeterminate(false);
            dialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            ArrayList<String> items = new ArrayList<>();

            try {
                String respStr = Utils.peticionGet("http://150.214.188.71:8080/HotelesAplication2/webresources/com.tfg.hotel/ofertas/" + idHotel);

                itemsJSON = new JSONArray(respStr);

                for (int i = 0; i < itemsJSON.length(); i++) {
                    JSONObject obj = itemsJSON.getJSONObject(i);

                    String nombre = obj.getString("nombre");


                    items.add(i, nombre);

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            ListView myListView = (ListView) fragment.getActivity().findViewById(R.id.myListView);
            ArrayAdapter<String> objAdapter = new ArrayAdapter<>(fragment.getActivity(),
                    R.layout.texto_listas,
                    result);
            myListView.setAdapter(objAdapter);
            if(this.dialog.isShowing()){
                this.dialog.dismiss();
            }

            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    JSONObject obj = null;
                    try {
                        obj= itemsJSON.getJSONObject(position);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(obj != null) {
                        Intent intent = new Intent(fragment.getActivity(), DetallesOfertaActivity.class);
                        Bundle b = new Bundle();
                        b.putString("OFERTA", obj.toString());
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                }
            });

        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

}