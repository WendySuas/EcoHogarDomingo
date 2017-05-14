package grupo11.ecohogar;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;

public class prueba extends AppCompatActivity {


    ListView lista;
    ArrayList<Vivienda> AL;
    ItemAdapter adapter;
    ObtenerWebService hiloconexion;

    String IP = "http://h2ogar.esy.es/";
    String GET= IP +"obtener_nombre_de_zona_por_id_cliente.php?id=2000001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);
        hiloconexion = new ObtenerWebService();
        hiloconexion.execute(GET);
        lista = (ListView)findViewById(R.id.LV_prueba);
        lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        AL = new ArrayList<Vivienda>();
        AL= hiloconexion.getDev();
        adapter = new ItemAdapter(this,AL);
    }
    public class ObtenerWebService extends AsyncTask<String,Void,ArrayList<String>>
    {
        ArrayList<Vivienda> dev = new ArrayList<Vivienda>();
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String cadena = params[0];
            URL url = null;
            ArrayList<String> devuelve = new ArrayList<>();
            try {
                url = new URL(cadena);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                int respuesta = connection.getResponseCode();
                StringBuilder result = new StringBuilder();
                if (respuesta == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    JSONObject respuestaJSON = new JSONObject(result.toString());
                    String resultJSON = respuestaJSON.getString("estado");
                    if (resultJSON.equals("1")){
                        JSONArray productosJSON = respuestaJSON.getJSONArray("dat");
                        for(int i=0;i<productosJSON.length();i++){
                            devuelve.add(productosJSON.getJSONObject(i).getString("nombre_zona"));
                        }
                    }
                    else if (resultJSON.equals("2")){
                        devuelve.add("0");
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return devuelve;
        }

        @Override
        protected void onCancelled(ArrayList<String> s) {
            super.onCancelled(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute (ArrayList<String> s) {
            if(!s.get(0).equals("0")) {
                llenar(s);
            }
            else {
                Toast toast= Toast.makeText(getApplicationContext(),"nada",Toast.LENGTH_SHORT);
                toast.show();
            }
            //super.onPostExecute(s);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        public void llenar(ArrayList<String>a)
        {
            for (int i =0;i<a.size();i++)
            {
                Vivienda vivienda= new Vivienda(a.get(i));
                dev.add(vivienda);
            }
            lista.setAdapter(adapter);
        }
        public ArrayList<Vivienda> getDev() {
            return dev;

        }
    }

    public void crear (String id)
    {
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput("meminterna.txt", Context.MODE_PRIVATE));
            fout.write(id);
            fout.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero en la memoria interna");
        }
    }
    public String leer()
    {
        String sw ="0";
        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("meminterna.txt")));

            String texto = fin.readLine();
            sw=texto;
            fin.close();
        }
        catch (Exception ex)
        {
            crear("");
        }
        return sw;
    }
}
