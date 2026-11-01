## Clases del sistema

| Clase | Tipo | Atributos principales | Responsabilidad |  Justificación
|---|---|---|---|---
| `Persona` | Abstracta | nombre, apellido, telefono, correo | `getNombreCompleto()`, getters/setters con validación, `describirRol()` (abstracto), `toString()` | Reúne los datos comunes de dueños, veterinarios y recepcionistas para no repetirlos. Es abstracta porque no existe una persona genérica en la clínica.
| `Propietario` | Concreta | direccion, mascotas (lista) | `agregarMascota(Mascota)`, `eliminarMascota(Mascota)`, `describirRol()` | Representa al dueño y su dato propio (dirección). Conoce a sus mascotas, que existen por sí mismas.
| `Veterinario` | Concreta | cedulaProfesional, especialidad | `describirRol()`, `toString()` | Profesional que atiende citas. Tiene cédula y especialidad, datos que no aplican a otros roles.
| `Recepcionista` | Concreta | turno | `describirRol()` | Personal administrativo con un dato propio (turno). Completa la jerarquía de personas.
| `Mascota` | Abstracta | nombre, edad, peso, propietario, historial | `hacerSonido()` (abstracto), `setPeso()/setEdad()` (validan > 0), `getHistorial()`, `toString()` | Agrupa lo común de todas las mascotas y crea su propio historial (composición). Es abstracta porque cada animal emite un sonido distinto.
| `Perro` | Concreta | raza, nivelEnergia | `hacerSonido()`, `toString()` | Especialización con datos propios. Implementa el método abstracto.
| `Gato` | Concreta | esDomestico | `hacerSonido()`, `toString()` | Especialización con su dato propio. Sonido distinto al del perro.
| `AveExotica` | Concreta | longitudAlas | `hacerSonido()`, `toString()` | Tercer tipo exigido. Demuestra el polimorfismo en `List<Mascota>`
| `HistorialClinico` | Concreta | consultas (lista de citas atendidas) | `registrarConsulta(Cita), getConsultas(), mostrarHistorial()` | No tiene sentido sin la mascota, ya que la mascota lo crea y si se elimina, se elimina con ella (composición).
| `Consultorio` | Concreta | `numerogetNumero(), toString()` | Espacio físico numerado. Lo crea la clínica y desaparece con ella (composición). |
| `EstadoCita` | Enumeración | PROGRAMADA, ATENDIDA, CANCELADA | Estados válidos de una cita (constantes) | Evita usar String para el estado.
| `Cita` | Concreta | mascota, veterinario, consultorio, servicio, fechaHora, estado | `atender(), cancelar(), coincideCon(Cita), calcularCosto()`, getters | Une objetos independientes mediante asociación. Esta clase controla su propio estado.
| `Servicio` | Interfaz | — | `calcularCosto(), getDescripcion()` | Contrato común de los servicios que se pueden cobrar. Permite calcular costos sin `instanceof` (polimorfismo).
| `ConsultaGeneral` | Concreta | tarifaBase | `calcularCosto(), getDescripcion()` | Implementa `Servicio` y el costo es la tarifa base.
| `Vacunacion` | Concreta | nombreVacuna, costoVacuna | `calcularCosto(), getDescripcion()` | Implementa `Servicio` se calcula con la tarifa base más el costo de la vacuna.
| `Cirugia` | Concreta | complejidad, horasQuirofano | `calcularCosto(), getDescripcion()` | Implementa `Servicio` y el costo depende de la complejidad y de las horas de quirófano.
| `Comprobante` | Concreta | folio, cita, total, fechaEmision, `static` contadorComprobantes | Registra el cobro de una cita atendida: `generar(Cita), getTotal(), getMascota(), getVeterinario(), toString()` | Registro del cobro de una cita atendida. Permite consultar cobros por mascota y por veterinario. El contador estático asigna el folio.
| `Clinica` | Concreta | nombre, consultorios, veterinarios, mascotas, propietarios, citas, comprobantes | `Clinica(), Clinica(nombre: String, numConsultorios: int), agregarVeterinario(veterinario: Veterinario): void, registrarPropietario(propietario: Propietario): void, registrarMascota(mascota: Mascota): void`, etc | Clase coordinadora. Crea sus consultorios (composición), recibe veterinarios ya creados (agregación).

### 3 Objetos de ejemplo
| Objeto | Clase | Valores
|---|---|---
kaiser | Perro | nombre = "Kaiser", edad = 4, peso = 17.5, propietario = mein, raza = "Labrador", nivelEnergia = "Alto"
mein | Propietario | nombre = "Mein", apellido = "Crosses", telefono = 921676767, correo = "mein@gmail.com", direccion = "Av. Universidad km 7.5"
cita01 | Cita | mascota = kaiser, veterinario = drLopez, consultorio = Consultorio 1, servicio = Vacunacion("Rabia"), fechaHora = 01-10-2026 4:00, estado = PROGRAMADA

### Clases candidatas descartadas
La clase `Direccion`, ya que solo es un dato de texto del propietario sin comportamiento propio, así que basta un String en Propietario. Y la clase `Vacuna` porque su único dato relevante es el costo, que ya vive en Vacunacion (nombreVacuna y costoVacuna).

