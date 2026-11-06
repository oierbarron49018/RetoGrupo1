import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        String opcion;

        do {
            System.out.println("\nMENU");
            System.out.println("a) Registrar equipo");
            System.out.println("b) Salir");
            System.out.print("Selecciona una opción: ");

            opcion = sc.nextLine().toLowerCase();

            switch (opcion) {
                case "a":
                    registrarEquipo();
                    break;
                case "b":
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opcion no válida");
                    break;
            }
        } while (!opcion.equals("b"));
    }

    public static void registrarEquipo() {
        System.out.println("REGISTRAR EQUIPO");
        StringBuilder sbequipo = new StringBuilder();
        StringBuilder sbentrenador = new StringBuilder();
        StringBuilder sbjugadores = new StringBuilder();

        // --- DATOS DEL EQUIPO ---
        System.out.println("\nDatos del equipo");

        System.out.print("Nombre del equipo: ");
        String nombreEquipo = sc.nextLine();

        // Formato ajustado a YYYY-MM-DD
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaCreacion = null;

        while (fechaCreacion == null) {
            System.out.print("Fecha de creación (YYYY-MM-DD): ");
            String fechaInput = sc.nextLine().trim();
            try {
                fechaCreacion = LocalDate.parse(fechaInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Error: La fecha introducida no es válida o no cumple el formato YYYY-MM-DD.");
            }
        }

        sbequipo.append("  Nombre: ").append(nombreEquipo)
                .append(" | Fecha de creación: ").append(fechaCreacion.format(formatter))
                .append("\n");

        // --- DATOS DEL ENTRENADOR ---
        System.out.println("\nDatos del entrenador");

        System.out.print("Nombre: ");
        String nombreent = sc.nextLine();

        System.out.print("Apellido: ");
        String apellidoent = sc.nextLine();

        System.out.print("Nickname: ");
        String nicknameent = sc.nextLine();

        String dnient = "";
        while (!dnient.matches("^[0-9]{8}[A-Za-z]$")) {
            System.out.print("DNI: ");
            dnient = sc.nextLine().trim();
            if (!dnient.matches("^[0-9]{8}[A-Za-z]$")) {
                System.out.println("Error: DNI no válido. Debe tener 8 números seguidos de 1 letra (ej: 12345678A).");
            }
        }

        int edadent = 0;
        while (edadent <= 18) {
            System.out.print("Edad (18+): ");
            if (sc.hasNextInt()) {
                edadent = sc.nextInt();
                sc.nextLine();
                if (edadent <= 18) {
                    System.out.println("Error: La edad debe ser mayor a 18.");
                }
            } else {
                System.out.println("Error: Ingresa un número entero para la edad.");
                sc.nextLine();
            }
        }

        System.out.print("País: ");
        String paisent = sc.nextLine();

        double sueldoent = 0;
        while (sueldoent < 1300) {
            System.out.print("Sueldo (mínimo 1300): ");
            if (sc.hasNextDouble()) {
                sueldoent = sc.nextDouble();
                sc.nextLine();
                if (sueldoent < 1300) {
                    System.out.println("Error: El sueldo debe ser mayor o igual a 1300.");
                }
            } else {
                System.out.println("Error: Ingresa un valor numérico para el sueldo.");
                sc.nextLine();
            }
        }

        sbentrenador.append("  ")
                .append(nombreent).append(" ").append(apellidoent)
                .append(" (\"").append(nicknameent).append("\")")
                .append(" | DNI: ").append(dnient.toUpperCase())
                .append(" | Edad: ").append(edadent)
                .append(" | País: ").append(paisent)
                .append(" | Sueldo: ").append(sueldoent).append("€")
                .append("\n");

        // --- DATOS DE JUGADORES ---
        int numJugadores = 0;
        while (numJugadores < 2 || numJugadores > 6) {
            System.out.print("\nNumero de jugadores (mínimo 2, máximo 6): ");
            if (sc.hasNextInt()) {
                numJugadores = sc.nextInt();
                sc.nextLine();
                if (numJugadores < 2 || numJugadores > 6) {
                    System.out.println("Error: El número de jugadores debe estar entre 2 y 6.");
                }
            } else {
                System.out.println("Error: Por favor, introduce un número entero.");
                sc.nextLine();
            }
        }

        for (int i = 1; i <= numJugadores; i++) {
            System.out.println("\n--- Datos del Jugador " + i + " ---");

            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            System.out.print("Apellido: ");
            String apellido = sc.nextLine();

            System.out.print("Nickname: ");
            String nickname = sc.nextLine();

            int edad = 0;
            while (edad <= 16) {
                System.out.print("Edad (16+): ");
                if (sc.hasNextInt()) {
                    edad = sc.nextInt();
                    sc.nextLine();
                    if (edad <= 16) {
                        System.out.println("Error: La edad debe ser mayor a 16.");
                    }
                } else {
                    System.out.println("Error: Ingresa un número entero para la edad.");
                    sc.nextLine();
                }
            }

            System.out.print("País: ");
            String pais = sc.nextLine();

            double sueldo = 0;
            while (sueldo < 1300) {
                System.out.print("Sueldo (mínimo 1300): ");
                if (sc.hasNextDouble()) {
                    sueldo = sc.nextDouble();
                    sc.nextLine();
                    if (sueldo < 1300) {
                        System.out.println("Error: El sueldo debe ser mayor o igual a 1300.");
                    }
                } else {
                    System.out.println("Error: Ingresa un valor numérico para el sueldo.");
                    sc.nextLine();
                }
            }

            String rol = "";
            while (!rol.equals("capitan") && !rol.equals("titular") && !rol.equals("suplente")) {
                System.out.print("Rol (capitan, titular, suplente): ");
                rol = sc.nextLine().toLowerCase();
                if (!rol.equals("capitan") && !rol.equals("titular") && !rol.equals("suplente")) {
                    System.out.println("Error: El rol debe ser 'capitan', 'titular' o 'suplente'.");
                }
            }

            String posicion = "";
            while (!posicion.equals("derecha") && !posicion.equals("izquierda")) {
                System.out.print("Posición (derecha, izquierda): ");
                posicion = sc.nextLine().trim().toLowerCase();
                if (!posicion.equals("derecha") && !posicion.equals("izquierda")) {
                    System.out.println("Error: La posición debe ser 'derecha' o 'izquierda'.");
                }
            }

            sbjugadores.append("  ").append(i).append(". ")
                    .append(nombre).append(" ").append(apellido)
                    .append(" (\"").append(nickname).append("\")")
                    .append(" | Edad: ").append(edad)
                    .append(" | País: ").append(pais)
                    .append(" | Sueldo: ").append(sueldo).append("€")
                    .append(" | Rol: ").append(rol)
                    .append(" | Posición: ").append(posicion)
                    .append("\n");
        }

        System.out.println("\n--- RESUMEN DEL EQUIPO REGISTRADO ---");
        System.out.println("Equipo:");
        System.out.print(sbequipo);
        System.out.println("Entrenador:");
        System.out.print(sbentrenador);
        System.out.println("Jugadores:");
        System.out.print(sbjugadores);
    }
}