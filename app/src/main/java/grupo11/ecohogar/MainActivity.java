package grupo11.ecohogar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText usuario;
    EditText contrasenia;
    Button ingresar;
    TextView irapaginaweb;
    URL web;
    int cont=0;

    String IP = "http://h2ogar.esy.es/PhpAndroid/";
    String GET_BY_ID= IP + "obtener_cliente_por_usuario.php";
    ObtenerWebService hiloconexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuario=(EditText)findViewById(R.id.edtUsuario);
        contrasenia=(EditText)findViewById(R.id.edtContrasenia);
        ingresar=(Button)findViewById(R.id.btnIngresar);
        ingresar.setOnClickListener(this);
        irapaginaweb=(TextView) findViewById(R.id.txtIrapaginaweb);
        irapaginaweb.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnIngresar:


                 hiloconexion = new ObtenerWebService();
                  String cadenallamada = GET_BY_ID + "?usuario=" + usuario.getText().toString();
                  String pass = contrasenia.getText().toString();
                  hiloconexion.execute(cadenallamada,pass);
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.txtIrapaginaweb:
                web =null;



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
            String sw="0";
            try {
                url = new URL(cadena);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();                 connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
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
                        if (params[1] .equals( respuestaJSON.getJSONObject("cliente").getString("password"))) {
                            sw = respuestaJSON.getJSONObject("cliente").getString("id")+"?"+
                                    respuestaJSON.getJSONObject("cliente").getString("paterno")+"?"+
                                    respuestaJSON.getJSONObject("cliente").getString("materno")+"?"+
                                    respuestaJSON.getJSONObject("cliente").getString("nombres")+"?"+
                                    respuestaJSON.getJSONObject("cliente").getString("fecNac")+"?"+
                                    respuestaJSON.getJSONObject("cliente").getString("celular")+"?"+
                                    respuestaJSON.getJSONObject("cliente").getString("usuario")+"?"+
                                    respuestaJSON.getJSONObject("cliente").getString("password")+"?"+
                                    respuestaJSON.getJSONObject("cliente").getString("Habilitado")+"?"+
                                    respuestaJSON.getJSONObject("cliente").getString("correo")+"?"+
                                    respuestaJSON.getJSONObject("cliente").getString("tipo")+"?"+
                                    respuestaJSON.getJSONObject("cliente").getString("fecha_inscripcion");
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
                Toast toast= Toast.makeText(getApplicationContext(),"El usuario o la contraseña son incorrectas",Toast.LENGTH_LONG);
                toast.show();

            }
            else
            {
                cont=0;
                Toast toast= Toast.makeText(getApplicationContext(),"Bienvenido "+separarFrase(s)[3],Toast.LENGTH_LONG);
                toast.show();
                crear(s);
                Intent intent = new Intent(MainActivity.this, Vistas.class);
                startActivity(intent);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch(keyCode){

            case KeyEvent.KEYCODE_BACK:
                cont++;
                if(cont == 2)
                {
                    cont=0;
                    finish();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(this, "Pulse nuevamente atras para salir",
                            Toast.LENGTH_SHORT).show();
                }

                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
