package com.android.project.sigecah;

import android.content.res.Configuration;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    private String[] opcionesMenu;
    private DrawerLayout drawerLayout;
    private ListView navList;
    private CharSequence tituloSeccion;

    private ActionBarDrawerToggle drawerToggle;

    private Integer idCliente;
    private String nombreUsuario;
    private Integer idHotel;
    private String nombreHotel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //NUEVO
        tituloSeccion = getTitle();

        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.navList = (ListView) findViewById(R.id.left_drawer);

        //Recuperamos la informacion pasada en el intent
        Bundle bundle = this.getIntent().getExtras();
        idCliente = bundle.getInt("IDCLIENTE");
        nombreUsuario = bundle.getString("NOMBRE");
        idHotel = bundle.getInt("IDHOTEL");
        nombreHotel = bundle.getString("NOMBREHOTEL");

        opcionesMenu = getResources().getStringArray(R.array.secciones_array);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.texto_listas, opcionesMenu);
        navList.setAdapter(adapter);

        navList.setOnItemClickListener(new DrawerItemClickListener());

        //Si es nulo (al principio lo es) mostrar la opcion 0 que es el inicio.
        if(savedInstanceState == null){
            selectItem(0);
        }

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(tituloSeccion);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(MainActivity.this.getString(R.string.selecciona_seccion));
                supportInvalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        //Mostrar icono del menu arriba a la izquierda para al pulsar mostrar el menu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Cuando se pulsa alguna opción
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    // Seleccionar el fragment que seleccionemos
    private void selectItem(int position) {

        Fragment fragment = null;

        Bundle arguments = new Bundle();
        arguments.putInt("IDHOTEL", idHotel);
        arguments.putInt("IDCLIENTE", idCliente);

        switch (position){
            case 0:
                Bundle arguments0 = new Bundle();
                arguments0.putString("NOMBRE", nombreUsuario);
                arguments0.putString("NOMBREHOTEL", nombreHotel);
                new InicioFragment();
                fragment = InicioFragment.newInstance(arguments0);
                break;
            case 1:
                new AnimacionesFragment();
                fragment = AnimacionesFragment.newInstance(arguments);
                break;
            case 2:
                new EventosFragment();
                fragment = EventosFragment.newInstance(arguments);
                break;
            case 3:
                new ServiciosFragment();
                fragment = ServiciosFragment.newInstance(arguments);
                break;
            case 4:
                new OfertasFragment();
                fragment = OfertasFragment.newInstance(arguments);
                break;
            case 5:
                new SitiosFragment();
                fragment = SitiosFragment.newInstance(arguments);
                break;
        }

        if(fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                    .commit();
            navList.setItemChecked(position, true);

            // Cambiamos el titulo según lo que pulsemos
            tituloSeccion = opcionesMenu[position];
            getSupportActionBar().setTitle(tituloSeccion);

        }else{

            MainActivity.this.finish();
        }

        drawerLayout.closeDrawer(navList);

    }

    @Override
    protected void onPostCreate (Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        drawerLayout.isDrawerOpen(navList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

}