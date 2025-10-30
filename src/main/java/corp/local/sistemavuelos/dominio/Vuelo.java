package corp.local.sistemavuelos.dominio;
import java.util.Date;

public class Vuelo {
    private int id;
    private String numeroVuelo;
    private String origen;
    private String destino;
    private Date fechaSalida;
    private Date fechaLlegada;
    private double precio;
    private int asientosDisponibles;
    private String aerolinea;
    private int duracionMinutos;
    private int escalas;

    // Constructores
    public Vuelo() {}

    public Vuelo(int id, String numeroVuelo, String origen, String destino, 
                Date fechaSalida, Date fechaLlegada, double precio, 
                int asientosDisponibles, String aerolinea, int duracionMinutos, int escalas) {
        this.id = id;
        this.numeroVuelo = numeroVuelo;
        this.origen = origen;
        this.destino = destino;
        this.fechaSalida = fechaSalida;
        this.fechaLlegada = fechaLlegada;
        this.precio = precio;
        this.asientosDisponibles = asientosDisponibles;
        this.aerolinea = aerolinea;
        this.duracionMinutos = duracionMinutos;
        this.escalas = escalas;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumeroVuelo() { return numeroVuelo; }
    public void setNumeroVuelo(String numeroVuelo) { this.numeroVuelo = numeroVuelo; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public Date getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(Date fechaSalida) { this.fechaSalida = fechaSalida; }

    public Date getFechaLlegada() { return fechaLlegada; }
    public void setFechaLlegada(Date fechaLlegada) { this.fechaLlegada = fechaLlegada; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getAsientosDisponibles() { return asientosDisponibles; }
    public void setAsientosDisponibles(int asientosDisponibles) { this.asientosDisponibles = asientosDisponibles; }

    public String getAerolinea() { return aerolinea; }
    public void setAerolinea(String aerolinea) { this.aerolinea = aerolinea; }

    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public int getEscalas() { return escalas; }
    public void setEscalas(int escalas) { this.escalas = escalas; }
}