package grupo11.ecohogar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Bienvenido extends Fragment {

    TextView cant2,fecha,fecha2;

    Activity activity;
    private OnFragmentInteractionListener mListener;

    public Bienvenido() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bienvenido, container, false);
        activity = getActivity();
        cant2 = (TextView) view.findViewById(R.id.txtcCantidad2);

        fecha = (TextView) view.findViewById(R.id.txtHora2);
        fecha2 = (TextView) view.findViewById(R.id.txtFecha2);
        fecha.setText(String.valueOf("11:28"));
        fecha2.setText("11/05/2017");
        cant2.setText("54234.343");

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
    public void crear (String id)
    {
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            getActivity().openFileOutput("dato.txt", Context.MODE_PRIVATE));
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
                                    getActivity().openFileInput("dato.txt")));

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
    public static String[] separaDatoSensor(String s) {
        int cp = 0; // Cantidad de palabras

        // Recorremos en busca de espacios
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ',') { // Si es un espacio
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
            if (s.charAt(i) == ',') { // Si hay un espacio
                ind++; // Pasamos a la siguiente palabra
                continue; // Próximo i
            }
            partes[ind] += s.charAt(i); // Sino, agregamos el carácter a la palabra actual
        }
        return partes; // Devolvemos las partes
    }
}
