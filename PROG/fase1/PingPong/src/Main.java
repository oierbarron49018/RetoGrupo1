import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner sc = new Scanner(System.in);

    public static final double SMI = 1300.0;
    public static int contadorEquipos = 1;
    public static int contadorEntrenadores = 1;
    public static int totalEquiposRegistrados = 0;

    public static boolean inscripcionesCerradas = false;
    public static String datosEquipos = "";

    public static DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        boolean salir = false;
        do {
            mostrarMenu();
            String opcion = sc.nextLine().trim().toLowerCase();

            switch (opcion) {
                case "a" -> crearEquipos();
                case "b" -> crearCalendario();
                case "d" -> {
                    System.out.println("Programa finalizado correctamente");
                    salir = true;
                }
                default -> System.out.println("Opción no válida");
            }
        } while (!salir);
    }

    public static void mostrarMenu() {
        System.out.println("\nCOMPETICION DE PING PONG :");
        System.out.println("a) Crear equipos");
        System.out.println("b) Crear calendario");
        System.out.println("d) Salir");
        System.out.print("Seleccione una opción: ");
    }

    public static void crearEquipos() {
        if (inscripcionesCerradas) {
            System.out.println("\n¡ERROR! La etapa de inscripción está CERRADA. El calendario ya fue generado.");
            return;
        }

        boolean finalizarInscripcion = false;

        do {
            String idEquipo = "eq" + contadorEquipos;

            System.out.println("\nRegistrando equipo con ID: " + idEquipo);
            System.out.print("Ingrese el nombre del equipo (o escriba 'salir' para finalizar): ");
            String entrada = sc.nextLine().trim();

            if (entrada.equalsIgnoreCase("salir")) {

                if (totalEquiposRegistrados == 0) {
                    System.out.println("No has registrado ningún equipo todavía.");
                    finalizarInscripcion = true;
                } else if (totalEquiposRegistrados % 2 != 0) {
                    System.out.println("¡ATENCIÓN! Has registrado " + totalEquiposRegistrados + " equipo(s) (número IMPAR).");
                    System.out.println("La competición exige un número PAR de equipos.");
                    System.out.println("Debes registrar al menos 1 equipo más para poder terminar.");
                } else {
                    System.out.println("Inscripción finalizada correctamente. Total de equipos: " + totalEquiposRegistrados);
                    finalizarInscripcion = true;
                }

            } else if (entrada.isEmpty()) {
                System.out.println("El nombre del equipo no puede estar vacío.");
            } else {
                int jugCount = datosEquipo(entrada, idEquipo);

                // Guardamos los datos en el String
                datosEquipos += idEquipo + ":" + entrada + ":" + jugCount + ":0;";

                System.out.println("-> Equipo '" + entrada + "' registrado correctamente con ID: " + idEquipo);

                contadorEquipos++;
                totalEquiposRegistrados++;
            }

        } while (!finalizarInscripcion);
    }

    public static int datosEquipo(String nombreEquipo, String idEquipo) {
        LocalDate fechaFundacion = pedirFecha("Ingrese la fecha de fundación del equipo (dd/MM/yyyy): ");

        int cantidadJugadores = 0;
        boolean cantidadValida = false;

        do {
            try {
                System.out.print("Cuantos jugadores tiene el equipo '" + nombreEquipo + "'? (mínimo 2, máximo 6): ");
                cantidadJugadores = sc.nextInt();
                sc.nextLine();

                cantidadValida = validarCantidadJugadores(cantidadJugadores);

            } catch (Exception e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                sc.nextLine();
            }
        } while (!cantidadValida);

        datosEntrenador();
        datosJugadores(cantidadJugadores);

        return cantidadJugadores;
    }

    public static boolean validarCantidadJugadores(int cantidadJugadores) {
        if (cantidadJugadores < 2 || cantidadJugadores > 6) {
            System.out.println("Error: La cantidad de jugadores debe estar entre 2 y 6.");
            return false;
        }
        return true;
    }

    public static void datosEntrenador() {
        System.out.println("\n--- DATOS DEL ENTRENADOR ---");

        String idEntrenador = "ent" + contadorEntrenadores;
        System.out.println("ID del Entrenador asignado: " + idEntrenador);

        String nombreCompleto = datosGlobales();

        System.out.println("-> Entrenador " + nombreCompleto + " (ID: " + idEntrenador + ") registrado.");

        contadorEntrenadores++;
    }

    public static void datosJugadores(int cantidadJugadores) {
        for (int i = 1; i <= cantidadJugadores; i++) {
            System.out.println("\n -> Datos del Jugador " + i + " de " + cantidadJugadores + ":");

            datosGlobales();

            System.out.print("    Nickname: ");
            String nickname = sc.nextLine().trim();

            String rol = pedirRol();
            String posicion = pedirPosicion();

            System.out.println("    -> Jugador [" + nickname + "] (" + rol + " - " + posicion + ") registrado.");
        }
    }

    public static String datosGlobales() {
        System.out.print("    Nombre: ");
        String nombre = sc.nextLine().trim();

        System.out.print("    Apellido: ");
        String apellido = sc.nextLine().trim();

        System.out.print("    Nacionalidad: ");
        String nacionalidad = sc.nextLine().trim();

        LocalDate fechaNac = pedirFecha("    Fecha de nacimiento (dd/MM/yyyy): ");
        double sueldo = pedirSueldo("    Sueldo mensual (€): ");

        return nombre + " " + apellido;
    }

    public static String pedirRol() {
        String rol = "";
        boolean valido = false;
        do {
            System.out.print("    Rol (Cap / Titu / Sup): ");
            rol = sc.nextLine().trim();

            if (rol.equalsIgnoreCase("Cap") || rol.equalsIgnoreCase("Titu") || rol.equalsIgnoreCase("Sup")) {
                valido = true;
            } else {
                System.out.println("    Error: Rol no válido. Debe ser 'Cap', 'Titu' o 'Sup'.");
            }
        } while (!valido);
        return rol;
    }

    public static String pedirPosicion() {
        String posicion = "";
        boolean valida = false;
        do {
            System.out.print("    Posición (der / izq): ");
            posicion = sc.nextLine().trim().toLowerCase();

            if (posicion.equals("der") || posicion.equals("izq")) {
                valida = true;
            } else {
                System.out.println("    Error: Posición no válida. Debe ser 'der' o 'izq'.");
            }
        } while (!valida);
        return posicion;
    }

    public static double pedirSueldo(String mensaje) {
        double sueldo = 0.0;
        boolean valido = false;
        do {
            try {
                System.out.print(mensaje);
                sueldo = Double.parseDouble(sc.nextLine().trim());

                if (sueldo <= SMI) {
                    throw new Exception("El salario debe ser estrictamente superior al SMI (" + SMI + "€).");
                }
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("    Error: Debe ingresar una cifra decimal válida.");
            } catch (Exception e) {
                System.out.println("    Error: " + e.getMessage());
            }
        } while (!valido);
        return sueldo;
    }

    public static LocalDate pedirFecha(String mensaje) {
        LocalDate fecha = null;
        boolean valida = false;

        while (!valida) {
            try {
                System.out.print(mensaje);
                String entrada = sc.nextLine().trim();
                fecha = LocalDate.parse(entrada, formatoFecha);

                if (fecha.isAfter(LocalDate.now())) {
                    throw new Exception("La fecha no puede ser futura. Por favor, introduzca una fecha válida.");
                }

                valida = true;
            } catch (DateTimeParseException e) {
                System.out.println("Error: Formato de fecha incorrecto. Use dd/MM/yyyy (ej. 15/04/2000).");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return fecha;
    }

    public static void crearCalendario() {
        System.out.println("\n==================================================");
        System.out.println("         GENERACIÓN DE CALENDARIO DE LIGA         ");
        System.out.println("==================================================");

        if (inscripcionesCerradas) {
            System.out.println("Error: El calendario ya ha sido generado previamente.");
            return;
        }
        if (totalEquiposRegistrados < 2 || totalEquiposRegistrados % 2 != 0) {
            System.out.println("Error: Se requiere un número PAR de equipos (mínimo 2). Equipos actuales: " + totalEquiposRegistrados);
            return;
        }

        for (int i = 0; i < totalEquiposRegistrados; i++) {
            String eqStr = extraerElemento(datosEquipos, i, ';');
            int numJug = Integer.parseInt(extraerElemento(eqStr, 2, ':'));
            if (numJug < 2) {
                System.out.println("Error: El equipo '" + extraerElemento(eqStr, 1, ':') + "' tiene menos de 2 jugadores. Completa las plantillas.");
                return;
            }
        }

        System.out.print("Ingrese el nombre de la Competición: ");
        String nombreCompeticion = sc.nextLine().trim();

        inscripcionesCerradas = true;
        System.out.println("-> LA ETAPA DE INSCRIPCIONES HA QUEDADO OFICIALMENTE CERRADA.");

        int totalJornadas = totalEquiposRegistrados - 1;
        int partidosPorJornada = totalEquiposRegistrados / 2;

        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaFin = fechaActual.plusWeeks(totalJornadas - 1);

        System.out.println("\n--- DATOS DE LA COMPETICIÓN ---");
        System.out.println("Nombre: " + nombreCompeticion);
        System.out.println("Fecha de inicio: " + fechaActual.format(formatoFecha));
        System.out.println("Fecha de fin: " + fechaFin.format(formatoFecha));
        System.out.println("-------------------------------\n");

        String rotacion = "";
        for (int i = 0; i < totalEquiposRegistrados; i++) {
            rotacion += extraerElemento(extraerElemento(datosEquipos, i, ';'), 0, ':') + ";";
        }

        int idEnfrentamiento = 1;

        for (int j = 0; j < totalJornadas; j++) {
            System.out.println("JORNADA " + (j + 1) + " | Fecha: " + fechaActual.format(formatoFecha));

            for (int i = 0; i < partidosPorJornada; i++) {
                String idLocal = extraerElemento(rotacion, i, ';');
                String idVisitante = extraerElemento(rotacion, totalEquiposRegistrados - 1 - i, ';');

                String nomLocal = obtenerNombrePorId(idLocal);
                String nomVisitante = obtenerNombrePorId(idVisitante);

                System.out.print("  [enf" + idEnfrentamiento++ + "] " + nomLocal + " vs " + nomVisitante + " -> ");
                simularPartidoPingPong(idLocal, idVisitante);
            }
            System.out.println();

            fechaActual = fechaActual.plusWeeks(1);

            String primerId = extraerElemento(rotacion, 0, ';');
            String ultimoId = extraerElemento(rotacion, totalEquiposRegistrados - 1, ';');

            String nuevaRotacion = primerId + ";" + ultimoId + ";";
            for (int k = 1; k < totalEquiposRegistrados - 1; k++) {
                nuevaRotacion += extraerElemento(rotacion, k, ';') + ";";
            }
            rotacion = nuevaRotacion;
        }

        mostrarClasificacionGeneral();
    }

    public static void simularPartidoPingPong(String idLocal, String idVisitante) {
        Random rand = new Random();
        int setsLocal = 0;
        int setsVisitante = 0;
        String detalleSets = "";

        for (int i = 1; i <= 3; i++) {
            int puntosLocal;
            int puntosVisitante;
            boolean ganaLocal = rand.nextBoolean();

            if (ganaLocal) {
                if (rand.nextInt(10) > 2) {
                    puntosLocal = 11;
                    puntosVisitante = rand.nextInt(10);
                } else {
                    puntosVisitante = 10 + rand.nextInt(3);
                    puntosLocal = puntosVisitante + 2;
                }
                setsLocal++;
            } else {
                if (rand.nextInt(10) > 2) {
                    puntosVisitante = 11;
                    puntosLocal = rand.nextInt(10);
                } else {
                    puntosLocal = 10 + rand.nextInt(3);
                    puntosVisitante = puntosLocal + 2;
                }
                setsVisitante++;
            }

            detalleSets += "(" + puntosLocal + "-" + puntosVisitante + ") ";

            if (setsLocal == 2 || setsVisitante == 2) {
                break;
            }
        }

        System.out.println("Sets: " + detalleSets.trim() + " | RESULTADO FINAL: " + setsLocal + "-" + setsVisitante);

        if (setsLocal > setsVisitante) {
            actualizarPuntos(idLocal, 2);
        } else {
            actualizarPuntos(idVisitante, 2);
        }
    }

    public static void actualizarPuntos(String idGanador, int puntosSumar) {
        StringBuilder nuevosDatos = new StringBuilder();

        for (int i = 0; i < totalEquiposRegistrados; i++) {
            String eqStr = extraerElemento(datosEquipos, i, ';');
            String id = extraerElemento(eqStr, 0, ':');

            if (id.equals(idGanador)) {
                String nombre = extraerElemento(eqStr, 1, ':');
                String jug = extraerElemento(eqStr, 2, ':');
                int puntos = Integer.parseInt(extraerElemento(eqStr, 3, ':')) + puntosSumar;

                nuevosDatos.append(id).append(":").append(nombre).append(":").append(jug).append(":").append(puntos).append(";");
            } else {
                nuevosDatos.append(eqStr).append(";");
            }
        }
        datosEquipos = nuevosDatos.toString();
    }

    public static void mostrarClasificacionGeneral() {
        System.out.println("\n==================================================");
        System.out.println("         CLASIFICACIÓN GENERAL FINAL              ");
        System.out.println("==================================================");
        System.out.printf("%-6s | %-20s | %-7s%n", "Pos", "Equipo", "Puntos");
        System.out.println("--------------------------------------------------");

        String equiposRestantes = datosEquipos;

        for (int i = 0; i < totalEquiposRegistrados; i++) {
            int maxPuntos = -1;
            String idMejor = "";
            String nombreMejor = "";

            for (int j = 0; j < totalEquiposRegistrados - i; j++) {
                String eqStr = extraerElemento(equiposRestantes, j, ';');
                int puntos = Integer.parseInt(extraerElemento(eqStr, 3, ':'));

                if (puntos > maxPuntos) {
                    maxPuntos = puntos;
                    idMejor = extraerElemento(eqStr, 0, ':');
                    nombreMejor = extraerElemento(eqStr, 1, ':');
                }
            }

            System.out.printf("%-6d | %-20s | %-7d%n", (i + 1), nombreMejor, maxPuntos);

            StringBuilder temp = new StringBuilder();
            boolean borrado = false;
            for (int j = 0; j < totalEquiposRegistrados - i; j++) {
                String eqStr = extraerElemento(equiposRestantes, j, ';');
                if (!borrado && extraerElemento(eqStr, 0, ':').equals(idMejor)) {
                    borrado = true;
                } else {
                    temp.append(eqStr).append(";");
                }
            }
            equiposRestantes = temp.toString();
        }
        System.out.println("==================================================\n");
    }

    public static String extraerElemento(String cadena, int posicion, char separador) {
        int indexInicio = 0;
        for (int i = 0; i < posicion; i++) {
            indexInicio = cadena.indexOf(separador, indexInicio) + 1;
            if (indexInicio == 0) return "";
        }
        int indexFin = cadena.indexOf(separador, indexInicio);
        if (indexFin == -1) indexFin = cadena.length();

        return cadena.substring(indexInicio, indexFin);
    }

    public static String obtenerNombrePorId(String idBuscado) {
        for (int i = 0; i < totalEquiposRegistrados; i++) {
            String eqStr = extraerElemento(datosEquipos, i, ';');
            if (extraerElemento(eqStr, 0, ':').equals(idBuscado)) {
                return extraerElemento(eqStr, 1, ':');
            }
        }
        return "Desconocido";
    }
}