package grupo11.ecohogar;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Vistas extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Bienvenido.OnFragmentInteractionListener,DatosVivienda.OnFragmentInteractionListener,
        DatosUsuario.OnFragmentInteractionListener,
        View.OnClickListener,ListaViviendas.OnFragmentInteractionListener,
        Notificacion.OnFragmentInteractionListener,DatosEnergia.OnFragmentInteractionListener{
    ImageButton casa;
    ImageButton foro;
    ImageButton energia;
    ImageButton agua;
    ImageButton notificacion;

    String IP = "http://h2ogar.esy.es/PhpAndroid/";
    String GET_BY_ID= IP + "obtener_vivienda_por_id.php";
    ObtenerWebService hiloconexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vistas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager1 = getFragmentManager();
        FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
        ListaViviendas fragment1 = new ListaViviendas();
        transaction1.replace(R.id.layout_vistas, fragment1);
        transaction1.commit();

        casa=(ImageButton)findViewById(R.id.imgbtnCasa);
        casa.setOnClickListener(this);
        foro =(ImageButton)findViewById(R.id.imgbtnForo);
        foro.setOnClickListener(this);
        energia=(ImageButton)findViewById(R.id.imgbtnEnergia);
        energia.setOnClickListener(this);
        agua=(ImageButton)findViewById(R.id.imgbtnAgua);
        agua.setOnClickListener(this);
        notificacion=(ImageButton)findViewById(R.id.imgbtnNotificacion);
        notificacion.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vistas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itemDatosdelavivienda) {
            FragmentManager fragmentManager1 = getFragmentManager();
            FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
            DatosVivienda datosVivienda = new DatosVivienda();
            transaction1.replace(R.id.layout_vistas, datosVivienda);
            transaction1.commit();
           // hiloconexion = new ObtenerWebService();
            //String cadenallamada = GET_BY_ID + "?id_vivienda=";//+ id_vivienda.getText().toString();
            //hiloconexion.execute(cadenallamada);
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.itemdatosdeUsuario) {
            FragmentManager fragmentManager1 = getFragmentManager();
            FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
            DatosUsuario datosUsuario = new DatosUsuario();
            transaction1.replace(R.id.layout_vistas, datosUsuario);
            transaction1.commit();
            // Handle the camera action
        } else if (id == R.id.itemVincularbluetooth) {
            Intent intent = new Intent(Vistas.this,DeviceList.class);
            startActivity(intent);

        } else if (id == R.id.itemHistorialdeuso) {
        } else if (id == R.id.itemCondicionesypoliticas) {
        } else if (id == R.id.itemReportarproblema) {
        } else if (id == R.id.itemCerrarsesion) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onFragmentInteraction(Uri Uri)
    {
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.imgbtnCasa:
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                ListaViviendas listaViviendas = new ListaViviendas();
                transaction1.replace(R.id.layout_vistas, listaViviendas);
                transaction1.commit();
                break;
            case R.id.imgbtnForo:
                Intent intent = new Intent(Vistas.this,MapsActivity.class);
                startActivity(intent);
            case R.id.imgbtnNotificacion:
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction transaction2 = fragmentManager2.beginTransaction();
                Notificacion notifica= new Notificacion();
                transaction2.replace(R.id.layout_vistas,notifica);
                transaction2.commit();
                break;
            case R.id.imgbtnEnergia:
                FragmentManager fragmentManager3 = getFragmentManager();
                FragmentTransaction transaction3 = fragmentManager3.beginTransaction();
                DatosEnergia datosEnergia= new DatosEnergia();
                transaction3.replace(R.id.layout_vistas,datosEnergia);
                transaction3.commit();
                break;

        }

    }
    public class ObtenerWebService extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String sw = "0";


            try {
                url = new URL(cadena);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                //connection.setHeader("content-type", "application/json");

                int respuesta = connection.getResponseCode();
                StringBuilder result = new StringBuilder();

                if (respuesta == HttpURLConnection.HTTP_OK){


                    InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                    // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                    // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                    // StringBuilder.

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);        // Paso toda la entrada al StringBuilder
                    }

                    //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                    JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                    //Accedemos al vector de resultados

                    String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                    if (resultJSON=="1") {      // hay un cliente que mostrar
                        if (params[1] .equals( respuestaJSON.getJSONObject("vivienda").getString("id_vivienda"))) {
                            sw = respuestaJSON.getJSONObject("vivienda").getString("nombres");
                        }

                    }
                    else if (resultJSON=="2"){

                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return sw;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("0"))
            {
                Toast toast= Toast.makeText(getApplicationContext(),"El usuario o la contraseña son incorrectas",Toast.LENGTH_LONG);
                toast.show();
            }
            else
            {
                Intent intent = new Intent(Vistas.this, Vistas.class);
                Toast toast= Toast.makeText(getApplicationContext(),"Bienvenido "+s,Toast.LENGTH_LONG);
                toast.show();
                startActivity(intent);
                finish();
            }
            //super.onPostExecute(s);
        }
        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }
    }

}