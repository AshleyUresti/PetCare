package modelo.servicios;

/**
 * Contrato común de Qtodo lo que la clinica puede cobrar, quien use un Servicio no necesita saber de qué tipo es, por ello basta con llamar a calcularCosto()
 * (polimorfismo, sin instanceof)
 */
public interface Servicio {

    double calcularCosto();

    String getDescripcion();
}