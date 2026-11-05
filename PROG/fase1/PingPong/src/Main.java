import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static Scanner sc = new Scanner(System.in);

    public static final double SMI = 1300.0;
    public static int contadorEquipos = 1;
    public static int contadorEntrenadores = 1;
    public static int totalEquiposRegistrados = 0;
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
        System.out.println("Seleccione una opción: ");
    }

    public static void crearEquipos() {
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
                datosEquipo(entrada, idEquipo);

                System.out.println("-> Equipo '" + entrada + "' registrado correctamente con ID: " + idEquipo);

                contadorEquipos++;
                totalEquiposRegistrados++;
            }

        } while (!finalizarInscripcion);
    }


    public static void datosEquipo(String nombreEquipo, String idEquipo) {
        LocalDate fechaFundacion = pedirFecha("Ingrese la fecha de fundación del equipo (dd/MM/yyyy): ");

        int cantidadJugadores = 0;
        boolean cantidadValida = false;

        do {
            try {
                System.out.println("Cuantos jugadores tiene el equipo '" + nombreEquipo + "'? (mínimo 2, máximo 6): ");
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
    }

    public static boolean validarCantidadJugadores(int cantidadJugadores) {
        if (cantidadJugadores < 2 || cantidadJugadores > 6) {
            System.out.println("Error: La cantidad de jugadores debe estar entre 2 y 6.");
            return false;
        }
        return true;
    }

    public static void datosEntrenador() {
        System.out.println("\nDATOS DEL ENTRENADOR");

        String idEntrenador = "ent" + contadorEntrenadores;
        System.out.println("ID del Entrenador asignado: " + idEntrenador);

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        System.out.print("Apellido: ");
        String apellido = sc.nextLine().trim();

        System.out.print("Nacionalidad: ");
        String nacionalidad = sc.nextLine().trim();

        LocalDate fechaNacimiento = pedirFecha("Fecha de nacimiento (dd/MM/yyyy): ");
        double sueldo = pedirSueldo("Sueldo mensual del entrenador (€): ");

        System.out.println("-> Entrenador " + nombre + " " + apellido + " (ID: " + idEntrenador + ") registrado.");

        contadorEntrenadores++;
    }

    public static void datosJugadores(int cantidadJugadores) {
        for (int i = 1; i <= cantidadJugadores; i++) {
            System.out.println("\n -> Datos del Jugador " + i + " de " + cantidadJugadores + ":");

            System.out.print("    Nombre: ");
            String nombre = sc.nextLine().trim();

            System.out.print("    Apellido: ");
            String apellido = sc.nextLine().trim();

            System.out.print("    Nacionalidad: ");
            String nacionalidad = sc.nextLine().trim();

            LocalDate fechaNac = pedirFecha("    Fecha de nacimiento (dd/MM/yyyy): ");

            System.out.print("    Nickname: ");
            String nickname = sc.nextLine().trim();

            String rol = pedirRol();

            String posicion = pedirPosicion();

            double sueldo = pedirSueldo("    Sueldo mensual (€): ");

            System.out.println("    -> Jugador [" + nickname + "] (" + rol + " - " + posicion + ") registrado.");
        }
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

    public static void crearCalendario() {}
}