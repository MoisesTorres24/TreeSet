# TreeSet en Java: conjuntos ordenados con árboles rojinegros

**Materia:** Diseño y Programación Orientada a Objetos
**Tema:** `java.util.TreeSet`

## 1. ¿Qué es un TreeSet?

### 1.1 Definición y jerarquía

Muchas veces necesitamos una colección sin elementos repetidos y siempre ordenada. En Java, `TreeSet` ofrece justo eso dentro del Java Collections Framework.

`TreeSet<E>` implementa `NavigableSet<E>` y guarda elementos únicos y ordenados. Su jerarquía es:

![Jerarquía de TreeSet](jerarquiaTreeSet.png)

Es un ejemplo de **interfaces y herencia**: `Set` prohíbe duplicados, `SortedSet` agrega orden y `NavigableSet` agrega métodos como `floor` o `ceiling`. Por **polimorfismo** podemos escribir `NavigableSet<Integer> conjunto = new TreeSet<>();`.
### 1.2 Características principales

- No permite duplicados
- Mantiene los elementos ordenados.
- No admite `null`.
- No es thread-safe; requiere sincronización externa (por ejemplo, `Collections.synchronizedSortedSet`).
- Usa **genéricos** para dar seguridad de tipos.

---

## 2. ¿Cómo funciona un TreeSet?

### 2.1 Estructura interna

Un `TreeSet` está respaldado por un `TreeMap`, donde los elementos son las claves. El `TreeMap` usa un **árbol rojinegro**: un árbol binario de búsqueda autobalanceado cuyos nodos son rojos o negros. Por ejemplo, la raíz es negra y un nodo rojo no tiene hijos rojos. Al insertar o eliminar, se reacomoda con recoloreos y rotaciones.

Estas reglas evitan que un camino sea mucho más largo que otro, así que la altura es proporcional a log n y las operaciones son O(log n) incluso en el peor caso (un árbol común podría degenerar en O(n)). Ejemplo tras insertar 10, 20, 30, 40, 50 (N = negro, R = rojo):

```
          20(N)
         /     \
      10(N)    40(N)
              /    \
           30(R)  50(R)
```

### 2.2 Orden natural vs. Comparator

- **Orden natural:** los elementos implementan `Comparable<T>` y definen `compareTo` (como `String` e `Integer`).
- **Orden personalizado:** se pasa un `Comparator<T>` al constructor.

Importante: el `TreeSet` considera iguales a dos elementos si `compareTo`/`compare` devuelve 0, **no con `equals`**. Oracle advierte que el orden debe ser consistente con `equals` para respetar el contrato de `Set`.

### 2.3 Complejidad de las operaciones

| Operación | Complejidad |
|---|---|
| `add` | O(log n) |
| `remove` | O(log n) |
| `contains` | O(log n) |
| `first` / `last` | O(log n) |

Recorrer todo el conjunto es O(n).

### 2.4 Métodos más importantes

| Método | Descripción |
|---|---|
| `add`, `remove`, `contains` | Insertar, eliminar y buscar |
| `first`, `last` | Menor y mayor elemento |
| `floor(e)` / `ceiling(e)` | Mayor elemento ≤ e / menor elemento ≥ e |
| `lower(e)` / `higher(e)` | Mayor elemento < e / menor elemento > e |
| `headSet(e)` / `tailSet(e)` | Elementos < e / elementos ≥ e |
| `subSet(a, b)` | Elementos en el rango [a, b) |
| `descendingSet()` | Vista en orden inverso |
| `pollFirst()` / `pollLast()` | Quita y devuelve el menor / el mayor |

`headSet`, `tailSet` y `subSet` devuelven **vistas**: si se modifica la vista, también cambia el conjunto original.

### 2.5 Errores comunes

```java
// 1. ClassCastException: la clase no implementa Comparable
TreeSet<Object> a = new TreeSet<>();
a.add(new Object());   // lanza ClassCastException

// 2. NullPointerException con orden natural
TreeSet<String> b = new TreeSet<>();
b.add(null);           // lanza NullPointerException

// 3. "Duplicados" perdidos por un compareTo mal hecho:
// si compareTo solo compara el promedio, dos estudiantes distintos
// con el mismo promedio devuelven 0 y el segundo NO se agrega.
```

Se soluciona con un criterio de desempate (sección 4).

---

## 3. Ejemplos de aplicaciones reales

> **Nota sobre las imágenes:** las imágenes de referencia de cada caso se buscaron en la web y se muestran en el chat. Para incluirlas en este archivo, guárdalas en una carpeta `imagenes/` y descomenta la línea de imagen de cada caso, indicando su fuente.

### 3.1 Ranking sin repetidos

<!-- ![Tabla de posiciones](imagenes/ranking.png) -->

