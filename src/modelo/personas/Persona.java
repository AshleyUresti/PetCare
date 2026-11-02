package modelo.personas;
/**
 * Clase abstracta base de las personas de la clínica, concentra los datos y validaciones comunes (nombre, apellido, teléfono, correo)
 */
public abstract class Persona {
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;

    // es protected porque el constructor solo tiene sentido al llamarse desde super()
    protected Persona(String nombre, String apellido, String telefono, String correo) {
        setNombre(nombre);
        setApellido(apellido);
        setTelefono(telefono);
        setCorreo(correo);
    }

    static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) { //para que no se pueda dejar vacío
            throw new IllegalArgumentException(campo + " no puede estar vacío!");
        }
        return valor.trim();
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    //los setters son "final" porque el constructor los invoca y así ninguna puede sobreescribirlos y romper la validación
    public final void setNombre(String nombre) {
        this.nombre = validarTexto(nombre, "El nombre");
    }

    public final void setApellido(String apellido) {
        this.apellido = validarTexto(apellido, "El apellido");
    }

    public final void setTelefono(String telefono) {
        String t = validarTexto(telefono, "El teléfono");
        if (!t.matches("\\d{10}")) {
            throw new IllegalArgumentException("El teléfono debe tener 10 dígitos");
        }
        this.telefono = t;
    }

    public final void setCorreo(String correo) {
        String c = validarTexto(correo, "El correo");
        if (!c.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("El correo no tiene un formato válido!");
        }
        this.correo = c;
    }

    public abstract String describirRol();

    @Override
    public String toString() {
        //cada clase hija sobreescribe con su propia descripción
        return getNombreCompleto() + " [" + describirRol() + "] Tel: " + telefono + "  Correo: " + correo;
    }
}