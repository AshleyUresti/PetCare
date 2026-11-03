package modelo.clinica;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import modelo.mascotas.Mascota;
import modelo.personas.Veterinario;
import modelo.servicios.Servicio;

/**
 * Comprobante de cobro de una cita atendida.
 * Es creado y poseído por la Clinica.
 */
public class Comprobante {

    // private + static: el contador es único para toda la clase y nadie fuera de Comprobante debe poder modificarlo para que los folios no se repitan
    private static int contadorComprobantes = 0;

    private final String folio;
    private final double total;
    private final LocalDateTime fechaEmision;
    private final Cita cita;

    public Comprobante(Cita cita) {
        if (cita == null) {
            throw new IllegalArgumentException("El comprobante requiere una cita");
        }
        if (cita.getEstado() != EstadoCita.ATENDIDA) {
            throw new IllegalArgumentException("Solo se puede generar comprobante de una cita atendida");
        }
        // El contador solo avanza si todas las validaciones pasaron
        contadorComprobantes++;
        this.folio = String.format("CMP-%04d", contadorComprobantes);
        this.cita = cita;
        this.total = cita.calcularTotal();
        this.fechaEmision = LocalDateTime.now();
    }

    public String getFolio() {
        return folio;
    }

    public double getTotal() {
        return total;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public Cita getCita() {
        return cita;
    }

    public Mascota getMascota() {
        return cita.getMascota();
    }

    public Veterinario getVeterinario() {
        return cita.getVeterinario();
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("========== COMPROBANTE ").append(folio).append(" ==========\n");
        sb.append("Emitido:      ").append(fechaEmision.format(formato)).append("\n");
        sb.append("Mascota:      ").append(getMascota().getNombre()).append("\n");
        sb.append("Propietario:  ").append(getMascota().getPropietario().getNombreCompleto()).append("\n");
        sb.append("Veterinario:  ").append(getVeterinario().getNombreCompleto()).append("\n");
        sb.append("Fecha cita:   ").append(cita.getFechaHora().format(formato)).append("\n");
        sb.append("Servicios:\n");
        for (Servicio servicio : cita.getServicios()) {
            sb.append(String.format("  - %-40s $%.2f%n", servicio.getDescripcion(), servicio.calcularCosto()));
        }
        sb.append(String.format("TOTAL: $%.2f%n", total));
        sb.append("==========================================");
        return sb.toString();
    }
}