package grupo11.ecohogar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
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
import java.util.ArrayList;


public class Notificacion extends Fragment {
    private OnFragmentInteractionListener mListener;
    ListView lista;
    ArrayList<Mensaje> AL;
    ItemAdaptador adaptador;
    //  ObtenerWebService hiloconexion;

    //  String IP = "http://h2ogar.esy.es/";
    //  String GET= IP +"obtener_nombre_de_zona_por_id_cliente.php?id=";

    String x;
    Activity activity;

    public Notificacion() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_notificacion, container, false);
        activity=getActivity();

        //   String y=GET+x;
        //  hiloconexion = new ObtenerWebService();
        //    hiloconexion.execute(GET);
        lista = (ListView)view.findViewById(R.id.lvNotificaciones);
        lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        AL = new ArrayList<Mensaje>();
        AL.add(new Mensaje(3453,34535,"Ha excedido el uso de energia!!!","fdgfdfsfdsfsdfdf"));
        AL.add(new Mensaje(232,355,"Ha excedido el uso de agua!!!","sdjnfksnfksd"));
        //     AL= hiloconexion.getDev();
        adaptador = new ItemAdaptador(activity,AL);
        lista.setAdapter(adaptador);
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
        ArrayList<Mensaje> dev = new ArrayList<Mensaje>();
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
                //  Mensaje mensaje= new Mensaje(a.get(i));
                // dev.add(mensaje);
            }
            // lista.setAdapter(adaptador);
        }
        public ArrayList<Mensaje> getDev()
        {
            return dev;

        }
    }
}
