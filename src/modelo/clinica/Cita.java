package modelo.clinica;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.mascotas.Mascota;
import modelo.personas.Veterinario;
import modelo.servicios.Servicio;

/** Cita es una asociación, ya que referencia a una Mascota, un Veterinario y un Consultorio que existen de forma independiente a la cita*/
public class Cita {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Mascota mascota;
    private final Veterinario veterinario;
    private final Consultorio consultorio;
    private final LocalDateTime fechaHora;
    private final List<Servicio> servicios;
    private EstadoCita estado;

    public Cita(Mascota mascota, Veterinario veterinario, Consultorio consultorio, LocalDateTime fechaHora) {
        if (mascota == null) {
            throw new IllegalArgumentException("La cita requiere una mascota");
        }
        if (veterinario == null) {
            throw new IllegalArgumentException("La cita requiere un veterinario");
        }
        if (consultorio == null) {
            throw new IllegalArgumentException("La cita requiere un consultorio");
        }
        if (fechaHora == null) {
            throw new IllegalArgumentException("La cita requiere fecha y hora");
        }
        this.mascota = mascota;
        this.veterinario = veterinario;
        this.consultorio = consultorio;
        this.fechaHora = fechaHora;
        this.servicios = new ArrayList<>();
        this.estado = EstadoCita.PROGRAMADA;
    }

    /** Solo se pueden agregar servicios mientras la cita está programada.*/
    public void agregarServicio(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio no puede ser nulo");
        }
        if (estado != EstadoCita.PROGRAMADA) {
            throw new IllegalStateException("Solo se pueden agregar servicios a una cita programada");
        }
        servicios.add(servicio);
    }

    /** Marca la cita como atendida y la registra en el historial clínico de la mascota */
    public void atender() {
        if (estado != EstadoCita.PROGRAMADA) {
            throw new IllegalStateException("Solo se puede atender una cita programada (estado actual: " + estado + ")");
        }
        if (servicios.isEmpty()) {
            throw new IllegalStateException("No se puede atender una cita sin servicios");
        }
        estado = EstadoCita.ATENDIDA;
        mascota.getHistorial().registrarConsulta(this);
    }

    public void cancelar() {
        if (estado != EstadoCita.PROGRAMADA) {
            throw new IllegalStateException("Solo se puede cancelar una cita programada (estado actual: " + estado + ")");
        }
        estado = EstadoCita.CANCELADA;
    }

    /** Indica si dos citas se empalman: ambas siguen programadas, tienen la misma fecha y hora, compartiendo consultorio, veterinario o mascota */
    public boolean coincideCon(Cita otraCita) {
        if (otraCita == null || otraCita == this) {
            return false;
        }
        if (estado != EstadoCita.PROGRAMADA || otraCita.estado != EstadoCita.PROGRAMADA) {
            return false;
        }
        if (!fechaHora.equals(otraCita.fechaHora)) {
            return false;
        }
        return consultorio == otraCita.consultorio
                || veterinario == otraCita.veterinario
                || mascota == otraCita.mascota;
    }

    /** Polimorfismo: se suma el costo de cada Servicio */
    public double calcularTotal() {
        double total = 0;
        for (Servicio servicio : servicios) {
            total += servicio.calcularCosto();
        }
        return total;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public Consultorio getConsultorio() {
        return consultorio;
    }

    /** Devuelve una vista de solo lectura para que nadie modifique la lista desde fuera */
    public List<Servicio> getServicios() {
        return Collections.unmodifiableList(servicios);
    }

    @Override
    public String toString() {
        return String.format("Cita [%s] %s | Mascota: %s | Veterinario: %s | %s | Servicios: %d | Total: $%.2f",
                estado,
                fechaHora.format(FORMATO),
                mascota.getNombre(),
                veterinario.getNombreCompleto(),
                consultorio,
                servicios.size(),
                calcularTotal());
    }
}