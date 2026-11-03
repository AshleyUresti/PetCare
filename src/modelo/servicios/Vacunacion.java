package modelo.servicios;

/** Para la aplicación de una vacuna se asigna una tarifa (por aplicar) más el costo de la vacuna */
public class Vacunacion implements Servicio {

    private double tarifaBase;
    private String nombreVacuna;
    private double costoVacuna;

    public Vacunacion(double tarifaBase, String nombreVacuna, double costoVacuna) {
        if (tarifaBase <= 0) {
            throw new IllegalArgumentException("La tarifa base debe ser mayor que 0");
        }
        if (nombreVacuna == null || nombreVacuna.isBlank()) {
            throw new IllegalArgumentException("El nombre de la vacuna no puede estar vacío");
        }
        if (costoVacuna < 0) {
            throw new IllegalArgumentException("El costo de la vacuna no puede ser negativo");
        }
        this.tarifaBase = tarifaBase;
        this.nombreVacuna = nombreVacuna.trim(); //significado
        this.costoVacuna = costoVacuna;
    }

    @Override
    public double calcularCosto() {
        return tarifaBase + costoVacuna;
    }

    @Override
    public String getDescripcion() {
        return "Vacunación (" + nombreVacuna + ")";
    }
}