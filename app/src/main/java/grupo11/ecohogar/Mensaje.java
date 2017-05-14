package grupo11.ecohogar;

/**
 * Created by Clau on 06/05/2017.
 */

public class Mensaje {
    protected long id;
    protected int limite_agua;
    protected int limite_luz;
    protected String fecha;
    protected String mensaje;

    public Mensaje()
    {

    }
    public Mensaje(int limite_agua, int limite_luz, String fecha,String mensaje) {
        this.limite_agua = limite_agua;
        this.limite_luz = limite_luz;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    public long getId() {
        return id;
    }

    public int getLimite_agua() {
        return limite_agua;
    }

    public int getLimite_luz() {
        return limite_luz;
    }

    public String getFecha() {
        return fecha;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLimite_agua(int limite_agua) {
        this.limite_agua = limite_agua;
    }

    public void setLimite_luz(int limite_luz) {
        this.limite_luz = limite_luz;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {

        return mensaje;
    }
}
