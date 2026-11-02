package modelo.personas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.mascotas.Mascota;

/** Dueño de una o más mascotas */
public class Propietario extends Persona {

    private String direccion;
    private final List<Mascota> mascotas = new ArrayList<>();

    public Propietario(String nombre, String apellido, String telefono, String correo, String direccion) {
        super(nombre, apellido, telefono, correo);
        this.direccion = validarTexto(direccion, "La dirección");
    }

    public void agregarMascota(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException("La mascota no puede ser nula");
        }
        if (mascotas.contains(mascota)) {
            throw new IllegalArgumentException("La mascota ya está registrada para este propietario");
        }
        mascotas.add(mascota);
    }

    public void eliminarMascota(Mascota mascota) {
        //solo se quita la referencia de esta lista
        if (!mascotas.remove(mascota)) {
            throw new IllegalArgumentException("La mascota no pertenece a este propietario");
        }
    }

    //para lectura
    public List<Mascota> getMascotas() {
        return Collections.unmodifiableList(mascotas);
    }

    @Override
    public String describirRol() {
        return "Propietario de " + mascotas.size() + " mascota/s, domicilio: " + direccion;
    }
}