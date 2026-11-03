package modelo.mascotas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.clinica.Cita;

/** Registro de las citas de una mascota, depende de la mascota que lo contiene */
public class HistorialClinico {

    //las citas se crean en Clinica, solo se guardan referencias
    private final List<Cita> consultas = new ArrayList<>();

    //su tipo de acceso es por defecto
    HistorialClinico() {
    }

    public void registrarConsulta(Cita cita) {
        if (cita == null) {
            throw new IllegalArgumentException("La cita no puede ser nula");
        }
        if (consultas.contains(cita)) {
            throw new IllegalArgumentException("La cita ya está en el historial");
        }
        consultas.add(cita);
    }

    public List<Cita> getConsultas() {
        return Collections.unmodifiableList(consultas);
    }

    public String mostrarHistorial() {
        if (consultas.isEmpty()) {
            return "Sin consultas registradas...";
        }
        StringBuilder sb = new StringBuilder();
        for (Cita c : consultas) { //bucle para recorrer las consultas
            sb.append("- ").append(c).append(System.lineSeparator());
        }
        return sb.toString();
    }
}