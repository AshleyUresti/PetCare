package modelo.personas;

/** Médico veterinario que atiende citas. */
public class Veterinario extends Persona {

    private String cedulaProfesional;
    private String especialidad;

    public Veterinario(String nombre, String apellido, String telefono, String correo, String cedulaProfesional, String especialidad) {
        super(nombre, apellido, telefono, correo);
        String cedula = validarTexto(cedulaProfesional, "La cédula profesional");
        if (!cedula.matches("\\d{7,8}")) {
            throw new IllegalArgumentException("La cédula profesional debe tener 7 u 8 dígitos.");
        }
        this.cedulaProfesional = cedula;
        this.especialidad = validarTexto(especialidad, "La especialidad");
    }

    @Override
    public String describirRol() {
        return "Veterinario especialista en " + especialidad;
    }

    @Override
    public String toString() {
        return super.toString() + " Cédula: " + cedulaProfesional;
    }
}