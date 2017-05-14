package grupo11.ecohogar;

public class Vivienda {
    protected String zona;
    protected long id;
    public Vivienda(String zona) {
        this.zona = zona;
    }
    public String getZona() {
        return zona;
    }
    public void setZona(String zona) {
        this.zona = zona;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}