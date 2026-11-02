package modelo.personas;

/** Personal de recepción, agenda y cobra, según el turno asignado. */
public class Recepcionista extends Persona {

    private String turno;

    public Recepcionista(String nombre, String apellido, String telefono, String correo, String turno) {
        super(nombre, apellido, telefono, correo);
        String t = validarTexto(turno, "El turno");
        if (!t.equalsIgnoreCase("Matutino") && !t.equalsIgnoreCase("Vespertino")) {
            throw new IllegalArgumentException("El turno debe ser Matutino o Vespertino");
        }
        this.turno = t;
    }

    @Override
    public String describirRol() {
        return "Recepcionista, turno " + turno;
    }
}