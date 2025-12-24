//import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EDDoubleLinkedListTest {

    static private <T> List<List<T>> permutaciones(List<T> vec) {
        List<List<T>> resultado = new LinkedList<>();

        if (vec.size() > 0) {
            List<T> aux = new LinkedList<>();
            aux.add(vec.get(0));
            resultado.add(aux);

            for (int i = 1; i < vec.size(); i++) {
                while (resultado.get(0).size() == i) {
                    for (int k = 0; k <= resultado.get(0).size(); k++) {
                        aux = new LinkedList<>(resultado.get(0));
                        aux.add(k, vec.get(i));
                        resultado.add(aux);
                    }
                    resultado.remove(0);
                }
            }
        } else {
            List<T> aux = new LinkedList<>();
            resultado.add(aux);
        }
        return resultado;
    }

    static private boolean[] crearMascara(int talla){
        return new boolean[talla];
    }

    static private boolean incrementaMascara(boolean mascara[]) {
        boolean propagar = false;
        int pos = 0;
        do {
            if (mascara[pos] == true) {
                mascara[pos] = false;
                propagar = true;
            } else {
                mascara[pos] = true;
                propagar = false;
            }
            pos++;
        } while (propagar && (pos < mascara.length));

        return (!propagar || pos != mascara.length);
    }

    static private <T> List<List<T>> todasSublistas(List<T> semilla) {
        List<List<T>> resultado = new LinkedList<>();

        boolean mascara[] = crearMascara(semilla.size());

        do {
            List<T> aux = new LinkedList<>();
            for(int i= 0; i < mascara.length; i++) {
                if (mascara[i])
                    aux.add(semilla.get(i));
            }
            resultado.add(aux);

        } while(incrementaMascara(mascara));

        return resultado;

    }

    static private <T> List<List<T>> permutacionesCompletas(List<T> semilla){
        List<List<T>> resultado = new LinkedList<>();

        List<List<T>> sublistas = todasSublistas(semilla);
        for(List<T> sub: sublistas)
            resultado.addAll(permutaciones(sub));


        return resultado;
    }

    static private <T> List<T> aplanar(List<List<T>> listas) {
        List<T> resultado = new LinkedList<>();

        for(List<T> l: listas)
            resultado.addAll(l);

        return resultado;
    }

    static private <T> List<List<T>> convertirMatriz(T[][] vec)
    {
        List<List<T>> resultado = new LinkedList<>();
        for (T[] elem: vec)
            resultado.add(Arrays.asList(elem));

        return resultado;
    }

    private String[][] vSemillasResverseTest = {{"A"}, {"B", "C"}, {"X", "Y"}, {"D", "E", "F"}};

    static private List<String> invierteLista(List<String> l) {
        List<String> resultado = new LinkedList<>();
        for (String elem: l)
            resultado.add(0,elem);

        return resultado;
    }

    @org.junit.Test
    public void reverseTest () {
        List<List<String>> aux = convertirMatriz(vSemillasResverseTest);

        List<List<List<String>>> permutaciones = permutacionesCompletas(aux);

        int cuenta = 0;
        for (List<List<String>> perm: permutaciones) {
            List<String> caso = aplanar(perm);
            List<String> esperado = invierteLista(caso);

            for(int i = 0; i <= caso.size(); i++) {
                EDDoubleLinkedList<String> actual = new EDDoubleLinkedList<>(caso);

                System.out.println("\nPRUEBA " + cuenta);
                System.out.println("ESTADO INCIAL DE LA LISTA");
                System.out.println("  " + actual);
                System.out.println("ESTADO FINAL ESPERADO");
                System.out.println("  " + esperado + ": " + esperado.size());

                actual.reverse();
                System.out.println("ESTADO FINAL OBTENIDO:");
                System.out.println("  " + actual);

                //boolean iguales = Arrays.equals(esperado.toArray(), actual.toArray());
                assertTrue(Arrays.equals(esperado.toArray(), actual.toArray()));
                cuenta++;
            }
        }

        System.out.println("Se han probado " + cuenta + " casos\nFIN");
    }

    static private String[][] v1SemillasShuffleTest = {{"A"}, {"B", "C"}, {"D", "E", "F"}};
    static private String[]   v2SemillasShuffleTest = {"a", "b", "c", "d"};

    static private <T> List<T> calcularRessultadShuffle(List<T> l1, List<T> l2) {
        List<T> resultado = new ArrayList<>();

        int i1 = 0, i2 = 0;

        for (int j = 0; j < (l1.size() + l2.size()); j++)
            if (j%2 == 0)
                if (i1 < l1.size()) {
                    resultado.add(l1.get(i1));
                    i1++;
                } else {
                    resultado.add(l2.get(i2));
                    i2++;
                }
            else
                if (i2 < l2.size()) {
                    resultado.add(l2.get(i2));
                    i2++;
                } else {
                    resultado.add(l1.get(i1));
                    i1++;
                }


        return resultado;
    }

    @org.junit.Test
    public void shuffleTest() {
        List<List<String>> aux1 = convertirMatriz(v1SemillasShuffleTest);
        List<List<List<String>>> permutaciones1 = permutacionesCompletas(aux1);

        List<List<String>> permutaciones2 = permutacionesCompletas(Arrays.asList(v2SemillasShuffleTest));

        int cuenta = 1;
        for (List<List<String>> perm1: permutaciones1)
            for (List<String> caso1: permutaciones2 ) {
                List<String> caso2 = aplanar(perm1);

                EDDoubleLinkedList<String> actual = new EDDoubleLinkedList<>(caso1);

                System.out.println("\nPRUEBA " + cuenta);
                System.out.println("ESTADO INCIAL");
                System.out.println("  this:" + actual);
                System.out.println("  otra:" + caso2);
                System.out.println("  this.shuffle(otra)");

                List<String> esperado = calcularRessultadShuffle(caso1, caso2);
                System.out.println("ESTADO FINAL ESPERADO");
                System.out.println("  " + esperado + ": " + esperado.size());

                actual.shuffle(caso2);
                System.out.println("ESTADO FINAL OBTENIDO:");
                System.out.println("  " + actual);

                assertTrue(Arrays.equals(esperado.toArray(), actual.toArray()));

                cuenta++;
            }


    }

    @org.junit.Test()
    public void testExceptionShuffle() {

        List<List<String>> permutaciones2 = permutacionesCompletas(Arrays.asList(v2SemillasShuffleTest));

        int cuenta = 1;
        for (List<String> caso1 : permutaciones2) {

            EDDoubleLinkedList<String> actual = new EDDoubleLinkedList<>(caso1);

            System.out.println("\nPRUEBA " + cuenta);
            System.out.println("ESTADO INCIAL");
            System.out.println("  this:" + actual);
            System.out.println("  otra: null");
            System.out.println("  this.shuffle(otra)");

            System.out.println("ESTADO FINAL ESPERADO: NullPointerException");

            boolean excepcion = false;
            try {
                actual.shuffle(null);
            } catch (NullPointerException e) {
                System.out.println(" ...OK");
                excepcion = true;
            }

            assertTrue("Excepcion no lanzada", excepcion);
            cuenta++;

        }
    }

    static private <T> List<T> calcularResultadoRetain(List<T> l1, List<T> l2) {
        List<T> resultado = new ArrayList<>(l1);

        resultado.retainAll(l2);

        return resultado;
    }

    @org.junit.Test
    public void retainAllTest() {
        List<List<String>> aux = convertirMatriz(vSemillasResverseTest);

        List<List<List<String>>> permutaciones = permutacionesCompletas(aux);


        int cuenta = 1;
        for (List<List<String>> perm1: permutaciones)
            for (List<String> caso1: perm1 ) {
                List<String> caso2 = aplanar(perm1);

                EDDoubleLinkedList<String> actual = new EDDoubleLinkedList<>(caso1);

                System.out.println("\nPRUEBA " + cuenta);
                System.out.println("ESTADO INICIAL");
                System.out.println("  this:" + actual);
                System.out.println("  otra:" + caso2);
                System.out.println("  this.retainAll(otra)");

                List<String> esperado = calcularResultadoRetain(caso1, caso2);
                System.out.println("ESTADO FINAL ESPERADO");
                System.out.println("  " + esperado + ": " + esperado.size());

                actual.retainAll(caso2);
                System.out.println("ESTADO FINAL OBTENIDO:");
                System.out.println("  " + actual);

                assertTrue(Arrays.equals(esperado.toArray(), actual.toArray()));

                cuenta++;
            }
        for (List<List<String>> perm1: permutaciones)
            for (List<String> caso1: perm1 ) {
                //List<String> caso2 = aplanar(perm1);

                EDDoubleLinkedList<String> actual = new EDDoubleLinkedList<>(caso1);

                System.out.println("\nPRUEBA " + cuenta);
                System.out.println("ESTADO INICIAL");
                System.out.println("  this:" + actual);
                System.out.println("  otra:" + caso1);
                System.out.println("  this.retainAll(otra)");

                List<String> esperado = calcularResultadoRetain(caso1, caso1);
                System.out.println("ESTADO FINAL ESPERADO");
                System.out.println("  " + esperado + ": " + esperado.size());

                actual.retainAll(caso1);
                System.out.println("ESTADO FINAL OBTENIDO:");
                System.out.println("  " + actual);

                assertTrue(Arrays.equals(esperado.toArray(), actual.toArray()));

                cuenta++;
            }
        for (List<List<String>> perm1: permutaciones)
            for (List<String> caso1: perm1 ) {
                List<String> caso2 = new ArrayList<>();

                EDDoubleLinkedList<String> actual = new EDDoubleLinkedList<>(caso1);

                System.out.println("\nPRUEBA " + cuenta);
                System.out.println("ESTADO INICIAL");
                System.out.println("  this:" + actual);
                System.out.println("  otra:" + caso2);
                System.out.println("  this.retainAll(otra)");

                List<String> esperado = calcularResultadoRetain(caso1, caso2);
                System.out.println("ESTADO FINAL ESPERADO");
                System.out.println("  " + esperado + ": " + esperado.size());

                actual.retainAll(caso2);
                System.out.println("ESTADO FINAL OBTENIDO:");
                System.out.println("  " + actual);

                assertTrue(Arrays.equals(esperado.toArray(), actual.toArray()));

                cuenta++;
            }


    }

    @org.junit.Test()
    public void testExceptionPrune() {
        List<List<String>> casos = new LinkedList<>();
        for (int i = 0; i<10; i++) {
            List<String> l = new LinkedList<>();
            for (int j = 0; j < i; j++)
                l.add(Character.toString('A' + j) );
            casos.add(l);
        }

        int cuenta = 1;
        for (List<String> caso : casos) {
            EDDoubleLinkedList<String> actual = new EDDoubleLinkedList<>(caso);
            int limites[][] = { {-5,-1},
                    {-1, -1},
                    {caso.size(), caso.size()},
                    {caso.size()+1, caso.size()+3}};

            for (int i = 0; i < limites.length; i++) {
                //for (int j = 0; j < limites[i].length; j++) {
                    int indiceInicial = limites[i][0];
                    int indiceFinal = limites[i][1];
                    System.out.println("\nPRUEBA " + cuenta);
                    System.out.println("ESTADO INICIAL");
                    System.out.println("  this = " + actual);
                    System.out.println("  this.prune(" + indiceInicial + ", " + indiceFinal + ")");

                    System.out.println("ESTADO FINAL ESPERADO: ArrayOutBoundException");

                    boolean excepcion = false;
                    try {
                        actual.prune(indiceInicial, indiceFinal);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println(" ...OK");
                        excepcion = true;
                    }

                    assertTrue("Excepcion no lanzada", excepcion);
                    cuenta++;
                }
        }
    }


    private List<String> calcularResultadoPrune (List<String> caso, int inicio, int finali) {
        List<String> res=new ArrayList<>();

        ListIterator<String> it = caso.listIterator(inicio);
        for (int i = inicio; i < finali; i++)
                res.add(caso.get(i));
        return res;
    }

    @org.junit.Test
    public void pruneTest () {
        List<List<String>> aux = convertirMatriz(vSemillasResverseTest);

        int cuenta = 1;
        //casos especiales
        List<String> toda=new ArrayList<>();
        for (List<String> lista:aux)
            toda.addAll(lista);

        List<String> esperado = null;
        int indexI, indexF;
        boolean res, resEsperado;

        EDDoubleLinkedList<String> actual = new EDDoubleLinkedList<>(toda);

        cuenta++;
        toda.clear();
        for (List<String> perm1: aux) {

            toda.addAll(perm1);
            for (indexI=0; indexI<toda.size(); indexI++)
                for (indexF=toda.size(); indexF>0; indexF--) {
                        actual = new EDDoubleLinkedList<>(toda);

                        System.out.println("\nPRUEBA " + cuenta);
                        System.out.println("ESTADO INICIAL");
                        System.out.println("  this:" + actual + " indice inicial: " + indexI + " indice final " + indexF);
                        System.out.println("  this.prune("+indexI+", "+ indexF+")");

                        esperado = calcularResultadoPrune(toda, indexI, indexF);
                        System.out.println("ESTADO FINAL ESPERADO");
                        System.out.println("  " + esperado + ": " + esperado.size());
                        resEsperado=esperado.size()!=toda.size();

                        res = actual.prune(indexI, indexF);
                        System.out.println("ESTADO FINAL OBTENIDO:");
                        System.out.println("  " + actual );


                        assertTrue(Arrays.equals(esperado.toArray(), actual.toArray()));
                        assertEquals(resEsperado,res);

                        cuenta++;
                    }
            }

    }


    private static final Random random = new Random();


    private static List<String> prepararListaRemoveAll(List<String> valoresBase, int tamaño, boolean permitirRepetidos) {
            if (valoresBase == null || valoresBase.isEmpty()) {
                throw new IllegalArgumentException("La lista de valores base no puede estar vacía.");
            }

            if (!permitirRepetidos && tamaño > valoresBase.size()) {
                throw new IllegalArgumentException("No se pueden generar más elementos únicos que los disponibles en valoresBase.");
            }

            List<String> listaResultado = new ArrayList<>();

            if (permitirRepetidos) {
                for (int i = 0; i < tamaño; i++) {
                    int indice = random.nextInt(valoresBase.size());
                    listaResultado.add(valoresBase.get(indice));
                }
            } else {
                List<String> copia = new ArrayList<>(valoresBase);
                Collections.shuffle(copia);
                listaResultado = copia.subList(0, tamaño);
            }

            return listaResultado;
    }

    private static List<List<String>> generarCasosRemoveAll () {
            List<String> valoresBase = Arrays.asList("rojo", "verde", "azul", "amarillo", "negro", "blanco");
            List<List<String>> res = new ArrayList<>();
            for (int i=1; i<4; i++) {
                List<String> listaSinRepetidos = prepararListaRemoveAll(valoresBase, i, false);
                res.add(listaSinRepetidos);

            }
            for (int i=1; i<25; i++) {
                List<String> listaConRepetidos = prepararListaRemoveAll(valoresBase, i, true);
                res.add(listaConRepetidos);
            }
            return res;
    }


    private int calcularRessultadoRemoveAllElement(List<String> caso, String elem, List<String> esperado) {

        int c=0;
        for (String e: caso){
            if (!e.equals(elem))
                esperado.add(e);
            else
                c++;
        }
        return c;
    }

    @org.junit.Test
    public void removeAllElementTest () {

        //Caso especial lista vacía
        int cuenta =1;
        List<String> listaVacia = new ArrayList<>();
        EDDoubleLinkedList<String> actual = new EDDoubleLinkedList<>(new ArrayList<>());
        String elem1="rojo";
        System.out.println("\nPRUEBA " + cuenta);
        System.out.println("ESTADO INICIAL");
        System.out.println("  this:" + actual);
        System.out.println("  this.removeAll("+elem1+")");
        System.out.println("Resultado esperado: "+listaVacia+": "+listaVacia.size());
        System.out.println("Elementos borrados: 0");
        int res = actual.removeAllElement(elem1);
        System.out.println("Resultado obtenido: "+actual+": "+actual.size());
        System.out.println("Elementos borrados: "+res);
        assertEquals(0, actual.size());
        assertEquals(0, res);
        cuenta++;

        List<List<String>> permutaciones2 = generarCasosRemoveAll();
        List<String> valoresBase = Arrays.asList("rojo", "verde", "azul", "amarillo", "negro", "blanco");

        int resEsperado;
        for (List<String> caso1: permutaciones2) {

                actual = new EDDoubleLinkedList<>(caso1);

                for (String elem:valoresBase) {
                    System.out.println("\nPRUEBA " + cuenta);
                    System.out.println("ESTADO INICIAL");
                    System.out.println("  this:" + actual);


                    System.out.println("  this.removeAllElement("+elem+")");

                    List<String> esperado = new ArrayList<>();
                    List<String> entrada = new ArrayList<>(actual);
                    resEsperado = calcularRessultadoRemoveAllElement(entrada, elem, esperado);
                    System.out.println("ESTADO FINAL ESPERADO");
                    System.out.println("  " + esperado + ": " + esperado.size());
                    System.out.println("  elementos borrados: "+resEsperado);

                    res=actual.removeAllElement(elem);
                    System.out.println("ESTADO FINAL OBTENIDO:");
                    System.out.println("  " + actual);
                    System.out.println("  elementos borrados: "+res);

                    assertTrue(Arrays.equals(esperado.toArray(), actual.toArray()));
                    assertEquals(resEsperado,res);

                    cuenta++;
                }
            }

    }

    private static int calcularResultadoRemoveAdjacentDuplicates (List<String> entrada, List<String> esperado) {
        int cuenta = 0;
        if (entrada.size()>1) {
            String ant = entrada.get(0);
            esperado.add(ant);
            for (int i=1; i<entrada.size(); i++) {
                if (!entrada.get(i).equals(ant)) {
                    esperado.add(entrada.get(i));
                }
                else
                    cuenta++;
                ant = entrada.get(i);
            }
        }
        else {
            esperado.addAll(entrada);
        }
        return cuenta;
    }

    @org.junit.Test
    public void removeAdjacentDuplicatesTest () {

        //Caso especial lista vacía
        int cuenta =1;
        List<String> listaVacia = new ArrayList<>();
        EDDoubleLinkedList<String> actual = new EDDoubleLinkedList<>(new ArrayList<>());
        String elem1="rojo";
        System.out.println("\nPRUEBA " + cuenta);
        System.out.println("ESTADO INICIAL");
        System.out.println("  this:" + actual);
        System.out.println("  this.removeAdjacentDuplicates()");
        System.out.println("Resultado esperado: "+listaVacia+": "+listaVacia.size());
        System.out.println("Elementos borrados: 0");
        int res = actual.removeAdjacentDuplicates();
        System.out.println("Resultado obtenido: "+actual);
        System.out.println("Elementos borrados: "+res);
        assertEquals(0, actual.size());
        assertEquals(0, res);
        cuenta++;


        List<List<String>> permutaciones2 = generarCasosRemoveAll();


        int resEsperado;
        for (List<String> caso1: permutaciones2) {

            actual = new EDDoubleLinkedList<>(caso1);


            System.out.println("\nPRUEBA " + cuenta);
            System.out.println("ESTADO INICIAL");
            System.out.println("  this:" + actual);

            System.out.println("  this.removeAdjacentDuplicates()");

            List<String> esperado = new ArrayList<>();
            List<String> entrada = new ArrayList<>(actual);
            resEsperado = calcularResultadoRemoveAdjacentDuplicates(entrada, esperado);
            System.out.println("ESTADO FINAL ESPERADO");
            System.out.println("  " + esperado + ": " + esperado.size());
            System.out.println("  elementos borrados: "+resEsperado);

            res=actual.removeAdjacentDuplicates();
            System.out.println("ESTADO FINAL OBTENIDO:");
            System.out.println("  " + actual);
            System.out.println("  elementos borrados: "+res);

            assertTrue(Arrays.equals(esperado.toArray(), actual.toArray()));
            assertEquals(resEsperado,res);

            cuenta++;
        }

        List<String> valoresBase = Arrays.asList("rojo", "verde", "azul", "azul", "negro", "azul", "verde","verde","azul");
        actual = new EDDoubleLinkedList<>(valoresBase);
        System.out.println("\nPRUEBA " + cuenta);
        System.out.println("ESTADO INICIAL");
        System.out.println("  this:" + actual);

        System.out.println("  this.removeAdjacentDuplicates()");

        List<String> esperado = new ArrayList<>();
        List<String> entrada = new ArrayList<>(actual);
        resEsperado = calcularResultadoRemoveAdjacentDuplicates(entrada, esperado);
        System.out.println("ESTADO FINAL ESPERADO");
        System.out.println("  " + esperado + ": " + esperado.size());
        System.out.println("  elementos borrados: "+resEsperado);

        res=actual.removeAdjacentDuplicates();
        System.out.println("ESTADO FINAL OBTENIDO:");
        System.out.println("  " + actual);
        System.out.println("  elementos borrados: "+res);

        assertTrue(Arrays.equals(esperado.toArray(), actual.toArray()));
        assertEquals(resEsperado,res);

        cuenta++;

        valoresBase = Arrays.asList("rojo", "rojo", "rojo", "rojo", "rojo");
        actual = new EDDoubleLinkedList<>(valoresBase);
        System.out.println("\nPRUEBA " + cuenta);
        System.out.println("ESTADO INICIAL");
        System.out.println("  this:" + actual);

        System.out.println("  this.removeAdjacentDuplicates()");

        esperado = new ArrayList<>();
        entrada = new ArrayList<>(actual);
        resEsperado = calcularResultadoRemoveAdjacentDuplicates(entrada, esperado);
        System.out.println("ESTADO FINAL ESPERADO");
        System.out.println("  " + esperado + ": " + esperado.size());
        System.out.println("  elementos borrados: "+resEsperado);

        res=actual.removeAdjacentDuplicates();
        System.out.println("ESTADO FINAL OBTENIDO:");
        System.out.println("  " + actual);
        System.out.println("  elementos borrados: "+res);

        assertTrue(Arrays.equals(esperado.toArray(), actual.toArray()));
        assertEquals(resEsperado,res);

        cuenta++;

        valoresBase = Arrays.asList("rojo", "verde", "azul", "azul", "negro", "azul", "verde","verde","verde");
        actual = new EDDoubleLinkedList<>(valoresBase);
        System.out.println("\nPRUEBA " + cuenta);
        System.out.println("ESTADO INICIAL");
        System.out.println("  this:" + actual);

        System.out.println("  this.removeAdjacentDuplicates()");

        esperado = new ArrayList<>();
        entrada = new ArrayList<>(actual);
        resEsperado = calcularResultadoRemoveAdjacentDuplicates(entrada, esperado);
        System.out.println("ESTADO FINAL ESPERADO");
        System.out.println("  " + esperado + ": " + esperado.size());
        System.out.println("  elementos borrados: "+resEsperado);

        res=actual.removeAdjacentDuplicates();
        System.out.println("ESTADO FINAL OBTENIDO:");
        System.out.println("  " + actual);
        System.out.println("  elementos borrados: "+res);

        assertTrue(Arrays.equals(esperado.toArray(), actual.toArray()));
        assertEquals(resEsperado,res);

        cuenta++;

        valoresBase = Arrays.asList("rojo", "rojo");
        actual = new EDDoubleLinkedList<>(valoresBase);
        System.out.println("\nPRUEBA " + cuenta);
        System.out.println("ESTADO INICIAL");
        System.out.println("  this:" + actual);

        System.out.println("  this.removeAdjacentDuplicates()");

        esperado = new ArrayList<>();
        entrada = new ArrayList<>(actual);
        resEsperado = calcularResultadoRemoveAdjacentDuplicates(entrada, esperado);
        System.out.println("ESTADO FINAL ESPERADO");
        System.out.println("  " + esperado + ": " + esperado.size());
        System.out.println("  elementos borrados: "+resEsperado);

        res=actual.removeAdjacentDuplicates();
        System.out.println("ESTADO FINAL OBTENIDO:");
        System.out.println("  " + actual);
        System.out.println("  elementos borrados: "+res);

        assertTrue(Arrays.equals(esperado.toArray(), actual.toArray()));
        assertEquals(resEsperado,res);



    }

}
