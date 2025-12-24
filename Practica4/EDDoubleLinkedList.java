import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class EDDoubleLinkedList<T> implements List<T> {
    private class Node {
        private T data;
        private Node next;
        private Node prev;

        public Node(T data) {
            this.data = data;
        }
    }

    private Node first = null;
    private Node last = null;
    private int size = 0;

    public EDDoubleLinkedList(Collection<T> col) {
        for (T elem: col) {
            Node n = new Node(elem);
            if (first == null)
                first = last = n;
            else {
                n.prev = last;
                last.next = n;
                last = n;
            }
        }
        size = col.size();
    }

    public void reverse() {
        // Caso base: si la lista está vacía o tiene solo un elemento, no hay nada que hacer
        if (first == null || first.next == null)
            return;

        Node current = first;
        Node temp = null; // lo usaremos para intercambiar prev y next

        // Recorremos toda la lista
        while (current != null) {
            // Intercambiamos next y prev de cada nodo
            temp = current.prev;
            current.prev = current.next;
            current.next = temp;

            // Avanzamos: después del intercambio, el siguiente es el "prev" original
            current = current.prev;
        }

        // Después del bucle, temp apunta al antiguo "prev" del primer nodo,
        // es decir, al nuevo primer nodo de la lista
        if (temp != null) {
            last = first;
            first = temp.prev; // temp.prev es el nuevo first
        }
    }


    public void shuffle(List<T> lista) {
        if (lista == null)
            throw new NullPointerException("La lista proporcionada es null");

        // Si la lista externa está vacía, no hay nada que mezclar
        if (lista.isEmpty())
            return;

        if (this.isEmpty()) {
            Iterator<T> it = lista.iterator();
            while (it.hasNext()) {
                Node n = new Node(it.next());
                if (first == null) {
                    first = last = n;
                } else {
                    n.prev = last;
                    last.next = n;
                    last = n;
                }
                size++;
            }
            return;
        }

        Node current = first; // nodo actual de la lista local
        Iterator<T> it = lista.iterator(); // para recorrer la lista externa

        // Mientras haya elementos en ambas listas
        while (current != null && it.hasNext()) {
            T elem = it.next(); // siguiente elemento de lista
            Node nuevo = new Node(elem); // nuevo nodo a insertar

            // Guardamos referencia al siguiente nodo de la lista local
            Node siguiente = current.next;

            // Insertamos el nuevo nodo entre current y siguiente
            nuevo.prev = current;
            nuevo.next = siguiente;
            current.next = nuevo;

            if (siguiente != null)
                siguiente.prev = nuevo;
            else
                last = nuevo; // si llegamos al final, actualizamos last

            size++; // aumentamos tamaño
            current = siguiente; // saltamos al siguiente original
        }

        // Si todavía quedan elementos en la lista externa, los añadimos al final
        while (it.hasNext()) {
            T elem = it.next();
            Node nuevo = new Node(elem);
            nuevo.prev = last;
            last.next = nuevo;
            last = nuevo;
            size++;
        }
    }


    public boolean prune(int inicio, int fin) throws IndexOutOfBoundsException {
        if ((inicio < 0 && fin < 0) || (inicio >= size && fin >= size))
            throw new IndexOutOfBoundsException();

        if (inicio < 0) inicio = 0;

        if (fin > size) fin = size;


        if (fin <= inicio) {
            clear();
            return true;
        }

        if (inicio == 0 && fin == size)
            return false;

        Node nInicio = first;

        for (int i = 0; i < inicio; i++)
            nInicio = nInicio.next;

        Node nFin = first;

        for (int i = 0; i < fin; i++)
            nFin = nFin.next;

        first = nInicio;
        first.prev = null;

        if (nFin != null) {
            last = nFin.prev;
            last.next = null;
        } else {
            while (last.next != null)
                last = last.next;
        }

        size = fin - inicio;

        return true;
    }

    public int removeAllElement(T elem) {
        if (first == null)      // lista vacía
            return 0;

        Node current = first;
        int removed = 0;

        while (current != null) {

            if ((current.data == null && elem == null) || (current.data != null && current.data.equals(elem))) {

                removed++;

                Node prev = current.prev;
                Node next = current.next;

                // Caso 1: es el primer nodo
                if (prev == null) {
                    first = next;  // avanzamos first
                    if (next != null)
                        next.prev = null;
                }
                // Caso 2: es un nodo intermedio
                else {
                    prev.next = next;
                    if (next != null)
                        next.prev = prev;
                }

                // Caso 3: era el último nodo
                if (next == null) {
                    last = prev;
                }

                size--;
                current = next;  // avanzar tras eliminar
            }
            else {
                current = current.next;  // avanzar si no eliminamos
            }
        }

        return removed;
    }


    public int removeAdjacentDuplicates() {

        if (first == null || first.next == null)
            return 0; // no hay nada que eliminar

        int removed = 0;
        Node current = first;

        while (current != null && current.next != null) {

            // Si hay duplicado adyacente
            if ((current.data == null && current.next.data == null) ||
                    (current.data != null && current.data.equals(current.next.data))) {

                Node toDelete = current.next;
                Node nextNode = toDelete.next;

                // Enlazar current con el siguiente de toDelete
                current.next = nextNode;

                if (nextNode != null)
                    nextNode.prev = current;
                else
                    last = current;   // hemos eliminado al último nodo

                removed++;
                size--;

                // NO AVANZAMOS current
                // porque nextNode puede ser otro duplicado
            }
            else {
                current = current.next;  // avanzar si no hay duplicado
            }
        }

        return removed;
    }


    @Override
    public boolean retainAll(Collection<?> otra) {

        if (otra == null)
            throw new NullPointerException();

        // Si this está vacía no hay nada que cambiar
        if (first == null)
            return false;

        boolean modified = false;
        Node current = first;

        while (current != null) {

            // Guardamos el siguiente nodo antes de posibles eliminaciones
            Node next = current.next;

            // Si current.data NO está en 'otra', debemos eliminarlo
            if (!otra.contains(current.data)) {

                modified = true;  // porque vamos a cambiar la lista

                Node prev = current.prev;

                // Caso 1: eliminamos el primer nodo
                if (prev == null) {
                    first = next;
                    if (next != null)
                        next.prev = null;
                }
                // Caso 2: eliminamos un nodo intermedio o último
                else {
                    prev.next = next;
                    if (next != null)
                        next.prev = prev;
                    else
                        last = prev; // eliminamos el último nodo
                }

                size--;
            }

            current = next; // avanzar al siguiente nodo
        }

        // Ajuste final: si la lista ha quedado vacía (no es necesario, pasa todos los tests)
        if (size == 0) {
            first = null;
            last = null;
        }

        return modified;
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
