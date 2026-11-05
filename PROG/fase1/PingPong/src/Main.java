import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    // CONSTANTES

    private static final String LETRAS = "A-Za-zÁÉÍÓÚÜÑáéíóúüñ";

    private static final String PATRON_TEXTO = "(?=.{2,30}$)[" + LETRAS + "]+( [" + LETRAS + "]+)*";

    private static final String PATRON_EQUIPO = "(?=.{3,30}$)[" + LETRAS + "0-9]+( [" + LETRAS + "0-9]+)*";

    private static final String PATRON_NICKNAME = "\\w{3,16}";

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final LocalDate FUNDACION_MINIMA = LocalDate.of(1970, 1, 1);

    private static final int EDAD_MINIMA = 16;
    private static final int EDAD_MAXIMA = 100;

    private static final int JUGADORES_MINIMOS = 1;
    private static final int JUGADORES_MAXIMOS = 6;

    private static final double SMI_MENSUAL = 1221;
    private static final double SUELDO_MAXIMO = 50000;

    private static final Scanner sc = new Scanner(System.in);

    // DATOS EN MEMORIA

    // Equipo
    private static String nombreEquipo;
    private static LocalDate fechaFundacion;
    private static int numeroJugadores;

    // Jugador
    private static String nombre;
    private static String apellido;
    private static String nacionalidad;
    private static LocalDate fechaNacimiento;
    private static String nickname;
    private static String rol;
    private static double sueldo;

    // Aquí se va guardando la ficha de cada jugador ya introducido
    private static StringBuilder jugadoresGuardados = new StringBuilder();

    public static void main(String[] args) {
        System.out.println("=== DATOS DEL EQUIPO ===");
        pedirDatosEquipo();
        numeroJugadores = pedirNumeroJugadores();

        for (int i = 1; i <= numeroJugadores; i++) {
            System.out.println();
            System.out.println("=== JUGADOR " + i + " de " + numeroJugadores + " ===");
            pedirDatosJugador();
            guardarJugador(i);
        }

        mostrarDatos();

        sc.close();
    }

    // ENTRADA DE DATOS

    private static void pedirDatosEquipo() {
        nombreEquipo = pedirCadena("Nombre del equipo: ", PATRON_EQUIPO,
                "entre 3 y 30 caracteres, solo letras, números y espacios entre palabras.");

        fechaFundacion = pedirFecha("Fecha de fundación (dd/MM/yyyy): ",
                FUNDACION_MINIMA, LocalDate.now());
    }

    private static int pedirNumeroJugadores() {
        int cantidad = 0;
        boolean valido = false;

        while (!valido) {
            System.out.print("¿Cuántos jugadores tiene el equipo? (entre " + JUGADORES_MINIMOS
                    + " y " + JUGADORES_MAXIMOS + "): ");
            String texto = sc.nextLine().trim();

            try {
                cantidad = Integer.parseInt(texto);
                valido = cantidad >= JUGADORES_MINIMOS && cantidad <= JUGADORES_MAXIMOS;

                if (!valido) {
                    System.out.println("   Error: el número de jugadores debe estar entre "
                            + JUGADORES_MINIMOS + " y " + JUGADORES_MAXIMOS + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("   Error: introduce un número entero válido.");
            }
        }
        return cantidad;
    }

    private static void pedirDatosJugador() {
        LocalDate hoy = LocalDate.now();

        nombre = pedirCadena("Nombre: ", PATRON_TEXTO,
                "entre 2 y 30 caracteres, solo letras y espacios entre palabras.");

        apellido = pedirCadena("Apellido: ", PATRON_TEXTO,
                "entre 2 y 30 caracteres, solo letras y espacios entre palabras.");

        nacionalidad = pedirCadena("Nacionalidad: ", PATRON_TEXTO,
                "entre 2 y 30 caracteres, solo letras y espacios entre palabras.");

        fechaNacimiento = pedirFecha("Fecha de nacimiento (dd/MM/yyyy): ",
                hoy.minusYears(EDAD_MAXIMA), hoy.minusYears(EDAD_MINIMA));

        nickname = pedirCadena("Nickname: ", PATRON_NICKNAME,
                "entre 3 y 16 caracteres, solo letras, números y guion bajo (sin espacios).");

        rol = pedirCadena("Rol: ", PATRON_TEXTO,
                "entre 2 y 30 caracteres, solo letras y espacios entre palabras.");

        sueldo = pedirSueldo("Sueldo mensual bruto en 14 pagas (euros): ");
    }

    private static String pedirCadena(String mensaje, String regex, String ayuda) {
        Pattern patron = Pattern.compile(regex);
        String texto = "";
        boolean valido = false;

        while (!valido) {
            System.out.print(mensaje);
            texto = sc.nextLine().trim();

            Matcher encaja = patron.matcher(texto);
            valido = encaja.matches();

            if (!valido) {
                System.out.println("   Error: " + ayuda);
            }
        }
        return texto;
    }

    private static LocalDate pedirFecha(String mensaje, LocalDate desde, LocalDate hasta) {
        LocalDate fecha = null;

        while (fecha == null) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();

            try {
                fecha = LocalDate.parse(texto, FORMATO_FECHA);

                if (fecha.isBefore(desde) || fecha.isAfter(hasta)) {
                    System.out.println("   Error: la fecha debe estar entre "
                            + desde.format(FORMATO_FECHA) + " y " + hasta.format(FORMATO_FECHA) + ".");
                    fecha = null;
                }
            } catch (Exception e) {
                System.out.println("   Error: fecha no válida. Usa el formato dd/MM/yyyy (por ejemplo 25/12/1990).");
            }
        }
        return fecha;
    }

    private static double pedirSueldo(String mensaje) {
        double valor = 0;
        boolean valido = false;

        while (!valido) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();

            try {
                valor = Double.parseDouble(texto.replace(',', '.'));
                valido = valor > SMI_MENSUAL && valor <= SUELDO_MAXIMO;

                if (!valido) {
                    System.out.println("   Error: el sueldo debe ser mayor que el SMI ("
                            + SMI_MENSUAL + " euros) y no superar " + SUELDO_MAXIMO + " euros.");
                }
            } catch (NumberFormatException e) {
                System.out.println("   Error: introduce un número válido (por ejemplo 1500.50).");
            }
        }
        return valor;
    }

    // GUARDA

    private static void guardarJugador(int numero) {
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

        jugadoresGuardados.append("JUGADOR ").append(numero).append("\n")
                .append("  Nombre: ").append(nombre).append(" ").append(apellido).append("\n")
                .append("  Nickname: ").append(nickname).append("\n")
                .append("  Nacionalidad: ").append(nacionalidad).append("\n")
                .append("  Fecha de nacimiento: ").append(fechaNacimiento.format(FORMATO_FECHA))
                .append(" (").append(edad).append(" años)\n")
                .append("  Rol: ").append(rol).append("\n")
                .append("  Sueldo: ").append(String.format("%.2f", sueldo))
                .append(" euros al mes (14 pagas)\n");
    }

    private static void mostrarDatos() {
        System.out.println();
        System.out.println("=== DATOS GUARDADOS ===");
        System.out.println("EQUIPO");
        System.out.println("  Nombre: " + nombreEquipo);
        System.out.println("  Fecha de fundación: " + fechaFundacion.format(FORMATO_FECHA));
        System.out.println("  Número de jugadores: " + numeroJugadores);
        System.out.print(jugadoresGuardados.toString());
    }
}