- **Contexto:** un torneo o videojuego muestra a los mejores jugadores.
- **Problema:** mantener el ranking ordenado por puntaje, sin repetidos.
- **Por qué TreeSet:** cada inserción lo deja ordenado sin un `sort` aparte.
- **Métodos:** `add`, `first`, `headSet`, `descendingSet`.

### 3.2 Agenda de eventos

<!-- ![Calendario de eventos](imagenes/agenda.png) -->

- **Contexto:** un calendario con eventos por fecha y hora.
- **Problema:** mostrarlos en orden cronológico y hallar el siguiente evento.
- **Por qué TreeSet:** con un `Comparator` por fecha, la agenda siempre está ordenada y la búsqueda es eficiente.
- **Métodos:** `add`, `ceiling`, `subSet`.

### 3.3 Autocompletado

<!-- ![Autocompletado](imagenes/autocompletado.png) -->

- **Contexto:** una barra de búsqueda que sugiere palabras.
- **Problema:** encontrar rápido las palabras con cierto prefijo.
- **Por qué TreeSet:** en orden alfabético, las palabras con el mismo prefijo quedan juntas.
- **Métodos:** `ceiling(prefijo)` y `subSet(prefijo, prefijo + "\uffff")`.

### 3.4 Tareas por prioridad

<!-- ![Tareas por prioridad](imagenes/tareas.png) -->

- **Contexto:** un tablero de tareas con prioridad.
- **Problema:** atender primero la más urgente y consultar rangos de prioridad.
- **Por qué TreeSet:** además de sacar la primera, permite consultar rangos.
- **Métodos:** `pollFirst`, `headSet`, `tailSet`.

---

## 4. Ejemplo en código Java

El programa se probó compilándolo y ejecutándolo. Todo va en un solo archivo, `EjemploTreeSet.java`, en el orden en que se explica a continuación. Usa solo clases normales, `Comparable` y un `Comparator` como clase aparte.

### 4.1 Importaciones

```java
import java.util.Comparator;
import java.util.TreeSet;
```

Se importan `TreeSet` (la colección) y `Comparator` (para el orden personalizado).

### 4.2 Clase Estudiante con Comparable

```java
// Clase propia que implementa Comparable.
// El orden natural será por promedio, de mayor a menor.
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

    @Override
    public int compareTo(Estudiante otro) {
        // Si el promedio es distinto, se ordena por promedio (mayor primero)
        if (this.promedio > otro.promedio) {
            return -1;
        }
        if (this.promedio < otro.promedio) {
            return 1;
        }
        // Si el promedio es igual, se desempata por nombre.
        // Sin esto, el TreeSet pensaría que son el mismo estudiante.
        return this.nombre.compareTo(otro.nombre);
    }

    @Override
    public String toString() {
        return nombre + " (" + promedio + ")";
    }
}
```

- Los atributos son `private` (**encapsulamiento**) y se consultan con getters.
- `implements Comparable<Estudiante>` obliga a escribir `compareTo`, que define el **orden natural**.
- `compareTo` devuelve un número negativo si este estudiante va antes, positivo si va después y 0 si se consideran iguales. Aquí va primero el de mayor promedio.
- El desempate por nombre evita que dos estudiantes con el mismo promedio se tomen como duplicados.
- `toString` sirve para imprimir cada estudiante de forma legible.

### 4.3 Comparator personalizado

```java
// Comparator personalizado: ordena a los estudiantes por nombre
class ComparadorPorNombre implements Comparator<Estudiante> {
    @Override
    public int compare(Estudiante a, Estudiante b) {
        return a.getNombre().compareTo(b.getNombre());
    }
}
```

Esta clase define otro criterio de orden sin modificar `Estudiante`. El método `compare` recibe dos estudiantes y los compara por nombre, usando el `compareTo` de `String`.

### 4.4 Método main: ejemplo básico (orden natural)

```java
public class EjemploTreeSet {

    public static void main(String[] args) {

        // 1. Ejemplo básico: orden natural con Integers
        System.out.println("=== 1. Orden natural con Integers ===");
        TreeSet<Integer> numeros = new TreeSet<>();
        numeros.add(40);
        numeros.add(10);
        numeros.add(30);
        numeros.add(20);
        numeros.add(10); // repetido: no se agrega
        System.out.println("Conjunto: " + numeros);
        System.out.println("Primero: " + numeros.first());
        System.out.println("Último: " + numeros.last());
        System.out.println("¿Contiene 30? " + numeros.contains(30));
```

Se crea un `TreeSet<Integer>` y se agregan números desordenados. El conjunto los ordena solo, ignora el 10 repetido y permite consultar el menor (`first`), el mayor (`last`) y si existe un valor (`contains`).

### 4.5 Estudiantes con Comparable

