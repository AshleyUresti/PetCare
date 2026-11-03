package modelo.mascotas;
import modelo.personas.Propietario;

public class AveExotica extends Mascota {

    private double longitudAlas; // en centimetros

    public AveExotica(String nombre, int edad, double peso, Propietario propietario, double longitudAlas) {
        super(nombre, edad, peso, propietario);

        if (longitudAlas <= 0) {
            throw new IllegalArgumentException("La longitud de las alas debe ser mayor que 0");
        }
        this.longitudAlas = longitudAlas;
    }

    @Override
    public String hacerSonido() {
        return "¡Pio pio!";
    }

    @Override
    public String toString() {
        return super.toString() + " | Alas: " + longitudAlas + " cm";
    }
}