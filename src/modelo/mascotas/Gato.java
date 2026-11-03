package modelo.mascotas;

import modelo.personas.Propietario;

public class Gato extends Mascota {

    private boolean esDomestico;

    public Gato(String nombre, int edad, double peso, Propietario propietario, boolean esDomestico) {
        super(nombre, edad, peso, propietario);
        this.esDomestico = esDomestico;
    }

    @Override
    public String hacerSonido() {
        return esDomestico ? "¿Miau?" : "¡Grrr (no tocar)!";
    }

    @Override
    public String toString() {
        return super.toString() + " | " + (esDomestico ? "Doméstico" : "No doméstico");
    }
}