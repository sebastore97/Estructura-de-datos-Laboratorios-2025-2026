import org.junit.Assert;

import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class EDLinkedHashSetTest {

    final static private String vChar = "abcedefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String seleccionaCaracteres(String s, int[] filtro) {
        StringBuilder bf = new StringBuilder();

        for(int i: filtro) {
            i = i % s.length();
            bf.append(s.charAt(i));
        }
        return bf.toString();
    }

    private String seleccionaRandomCaracteres(String s, int[] filtro) {
        StringBuilder bf = new StringBuilder();

        Random num= new Random();
        for(int i: filtro) {
            //i = i % s.length();
            int pos=num.nextInt(vChar.length());
            bf.append(s.charAt(pos));
        }
        return bf.toString();
    }

    private List<String> generateStrings(int nElem) {
        Set<String> set = new HashSet<>();

        int[] actual = {0,1,2};
        int i = 0;
        while (set.size() < nElem) {
            String s = seleccionaCaracteres(vChar, actual);
            set.add(s);

            if (actual[0] == vChar.length()) {
                actual[0] = 0;
                actual[1] = i;
                actual[2] = 2*i;
            } else
            {
                actual[0]++;
                actual[1]++;
                actual[2]++;
            }
            i++;
        }

        return new LinkedList<>(set);
    }

    private List<String> generateRandomStrings(int nElem) {
        Set<String> set = new HashSet<>();

        int[] actual = {0,1,2};
        int i = 0;
        while (set.size() < nElem) {
            String s = seleccionaRandomCaracteres(vChar, actual);
            set.add(s);

        }

        return new LinkedList<>(set);
    }

    private <T> void checkTable(EDLinkedHashSet<T> s)
    {
        if (s.table.length != s.used.length) {
            System.out.println("  table[] y used[] tienen distinto tamaño (" + s.table.length + ", " + s.used.length +
                    ")");
            assertEquals(s.table.length, s.used.length);
        }

        int count=0;
        for(int i=0; i < s.table.length; i++) {
            if (s.table[i]!= null && !s.used[i]) {
                System.out.println("  table[ " + i + "] ocupado pero used[" + i + "] == false");
                assertFalse(s.table[i] != null && !s.used[i]);
            }
            if (s.used[i]) count++;
        }

        if (count != s.dirty) {
            System.out.println("  La cuenta de casillas usadas es incorrecta, debería ser " + count + " y es " + s.dirty);
            assertEquals(count, s.dirty);
        }

        if (s.dirty >= s.rehashThreshold) {
            System.out.println("  La ocpuación de la tabla (" + s.dirty + ") es mayor que el umbral de rehash (" + s.rehashThreshold +")");
        }
        assertTrue(s.dirty < s.rehashThreshold);

    }

    private <T> void checkElementsTable(EDLinkedHashSet<T> s) {
        for (int i = 0; i < s.table.length; i++) {
            if (s.used[i] && s.table[i] != null) {
                int code = s.hash(s.table[i].data);

                while (code != i) {
                    if (!s.used[code]) {
                        System.out.println("  El elemento " + s.table[i].data + " almacenado en table[" + i + "] no " +
                                "esta" + " almacenado en una posición alcanzable desde " + s.hash(s.table[i].data) + "por que " +
                                "table[" + code + "] en false");
                        Assert.fail();
                    }
                    code = (code + 1) % s.table.length;
                }
            }
        }
    }

    private <T> void checkOrderTable(EDLinkedHashSet<T> s, List<T> l) {
        T[] elements = (T[])s.toArray();

        System.out.println(  " Orden de los elementos " + Arrays.toString(elements) + ": " + elements.length);
        if (elements.length != l.size()) {
            System.out.println("  Los nodos no están bien enlazados, faltan elementos");
            Assert.fail();
        }

        for(int i=0; i < elements.length; i++) {
            if (!l.get(i).equals(elements[i])) {
                System.out.println("  El conjunto no mantiene el orden de insercíon, el elemento " + elements[i] + " " +
                        "en la posición " + i + " esta fuera de lugar");
                Assert.fail();
            }
        }
    }

    @org.junit.Test
    public void containsTest() {
        System.out.println("\nPROBANDO EL METODO contains()...");
        EDLinkedHashSet<String> set = new EDLinkedHashSet<>();

        int paquete = set.rehashThreshold - 1;
        List<String> lista = generateStrings(paquete*50);

        int cuenta = 1;
        //Caso set vacío
        List<String> caso = new LinkedList<>();
        set = new EDLinkedHashSet<>(caso);
        System.out.println("\nPRUEBA " + cuenta);
        System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
        System.out.println(set);
        String elem = "ZZZ";
        System.out.println("\ncontains(" + elem + ")");
        boolean esperado= caso.contains(elem);

        System.out.println("\nESTADO FINAL OBTENIDO");
        boolean obtenido = set.contains(elem);
        System.out.println("esperado: "+esperado);
        System.out.println("obtenido: "+obtenido);
        assertEquals(esperado, obtenido);
        cuenta++;

        //Caso 1 solo elemento, esta
        elem = "ZZZ";
        caso = new LinkedList<>();
        caso.add(elem);
        set = new EDLinkedHashSet<>(caso);
        System.out.println("\nPRUEBA " + cuenta);
        System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
        System.out.println(set);

        System.out.println("\ncontains(" + elem + ")");
        esperado= caso.contains(elem);

        System.out.println("\nESTADO FINAL OBTENIDO");
        obtenido = set.contains(elem);
        System.out.println("esperado: "+esperado);
        System.out.println("obtenido: "+obtenido);
        assertEquals(esperado, obtenido);
        cuenta++;

        //Caso 1 solo elemento, no esta
        elem = "ZZZ";
        caso = new LinkedList<>();
        caso.add(elem);
        set = new EDLinkedHashSet<>(caso);
        System.out.println("\nPRUEBA " + cuenta);
        System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
        System.out.println(set);

        String elem2="XXX";
        System.out.println("\ncontains(" + elem2 + ")");
        esperado= caso.contains(elem2);

        System.out.println("\nESTADO FINAL OBTENIDO");
        obtenido = set.contains(elem2);
        System.out.println("esperado: "+esperado);
        System.out.println("obtenido: "+obtenido);
        assertEquals(esperado, obtenido);
        cuenta++;

        for (int i = 0; i < 50; i++) {
            caso = new LinkedList<>(lista.subList(paquete * i, paquete * (i + 1)));
            set = new EDLinkedHashSet<>(caso);


            System.out.println("\nPRUEBA " + cuenta);
            System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
            System.out.println(set);
            elem = "ZZZ";
            System.out.println("\ncontains(" + elem + ")");
            esperado= caso.contains(elem);

            obtenido= set.contains(elem);

            System.out.println("esperado: "+esperado);
            System.out.println("obtenido: "+obtenido);
            assertEquals(esperado, obtenido);

            cuenta++;

            for (int j=0; j<10; j++) {
                System.out.println("\nPRUEBA " + cuenta);
                System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
                System.out.println(set);
                elem = caso.get((cuenta * 13) % caso.size());
                System.out.println("\ncontains(" + elem + ")");
                esperado = caso.contains(elem);
                obtenido = set.contains(elem);
                System.out.println("esperado: "+esperado);
                System.out.println("obtenido: "+obtenido);
                assertEquals(esperado, obtenido);

                cuenta++;
            }
        }
    }


    @org.junit.Test
    public void removeTest() {
        System.out.println("\nPROBANDO EL METODO remove()...");
        EDLinkedHashSet<String> set = new EDLinkedHashSet<>();

        int paquete = set.rehashThreshold - 1;
        List<String> lista = generateStrings(paquete*50);

        int cuenta = 1;

        //caso conjunto vacío
        List<String> caso = new LinkedList<>();
        set = new EDLinkedHashSet<>(caso);
        System.out.println("\nPRUEBA " + cuenta);
        System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
        System.out.println(set);
        String elem = "AAA";
        System.out.println("\nremove(" + elem + ")");
        caso.remove(elem);

        System.out.println("\nORDEN FINAL ESPERADO");
        System.out.println(caso + ": " + caso.size());

        System.out.println("\nESTADO FINAL OBTENIDO");
        set.remove(elem);
        System.out.println(set);

        checkTable(set);
        checkElementsTable(set);
        checkOrderTable(set, caso);

        cuenta++;

        //CASO HASHSET CON UN SOLO ELEMENTO
        elem= "AAA";
        caso = new LinkedList<>();
        caso.add(elem);
        set = new EDLinkedHashSet<>(caso);
        System.out.println("\nPRUEBA " + cuenta);
        System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
        System.out.println(set);
        System.out.println("\nremove(" + elem + ")");
        caso.remove(elem);

        System.out.println("\nORDEN FINAL ESPERADO");
        System.out.println(caso + ": " + caso.size());

        System.out.println("\nESTADO FINAL OBTENIDO");
        set.remove(elem);
        System.out.println(set);

        checkTable(set);
        checkElementsTable(set);
        checkOrderTable(set, caso);

        cuenta++;

        for (int i = 0; i < 50; i++) {
            caso = new LinkedList<>(lista.subList(paquete * i, paquete * (i + 1)));
            set = new EDLinkedHashSet<>(caso);

            while (!caso.isEmpty()) {
                System.out.println("\nPRUEBA " + cuenta);
                System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
                System.out.println(set);
                elem = caso.get((cuenta * 13) % caso.size());
                System.out.println("\nremove(" + elem + ")");
                caso.remove(elem);

                System.out.println("\nORDEN FINAL ESPERADO");
                System.out.println(caso + ": " + caso.size());

                System.out.println("\nESTADO FINAL OBTENIDO");
                set.remove(elem);
                System.out.println(set);

                checkTable(set);
                checkElementsTable(set);
                checkOrderTable(set, caso);

                cuenta++;
            }
        }
    }

    @org.junit.Test
    public void removeRehash() {
        System.out.println("\nPROBANDO EL METODO rehash()...");
        EDLinkedHashSet<String> set = new EDLinkedHashSet<>();

        int paquete = set.rehashThreshold - 1;
        List<String> lista = generateStrings(paquete*90);

        int cuenta = 1;
        for (int i = 0; i < 30; i++) {
            List<String> caso = new LinkedList<>(lista.subList(paquete * i, paquete * (i + 3)));
            set = new EDLinkedHashSet<>();

            System.out.println("\nPRUEBA " + cuenta);
            System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
            System.out.println(set);

            System.out.println("addAll(" + caso + ")");
            set.addAll(caso);

            System.out.println("\nESTADO FINAL OBTENIDO");
            System.out.println(set);

            checkTable(set);
            checkElementsTable(set);
            checkOrderTable(set, caso);

            cuenta++;

        }
    }

    @org.junit.Test
    public void addTest() {
        System.out.println("\nPROBANDO EL METODO add()...");
        EDLinkedHashSet<String> set = new EDLinkedHashSet<>();



        int cuenta = 1;
        List<String> listaElem = generateStrings(4);

        set = new EDLinkedHashSet<>();
        List<String> caso = new LinkedList<>();
        for (String elem : listaElem) {
            System.out.println("\nPRUEBA " + cuenta);
            System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
            System.out.println(set);
            //int pos = (int) (Math.random()*vChar.length());
            //String elem = caso.get((cuenta * 13) % caso.size());
            System.out.println("\nadd(" + elem + ")");
            if (!caso.contains(elem))
                caso.add(elem);

            System.out.println("\nORDEN FINAL ESPERADO");
            System.out.println(caso + ": " + caso.size());

            System.out.println("\nESTADO FINAL OBTENIDO");
            set.add(elem);
            System.out.println(set);

            checkTable(set);
            checkElementsTable(set);
            checkOrderTable(set, caso);

            cuenta++;
        }

        int paquete = set.rehashThreshold - 1;
        List<String> lista = generateStrings(paquete * 50);

        //List<String> caso = new LinkedList<>(lista.subList(paquete * i, paquete * (i + 1)));

        //set = new EDLinkedHashSet<>(caso);

        for (int i = 0; i < 50; i++) {
            //List<String> caso = new LinkedList<>(lista.subList(paquete * i, paquete * (i + 1)));
            caso = new LinkedList<>(lista.subList(paquete * i, paquete * (i + 1)));
            set = new EDLinkedHashSet<>(caso);

            //List<String> listaElem = generateStrings(4);
            listaElem = generateStrings(4);

            //while (!caso.isEmpty()) {
            for (String elem : listaElem) {
                System.out.println("\nPRUEBA " + cuenta);
                System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
                System.out.println(set);
                //int pos = (int) (Math.random()*vChar.length());
                //String elem = caso.get((cuenta * 13) % caso.size());
                System.out.println("\nadd(" + elem + ")");
                if (!caso.contains(elem))
                    caso.add(elem);

                System.out.println("\nORDEN FINAL ESPERADO");
                System.out.println(caso + ": " + caso.size());

                System.out.println("\nESTADO FINAL OBTENIDO");
                set.add(elem);
                System.out.println(set);

                checkTable(set);
                checkElementsTable(set);
                checkOrderTable(set, caso);

                cuenta++;
            }
        }
    }

    @org.junit.Test
    public void retainAllTest() {
        System.out.println("\nPROBANDO EL METODO retainAll()...");
        EDLinkedHashSet<String> set1 = new EDLinkedHashSet<>();
        EDLinkedHashSet<String> set2 = new EDLinkedHashSet<>();
        //int paquete = set.rehashThreshold - 1;
        //List<String> lista = generateStrings(paquete*50);


        int cuenta = 1;
        List<String> listaElem = generateStrings(4);

        set1 = new EDLinkedHashSet<>();
        List<String> casoA = new LinkedList<>();
        List<String> casoB = new LinkedList<>();
        System.out.println("\nPRUEBA " + cuenta);
        System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
        System.out.println(set1);
        System.out.println("\nset1.retainAll(set2)");
        System.out.println("  set2: "+set2);
        System.out.println("\nORDEN FINAL ESPERADO");
        System.out.println(casoA + ": " + casoA.size());

        System.out.println("\nESTADO FINAL OBTENIDO");
        set1.retainAll(set2);
        System.out.println(set1);

        checkTable(set1);
        checkElementsTable(set1);
        checkOrderTable(set1, casoA);

        cuenta++;

        casoB=new LinkedList<>(listaElem);
        set2 = new EDLinkedHashSet<>(casoB);
        System.out.println("\nPRUEBA " + cuenta);
        System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
        System.out.println(set1);
        System.out.println("\nset1.retainAll(set2)");
        System.out.println("  set2: "+set2);
        System.out.println("\nORDEN FINAL ESPERADO");
        System.out.println(casoA + ": " + casoA.size());

        System.out.println("\nESTADO FINAL OBTENIDO");
        set1.retainAll(set2);
        System.out.println(set1);

        checkTable(set1);
        checkElementsTable(set1);
        checkOrderTable(set1, casoA);

        cuenta++;

        casoA=new LinkedList<>();
        casoB=new LinkedList<>();
        set2 = new EDLinkedHashSet<>(casoB);
        set1 = new EDLinkedHashSet<>(listaElem);
        System.out.println("\nPRUEBA " + cuenta);
        System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
        System.out.println(set1);
        System.out.println("\nset1.retainAll(set2)");
        System.out.println("  set2: "+set2);
        System.out.println("\nORDEN FINAL ESPERADO");
        System.out.println(casoA + ": " + casoA.size());

        System.out.println("\nESTADO FINAL OBTENIDO");
        set1.retainAll(set2);
        System.out.println(set1);

        checkTable(set1);
        checkElementsTable(set1);
        checkOrderTable(set1, casoA);

        cuenta++;


        int paquete = set1.rehashThreshold - 1;
        List<String> lista = generateStrings(paquete*50);

        //Test conjuntos iguales
        for (int i = 1; i < 5; i++) {
            //List<String> caso = new LinkedList<>(lista.subList(paquete * i, paquete * (i + 1)));
            casoA = new LinkedList<>(lista.subList(paquete * i, paquete * (i + 1)));
            set1 = new EDLinkedHashSet<>(casoA);

            casoB = new LinkedList<>(lista.subList(paquete*(i-1), paquete*i));
            set2 = new EDLinkedHashSet<>(casoB);

            //List<String> listaElem = generateStrings(4);
            listaElem = generateStrings(4);

            System.out.println("\nPRUEBA " + cuenta);
            System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
            System.out.println(set1);
            //int pos = (int) (Math.random()*vChar.length());
            //String elem = caso.get((cuenta * 13) % caso.size());
            System.out.println("\nset1.retainAll(set2)");

            casoA.retainAll(casoB);
            System.out.println("\nORDEN FINAL ESPERADO");
            System.out.println(casoA + ": " + casoA.size());

            System.out.println("\nESTADO FINAL OBTENIDO");
            set1.retainAll(set2);
            System.out.println(set1);

            checkTable(set1);
            checkElementsTable(set1);
            checkOrderTable(set1, casoA);

            cuenta++;

        }

        Random num = new Random();

        for (int i = 0; i < 10; i++) {
            //List<String> caso = new LinkedList<>(lista.subList(paquete * i, paquete * (i + 1)));
            casoA = new LinkedList<>(lista.subList(paquete * i, paquete * (i + 1)));
            set1 = new EDLinkedHashSet<>(casoA);


            for (int j=0; j<casoA.size(); j++) {
                List<String> copia = new LinkedList<>(casoA);
                casoB = new LinkedList<>(casoA.subList(j, casoA.size()));
                set2 = new EDLinkedHashSet<>(casoB);


                System.out.println("\nPRUEBA " + cuenta);
                System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
                System.out.println(set1);
                System.out.println("\nset1.retainAll(set2)");
                //System.out.println("   set2: " + set2);
                //Para simplificar la visualización
                System.out.println("    set2: " + casoB);

                casoA.retainAll(casoB);
                System.out.println("\nORDEN FINAL ESPERADO");
                System.out.println(casoA + ": " + casoA.size());

                System.out.println("\nESTADO FINAL OBTENIDO");
                set1.retainAll(set2);
                System.out.println(set1);

                checkTable(set1);
                checkElementsTable(set1);
                checkOrderTable(set1, casoA);

                cuenta++;
                casoA.clear();
                casoA.addAll(copia);

            }
        }
    }


}
