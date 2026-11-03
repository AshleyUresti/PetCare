package modelo.servicios;

/** Cirugía: se cobra por hora de quirófano, con un recargo según la complejidad (1 = mínima, 5 = máxima)*/
public class Cirugia implements Servicio {

    //static final porque es igual para todas las cirugias
    private static final double INCREMENTO_POR_COMPLEJIDAD = 0.20;
    private double tarifaPorHora;
    private int horasQuirofano;
    private int complejidad;

    public Cirugia(double tarifaPorHora, int horasQuirofano, int complejidad) {
        if (tarifaPorHora <= 0) {
            throw new IllegalArgumentException("La tarifa por hora debe ser mayor que 0");
        }
        this.tarifaPorHora = tarifaPorHora;
        setHorasQuirofano(horasQuirofano);
        setComplejidad(complejidad);
    }

    //tipo "final" porque el constructor es quien invoca los métodos
    public final void setHorasQuirofano(int horasQuirofano) {
        if (horasQuirofano <= 0) {
            throw new IllegalArgumentException("Las horas de quirófano deben ser al menos 1");
        }
        this.horasQuirofano = horasQuirofano;
    }

    public final void setComplejidad(int complejidad) {
        if (complejidad < 1 || complejidad > 5) {
            throw new IllegalArgumentException("La complejidad debe estar entre 1 y 5");
        }
        this.complejidad = complejidad;
    }

    @Override
    public double calcularCosto() {
        //utilizamos variable local, ya que el factor solo se necesita durante este cálculo
        double factor = 1 + INCREMENTO_POR_COMPLEJIDAD * (complejidad - 1);
        return tarifaPorHora * horasQuirofano * factor;
    }

    @Override
    public String getDescripcion() {
        return "Cirugía (complejidad " + complejidad + ", " + horasQuirofano + " hora/hrs de quirófano)";
    }
}