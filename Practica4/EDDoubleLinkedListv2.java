import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// Implementación de una lista doblemente enlazada con acceso a cabeza (first), cola (last) y tamaño (size).
// Se mantiene la estructura consistente actualizando next/prev al insertar/eliminar nodos.
public class EDDoubleLinkedListv2<T> implements List<T> {
    // Nodo de la lista: guarda el dato y enlaces al anterior (prev) y siguiente (next)
    private class Node {
        private T data;
        private Node next;
        private Node prev;

        public Node(T data) {
            this.data = data;
        }
    }

    // Punteros a la cabeza y a la cola de la lista, y contador de elementos
    private Node first = null;
    private Node last = null;
    private int size = 0;

    // Construye la lista encadenando los elementos de la colección en el mismo orden.
    // Recorre la colección: si la lista está vacía, first y last apuntan al primer nodo;
    // si no, se enlaza el nuevo nodo al final (last.next) y se actualiza last.
    public EDDoubleLinkedListv2(Collection<T> col) {
        for (T elem: col) {
            Node n = new Node(elem);
            if (first == null)
                first = last = n;
            else {
                n.prev = last;      // el nuevo nodo mira hacia atrás a la antigua cola
                last.next = n;      // la antigua cola mira hacia adelante al nuevo
                last = n;           // el nuevo nodo pasa a ser la cola
            }
        }
        size = col.size();          // tamaño final igual al de la colección
    }

    // Invierte la lista in-place. Para cada nodo, intercambia sus punteros prev/next.
    // Al terminar, ajusta first y last: first pasa a ser la antigua cola y viceversa.
    public void reverse() {
        if (first != null && first.next != null) {
            Node actual = first;
            Node aux = null;
            // Recorremos y para cada nodo: prev <- next y next <- prev (intercambio)
            for(; actual != null; actual = actual.prev) {
                aux = actual.prev;      // guardamos prev antes de intercambiar
                actual.prev = actual.next;
                actual.next = aux;
            }
            // Tras el bucle, aux queda en el antiguo primer nodo; ajustamos extremos
            if (aux != null) {
                last = first;           // el antiguo first ahora es la cola
                first = aux.prev;       // el nuevo first es el que quedó antes del null
            }
        }
    }

    // Intercala elementos de 'lista' dentro de "this" de la siguiente forma:
    // - Si this está vacía: se copian todos los elementos de 'lista' en orden.
    // - Si this NO está vacía: se inserta un elemento de 'lista' después de cada nodo de this
    //   hasta que se agote 'lista' o los nodos de this. Si quedan elementos en 'lista', se anexan al final.
    public void shuffle(List<T> lista) {
        if (lista == null)
            throw new NullPointerException();

        // Caso A) this vacía: clonar 'lista' creando nuevos nodos y encadenándolos
        if (this.isEmpty()) {
            Iterator<T> it = lista.iterator();
            while (it.hasNext()) {
                Node n = new Node(it.next());
                if (first == null) {
                    first = n;         // primer nodo: inicializa cabeza y cola
                    last = n;
                } else {
                    n.prev = last;     // enlaza con la cola actual
                    last.next = n;
                    last = n;          // avanza la cola
                }
                size++;                 // actualiza el tamaño
            }
            return;
        }

        // Caso B) this no vacía: intercalar
        Node actual = first;            // puntero que recorre los nodos de this
        Iterator<T> it = lista.iterator();

        // Mientras haya nodos en this y elementos en lista, insertar uno de lista tras cada 'actual'
        while (actual != null && it.hasNext()) {
            T elem = it.next();
            Node nuevo = new Node(elem);
            Node siguiente = actual.next;   // guardamos el siguiente de 'actual' para reanudar luego
            // insertar 'nuevo' entre 'actual' y 'siguiente'
            nuevo.prev = actual;
            nuevo.next = siguiente;
            actual.next = nuevo;
            if (siguiente != null)
                siguiente.prev = nuevo;
            else
                last = nuevo;               // si no había siguiente, 'nuevo' pasa a ser la cola
            size++;
            actual = siguiente;             // saltamos al nodo original siguiente (no al nuevo) para intercalar correctamente
        }

        // Si aún quedan elementos en 'lista' después de intercalar, anexarlos al final
        while (it.hasNext()) {
            T elem = it.next();
            Node nuevo = new Node(elem);
            nuevo.prev = last;
            last.next = nuevo;
            last = nuevo;
            size++;
        }
    }

