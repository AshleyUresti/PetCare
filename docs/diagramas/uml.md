@startuml
package "modelo.personas" {
    abstract class Persona {
      - nombre: String
      - apellido: String
      - telefono: String
      - correo: String
      # Persona(nombre: String, apellido: String, telefono: String, correo: String)
      + getNombreCompleto(): String
      + setNombre(nombre: String): void
      + setApellido(apellido: String): void
      + setTelefono(telefono: String): void
      + setCorreo(correo: String): void
      + describirRol(): String {abstract}
      + toString(): String
  }
    class Propietario {
      - direccion: String
      + Propietario(nombre: String, apellido: String, telefono: String, correo: String, direccion: String)
      + agregarMascota(mascota: Mascota): void
      + eliminarMascota(mascota: Mascota): void
      + describirRol(): String

  }
    class Veterinario {
      - cedulaProfesional: String
      - especialidad: String
      + Veterinario(nombre: String, apellido: String, telefono: String, correo: String, cedulaProfesional: String, especialidad: String)
      + describirRol(): String
      + toString(): String
  }
    class Recepcionista {
      - turno: String
      + Recepcionista(nombre: String, apellido: String, telefono: String, correo: String, turno: String)
      + describirRol(): String
  }
}

package "modelo.mascotas" {
    abstract class Mascota {
      - nombre: String
      - edad: int
      - peso: double
      # Mascota(nombre: String, edad: int, peso: double, propietario: Propietario)
      + hacerSonido(): String {abstract}
      + setPeso(peso: double): void
      + setEdad(edad: int): void
      + getHistorial(): HistorialClinico
      + toString(): String
 }
    class Perro {
      - raza: String
      - nivelEnergia: String
      + Perro(nombre: String, edad: int, peso: double, propietario: Propietario, raza: String, nivelEnergia: String)
      + hacerSonido(): String
      + toString(): String
 }
    class Gato {
      - esDomestico: boolean
      + Gato(nombre: String, edad: int, peso: double, propietario: Propietario, esDomestico: boolean)
      + hacerSonido(): String
      + toString(): String
 }
    class AveExotica {
      - longitudAlas: double
      + AveExotica(nombre: String, edad: int, peso: double, propietario: Propietario, longitudAlas: double)
      + hacerSonido(): String
      + toString(): String
 }
    class HistorialClinico {
      + HistorialClinico ()
      + registrarConsulta(cita: Cita): void
      + getConsultas(): List<Cita>
      + mostrarHistorial(): String
 }
}
package "modelo.clinica" {
    class Clinica {
      - nombre: String
      - numConsultorios: int
      + Clinica(nombre: String, numConsultorios: int)
      + Clinica()
      + agregarVeterinario(veterinario: Veterinario): void
      + registrarPropietario(propietario: Propietario): void
      + registrarMascota(mascota: Mascota): void
      + agendarCita(cita: Cita): void
      + generarComprobante(cita: Cita): Comprobante
      + getCitas(): List<Cita>
      + consultarCobrosPorMascota(mascota: Mascota): List<Comprobante>
      + consultarCobrosPorVeterinario(veterinario: Veterinario): List<Comprobante>
    }

    class Consultorio {
      - numero: int
      + Consultorio(numero: int)
      + getNumero(): int
      + toString(): String
    }

    enum EstadoCita {
      PROGRAMADA
      ATENDIDA
      CANCELADA
    }

    class Cita {
      - fechaHora: LocalDateTime
      + Cita(mascota: Mascota, veterinario: Veterinario, consultorio: Consultorio, fechaHora: LocalDateTime)
      + agregarServicio(servicio: Servicio): void
      + atender(): void
      + cancelar(): void
      + coincideCon(otraCita: Cita): boolean
      + calcularTotal(): double
      + getEstado(): EstadoCita
      + getFechaHora(): LocalDateTime
      + getMascota(): Mascota
      + getVeterinario(): Veterinario
      + getConsultorio(): Consultorio
      + getServicios(): Servicios
      + toString(): String
    }

    class Comprobante {
      - folio: String
      - total: double
      - fechaEmision: LocalDateTime
      - {static} contadorComprobantes: int
      + Comprobante(cita: Cita)
      + getTotal(): double
      + getMascota(): Mascota
      + getVeterinario(): Veterinario
      + toString(): String
    }
}

package "modelo.servicios" {
  interface Servicio {
      + calcularCosto(): double
      + getDescripcion(): String
 }
  class ConsultaGeneral {
      - tarifaBase: double
      + ConsultaGeneral(tarifaBase: double)
      + calcularCosto(): double
      + getDescripcion(): String
 }
  class Vacunacion {
      - tarifaBase: double
      - nombreVacuna: String
      - costoVacuna: double
      + Vacunacion(tarifaBase: double, nombreVacuna: String, costoVacuna: double)
      + calcularCosto(): double
      + getDescripcion(): String
 }
  class Cirugia {
      - tarifaPorHora: double
      - horasQuirofano: int
      - complejidad: int
      + Cirugia(tarifaPorHora: double, horasQuirofano: int, complejidad: int)
      + calcularCosto(): double
      + getDescripcion(): String
      + setHorasQuirofano(horasQuirofano: int): void
      + setComplejidad(complejidad: int): void
 }
}

'Herencia
Persona <|-- Propietario
Persona <|-- Veterinario
Persona <|-- Recepcionista

Mascota <|-- Perro
Mascota <|-- Gato
Mascota <|-- AveExotica

' Realización de interfaz
Servicio <|.. ConsultaGeneral
Servicio <|.. Vacunacion
Servicio <|.. Cirugia

' Mascota / Propietario / Historial
Propietario "1" -- "*" Mascota : posee >
Mascota "1" *-- "1" HistorialClinico : tiene >
HistorialClinico "1" --> "*" Cita : registra >

' Clinica: Composición
Clinica "1" *-- "1..*" Consultorio : crea >
Clinica "1" *-- "*" Comprobante : emite >

' Agregación: recibe objetos ya creados
Clinica "1" o-- "*" Veterinario : guarda >
Clinica "1" o-- "*" Propietario : registra >
Clinica "1" o-- "*" Mascota : registra >
Clinica "1" o-- "*" Cita : agenda >

' Cita (asociaciones)
Cita "*" --> "1" Mascota : es para >
Cita "*" --> "1" Veterinario : atiende >
Cita "*" --> "1" Consultorio : se realiza en >
Cita "1" --> "1..*" Servicio : incluye >
Cita "1" --> "1" EstadoCita : tiene >

' Comprobante
Comprobante "1" --> "1" Cita : cobra >


@enduml