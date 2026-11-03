package modelo.servicios;

/** Consulta de revisión general, se maneja con una tarifa fija */
public class ConsultaGeneral implements Servicio {
    private double tarifaBase;

    public ConsultaGeneral(double tarifaBase) {
        if (tarifaBase <= 0) {
            throw new IllegalArgumentException("La tarifa base debe ser mayor que 0");
        }
        this.tarifaBase = tarifaBase;
    }

    @Override
    public double calcularCosto() {
        return tarifaBase;
    }

    @Override
    public String getDescripcion() {
        return "Consulta general";
    }
}