    // Conserva únicamente el segmento [inicio, fin) de la lista.
    // Normaliza los límites (satura por 0 y size); si el rango es vacío (fin <= inicio) limpia la lista.
    // Si el rango coincide con la lista completa devuelve false (no hay cambios). En otro caso, recoloca first/last
    // para que apunten a los extremos del segmento y ajusta los enlaces de borde.
    public boolean prune(int inicio, int fin) throws IndexOutOfBoundsException {
        int origInicio = inicio;
        int origFin = fin;

        if (inicio < 0) inicio = 0;        // saturar por abajo

        if (fin > size) fin = size;        // saturar por arriba

        // Ambos límites fuera por el mismo lado: error de índices
        if ((origInicio < 0 && origFin < 0) || (origInicio >= size && origFin >= size))
            throw new IndexOutOfBoundsException();

        // Rango vacío -> limpiar completamente
        if (fin <= inicio) {
            clear();
            return true;
        }

        // Rango cubre toda la lista -> no hay cambios
        if (inicio == 0 && fin == size)
            return false;

        // Avanzar hasta el nodo de inicio
        Node nInicio = first;

        for (int i = 0; i < inicio; i++)
            nInicio = nInicio.next;

        // Avanzar hasta el nodo fin (exclusivo)
        Node nFin = first;

        for (int i = 0; i < fin; i++)
            nFin = nFin.next;

        // Recortar: nuevo first es nInicio, y desconectar por delante
        first = nInicio;
        first.prev = null;

        if (nFin != null) {
            // Si hay nodo fin, su anterior será la nueva cola y se corta el enlace hacia delante
            last = nFin.prev;
            last.next = null;
        } else {
            // Si fin == size, la cola queda siendo el último del tramo
            while (last.next != null)
                last = last.next;
        }

        size = fin - inicio;               // nuevo tamaño exacto del tramo conservado
        return true;
    }

    // Elimina todos los nodos cuyo dato sea igual a 'elem'. Recorre la lista y, para cada coincidencia,
    // vuelve a enlazar prev y next saltándose el nodo actual. Actualiza first/last y size en cada borrado.
    public int removeAllElement(T elem) {
        if (first == null)
            return 0;

        Node actual = first;
        int removed = 0;

        while (actual != null) {
            if ((actual.data == null && elem == null) || (actual.data != null && actual.data.equals(elem))) {
                removed++;
                Node prev = actual.prev;
                Node actualSiguiente = actual.next;
                if (prev == null) {
                    first = actualSiguiente;        // si borramos la cabeza, mover first
                    if (actualSiguiente != null)
                        actualSiguiente.prev = null;
                } else {
                    prev.next = actualSiguiente;     // puenteamos el nodo actual
                    if (actualSiguiente != null)
                        actualSiguiente.prev = prev;
                }
                if (actualSiguiente == null)
                    last = prev;                     // si borramos la cola, retrocede last
                size--;
                actual = actualSiguiente;            // continuar desde el siguiente
            } else {
                actual = actual.next;                // avanzar si no hay borrado
            }
        }

        return removed;
    }

    // Elimina duplicados adyacentes (nodos consecutivos con datos iguales). Recorre desde la cabeza y,
    // cuando detecta un duplicado inmediato, quita el segundo y repara los enlaces; si borra la cola, ajusta 'last'.
    public int removeAdjacentDuplicates() {
        if (first == null || first.next == null)
            return 0;

        int cont = 0;
        Node actual = first;

        while (actual.next != null) {
            if ((actual.data == null && actual.next.data == null) || (actual.data != null && actual.data.equals(actual.next.data))) {
                Node borrar = actual.next;           // el duplicado inmediato
                Node borrarSiguiente = borrar.next;  // el que quedará después de eliminar 'borrar'
                actual.next = borrarSiguiente;       // saltar el duplicado
                if (borrarSiguiente != null)
                    borrarSiguiente.prev = actual;   // cerrar enlace hacia atrás
                else
                    last = actual;                   // si no había siguiente, hemos borrado la cola
                cont++;
                size--;
            } else {
                actual = actual.next;                // avanzar cuando no hay duplicado en esta posición
            }
        }
        return cont;
    }

    // Mantiene solo los elementos que están contenidos en 'otra'. Recorre y elimina los que no estén,
    // reparando enlaces y actualizando first/last/size. Si la lista queda vacía, reinicia ambos extremos.
    @Override
    public boolean retainAll(Collection<?> otra) {
        if (otra == null)
            throw new NullPointerException();

        if (first == null)
            return false;

        boolean modificado = false;
        Node actual = first;

        while (actual != null) {
            Node actualSiguiente = actual.next;      // guardamos el siguiente antes de potencialmente eliminar
            if (!otra.contains(actual.data)) {
                modificado = true;
                Node actualAnterior = actual.prev;
                if (actualAnterior == null) {
                    first = actualSiguiente;         // si borramos cabeza
                    if (actualSiguiente != null)
                        actualSiguiente.prev = null;
                } else {
                    actualAnterior.next = actualSiguiente; // puentea el eliminado
                    if (actualSiguiente != null)
                        actualSiguiente.prev = actualAnterior;
                    else
                        last = actualAnterior;       // si borramos la cola
                }
                size--;
            }
            actual = actualSiguiente;                // avanzar en cualquier caso
        }
        //No es necesario, pasa todos los tests
        if (size == 0) {
            first = null;
            last = null;
        }
        return modificado;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() {
        // Copia los elementos en un array iterando desde first hasta last
        Object[] v = new Object[size];
        Node n = first;
        int i = 0;
        while(n != null) {
            v[i] = n.data;
            n = n.next;
            i++;
        }
        return v;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        first = last = null;
        size = 0;
    }

    @Override
    public T get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        // Construye una representación [a, b, c]: size
        final StringBuilder sb = new StringBuilder();
        if (isEmpty())
            sb.append("[]");
        else {
            sb.append("[");
            Node ref = first;
            while (ref != null) {
                sb.append(ref.data);
                ref = ref.next;
                if (ref == null)
                    sb.append("]");
                else
                    sb.append(", ");
            }
        }
        sb.append(": ");
        sb.append(size);
        return sb.toString();
    }
}
