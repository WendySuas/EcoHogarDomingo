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

public class DatosUsuario extends Fragment {
    private OnFragmentInteractionListener mListener;

    TextView cliente;
    TextView fecha;
    TextView celular;
    TextView correo;
    TextView usuario;

    Activity activity;
    public DatosUsuario() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datos_usuario, container, false);
        activity = getActivity();
        String x = leer();
        cliente = (TextView) view.findViewById(R.id.txtCliente);
        fecha = (TextView) view.findViewById(R.id.txtFechanacimiento);
        celular = (TextView) view.findViewById(R.id.txtCelular);
        usuario = (TextView) view.findViewById(R.id.txtUsuario);
        correo = (TextView) view.findViewById(R.id.txtCorreo);

        cliente.setText(separarFrase(x)[1]+" "+separarFrase(x)[2]+" "+separarFrase(x)[3]);
        fecha.setText(separarFrase(x)[4]);
        celular.setText((separarFrase(x)[5]));
        usuario.setText(separarFrase(x)[6]);
        correo.setText(separarFrase(x)[9]);


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
                            getActivity().openFileOutput("meminterna.txt", Context.MODE_PRIVATE));
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
