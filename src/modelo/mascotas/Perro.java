package modelo.mascotas;
import modelo.personas.Propietario;

public class Perro extends Mascota {

    private String raza;
    private String nivelEnergia;

    public Perro(String nombre, int edad, double peso, Propietario propietario, String raza, String nivelEnergia) {
        super(nombre, edad, peso, propietario);
        this.raza = validarTexto(raza, "La raza");

        String nivel = validarTexto(nivelEnergia, "El nivel de energía");
        if (!nivel.equalsIgnoreCase("Bajo") && !nivel.equalsIgnoreCase("Medio") && !nivel.equalsIgnoreCase("Alto")) {
            throw new IllegalArgumentException("El nivel de energía debe ser Bajo, Medio o Alto"); //para validar que se ingrese solo uno de esos 3 niveles de energia
        }
        this.nivelEnergia = nivel;
    }

    @Override
    public String hacerSonido() {
        return "¡Woof woof!";
    }

    @Override
    public String toString() {
        return super.toString() + " | Raza: " + raza + " | Energía: " + nivelEnergia;
    }
}