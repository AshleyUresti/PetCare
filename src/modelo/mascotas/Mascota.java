package modelo.mascotas;
import modelo.personas.Propietario;

/**
 * Clase abstracta base de todas las mascotas de la clínica, cada mascota crea y posee su propio historial clínico (composición) */
public abstract class Mascota {

    private String nombre;
    private int edad;
    private double peso;
    private final Propietario propietario;
    //el historial se crea aquí dentro
    private final HistorialClinico historial = new HistorialClinico();

    protected Mascota(String nombre, int edad, double peso, Propietario propietario) {
        if (propietario == null) {
            throw new IllegalArgumentException("La mascota debe tener un propietario!");
        }
        this.propietario = propietario;
        setNombre(nombre);
        setEdad(edad);
        setPeso(peso);
        propietario.agregarMascota(this);
    }

    //su tipo de acceso es por defecto
    static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío!");
        }
        return valor.trim();
    }

    /** Cada especie produce su propio sonido (polimorfismo) */
    public abstract String hacerSonido();

    public String getNombre() {
        return nombre;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    // Los setters son final porque el constructor los invoca
    public final void setNombre(String nombre) {
        this.nombre = validarTexto(nombre, "El nombre de la mascota");
    }

    public final void setEdad(int edad) {
        if (edad < 0 || edad > 50) {
            throw new IllegalArgumentException("La edad debe estar entre 0 y 50 años");
        }
        this.edad = edad;
    }

    public final void setPeso(double peso) {
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que 0");
        }
        this.peso = peso;
    }

    public HistorialClinico getHistorial() {
        return historial;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + nombre + ", " + edad + " año(s) y " + peso + " kg";
    }
}