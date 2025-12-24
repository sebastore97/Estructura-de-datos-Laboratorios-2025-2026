import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class EDLinkedHashSet<T> implements Set<T> {
    protected class Node<U>{
        U data;
        Node<U> next = null;
        Node<U> prev = null;

        Node(U data) {
            this.data = data;
        }
    }

    static final private int DEFAULT_CAPACITY = 10;
    static final private int DEFAULT_THRESHOLD = 7;

    Node<T>[] table;
    boolean[] used;
    int size = 0;
    int dirty = 0;
    int rehashThreshold;
    Node<T> first = null;
    Node<T> last = null;

    public EDLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        used = new boolean[DEFAULT_CAPACITY];
        rehashThreshold = DEFAULT_THRESHOLD;
    }

    public EDLinkedHashSet(Collection<T> col) {
        table = new Node[DEFAULT_CAPACITY];
        used = new boolean[DEFAULT_CAPACITY];
        rehashThreshold = DEFAULT_THRESHOLD;

        addAll(col);
    }

    /**
     * @param item  Un valor cualquiera, distinto de <code>null</code>.
     * @return Código de disersión ajustado al tamaño de la tabla.
     */
    int hash(T item) {
        return (item.hashCode() & Integer.MAX_VALUE) % table.length;
    }


    @Override
    public boolean add(T item) {
        if (item == null)
            return false;

        int hashIndex = hash(item);
        int insertionIndex = -1;

        while (used[hashIndex]) {
            if (table[hashIndex] == null) {
                if (insertionIndex == -1)
                    insertionIndex = hashIndex;
            } else if (table[hashIndex].data.equals(item)) {
                return false;
            }

            hashIndex = (hashIndex + 1) % table.length;
        }

        if (insertionIndex != -1)
            hashIndex = insertionIndex;

        Node<T> n = new Node<>(item);

        if (last != null) {
            last.next = n;
            n.prev = last;
            last = n;
        } else {
            first = n;
            last = n;
        }

        table[hashIndex] = n;

        if (!used[hashIndex]) {
            used[hashIndex] = true;
            dirty++;
        }

        size++;

        rehash();

        return true;
    }

    @Override
    public boolean contains(Object item) {
        if (item == null)
            return false;

        int hashIndex = hash((T) item);

        while (used[hashIndex]) {
            Node<T> actual = table[hashIndex];
            if (actual != null && actual.data.equals(item))
                return true;
            hashIndex = (hashIndex + 1) % table.length;
        }

        return false;
    }

    private void rehash() {
        if (dirty < rehashThreshold)
            return;

        int nCapacity = table.length * 2;
        Node<T>[] nTable = new Node[nCapacity];
        boolean[] nUsed = new boolean[nCapacity];
        table = nTable;
        used = nUsed;
        rehashThreshold *= 2;
        dirty = size;
        Node<T> actual = first;

        while (actual != null) {
            int hashIndex = (actual.data.hashCode() & Integer.MAX_VALUE) % nCapacity;
            while (nUsed[hashIndex])
                hashIndex = (hashIndex + 1) % nCapacity;
            nTable[hashIndex] = actual;
            nUsed[hashIndex] = true;
            actual = actual.next;
        }
    }

    @Override
    public boolean remove(Object item) {
        if (item == null || size == 0)
            return false;

        int hashIndex = hash((T) item);

        while (used[hashIndex]) {
            Node<T> actual = table[hashIndex];
            if (actual != null && actual.data.equals(item)) {
                if (actual.prev != null)
                    actual.prev.next = actual.next;
                else first = actual.next;
                if (actual.next != null)
                    actual.next.prev = actual.prev;
                else last = actual.prev;
                table[hashIndex] = null;
                size--;
                return true;
            }
            hashIndex = (hashIndex + 1) % table.length;
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null)
            throw new NullPointerException();

        boolean modified = false;
        Node<T> actual = first;

        while (actual != null) {
            Node<T> nextNode = actual.next;
            if (!c.contains(actual.data)) {
                remove(actual.data);
                modified = true;
            }
            actual = nextNode;
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
    public Iterator<T> iterator()  { throw new UnsupportedOperationException(); }


    @Override
    public Object[] toArray() {
        Object[] v = new Object[size];

        Node<T> ref = first;
        int i = 0;
        while (ref != null) {
            v[i] = ref.data;
            ref = ref.next;
            i++;
        }

        return v;
    }

    @Override
    public <T1> T1[] toArray(T1[] a)  {
        throw new UnsupportedOperationException();
    }



    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {

            if (c == null)
                throw new NullPointerException();

            if (c.size() == 0)
                return false;

            int s = size;
            for (T item : c)
                add(item);

            return size != s;


    }

    @Override
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }


    @Override
    public void clear() {
        size = dirty  = 0;
        first = last = null;

        for (int i=0; i < table.length; i++) {
            used[i] = false;
            table[i] = null;
        }
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Table: {");
        boolean coma = false;
        for (int i = 0; i < table.length; i++){
            if (table[i] != null) {
                if (coma)
                    sb.append(", ");
                sb.append(i + ": " + table[i].data);
                coma = true;
            }
        }

        sb.append("}\n");

        sb.append("Insertion order: {");
        Node ref = first;
        coma = false;
        while (ref != null) {
            if (coma)
                sb.append(", ");
            sb.append(ref.data);
            ref = ref.next;
            coma = true;
        }
        sb.append("}\n");
        sb.append("size: " + size);
        sb.append(", capacity: " + table.length);
        sb.append(", rehashThreshold: " + rehashThreshold);

        return sb.toString();
    }

}
