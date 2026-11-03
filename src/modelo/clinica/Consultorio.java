package modelo.clinica;

/**
 * Consultorio físico de la clínica.
 * Es una parte de Clinica (composición): solo la clínica los crea y existen mientras exista la clínica
 */
public class Consultorio {

    private final int numero;

    public Consultorio(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de consultorio debe ser mayor a 0");
        }
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return "Consultorio " + numero;
    }
}