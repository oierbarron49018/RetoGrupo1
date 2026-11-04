/*

La aplicación contemplará dos etapas:
1. Etapa 1: inscripción de equipos y jugadores, así como la generación del calendario de
los distintos enfrentamientos que se darán a lo largo de todas las jornadas de la
competición.
2. Etapa del campeonato donde se guardarán los resultados de los enfrentamientos.
Para poder empezar con la etapa del campeonato, la etapa de inscripción tiene que estar
cerrada.
La aplicación almacenará la información de cada jugador (nombre, apellido, nacionalidad,
fecha nacimiento, nickname, rol, sueldo), equipos (nombre, fecha de fundación, jugadores),
jornadas del calendario de la competición, los enfrentamientos/las partidas de la competición
(fecha del enfrentamiento, número de jornada, equipos que intervienen en cada
enfrentamiento, hora del enfrentamiento, resultado)

 */

import  java.util.Scanner;

public class Main {

    //Variables globales a utilizar en el programa
    private static StringBuilder equipos = new StringBuilder();
    private static StringBuilder jugadores = new StringBuilder();

    private static boolean inscripcionCerrada = false;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int opcion = -1;
        //Programa principal
        do {
            mostrarMenu();
            try {
                opcion = leerEntero("Opción: ");
                switch (opcion) {
                    case 1:
                        inscribirEquipo();
                        break;
                    case 2:
                        inscribirJugador();
                        break;
                    case 3:
                        listarEquipos();
                        break;
                    case 4:
                        cerrarInscripcion();
                        break;
                    case 0:
                        System.out.println("Hasta pronto.");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        } while (opcion != 0);
    }
    //Menú
    public static void mostrarMenu() {
        String etapa = inscripcionCerrada ? "CAMPEONATO" : "INSCRIPCIÓN";
        System.out.println("\n=== CAMPEONATO (etapa: " + etapa + ") ===");
        System.out.println("[1] Inscribir equipo");
        System.out.println("[2] Inscribir Jugador");
        System.out.println("[3] Ver equipos y jugadores");
        System.out.println("[4] Cerrar inscripción");
        System.out.println("[0] Salir");
    }

    //Inscripciones de equipos y jugadores
    public static void inscribirEquipo() {
        if (inscripcionCerrada) {
            System.out.println("La inscripción ya está cerrada.");
            return;
        }
        String nombre = leerTexto("Nombre del equipo: ");
        if (buscarEquipo(nombre) != -1) {
            System.out.println("Ya existe un equipo con ese nombre.");
            return;
        }
        String fundacion = leerTexto("Fecha de fundación: ");
        equipos.append(nombre).append(";").append(fundacion).append("\n");
        System.out.println("Equipo inscrito.");
    }

    public static void inscribirJugador() {
        if (inscripcionCerrada) {
            System.out.println("La inscripción ya está cerrada.");
            return;
        }
        int posicion = buscarEquipo(leerTexto("Equipo del jugador: "));
        if (posicion == -1) {
            System.out.println("Ese equipo no existe.");
            return;
        }
        String equipo = campo(linea(equipos, posicion), 0);
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String nacionalidad = leerTexto("Nacionalidad: ");
        String nacimiento = leerTexto("Fecha de nacimiento: ");
        String rol = leerTexto("Rol: ");
        double sueldo = leerDecimal("Sueldo: ");

        jugadores.append(equipo).append(";").append(nombre).append(";").append(apellido)
                .append(";").append(nacionalidad).append(";").append(nacimiento)
                .append(";").append(";").append(rol)
                .append(";").append(sueldo).append("\n");
        System.out.println("Jugador inscrito.");
    }

    //Listado de equipos
    public static void listarEquipos() {
        if (contarLineas(equipos) == 0) {
            System.out.println("No hay equipos inscritos.");
            return;
        }
        for (int i = 0; i < contarLineas(equipos); i++) {
            String e = linea(equipos, i);
            System.out.println("- " + campo(e, 0) + " (fundado: " + campo(e, 1) + ")");
            for (int j = 0; j < contarLineas(jugadores); j++) {
                String p = linea(jugadores, j);
                if (campo(p, 0).equals(campo(e, 0))) {
                    System.out.println("    · " + campo(p, 5) + " | " + campo(p, 1) + " "
                            + campo(p, 2) + " | " + campo(p, 3) + " | " + campo(p, 6)
                            + " | " + campo(p, 7) + " €");
                }
            }
        }
    }

    //Pasar de fase de inscripción a fase campeonato
    public static void cerrarInscripcion() {
        if (inscripcionCerrada) {
            System.out.println("La inscripción ya estaba cerrada.");
            return;
        }
        if (contarLineas(equipos) < 2) {
            System.out.println("Hacen falta al menos 2 equipos para cerrar la inscripción.");
            return;
        }
        inscripcionCerrada = true;
        System.out.println("Inscripción cerrada. Ya se puede pasar al campeonato.");
    }


    //funciones auxiliares para hacer todo mas facil
    public static int contarLineas(StringBuilder datos) {
        int total = 0;
        for (int i = 0; i < datos.length(); i++) {
            if (datos.charAt(i) == '\n') {
                total++;
            }
        }
        return total;
    }


    public static String linea(StringBuilder datos, int n) {
        int inicio = 0;
        for (int i = 0; i < n; i++) {
            inicio = datos.indexOf("\n", inicio) + 1;
        }
        int fin = datos.indexOf("\n", inicio);
        return datos.substring(inicio, fin);
    }

        public static String campo(String linea, int n) {
            int inicio = 0;
            for (int i = 0; i < n; i++) {
                inicio = linea.indexOf(";", inicio) + 1;
            }
            int fin = linea.indexOf(";", inicio);
            if (fin == -1) {
                fin = linea.length();
            }
            return linea.substring(inicio, fin);
        }

    public static int buscarEquipo(String nombre) {
        for (int i = 0; i < contarLineas(equipos); i++) {
            if (campo(linea(equipos, i), 0).equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim().replace(";", ",");
    }

    public static int leerEntero(String mensaje) {
        boolean correcto = false;
        int numero = 0;

        while (!correcto) {
            try {
                numero = Integer.parseInt(leerTexto(mensaje));
                correcto = true;
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero.");
            }
        }

        return numero;
    }

    public static double leerDecimal(String mensaje) {
        boolean correcto = false;
        double numero = 0;

        while (!correcto) {
            try {
                numero = Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
                correcto = true;
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número.");
            }
        }

        return numero;
    }
 //absolute cinema \_(•-•)_/
}