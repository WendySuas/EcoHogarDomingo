package grupo11.ecohogar;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

public class ListaViviendas extends Fragment   {
    private OnFragmentInteractionListener mListener;

    ListView lista;
    ArrayList<Vivienda> AL;
    ItemAdapter adapter;
    ObtenerWebService hiloconexion;
     ObtenerWebService1 hiloconexion1;
    TextView nombrefamilia,nomb;
    Bienvenido bienvenido;
    DatosEnergia datosEnergia;
    TextView id_vivienda;

    String IP = "http://h2ogar.esy.es/PhpAndroid/";
    String GET= IP +"obtener_vivienda_por_id_cliente.php?id=";

    Activity activity;

    String id;

    public ListaViviendas() {
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_viviendas, container, false);
        activity=getActivity();
        nombrefamilia=(TextView)view.findViewById(R.id.txtNombreFamilia);
        nomb=(TextView)view.findViewById(R.id.txtFechanacimiento);

        nombrefamilia.setText(separarFrase(leer().toString())[2]);
        id = separarFrase(leer())[0];
        GET= GET+id;
        hiloconexion = new ObtenerWebService();

        hiloconexion.execute(GET);
        lista = (ListView)view.findViewById(R.id.lvListaviviendas);
        lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        AL = new ArrayList<Vivienda>();
        AL= hiloconexion.getDev();
        adapter = new ItemAdapter(activity,AL);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                TextView v = (TextView) view.findViewById(R.id.TV_vivienda);
                crear1(v.getText().toString());
               // Toast.makeText(getActivity(), "You clicked "+leer1(), Toast.LENGTH_LONG).show();

               //hiloconexion1 = new ObtenerWebService1();// nuevo metodo para sacar los datos de la vivienda
                //String cadenallamada = "http://h2ogar.esy.es/obtener_vivienda_por_id_vivienda.phpPhpAndroid/?id_vivienda="+v.getText();// saCamos el id del item(de la vivenda)
               // hiloconexion1.execute(cadenallamada);

                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
               datosEnergia = new DatosEnergia();
                transaction1.replace(R.id.layout_vistas, datosEnergia);
                transaction1.commit();
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
                        JSONArray productosJSON = respuestaJSON.getJSONArray("viviendas");
                        for(int i=0;i<productosJSON.length();i++){
                            devuelve.add(productosJSON.getJSONObject(i).getString("id_vivienda"));
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
                Vivienda vivienda= new Vivienda(a.get(i));
                dev.add(vivienda);
            }
            lista.setAdapter(adapter);
        }
        public ArrayList<Vivienda> getDev() {
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
            String sw="0";
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


                             sw = respuestaJSON.getJSONObject("viviendas").getString("nrCasa");
                             Toast.makeText(getActivity(), "resultado 1 ", Toast.LENGTH_LONG).show();
                        }
                    }
                    else if (resultJSON=="2"){
                        sw="0";
                        Toast.makeText(getActivity(), "resultado 0 ", Toast.LENGTH_LONG).show();

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
                 Toast.makeText(getActivity(), "no esta sacando los datoss ", Toast.LENGTH_LONG).show();

            }
            else
            {


                crear1(s);
                Toast.makeText(getActivity(), "ya se creo", Toast.LENGTH_LONG).show();

                //  finish();
            }
            //super.onPostExecute(s);
        }
        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }
    }
    public void crear (String id)
    {
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            getActivity().openFileOutput("meminterna.txt", Context.MODE_PRIVATE));
            fout.write(id);
            fout.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al escribir fichero en la memoria interna");
        }
    }

    public void crear1 (String id)
    {
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                           getActivity().openFileOutput("DatosVivienda.txt", Context.MODE_PRIVATE));
            fout.write(id);
            fout.close();
           // Toast.makeText(getActivity(), "datos sacados ", Toast.LENGTH_LONG).show();

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
                                    getActivity().openFileInput("meminterna.txt")));

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
    public String leer1()
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
    public static String[] separarFrase(String s) {
        int cp = 0; // Cantidad de palabras

        // Recorremos en busca de espacios
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '?') { // Si es un espacio
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
            if (s.charAt(i) == '?') { // Si hay un espacio
                ind++; // Pasamos a la siguiente palabra
                continue; // Próximo i
            }
            partes[ind] += s.charAt(i); // Sino, agregamos el carácter a la palabra actual
        }
        return partes; // Devolvemos las partes
    }

}