```java
        // 2. Clase propia con Comparable
        System.out.println("\n=== 2. Estudiantes con Comparable ===");
        TreeSet<Estudiante> estudiantes = new TreeSet<>();
        estudiantes.add(new Estudiante("Ana", 9.5));
        estudiantes.add(new Estudiante("Luis", 8.7));
        estudiantes.add(new Estudiante("Carla", 9.5));
        estudiantes.add(new Estudiante("Pedro", 7.9));
        for (Estudiante e : estudiantes) {
            System.out.println(e);
        }
```

Como no se pasa ningún `Comparator`, el `TreeSet` usa el `compareTo` de `Estudiante`. El `for` recorre el conjunto ya ordenado por promedio.

### 4.6 Estudiantes con Comparator

```java
        // 3. Comparator personalizado (ordenar por nombre)
        System.out.println("\n=== 3. Comparator personalizado ===");
        TreeSet<Estudiante> porNombre = new TreeSet<>(new ComparadorPorNombre());
        porNombre.add(new Estudiante("Pedro", 7.9));
        porNombre.add(new Estudiante("Ana", 9.5));
        porNombre.add(new Estudiante("Luis", 8.7));
        for (Estudiante e : porNombre) {
            System.out.println(e);
        }
```

Al pasar `new ComparadorPorNombre()` al constructor, el conjunto ignora el orden natural y ordena alfabéticamente por nombre. Es **polimorfismo**: el `TreeSet` solo conoce la interfaz `Comparator`.

### 4.7 Métodos de navegación

```java
        // 4. Métodos de navegación
        System.out.println("\n=== 4. Métodos de navegación ===");
        TreeSet<Integer> nav = new TreeSet<>();
        nav.add(10);
        nav.add(20);
        nav.add(30);
        nav.add(40);
        nav.add(50);
        System.out.println("Conjunto: " + nav);
        System.out.println("floor(25) = " + nav.floor(25));         // mayor que sea <= 25
        System.out.println("ceiling(25) = " + nav.ceiling(25));     // menor que sea >= 25
        System.out.println("headSet(30) = " + nav.headSet(30));     // elementos < 30
        System.out.println("tailSet(30) = " + nav.tailSet(30));     // elementos >= 30
        System.out.println("descendingSet() = " + nav.descendingSet());
        System.out.println("pollFirst() = " + nav.pollFirst());     // quita el menor
        System.out.println("Después de pollFirst: " + nav);
    }
}
```

Con el 25, que no está en el conjunto, `floor` devuelve el vecino inferior (20) y `ceiling` el superior (30). `headSet` y `tailSet` devuelven la parte menor y la mayor o igual a 30, `descendingSet` muestra el orden inverso y `pollFirst` quita y devuelve el menor. Las dos últimas llaves cierran el método `main` y la clase.

### Salida esperada

```
=== 1. Orden natural con Integers ===
Conjunto: [10, 20, 30, 40]
Primero: 10
Último: 40
¿Contiene 30? true

=== 2. Estudiantes con Comparable ===
Ana (9.5)
Carla (9.5)
Luis (8.7)
Pedro (7.9)

=== 3. Comparator personalizado ===
Ana (9.5)
Luis (8.7)
Pedro (7.9)

=== 4. Métodos de navegación ===
Conjunto: [10, 20, 30, 40, 50]
floor(25) = 20
ceiling(25) = 30
headSet(30) = [10, 20]
tailSet(30) = [30, 40, 50]
descendingSet() = [50, 40, 30, 20, 10]
pollFirst() = 10
Después de pollFirst: [20, 30, 40, 50]
```

Ana y Carla tienen el mismo promedio y ambas aparecen gracias al desempate por nombre.

---

## 5. Conclusión

Un `TreeSet` es un conjunto sin duplicados y ordenado mediante un árbol rojinegro, con operaciones O(log n). El orden y la igualdad dependen de `Comparable` o `Comparator`, no de `equals`. Conviene usarlo cuando se necesita orden permanente o consultas por rango (`floor`, `ceiling`, `subSet`). Si solo se verifica pertenencia, `HashSet` es más rápido; si solo se extrae el elemento de mayor prioridad, `PriorityQueue` es más simple.

Para mí, `TreeSet` muestra la utilidad de la POO: programar con interfaces, separar el criterio de orden en un `Comparator` y usar genéricos. También me hizo ver que implementar bien `compareTo` es parte del diseño de una clase.

---

## Referencias

- Oracle. *Java SE API Documentation: `java.util.TreeSet`, `NavigableSet`, `SortedSet`, `Comparable` y `Comparator`*. https://docs.oracle.com/en/java/javase/21/docs/api/
- Oracle. *The Java Tutorials: Trail "Collections"*. https://docs.oracle.com/javase/tutorial/collections/index.html
- Bloch, J. (2018). *Effective Java* (3.ª ed.). Addison-Wesley. (Ítem sobre implementar `Comparable`).
- Cormen, T. H., Leiserson, C. E., Rivest, R. L. y Stein, C. *Introduction to Algorithms*. MIT Press. (Capítulo sobre árboles rojinegros).
