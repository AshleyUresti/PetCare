package app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import modelo.clinica.Cita;
import modelo.clinica.Clinica;
import modelo.clinica.Comprobante;
import modelo.clinica.Consultorio;
import modelo.clinica.EstadoCita;
import modelo.mascotas.AveExotica;
import modelo.mascotas.Gato;
import modelo.mascotas.Mascota;
import modelo.mascotas.Perro;
import modelo.personas.Propietario;
import modelo.personas.Veterinario;
import modelo.servicios.Cirugia;
import modelo.servicios.ConsultaGeneral;
import modelo.servicios.Servicio;
import modelo.servicios.Vacunacion;

public class Main {

    private static final Scanner SC = new Scanner(System.in);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final Clinica clinica = new Clinica("Clínica Veterinaria PetCare", 3);

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elige una opción: ", 1, 10);
            try {
                switch (opcion) {
                    case 1 -> registrarDuenoYMascota();
                    case 2 -> registrarVeterinario();
                    case 3 -> agendarCita();
                    case 4 -> atenderOCancelarCita();
                    case 5 -> generarComprobante();
                    case 6 -> consultarHistorial();
                    case 7 -> listarMascotasYSonidos();
                    case 8 -> totalPorVeterinario();
                    case 9 -> darDeBajaMascota();
                    case 10 -> System.out.println("Nos vemos!");
                    default -> { }
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("No se pudo completar: " + e.getMessage());
            }
        } while (opcion != 10);
    }

    // para el MENÚ

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== " + clinica.getNombre() + " =====");
        System.out.println(" 1. Registrar dueño y mascota");
        System.out.println(" 2. Registrar veterinario");
        System.out.println(" 3. Agendar una cita");
        System.out.println(" 4. Atender o cancelar una cita");
        System.out.println(" 5. Generar comprobante de una cita atendida");
        System.out.println(" 6. Consultar historial clínico de una mascota");
        System.out.println(" 7. Listar mascotas y su sonido");
        System.out.println(" 8. Ver total cobrado por veterinario");
        System.out.println(" 9. Dar de baja una mascota");
        System.out.println("10. Salir");
    }

    // para las OPCIONES

    private static void registrarDuenoYMascota() {
        Propietario propietario;
        List<Propietario> existentes = clinica.getPropietarios();
        if (!existentes.isEmpty() && leerEntero("¿Dueño nuevo (1) o ya registrado (2)? ", 1, 2) == 2) {
            propietario = elegir("Dueños registrados:", existentes);
            if (propietario == null) {
                return;
            }
        } else {
            System.out.println("--- Datos del dueño ---");
            propietario = new Propietario(
                    leerTexto("Nombre: "),
                    leerTexto("Apellido: "),
                    leerTexto("Teléfono (10 dígitos): "),
                    leerTexto("Correo: "),
                    leerTexto("Dirección: "));
        }

        System.out.println("-- Datos de la mascota --");
        System.out.println("Tipo: 1) Perro  2) Gato  3) Ave exótica");
        int tipo = leerEntero("Tipo: ", 1, 3);
        String nombre = leerTexto("Nombre: ");
        int edad = leerEntero("Edad (años): ", 0, 60);
        double peso = leerDecimal("Peso (kg): ");

        Mascota mascota;
        switch (tipo) {
            case 1 -> mascota = new Perro(nombre, edad, peso, propietario,
                    leerTexto("Raza: "), leerTexto("Nivel de energía (bajo/medio/alto): "));
            case 2 -> mascota = new Gato(nombre, edad, peso, propietario,
                    leerSiNo("¿Es doméstico? (s/n): "));
            default -> mascota = new AveExotica(nombre, edad, peso, propietario,
                    leerDecimal("Longitud de alas (cm): "));
        }

        // Se registra hasta que mascota y dueño se hayan creado bien
        if (!clinica.getPropietarios().contains(propietario)) {
            clinica.registrarPropietario(propietario);
        }
        clinica.registrarMascota(mascota);
        System.out.println("Registrado: " + mascota);
    }

    private static void registrarVeterinario() {
        Veterinario veterinario = new Veterinario(
                leerTexto("Nombre: "),
                leerTexto("Apellido: "),
                leerTexto("Teléfono (10 dígitos): "),
                leerTexto("Correo: "),
                leerTexto("Cédula profesional: "),
                leerTexto("Especialidad: "));
        clinica.agregarVeterinario(veterinario);
        System.out.println("Veterinario asignado a la clínica: " + veterinario.getNombreCompleto());
    }

    private static void agendarCita() {
        Mascota mascota = elegir("Mascotas:", clinica.getMascotas());
        if (mascota == null) {
            return;
        }
        Veterinario veterinario = elegir("Veterinarios:", clinica.getVeterinarios());
        if (veterinario == null) {
            return;
        }
        Consultorio consultorio = elegir("Consultorios:", clinica.getConsultorios());
        LocalDateTime fechaHora = leerFechaHora("Fecha y hora (dd/MM/yyyy HH:mm): ");
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de la cita no puede estar en el pasado!");
        }

        Cita cita = new Cita(mascota, veterinario, consultorio, fechaHora);
        agregarServicios(cita);
        clinica.agendarCita(cita);
        System.out.println("Cita agendada: " + cita);
    }

    private static void agregarServicios(Cita cita) {
        int opcion;
        do {
            System.out.println("Servicios: \n1) Consulta general \n2) Vacunación \n3) Cirugía \n0) Terminar");
            opcion = leerEntero("Servicio: ", 0, 3);
            Servicio servicio = null;
            try {
                switch (opcion) {
                    case 1 -> servicio = new ConsultaGeneral(leerDecimal("Tarifa de consulta: "));
                    case 2 -> servicio = new Vacunacion(leerDecimal("Tarifa base: "),
                            leerTexto("Nombre de la vacuna: "), leerDecimal("Costo de la vacuna: "));
                    case 3 -> servicio = new Cirugia(leerDecimal("Tarifa por hora: "),
                            leerEntero("Horas de quirófano: ", 1, 24),
                            leerEntero("Complejidad (1-5): ", 1, 5));
                    default -> { }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Servicio no agregado: " + e.getMessage());
                continue;
            }
            if (servicio != null) {
                cita.agregarServicio(servicio);
                System.out.println("Agregado: " + servicio.getDescripcion());
            } else if (cita.getServicios().isEmpty()) {
                System.out.println("La cita necesita al menos un servicio!");
                opcion = -1; // sigue en el ciclo hasta que haya uno
            }
        } while (opcion != 0);
    }

    private static void atenderOCancelarCita() {
        Cita cita = elegir("Citas programadas:", citasConEstado(EstadoCita.PROGRAMADA));
        if (cita == null) {
            return;
        }
        System.out.println("1) Atender  2) Cancelar");
        if (leerEntero("Acción: ", 1, 2) == 1) {
            cita.atender();
            System.out.println("Cita atendida y registrada en el historial");
        } else {
            cita.cancelar();
            System.out.println("Cita cancelada...");
        }
    }

    private static void generarComprobante() {
        List<Cita> cobrables = new ArrayList<>();
        for (Cita cita : citasConEstado(EstadoCita.ATENDIDA)) {
            boolean yaCobrada = clinica.getComprobantes().stream().anyMatch(c -> c.getCita() == cita);
            if (!yaCobrada) {
                cobrables.add(cita);
            }
        }
        Cita cita = elegir("Citas atendidas sin comprobante:", cobrables);
        if (cita == null) {
            return;
        }
        Comprobante comprobante = clinica.generarComprobante(cita);
        System.out.println(comprobante);
    }

    private static void consultarHistorial() {
        Mascota mascota = elegir("Mascotas:", clinica.getMascotas());
        if (mascota == null) {
            return;
        }
        System.out.println("Historial de " + mascota.getNombre() + ":");
        System.out.println(mascota.getHistorial().mostrarHistorial());
    }

    /** Polimorfismo: una List<Mascota> con perros, gatos y aves. Cada una responde distinto */
    private static void listarMascotasYSonidos() {
        List<Mascota> mascotas = clinica.getMascotas();
        if (mascotas.isEmpty()) {
            System.out.println("No hay mascotas registradas :(");
            return;
        }
        for (Mascota mascota : mascotas) {
            System.out.println(mascota.getNombre() + " dice: " + mascota.hacerSonido());
        }
    }

    private static void totalPorVeterinario() {
        List<Veterinario> veterinarios = clinica.getVeterinarios();
        if (veterinarios.isEmpty()) {
            System.out.println("No hay veterinarios registrados");
            return;
        }
        for (Veterinario veterinario : veterinarios) {
            double total = 0;
            List<Comprobante> cobros = clinica.consultarCobrosPorVeterinario(veterinario);
            for (Comprobante comprobante : cobros) {
                total += comprobante.getTotal();
            }
            System.out.printf("%s: %d comprobante(s), total $%.2f%n",
                    veterinario.getNombreCompleto(), cobros.size(), total);
        }
    }

    private static void darDeBajaMascota() {
        Mascota mascota = elegir("Mascotas:", clinica.getMascotas());
        if (mascota == null) {
            return;
        }
        if (leerSiNo("Se borrarán también sus citas, comprobantes e historial. ¿Continuar? (s/n): ")) {
            clinica.darDeBajaMascota(mascota);
            //esta variable local desaparece al terminar el método
            System.out.println("Mascota dada de baja.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // para UTILIDADES

    private static List<Cita> citasConEstado(EstadoCita estado) {
        List<Cita> resultado = new ArrayList<>();
        for (Cita cita : clinica.getCitas()) {
            if (cita.getEstado() == estado) {
                resultado.add(cita);
            }
        }
        return resultado;
    }

    /** Muestra una lista numerada y devuelve el elemento elegido (null si la lista está vacía) */
    private static <T> T elegir(String titulo, List<T> opciones) {
        if (opciones.isEmpty()) {
            System.out.println("Ups! No hay elementos disponibles para esta opción");
            return null;
        }
        System.out.println(titulo);
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + opciones.get(i));
        }
        return opciones.get(leerEntero("Número: ", 1, opciones.size()) - 1);
    }

    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = SC.nextLine().trim();
            if (!linea.isEmpty()) {
                return linea;
            }
            System.out.println("Este dato no puede estar vacío!");
        }
    }

    private static int leerEntero(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = Integer.parseInt(SC.nextLine().trim());
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("Escribe un número entre " + min + " y " + max);
            } catch (NumberFormatException e) {
                System.out.println("Eso no es un número entero válido...");
            }
        }
    }

    private static double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double valor = Double.parseDouble(SC.nextLine().trim());
                if (valor > 0 && !Double.isInfinite(valor)) {
                    return valor;
                }
                System.out.println("El valor debe ser mayor a 0");
            } catch (NumberFormatException e) {
                System.out.println("Eso no es un número válido...");
            }
        }
    }

    //para las respuestas de sí y no
    private static boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String respuesta = SC.nextLine().trim().toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                return true;
            }
            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }
            System.out.println("Responde 's' o 'n'");
        }
    }

    private static LocalDateTime leerFechaHora(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return LocalDateTime.parse(SC.nextLine().trim(), FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Ejemplo: 22/11/2026 10:30");
            }
        }
    }
}