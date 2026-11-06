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
                finalizarInscripcion = comprobarCierreInscripcion();
            } else if (entrada.isEmpty()) {
                System.out.println("El nombre del equipo no puede estar vacío.");
            } else {
                int jugCount = datosEquipo(entrada, idEquipo);
                datosEquipos += idEquipo + ":" + entrada + ":" + jugCount + ":0;";
                System.out.println("-> Equipo '" + entrada + "' registrado correctamente con ID: " + idEquipo);

                contadorEquipos++;
                totalEquiposRegistrados++;
            }
        } while (!finalizarInscripcion);
    }

    public static boolean comprobarCierreInscripcion() {
        if (totalEquiposRegistrados == 0) {
            System.out.println("No has registrado ningún equipo todavía.");
            return true;
        } else if (totalEquiposRegistrados % 2 != 0) {
            System.out.println("¡ATENCIÓN! Has registrado " + totalEquiposRegistrados + " equipo(s) (número IMPAR).");
            System.out.println("La competición exige un número PAR de equipos. Debes registrar al menos 1 más.");
            return false;
        } else {
            System.out.println("Inscripción finalizada correctamente. Total de equipos: " + totalEquiposRegistrados);
            return true;
        }
    }

    public static int datosEquipo(String nombreEquipo, String idEquipo) {
        LocalDate fechaFundacion = pedirFecha("Ingrese la fecha de fundación del equipo (dd/MM/yyyy): ");
        int cantidadJugadores = pedirCantidadJugadores(nombreEquipo);

        datosEntrenador();
        datosJugadores(cantidadJugadores);

        return cantidadJugadores;
    }

    public static int pedirCantidadJugadores(String nombreEquipo) {
        int cantidad = 0;
        boolean valida = false;
        do {
            try {
                System.out.print("Cuantos jugadores tiene el equipo '" + nombreEquipo + "'? (mínimo 2, máximo 6): ");
                cantidad = Integer.parseInt(sc.nextLine().trim());
                valida = validarCantidadJugadores(cantidad);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
            }
        } while (!valida);
        return cantidad;
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
        boolean equipoValido = false;

        do {
            boolean tieneCapitan = false;
            int contadorTitu = 0;
            int contadorDer = 0;
            int contadorIzq = 0;

            String posCap = "";
            String posTitu1 = "";
            String posTitu2 = "";

            for (int i = 1; i <= cantidadJugadores; i++) {
                System.out.println("\n -> Datos del Jugador " + i + " de " + cantidadJugadores + ":");
                datosGlobales();

                System.out.print("    Nickname: ");
                String nickname = sc.nextLine().trim();

                String rol = pedirRolConControl(tieneCapitan, contadorTitu, i, cantidadJugadores);
                if (rol.equalsIgnoreCase("Cap")) tieneCapitan = true;
                else if (rol.equalsIgnoreCase("Titu")) contadorTitu++;

                String posicion = pedirPosicionConControl(contadorDer, contadorIzq, cantidadJugadores);
                if (posicion.equals("der")) contadorDer++;
                else contadorIzq++;

                if (rol.equalsIgnoreCase("Cap")) posCap = posicion;
                else if (rol.equalsIgnoreCase("Titu")) {
                    if (posTitu1.isEmpty()) posTitu1 = posicion;
                    else posTitu2 = posicion;
                }

                System.out.println("    -> Jugador [" + nickname + "] (" + rol + " - " + posicion + ") registrado.");
            }

            equipoValido = validarReglasPlantilla(tieneCapitan, contadorTitu, posCap, posTitu1, posTitu2);

        } while (!equipoValido);
    }

    public static boolean validarReglasPlantilla(boolean tieneCapitan, int contadorTitu, String posCap, String posTitu1, String posTitu2) {
        if (!tieneCapitan) {
            System.out.println("\n[ERROR] El equipo debe tener obligatoriamente 1 Capitán. Reiniciando...");
            return false;
        }
        if (contadorTitu == 0) {
            System.out.println("\n[ERROR] Debe haber al menos 1 Titular (Titu). Reiniciando...");
            return false;
        }
        if (contadorTitu == 1 && posCap.equals(posTitu1)) {
            System.out.println("\n[ERROR] El Cap (" + posCap + ") y el único Titu (" + posTitu1 + ") son del mismo lado. Añada un Titu al lado contrario. Reiniciando...");
            return false;
        }
        if (contadorTitu == 2 && posTitu1.equals(posTitu2)) {
            System.out.println("\n[ERROR] Los dos Titulares juegan en el mismo lado (" + posTitu1 + "). Reiniciando...");
            return false;
        }
        return true;
    }

    public static String pedirRolConControl(boolean tieneCapitan, int contadorTitu, int numJugadorActual, int totalJugadores) {
        String rol;
        boolean valido = false;
        do {
            System.out.print("    Rol (Cap / Titu / Sup): ");
            rol = sc.nextLine().trim();

            if (rol.equalsIgnoreCase("Cap")) {
                if (tieneCapitan) System.out.println("    Error: Solo 1 Capitán por equipo.");
                else valido = true;
            } else if (rol.equalsIgnoreCase("Titu")) {
                if (contadorTitu >= 2) System.out.println("    Error: Límite máximo de Titulares (2).");
                else valido = true;
            } else if (rol.equalsIgnoreCase("Sup")) {
                valido = true;
            } else {
                System.out.println("    Error: Rol no válido.");
            }

            if (numJugadorActual == totalJugadores && !tieneCapitan && !rol.equalsIgnoreCase("Cap")) {
                System.out.println("    Error: Es el último jugador y el equipo AÚN NO TIENE Capitán.");
                valido = false;
            }
        } while (!valido);
        return rol;
    }

    public static String pedirPosicionConControl(int contadorDer, int contadorIzq, int totalJugadores) {
        String posicion;
        int maxPorPosicion = (totalJugadores + 1) / 2;
        boolean valida = false;

        do {
            System.out.print("    Posición (der / izq): ");
            posicion = sc.nextLine().trim().toLowerCase();

            if (!posicion.equals("der") && !posicion.equals("izq")) {
                System.out.println("    Error: Posición no válida.");
            } else if (posicion.equals("der") && contadorDer >= maxPorPosicion) {
                System.out.println("    Error: Límite de jugadores en 'der' alcanzado (" + maxPorPosicion + ").");
            } else if (posicion.equals("izq") && contadorIzq >= maxPorPosicion) {
                System.out.println("    Error: Límite de jugadores en 'izq' alcanzado (" + maxPorPosicion + ").");
            } else {
                valida = true;
            }
        } while (!valida);
        return posicion;
    }

    public static String pedirTextoValido(String mensaje) {
        String regex = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s\\-]+$";
        String entrada = "";
        boolean valido = false;

        do {
            System.out.print(mensaje);
            entrada = sc.nextLine().trim();

            if (entrada.matches(regex)) {
                valido = true;
            } else {
                System.out.println("    Error: Entrada no válida. Solo se permiten letras, espacios y guiones (-).");
            }
        } while (!valido);

        return entrada;
    }

    public static String datosGlobales() {
        String nombre = pedirTextoValido("    Nombre: ");
        String apellido = pedirTextoValido("    Apellido: ");
        String nacionalidad = pedirTextoValido("    Nacionalidad: ");

        pedirFecha("    Fecha de nacimiento (dd/MM/yyyy): ");
        pedirSueldo("    Sueldo mensual (€): ");

        return nombre + " " + apellido;
    }

    public static double pedirSueldo(String mensaje) {
        double sueldo = 0.0;
        boolean valido = false;
        do {
            try {
                System.out.print(mensaje);
                sueldo = Double.parseDouble(sc.nextLine().trim());
                if (sueldo <= SMI) throw new Exception("Debe ser mayor al SMI (" + SMI + "€).");
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("    Error: Cifra decimal válida requerida.");
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
                fecha = LocalDate.parse(sc.nextLine().trim(), formatoFecha);
                if (fecha.isAfter(LocalDate.now())) throw new Exception("La fecha no puede ser futura.");
                valida = true;
            } catch (DateTimeParseException e) {
                System.out.println("Error: Formato incorrecto. Use dd/MM/yyyy.");
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

        if (inscripcionesCerradas || totalEquiposRegistrados < 2 || totalEquiposRegistrados % 2 != 0) {
            System.out.println("Error: Calendario ya generado o faltan equipos pares (Mínimo 2).");
            return;
        }

        System.out.print("Ingrese el nombre de la Competición: ");
        String nombreCompeticion = sc.nextLine().trim();
        inscripcionesCerradas = true;

        int totalJornadas = totalEquiposRegistrados - 1;
        LocalDate fechaActual = LocalDate.now();

        System.out.println("-> LA ETAPA DE INSCRIPCIONES HA QUEDADO CERRADA.");
        System.out.println("\n--- DATOS DE LA COMPETICIÓN ---");
        System.out.println("Nombre: " + nombreCompeticion);
        System.out.println("Fecha inicio: " + fechaActual.format(formatoFecha));
        System.out.println("-------------------------------\n");

        generarJornadas(totalJornadas, fechaActual);
        mostrarClasificacionGeneral();
    }

    public static void generarJornadas(int totalJornadas, LocalDate fechaActual) {
        String rotacion = "";
        for (int i = 0; i < totalEquiposRegistrados; i++) {
            rotacion += extraerElemento(extraerElemento(datosEquipos, i, ';'), 0, ':') + ";";
        }

        int partidosPorJornada = totalEquiposRegistrados / 2;
        int idEnfrentamiento = 1;

        for (int j = 0; j < totalJornadas; j++) {
            System.out.println("JORNADA " + (j + 1) + " | Fecha: " + fechaActual.format(formatoFecha));

            for (int i = 0; i < partidosPorJornada; i++) {
                String idLocal = extraerElemento(rotacion, i, ';');
                String idVisitante = extraerElemento(rotacion, totalEquiposRegistrados - 1 - i, ';');

                System.out.print("  [enf" + idEnfrentamiento++ + "] " + obtenerNombrePorId(idLocal) + " vs " + obtenerNombrePorId(idVisitante) + " -> ");
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
    }

    public static void simularPartidoPingPong(String idLocal, String idVisitante) {
        Random rand = new Random();
        int setsLocal = 0, setsVisitante = 0;
        String detalleSets = "";

        for (int i = 1; i <= 3; i++) {
            int puntosLocal, puntosVisitante;
            if (rand.nextBoolean()) {
                puntosLocal = (rand.nextInt(10) > 2) ? 11 : rand.nextInt(10) + 12;
                puntosVisitante = (puntosLocal == 11) ? rand.nextInt(10) : puntosLocal - 2;
                setsLocal++;
            } else {
                puntosVisitante = (rand.nextInt(10) > 2) ? 11 : rand.nextInt(10) + 12;
                puntosLocal = (puntosVisitante == 11) ? rand.nextInt(10) : puntosVisitante - 2;
                setsVisitante++;
            }
            detalleSets += "(" + puntosLocal + "-" + puntosVisitante + ") ";
            if (setsLocal == 2 || setsVisitante == 2) break;
        }

        System.out.println("Sets: " + detalleSets.trim() + " | FINAL: " + setsLocal + "-" + setsVisitante);
        actualizarPuntos((setsLocal > setsVisitante) ? idLocal : idVisitante, 2);
    }

    public static void actualizarPuntos(String idGanador, int puntosSumar) {
        StringBuilder nuevosDatos = new StringBuilder();
        for (int i = 0; i < totalEquiposRegistrados; i++) {
            String eqStr = extraerElemento(datosEquipos, i, ';');
            if (extraerElemento(eqStr, 0, ':').equals(idGanador)) {
                int puntos = Integer.parseInt(extraerElemento(eqStr, 3, ':')) + puntosSumar;
                nuevosDatos.append(extraerElemento(eqStr, 0, ':')).append(":")
                        .append(extraerElemento(eqStr, 1, ':')).append(":")
                        .append(extraerElemento(eqStr, 2, ':')).append(":")
                        .append(puntos).append(";");
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

            String datosMejor = obtenerMejorEquipo(equiposRestantes, totalEquiposRegistrados - i);
            String idMejor = extraerElemento(datosMejor, 0, ':');
            String nombreMejor = extraerElemento(datosMejor, 1, ':');
            String puntosMejor = extraerElemento(datosMejor, 2, ':');

            System.out.printf("%-6d | %-20s | %-7s%n", (i + 1), nombreMejor, puntosMejor);

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
    }

    public static String obtenerMejorEquipo(String equiposRestantes, int cantidad) {
        int maxPuntos = -1;
        String idMejor = "", nombreMejor = "";

        for (int j = 0; j < cantidad; j++) {
            String eqStr = extraerElemento(equiposRestantes, j, ';');
            int puntos = Integer.parseInt(extraerElemento(eqStr, 3, ':'));

            if (puntos > maxPuntos) {
                maxPuntos = puntos;
                idMejor = extraerElemento(eqStr, 0, ':');
                nombreMejor = extraerElemento(eqStr, 1, ':');
            }
        }
        return idMejor + ":" + nombreMejor + ":" + maxPuntos;
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
            if (extraerElemento(eqStr, 0, ':').equals(idBuscado)) return extraerElemento(eqStr, 1, ':');
        }
        return "Desconocido";
    }
}