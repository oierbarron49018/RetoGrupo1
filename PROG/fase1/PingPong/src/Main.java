import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Main {
    public static Scanner sc = new Scanner(System.in);
    public static final double SMI = 1300.0;
    public static DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        boolean salir = false;
        do {
            mostrarMenu();
            String opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1" -> validarEquipo();
                case "2" -> validarJugador();
                case "0" -> salir = true;
                default -> System.out.println("Opción no válida. Inténtelo de nuevo.");
            }
        } while (!salir);
    }

    public static void mostrarMenu() {
        System.out.println("\n=== MENU  ===");
        System.out.println("[1] Crear Equipo");
        System.out.println("[2] Crear Jugador");
        System.out.println("[0] Salir");
        System.out.print("Seleccione una opción: ");
    }

    public static void validarEquipo() {
        System.out.println("\n--- CREACIÓN DE EQUIPO ---");

        System.out.print("Nombre del equipo: ");
        String nombreEquipo = sc.nextLine().trim();
        LocalDate fechaFundacion = pedirFechaFundacion();

        System.out.println("\n--- DATOS DEL ENTRENADOR ---");
        String nombreEntrenador = pedirTextoValido("Nombre : ");
        String apellidoEntrenador = pedirTextoValido("Apellido : ");
        LocalDate fechaNacEntrenador = pedirFechaNacimiento(18, "Entrenador");
        System.out.println("Equipo creado con éxito: " + nombreEquipo);  }

    public static void validarJugador() {
        System.out.println("\n--- CREACIÓN DE JUGADOR ---");        String nombre = pedirTextoValido("Nombre: ");
        String apellido = pedirTextoValido("Apellido: ");
        String nacionalidad = pedirTextoValido("Nacionalidad: ");
        LocalDate fechaNac = pedirFechaNacimiento(16, "Jugador");
        double sueldo = pedirSueldo("Sueldo mensual (€): ");
        String rol = pedirRolBasico();
        String posicion = pedirPosicionBasica();

        System.out.println("Jugador creado con éxito: " + nombre + " " + apellido +  ", rol: " + rol + ", posición: " + posicion);
    }

    public static String pedirTextoValido(String mensaje) {
        String regex = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s\\-]+$";
        String entrada = "";
        boolean valido = false;
        do {
            System.out.print(mensaje);
            entrada = sc.nextLine().trim();
            if (entrada.matches(regex) && !entrada.isEmpty()) {
                valido = true;
            } else {
                System.out.println("[ERROR] Solo se permiten letras, espacios y guiones (-).");
            }
        } while (!valido);
        return entrada;
    }

    public static double pedirSueldo(String mensaje) {
        double sueldo = 0.0;
        boolean valido = false;
        do {
            try {
                System.out.print(mensaje);
                sueldo = Double.parseDouble(sc.nextLine().trim());
                if (sueldo <= SMI) throw new Exception("El sueldo debe ser mayor al SMI (" + SMI + "€).");
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Cifra decimal válida requerida.");
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        } while (!valido);
        return sueldo;
    }

    public static LocalDate pedirFechaFundacion() {
        LocalDate fecha = null;
        boolean valida = false;
        while (!valida) {
            try {
                System.out.print("Fecha de fundación (dd/MM/yyyy): ");
                fecha = LocalDate.parse(sc.nextLine().trim(), formatoFecha);
                if (fecha.isAfter(LocalDate.now())) throw new Exception("La fecha no puede ser en el futuro.");
                if (fecha.getYear() < 1980) throw new Exception("El equipo no pudo fundarse antes de 1980.");
                valida = true;
            } catch (DateTimeParseException e) {
                System.out.println("[ERROR] Formato incorrecto. Use dd/MM/yyyy.");
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
        return fecha;
    }

    public static LocalDate pedirFechaNacimiento(int edadMinima, String rolPersona) {
        LocalDate fecha = null;
        boolean valida = false;
        while (!valida) {
            try {
                System.out.print("Fecha de nacimiento de " + rolPersona + " (dd/MM/yyyy): ");
                fecha = LocalDate.parse(sc.nextLine().trim(), formatoFecha);
                if (fecha.isAfter(LocalDate.now())) throw new Exception("La fecha no puede ser en el futuro.");

                long edad = ChronoUnit.YEARS.between(fecha, LocalDate.now());
                if (edad < edadMinima) throw new Exception("El " + rolPersona + " debe tener al menos " + edadMinima + " años.");

                valida = true;
            } catch (DateTimeParseException e) {
                System.out.println("[ERROR] Formato incorrecto. Use dd/MM/yyyy.");
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
        return fecha;
    }

    public static String pedirRolBasico() {
        String rol;
        boolean valido = false;
        do {
            System.out.print("Rol (Cap / Titu / Sup): ");
            rol = sc.nextLine().trim();
            if (rol.equalsIgnoreCase("Cap") || rol.equalsIgnoreCase("Titu") || rol.equalsIgnoreCase("Sup")) {
                valido = true;
            } else {
                System.out.println("[ERROR] Rol no válido.");
            }
        } while (!valido);
        return rol;
    }

    public static String pedirPosicionBasica() {
        String posicion;
        boolean valida = false;
        do {
            System.out.print("Posición (der / izq): ");
            posicion = sc.nextLine().trim().toLowerCase();
            if (posicion.equals("der") || posicion.equals("izq")) {
                valida = true;
            } else {
                System.out.println("[ERROR] Posición no válida.");
            }
        } while (!valida);
        return posicion;
    }
}