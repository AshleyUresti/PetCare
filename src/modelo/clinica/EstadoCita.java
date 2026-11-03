package modelo.clinica;

/**
 * Estados posibles de una cita.
 * Una cita nace PROGRAMADA y pasa a ATENDIDA o CANCELADA, como sus estados finales
 */
public enum EstadoCita {
    PROGRAMADA,
    ATENDIDA,
    CANCELADA
}