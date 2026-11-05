import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Pattern patronEquipo = Pattern.compile("^[A-Za-z0-9 .'\"&\\-_]+$");
        Pattern patronPersona = Pattern.compile("^[A-Z][a-záéíóúñÁÉÍÓÚÑ ]+$");

        String equipo = pedirEquipo(sc, patronEquipo);
        String nombre = pedirNombreOApellido(sc, patronPersona, "Nombre del jugador: ",
                "El nombre solo puede tener letras y empezar con una mayúscula");

        String apellido1 = pedirNombreOApellido(sc, patronPersona, "Primer apellido del jugador: ",
                "El primer apellido solo puede tener letras y empezar con una mayúscula");

        String apellido2 = pedirNombreOApellido(sc, patronPersona, "Segundo apellido del jugador: ",
                "El segundo apellido solo puede tener letras y empezar con una mayúscula");

        mostrarResultado(equipo, nombre, apellido1, apellido2);

        sc.close();
    }

    static String pedirEquipo(Scanner sc, Pattern patron) {
        String equipo;
        Matcher esIgual;

        do {
            System.out.print("Nombre del equipo: ");
            equipo = sc.nextLine();

            esIgual = patron.matcher(equipo);

            if (!esIgual.matches()) {
                System.out.println("Nombre de equipo no válido");
            }

        } while (!esIgual.matches());

        return equipo;
    }

    static String pedirNombreOApellido(Scanner sc, Pattern patron, String mensaje, String error) {
        String texto;
        Matcher esIgual;

        do {
            System.out.print(mensaje);
            texto = sc.nextLine();

            esIgual = patron.matcher(texto);

            if (!esIgual.matches()) {
                System.out.println(error);
            }

        } while (!esIgual.matches());

        return texto;
    }

    static void mostrarResultado(String equipo, String nombre, String apellido1, String apellido2) {
        System.out.println("Equipo: " + equipo);
        System.out.println("Jugador: " + nombre + " " + apellido1 + " " + apellido2);
    }
}