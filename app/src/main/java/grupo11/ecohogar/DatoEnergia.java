package grupo11.ecohogar;
/**
 * Created by Wen on 18/05/2017.
 */
public class DatoEnergia {
    protected String valor;
    protected  String Nombre_habitacion;
    protected long id;
    public DatoEnergia(String valor) {
        this.valor = valor;
    }
    public String getValor() {
        return valor;
    }
    public String getNombre_habitacion(){return Nombre_habitacion;}
    public void setValor(String valor) {
        this.valor = valor;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}