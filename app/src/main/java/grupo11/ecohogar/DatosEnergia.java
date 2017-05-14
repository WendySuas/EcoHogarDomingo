package grupo11.ecohogar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
/**
 * Created by Wen on 18/05/2017.
 */
public class DatosEnergia extends Fragment {
    private OnFragmentInteractionListener mListener;

    ListView lista;
    ArrayList<DatoEnergia> AL;
    ItemAdapter1 adapter;
    ObtenerWebService hiloconexion; //este hilo es para desplegar los sensores

    ObtenerWebService1 hiloconexion1; // este es para desplegar los datos estadisticos

    TextView DatosSensor; // el textView donde estan los datos

   String IP = "http://h2ogar.esy.es/PhpAndroid/";
    String GET= IP +"obtener_sensor_por_id_vivienda.php?id_vivienda=";

    Activity activity;
    String id;
    Spinner opciones;


    public DatosEnergia() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_luz, container, false);
        activity=getActivity();
        id=leer();// guardamos en una variable el id vivienda
        GET=GET+id;
        hiloconexion = new ObtenerWebService();
        hiloconexion.execute(GET);

        lista = (ListView)view.findViewById(R.id.lvListasensores);
        lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        AL = new ArrayList<DatoEnergia>();
        AL= hiloconexion.getDev();
        adapter = new ItemAdapter1(activity,AL);
         String x= leer();

        opciones=(Spinner)view.findViewById(R.id.sp01) ;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.opciones, android.R.layout.simple_spinner_item);
         opciones.setAdapter(adapter);

        DatosSensor=(TextView)view.findViewById(R.id.TV_datos_sensor);

        opciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //metodo para cuando se haga click en algun item del spinner

           public void onItemSelected(AdapterView<?> parent, View view,
                                      int pos, long id) {
            //     An spinnerItem was selected. You can retrieve the selected item using
                 parent.getItemAtPosition(pos);
                String selected = parent.getItemAtPosition(pos).toString(); //sacamos el id del sensor seleccionado
                switch (selected){
                    case "anuales":
                        hiloconexion1 = new ObtenerWebService1();// nuevo metodo para sacar los datos de la vivienda
                        String cadenallamada = IP+"obtener_datos_anuales_por_id_sensor.php?id_sensor="+leer3();// saCamos el id del del sensor guardado en leer3()
                        hiloconexion1.execute(cadenallamada);
                       break;
                    case "mensuales":
                        hiloconexion1 = new ObtenerWebService1();// nuevo metodo para sacar los datos de la vivienda
                        String cadenallamada2 = IP+"obtener_datos_mensuales_por_id_sensor.php?id_sensor="+leer3();// saCamos el id del del sensor guardado en leer3()
                        hiloconexion1.execute(cadenallamada2);
                       break;
                    case "diarias":
                        hiloconexion1 = new ObtenerWebService1();// nuevo metodo para sacar los datos de la vivienda
                        String cadenallamada3 = IP+"obtener_dato_por_id_sensor.php?id_sensor="+leer3();// saCamos el id del del sensor guardado en leer3()
                        hiloconexion1.execute(cadenallamada3);
                        break;
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback

           }

       });


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

               TextView v = (TextView) view.findViewById(R.id.TV_id_sensor);
                crear3(v.getText().toString());
              //  Toast toast= Toast.makeText(getActivity(),leer2(),Toast.LENGTH_LONG);
                //toast.show();
               hiloconexion1 = new ObtenerWebService1();// nuevo metodo para sacar los datos de la vivienda
               String cadenallamada = IP+"obtener_dato_por_id_sensor.php?id_sensor="+leer3();// saCamos el id del item(del sensor)
               hiloconexion1.execute(cadenallamada);
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class ObtenerWebService extends AsyncTask<String,Void,ArrayList<String>>
    {
        ArrayList<DatoEnergia> dev = new ArrayList<DatoEnergia>();
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
                            if(productosJSON.getJSONObject(i).getString("tipo").equals("corriente"))
                            {
                                devuelve.add(productosJSON.getJSONObject(i).getString("id_sensor"));
                            }
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
                DatoEnergia datoEnergia= new DatoEnergia(a.get(i));
                dev.add(datoEnergia);
            }
            lista.setAdapter(adapter);
        }
        public ArrayList<DatoEnergia> getDev() {
            return dev;

        }
    }
    public class ObtenerWebService1 extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String sw="";
            try {
                url = new URL(cadena);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                int respuesta = connection.getResponseCode();
                StringBuilder result = new StringBuilder();
                if (respuesta == HttpURLConnection.HTTP_OK){
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);                      }

                    JSONObject respuestaJSON = new JSONObject(result.toString());
                    String resultJSON = respuestaJSON.getString("estado");
                    if (resultJSON=="1") {
                        {
                            JSONArray productosJSON = respuestaJSON.getJSONArray("dat");
                            for(int i=0;i<productosJSON.length();i++){
                                sw=sw+"$"+productosJSON.getJSONObject(i).getString("valor")+"?"+productosJSON.getJSONObject(i).getString("fecha");
                            }
                        }
                    }
                    else if (resultJSON=="2"){
                        sw="0";

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

            }
            else
            {
                crear2(s);
                String y =leer2();
                DatosSensor.setText(y);

                //  finish();
            }
            //super.onPostExecute(s);
        }
        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }
    }

    public void crear (String id) //crea el archivo donde guardamos el id vivienda
    {
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            getActivity().openFileOutput("DatosVivienda.txt", Context.MODE_PRIVATE));
            fout.write(id);
            fout.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero en la memoria interna");
        }
    }
    public String leer() // lee el archivo donde guardamos el id vivienda
    {
        String sw ="0";
        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    getActivity().openFileInput("DatosVivienda.txt")));

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
    public void crear2 (String id)  //crea el archivo donde guardamos los datos estadisticos por sensor
    {
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            getActivity().openFileOutput("DatosSensor.txt", Context.MODE_PRIVATE));
            fout.write(id);
            fout.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero en la memoria interna");
        }
    }
    public String leer2()// lee el archivo donde guardamos los datos estadisticos por sensor
    {
        String sw ="0";
        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    getActivity().openFileInput("DatosSensor.txt")));

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
    public void crear3 (String id) //crea el archivo donde guardamos el id del sensor
    {
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            getActivity().openFileOutput("IdSensor.txt", Context.MODE_PRIVATE));
            fout.write(id);
            fout.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero en la memoria interna");
        }
    }
    public String leer3()// lee el archivo donde guardamos el id del sensor
    {
        String sw ="0";
        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    getActivity().openFileInput("IdSensor.txt")));

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


    public static String[] separarFrase(String s) {
        int cp = 0; // Cantidad de palabras

        // Recorremos en busca de espacios
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '$') { // Si es un espacio
                cp++; // Aumentamos en uno la cantidad de palabras
            }
        }

        // "Este blog es genial" tiene 3 espacios y 3 + 1 palabras
        String[] partes = new String[cp + 1];
        for (int i = 0; i < partes.length; i++) {
            partes[i] = ""; // Se inicializa en "" en lugar de null (defecto)
        }

        int ind = 0; // Creamos un índice para las palabras
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '$') { // Si hay un espacio
                ind++; // Pasamos a la siguiente palabra
                continue; // Próximo i
            }
            partes[ind] += s.charAt(i); // Sino, agregamos el carácter a la palabra actual
        }
        return partes; // Devolvemos las partes
    }



}















