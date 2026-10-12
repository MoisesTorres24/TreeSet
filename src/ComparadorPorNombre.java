import java.util.Comparator;

class ComparadorPorNombre implements Comparator<Estudiante> {
    @Override
    public int compare(Estudiante a, Estudiante b) {

        // Ignoramos el promedio y usamos orden alfabético tradicional
        return a.getNombre().compareTo(b.getNombre());
    }
}
