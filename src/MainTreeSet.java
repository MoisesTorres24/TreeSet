import java.util.TreeSet;

public class MainTreeSet {
    public static void main(String[] args) {

        // Utilizando el Orden Natural
        System.out.println("--- Orden Natural (Por Promedio) ---");
        TreeSet<Estudiante> conjuntoNatural = new TreeSet<>();

        conjuntoNatural.add(new Estudiante("Ana", 9.5));
        conjuntoNatural.add(new Estudiante("Luis", 8.7));
        conjuntoNatural.add(new Estudiante("Carlos", 9.5));
        conjuntoNatural.add(new Estudiante("Beto", 7.2));

        conjuntoNatural.add(new Estudiante("Luis", 8.7)); // duplicado

        for (Estudiante e : conjuntoNatural) {
            System.out.println(e);
        }

        // Utilizando el Comparator
        System.out.println("\n--- Orden Personalizado (Por Nombre) ---");
        TreeSet<Estudiante> conjuntoNombres = new TreeSet<>(new ComparadorPorNombre());

        conjuntoNombres.add(new Estudiante("Luis", 8.7));
        conjuntoNombres.add(new Estudiante("Ana", 9.5));
        conjuntoNombres.add(new Estudiante("Carlos", 9.5));
        conjuntoNombres.add(new Estudiante("Beto", 7.2));

        for (Estudiante e : conjuntoNombres) {
            System.out.println(e);
        }
        // Pruebas de Navegación
        System.out.println("\n--- Navegación Relativa ---");
        TreeSet<Integer> numeros = new TreeSet<>();
        numeros.add(10);
        numeros.add(20);
        numeros.add(30);
        numeros.add(40);
        numeros.add(50);

        System.out.println("Conjunto base: " + numeros);

        // Consultas de extremos y aproximaciones
        System.out.println("Elemento más pequeño (first): " + numeros.first());
        System.out.println("Elemento más grande (last): " + numeros.last());
        System.out.println("Inmediato mayor o igual a 25 (ceiling): " + numeros.ceiling(25));
        System.out.println("Inmediato menor estrictamente a 30 (lower): " + numeros.lower(30));

        // Consultas por rangos
        System.out.println("Rango del 20 al 40 exclusivo (subSet): " + numeros.subSet(20, 40));
        System.out.println("Menores a 30 (headSet): " + numeros.headSet(30));
        System.out.println("Mayores o iguales a 30 (tailSet): " + numeros.tailSet(30));

        // Extracción de datos
        System.out.println("Extrayendo el menor (pollFirst): " + numeros.pollFirst());
        System.out.println("Extrayendo el mayor (pollLast): " + numeros.pollLast());

        System.out.println("Conjunto final: " + numeros);
    }
}
