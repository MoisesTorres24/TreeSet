// Implementamos Comparable para definir el orden por defecto
class Estudiante implements Comparable<Estudiante> {
    private String nombre;
    private double promedio;

    public Estudiante(String nombre, double promedio) {
        this.nombre = nombre;
        this.promedio = promedio;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPromedio() {
        return promedio;
    }

    // Sobrescribimos compareTo
    @Override
    public int compareTo(Estudiante otro) {
        // Ordenamos de mayor a menor promedio
        if (this.promedio > otro.promedio) return -1;
        if (this.promedio < otro.promedio) return 1;

        // Criterio de desempate.
        // Si el promedio es idéntico, comparamos por nombre para evitar que el TreeSet crea que son duplicados y los borre.
        return this.nombre.compareTo(otro.nombre);
    }

    @Override
    public String toString() {
        return nombre + " (" + promedio + ")";
    }
}
