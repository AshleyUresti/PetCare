package modelo.clinica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.mascotas.Mascota;
import modelo.personas.Propietario;
import modelo.personas.Veterinario;

/** Clase central del sistema. Vemos la composición cuando crea y posee sus Consultorios y sus Comprobantes. Y la agregación al guarda Veterinarios, Propietarios, Mascotas y Citas que ya fueron creados fuera. Si la clínica desaparece, esos objetos siguen existiendo */
public class Clinica {

    //atributos private para que solo sean modificables mediantes métodos que validen los datos
    private final String nombre;
    private final int numConsultorios;
    private final List<Consultorio> consultorios;
    private final List<Veterinario> veterinarios;
    private final List<Propietario> propietarios;
    private final List<Mascota> mascotas;
    private final List<Cita> citas;
    private final List<Comprobante> comprobantes;

    /** Constructor por defecto, una clínica con un solo consultorio */
    public Clinica() {
        this("Clínica Veterinaria", 1);
    }

    /** Constructor con todos los datos. Los consultorios se crean aquí dentro, aplicando composición */
    public Clinica(String nombre, int numConsultorios) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la clínica no puede estar vacío");
        }
        if (numConsultorios < 1) {
            throw new IllegalArgumentException("La clínica necesita al menos un consultorio");
        }
        this.nombre = nombre.trim();
        this.numConsultorios = numConsultorios;
        this.consultorios = new ArrayList<>();
        for (int i = 1; i <= numConsultorios; i++) {
            consultorios.add(new Consultorio(i));
        }
        this.veterinarios = new ArrayList<>();
        this.propietarios = new ArrayList<>();
        this.mascotas = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.comprobantes = new ArrayList<>();
    }

    // Para los REGISTROS

    public void agregarVeterinario(Veterinario veterinario) {
        if (veterinario == null) {
            throw new IllegalArgumentException("El veterinario no puede ser nulo");
        }
        if (veterinarios.contains(veterinario)) {
            throw new IllegalArgumentException("El veterinario ya está asignado a la clínica");
        }
        veterinarios.add(veterinario);
    }

    public void registrarPropietario(Propietario propietario) {
        if (propietario == null) {
            throw new IllegalArgumentException("El propietario no puede ser nulo");
        }
        if (propietarios.contains(propietario)) {
            throw new IllegalArgumentException("El propietario ya está registrado");
        }
        propietarios.add(propietario);
    }

    /** La mascota debe pertenecer a un propietario ya registrado en la clínica */
    public void registrarMascota(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException("La mascota no puede ser nula");
        }
        if (mascotas.contains(mascota)) {
            throw new IllegalArgumentException("La mascota ya está registrada");
        }
        Propietario propietario = mascota.getPropietario();
        if (!propietarios.contains(propietario)) {
            throw new IllegalArgumentException("Primero debes registrar al propietario de la mascota");
        }
        mascotas.add(mascota);
        if (!propietario.getMascotas().contains(mascota)) {
            propietario.agregarMascota(mascota);
        }
    }

    // para las CITAS

    /**
     * Agenda una cita ya armada. Valida que todo lo referenciado exista en la
     * clínica, que tenga al menos un servicio y que no se empalme con otra.
     */
    public void agendarCita(Cita cita) {
        if (cita == null) {
            throw new IllegalArgumentException("La cita no puede ser nula");
        }
        if (!mascotas.contains(cita.getMascota())) {
            throw new IllegalArgumentException("La mascota de la cita no está registrada en la clínica");
        }
        if (!veterinarios.contains(cita.getVeterinario())) {
            throw new IllegalArgumentException("El veterinario de la cita no pertenece a la clínica");
        }
        if (!consultorios.contains(cita.getConsultorio())) {
            throw new IllegalArgumentException("El consultorio de la cita no pertenece a la clínica");
        }
        if (cita.getServicios().isEmpty()) {
            throw new IllegalArgumentException("La cita debe incluir al menos un servicio");
        }
        for (Cita existente : citas) {
            if (existente.coincideCon(cita)) {
                throw new IllegalArgumentException(
                        "La cita se empalma con otra ya programada: " + existente);
            }
        }
        citas.add(cita);
    }

    /** Sobrecarga 1: busca citas por nombre de mascota */
    public List<Cita> buscarCita(String nombreMascota) {
        if (nombreMascota == null || nombreMascota.isBlank()) {
            throw new IllegalArgumentException("El nombre de la mascota no puede estar vacío");
        }
        List<Cita> resultado = new ArrayList<>();
        for (Cita cita : citas) {
            if (cita.getMascota().getNombre().equalsIgnoreCase(nombreMascota.trim())) {
                resultado.add(cita);
            }
        }
        return resultado;
    }

    /** Sobrecarga 2: busca citas por día */
    public List<Cita> buscarCita(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        List<Cita> resultado = new ArrayList<>();
        for (Cita cita : citas) {
            if (cita.getFechaHora().toLocalDate().equals(fecha)) {
                resultado.add(cita);
            }
        }
        return resultado;
    }

    // para COMPROBANTES

    /** Crea el comprobante de una cita atendida (composición). Una cita solo se cobra una vez */
    public Comprobante generarComprobante(Cita cita) {
        if (cita == null) {
            throw new IllegalArgumentException("La cita no puede ser nula");
        }
        if (!citas.contains(cita)) {
            throw new IllegalArgumentException("La cita no pertenece a esta clínica");
        }
        for (Comprobante existente : comprobantes) {
            if (existente.getCita() == cita) {
                throw new IllegalArgumentException(
                        "Esta cita ya tiene comprobante: " + existente.getFolio());
            }
        }
        Comprobante comprobante = new Comprobante(cita); // valida que esté ATENDIDA
        comprobantes.add(comprobante);
        return comprobante;
    }

    public List<Comprobante> consultarCobrosPorMascota(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException("La mascota no puede ser nula");
        }
        List<Comprobante> resultado = new ArrayList<>();
        for (Comprobante comprobante : comprobantes) {
            if (comprobante.getMascota() == mascota) {
                resultado.add(comprobante);
            }
        }
        return resultado;
    }

    public List<Comprobante> consultarCobrosPorVeterinario(Veterinario veterinario) {
        if (veterinario == null) {
            throw new IllegalArgumentException("El veterinario no puede ser nulo");
        }
        List<Comprobante> resultado = new ArrayList<>();
        for (Comprobante comprobante : comprobantes) {
            if (comprobante.getVeterinario() == veterinario) {
                resultado.add(comprobante);
            }
        }
        return resultado;
    }

    // para una baja de mascota

    /**
     * Da de baja una mascota junto con su información asociada.
     * Solo se quitan las referencias (de la clínica, del propietario y de las citas/comprobantes).
     * Como el HistorialClinico es parte de la Mascota, al quedar la mascota sin referencias su historial también queda inalcanzable
     * */
    public void darDeBajaMascota(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException("La mascota no puede ser nula");
        }
        if (!mascotas.contains(mascota)) {
            throw new IllegalArgumentException("La mascota no está registrada en la clínica");
        }

        citas.removeIf(cita -> cita.getMascota() == mascota);
        comprobantes.removeIf(comprobante -> comprobante.getMascota() == mascota);
        mascotas.remove(mascota);

        mascota.getPropietario().eliminarMascota(mascota);
    }

    // para CONSULTAS

    /** Busca uno de los consultorios que la clínica creó */
    public Consultorio buscarConsultorio(int numero) {
        for (Consultorio consultorio : consultorios) {
            if (consultorio.getNumero() == numero) {
                return consultorio;
            }
        }
        throw new IllegalArgumentException("No existe el consultorio " + numero);
    }

    public String getNombre() {
        return nombre;
    }

    public int getNumConsultorios() {
        return numConsultorios;
    }

    // Vistas de solo lectura: se puede consultar pero no alterar la lista desde fuera
    public List<Consultorio> getConsultorios() {
        return Collections.unmodifiableList(consultorios);
    }

    public List<Veterinario> getVeterinarios() {
        return Collections.unmodifiableList(veterinarios);
    }

    public List<Propietario> getPropietarios() {
        return Collections.unmodifiableList(propietarios);
    }

    public List<Mascota> getMascotas() {
        return Collections.unmodifiableList(mascotas);
    }

    public List<Cita> getCitas() {
        return Collections.unmodifiableList(citas);
    }

    public List<Comprobante> getComprobantes() {
        return Collections.unmodifiableList(comprobantes);
    }
}