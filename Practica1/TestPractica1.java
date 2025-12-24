package practica1;

//import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static practica1.Practica1.*;


public class TestPractica1 {
    private final Integer[] conjuntoTest1 = {1, 2, 3};
    private final Integer[] conjuntoTest2 = {1, 2, 3, 4};
    private final Integer[] conjuntoTest3 = {6, 7,8};
    private final Integer[] subconj1 = {1, 2, 3};
    private final Integer[] subconj2 = {1, 2};
    private final Integer[] subconj3 = {3, 4};
    private final Integer[] subconj4 = {2, 3};
    private final Integer[] subconj5 = {1, 3};
    private final Integer[] subconj6 = {1};
    private final Integer[] subconj7 = {2};
    private final Integer[] subconj8 = {3};

    private final String[] colPeq1 = {"uno"};
    private final String[] colPeq2 = {"uno", "uno"};
    private final String[] colPeq3 = {"uno", "dos", "tres"};


    private LinkedList<String> generarColAleatoria(int size, int limInf, int limSup) {
        LinkedList<String> col = new LinkedList<>();
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            int n = rand.nextInt();
            if (n < 0)
                n = n * (-1);
            n = n % limSup + limInf;
            col.add(String.valueOf(n));
        }
        return col;
    }

    private Set<String> generarConjAleatorio(int size, int limInf, int limSup) {
        Set<String> s = new HashSet<>();
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            int n = rand.nextInt() * (limSup - limInf + 1) + limInf;
            if (n < 0)
                n = n * (-1);
            n = n % limSup + limInf;
            String str = String.valueOf(n);
            if (!s.contains(str)) {
                s.add(str);
            }
        }
        return s;
    }



    @org.junit.Test
    public void coverageSet2Test() {


        Set<Integer> uInicial = new HashSet<>();
        ArrayList<Set<Integer>> colInicial = new ArrayList<>();
        Collection<Set<Integer>> colVacia = new HashSet<>();

        //Prueba1: conjunto vacío, col vacía
        System.out.println("Entrada: ");
        System.out.println(" Conjunto1 : " + uInicial);
        System.out.println(" Coleccion : " + colInicial);
        Collection<Set<Integer>> obtenido = coverageSet2(uInicial, colInicial);
        System.out.println("Salida esperada: " + colVacia);
        System.out.println("Salida obtenida: " + obtenido);
        assertTrue("coverageSet2: respuesta incorrecta ", obtenido.isEmpty());
        assertEquals("coverageSet2: respuesta incorrecta ", obtenido, colVacia );

        System.out.println("--------------------");

        //Prueba2: u vacío, col no
        Set<Integer> aux = new HashSet<>(List.of(subconj1));
        colInicial.add(aux);
        aux = new HashSet<>(List.of(subconj2));
        colInicial.add(aux);
        System.out.println("Entrada: ");
        System.out.println(" Conjunto1 : " + uInicial);
        System.out.println(" Coleccion : " + colInicial);
        obtenido = coverageSet2(uInicial, colInicial);
        System.out.println("Salida esperada: " + colVacia);
        System.out.println("Salida obtenida: " + obtenido);
        assertTrue("coverageSet2: respuesta incorrecta ", obtenido.isEmpty());
        assertEquals("coverageSet2: respuesta incorrecta ", obtenido, colVacia );
        System.out.println("--------------------");

        //Prueba3:
        uInicial = new HashSet<>(List.of(conjuntoTest1));
        colInicial = new ArrayList<>();
        //aux=new HashSet<>(subconj2);
        //colInicial.add(aux);
        System.out.println(" Conjunto1 : " + uInicial);
        System.out.println(" Coleccion : " + colInicial);
        obtenido = coverageSet2(uInicial, colInicial);
        System.out.println("Salida esperada: " + colVacia);
        System.out.println("Salida obtenida: " + obtenido);
        assertTrue("coverageSet2: respuesta incorrecta ", obtenido.isEmpty());
        assertEquals("coverageSet2: respuesta incorrecta ", obtenido, colVacia );
        System.out.println("--------------------");

        //Prueba4:
        uInicial = new HashSet<>(List.of(conjuntoTest1));
        colInicial = new ArrayList<>();
        aux = new HashSet<>(List.of(subconj2));
        colInicial.add(aux);
        System.out.println(" Conjunto1 : " + uInicial);
        System.out.println(" Coleccion : " + colInicial);
        obtenido = coverageSet2(uInicial, colInicial);
        System.out.println("Salida esperada: " + colVacia);
        System.out.println("Salida obtenida: " + obtenido);
        assertTrue("coverageSet2: respuesta incorrecta ", obtenido.isEmpty());
        assertEquals("coverageSet2: respuesta incorrecta ", obtenido, colVacia );
        System.out.println("--------------------");

        //Prueba5:
        uInicial = new HashSet<>(List.of(conjuntoTest1));
        colInicial = new ArrayList<>();
        aux = new HashSet<>(List.of(subconj1));
        colInicial.add(aux);
        aux = new HashSet<>(List.of(subconj2));
        colInicial.add(aux);
        aux = new HashSet<>(List.of(subconj4));
        colInicial.add(aux);
        System.out.println(" Conjunto1 : " + uInicial);
        System.out.println(" Coleccion : " + colInicial);
        Set<Set<Integer>> esperado = new HashSet<>();
        esperado.add(new HashSet<>(List.of(subconj2)));
        esperado.add(new HashSet<>(List.of(subconj4)));

        obtenido = coverageSet2(uInicial, colInicial);
        System.out.println("Salida esperada: " + esperado);
        System.out.println("Salida obtenida: " + obtenido);
        assertFalse( "coverageSet2: respuesta incorrecta ", obtenido.isEmpty());
        assertEquals(2, obtenido.size());
        assertEquals("coverageSet2: respuesta incorrecta ", obtenido, esperado );


        Set<Integer> union = new HashSet<>();
        for (Set<Integer> s : obtenido) {
            union.addAll(s);
        }
        assertEquals(uInicial.size(), union.size());
        assertEquals("coverageSet2: respuesta incorrecta ", uInicial, union );
        System.out.println("--------------------");


        //Prueba6:
        uInicial = new HashSet<>(List.of(conjuntoTest1));
        colInicial = new ArrayList<>();
        aux = new HashSet<>(List.of(subconj2));
        colInicial.add(aux);
        //aux=new HashSet<>(List.of(subconj2));
        colInicial.add(aux);
        aux = new HashSet<>(List.of(subconj4));
        colInicial.add(aux);
        System.out.println(" Conjunto1 : " + uInicial);
        System.out.println(" Coleccion : " + colInicial);
        obtenido = coverageSet2(uInicial, colInicial);
        esperado = new HashSet<>();
        esperado.add(new HashSet<>(List.of(subconj2)));
        esperado.add(new HashSet<>(List.of(subconj4)));
        System.out.println("Salida esperada: " + esperado);
        System.out.println("Salida obtenida: " + obtenido);
        assertEquals(2, obtenido.size());
        assertEquals("coverageSet2: respuesta incorrecta ", esperado, obtenido );
        union = new HashSet<>();
        for (Set<Integer> s : obtenido) {
            union.addAll(s);
        }
        assertEquals(uInicial.size(), union.size());
        assertEquals("coverageSet2: respuesta incorrecta ", uInicial, union );
        System.out.println("--------------------");


        //Prueba8:
        uInicial = new HashSet<>(List.of(conjuntoTest2));
        colInicial = new ArrayList<>();
        aux = new HashSet<>(List.of(subconj3));
        colInicial.add(aux);
        aux = new HashSet<>(List.of(subconj6));
        colInicial.add(aux);
        aux = new HashSet<>(List.of(subconj7));
        colInicial.add(aux);
        aux = new HashSet<>(List.of(subconj8));
        colInicial.add(aux);
        System.out.println(" Conjunto1 : " + uInicial);
        System.out.println(" Coleccion : " + colInicial);
        obtenido = coverageSet2(uInicial, colInicial);
        //esperado = new HashSet<>();
        //esperado.add(new HashSet<>(List.of(subconj2)));
        //esperado.add(new HashSet<>(List.of(subconj3)));
        System.out.println("Salida esperada: " + colVacia);
        System.out.println("Salida obtenida: " + obtenido);
        assertEquals("coverageSet2 RESPUESTA INCORRECTA: ", 0, obtenido.size());
        assertEquals("coverageSet2: respuesta incorrecta ", colVacia, obtenido );
        union = new HashSet<>();
        for (Set<Integer> s : obtenido) {
            union.addAll(s);
        }
        assertNotEquals("coverageSet2 RESPUESTA INCORRECTA: ", uInicial.size(), union.size());
        if (uInicial.containsAll(union) && union.containsAll(uInicial))
            System.out.println("coverageSet2 true");
        else
            System.out.println("coverageSet2 false");
        assertFalse("coverageSet2 RESPUESTA INCORRECTA: ",uInicial.containsAll(union) && union.containsAll(uInicial));

        System.out.println("--------------------");

        //Prueba9:
        uInicial = new HashSet<>(List.of(conjuntoTest2));
        colInicial = new ArrayList<>();
        aux = new HashSet<>(List.of(subconj3));
        colInicial.add(aux);
        aux = new HashSet<>(List.of(subconj6));
        colInicial.add(aux);
        aux = new HashSet<>(List.of(subconj2));
        colInicial.add(aux);
        aux = new HashSet<>(List.of(subconj5));
        colInicial.add(aux);
        System.out.println(" Conjunto1 : " + uInicial);
        System.out.println(" Coleccion : " + colInicial);
        obtenido = coverageSet2(uInicial, colInicial);
        esperado = new HashSet<>();
        esperado.add(new HashSet<>(List.of(subconj2)));
        esperado.add(new HashSet<>(List.of(subconj3)));
        System.out.println("Salida esperada: " + esperado);
        System.out.println("Salida obtenida: " + obtenido);
        assertEquals("coverageSet2 RESPUESTA INCORRECTA: ", 2, obtenido.size());
        assertEquals("coverageSet2: respuesta incorrecta ", esperado, obtenido );
        union = new HashSet<>();
        for (Set<Integer> s : obtenido) {
            union.addAll(s);
        }
        assertEquals("coverageSet2 RESPUESTA INCORRECTA: ", uInicial.size(), union.size());
        assertEquals("coverageSet2: respuesta incorrecta ", uInicial, union );
        System.out.println("--------------------");

        //Prueba9:
        uInicial = new HashSet<>(List.of(conjuntoTest3));
        System.out.println(" Conjunto1 : " + uInicial);
        System.out.println(" Coleccion : " + colInicial);
        obtenido = coverageSet2(uInicial, colInicial);
        esperado = new HashSet<>();
        System.out.println("Salida esperada: " + esperado);
        System.out.println("Salida obtenida: " + obtenido);
        assertEquals("coverageSet2: respuesta incorrecta ", esperado, obtenido );

    }


    private void preparaParaDivideInSets (LinkedList<String> colAlea, ArrayList<HashSet<String>> sol, int size) {
        colAlea.clear();
        sol.clear();
        LinkedList<String> col = generarColAleatoria(size,1, 15);
        colAlea.addAll(col);
        HashMap<String, Integer> mapa = new HashMap<>();
        for (String s:colAlea) {
            if (mapa.containsKey(s)) {
                int n = mapa.get(s);
                mapa.put(s,n+1);
            }
            else
                mapa.put(s,1);
        }
        boolean seguir = true;

        while (seguir) {
            seguir = false;
            HashSet<String> aux = new HashSet<>();
            for (String s:mapa.keySet()) {
                int nocur = mapa.get(s);
                if (nocur>0) {
                    seguir = true;
                    aux.add(s);
                    mapa.put(s,nocur-1);
                }
            }
            if (seguir) {
                sol.add(aux);
            }

        }
        System.out.println("Col "+colAlea);
    }

    @org.junit.Test
    public void divideInSetsTest() {

        ArrayList<String> colInicial = new ArrayList<>();
        Collection<Set<String>> colVacia = new ArrayList<>();

        //Prueba1: secuencia vacía --> conjunto vacío
        System.out.println("Entrada: ");
        System.out.println(" Secuencia : " + colInicial);
        Collection<Set<String>> obtenido = divideInSets(colInicial.iterator());
        System.out.println("Salida esperada: " + colVacia);
        System.out.println("Salida obtenida: " + obtenido);
        assertTrue("separate RESPUESTA INCORRECTA: ", obtenido.isEmpty());
        System.out.println("--------------------");

        //Prueba2: secuencia un solo elemento --> un solo conjunto
        colInicial = new ArrayList<>(List.of(colPeq1));
        System.out.println("Entrada: ");
        System.out.println(" Secuencia : " + colInicial);
        obtenido = divideInSets(colInicial.iterator());
        Collection<Set<String>> esperado = new ArrayList<>();
        esperado.add(new HashSet<>(List.of(colPeq1)));
        System.out.println("Salida esperada: " + esperado);
        System.out.println("Salida obtenida: " + obtenido);
        assertEquals("separate RESPUESTA INCORRECTA: ", 1, obtenido.size());
        assertEquals("separate RESPUESTA INCORRECTA: ", esperado, obtenido);
        System.out.println("--------------------");

        //Prueba3: secuencia 2 elementos --> dos conjuntos
        colInicial = new ArrayList<>(List.of(colPeq2));
        System.out.println("Entrada: ");
        System.out.println(" Secuencia : " + colInicial);
        obtenido = divideInSets(colInicial.iterator());
        esperado = new ArrayList<>();
        HashSet<String> aux = new HashSet<>(List.of(colPeq1));
        esperado.add(aux);
        esperado.add(aux);
        System.out.println("Salida esperada: " + esperado);
        System.out.println("Salida obtenida: " + obtenido);
        assertEquals("separate RESPUESTA INCORRECTA: ", esperado.size(), obtenido.size());
        assertEquals("separate RESPUESTA INCORRECTA: ", esperado, obtenido);
        System.out.println("--------------------");

        //Prueba4: secuencia 3 elementos --> un conjunto
        colInicial = new ArrayList<>(List.of(colPeq3));
        System.out.println("Entrada: ");
        System.out.println(" Secuencia : " + colInicial);
        obtenido = divideInSets(colInicial.iterator());
        esperado = new ArrayList<>();
        esperado.add(new HashSet<>(List.of(colPeq3)));
        System.out.println("Salida esperada: " + esperado);
        System.out.println("Salida obtenida: " + obtenido);
        assertEquals("separate RESPUESTA INCORRECTA: ", esperado.size(), obtenido.size());
        assertEquals("separate RESPUESTA INCORRECTA: ", esperado, obtenido);
        System.out.println("--------------------");

        //Prueba5
        LinkedList<String> colInicial2=new LinkedList<>();
        ArrayList<HashSet<String>> sol = new ArrayList<>();
        int initial_size=10;
        for (int i=0; i<10; i++) {
            preparaParaDivideInSets(colInicial2, sol, initial_size);
            System.out.println("Entrada: ");
            System.out.println(" Secuencia : " + colInicial2);
            obtenido = divideInSets(colInicial2.iterator());
            System.out.println("Salida esperada: " + sol);
            System.out.println("Salida obtenida: " + obtenido);
            assertEquals("separate RESPUESTA INCORRECTA: ", sol.size(), obtenido.size());
            assertEquals("separate RESPUESTA INCORRECTA: ", sol, obtenido);

            colInicial2.clear();
            sol.clear();
            initial_size += 2;
            System.out.println("--------------------");
        }

    }

    private final int[][] vFiltraSemillas = {{}, {0}, {1}, {2, 3},  {3, 2, 5}, {11, 5, 3, 7}};
    private final int[][] vFiltraMultiplicador = {{0}, {1}, {2, 3}, {1, -2, 5}, {5, 3, 6, 4, -5, 9, 2, -9, 7}};

    private void preparaFiltraCaso(int[] semilla, int[] multiplicador, Collection<Integer> col, Set<Integer> resultado) {
        col.clear();
        resultado.clear();

        for (int s: semilla) {
            for (int m: multiplicador) {
                int mult=s*m;
                col.add(mult);
                if (mult!=0)
                    resultado.add(mult);
            }
            col.add(s);
            //if (s != 0)
            //resultado.add(s);
        }
    }
    @org.junit.Test
    public void multiplosTest() {
        Collection<Integer> caso = new LinkedList<>();
        Set<Integer> resultado;
        Set<Integer> esperado = new HashSet<>();

        for (int[] semilla: vFiltraSemillas)
            for(int[] multiplicador: vFiltraMultiplicador) {
                preparaFiltraCaso(semilla, multiplicador, caso, esperado);

                //System.out.println("\n Caso - semilla: " + Arrays.toString(semilla) + " multiplicador: " + Arrays.toString(multiplicador));
                System.out.println("Entrada: ");
                System.out.println("  Iterador de : " + caso);
                System.out.println("Salida esperada:");
                System.out.println("  Set: " + esperado);

                resultado = multiplos(caso.iterator());

                System.out.println("Salida");
                System.out.println("  Set: " + resultado);

                assertEquals("multiplos: RESPUESTA INCORRECTA", esperado, resultado);
                System.out.println("--------------------");
            }

    }

    private final int[] vobligatorios1 = {3,9};
    private final int[] opcionales1 = {2, 1, 16};
    private final int[] nocuadrados_test = {1, 2, 3};

    @org.junit.Test
    public void separateTest() {
        HashSet<Integer> cuadrados = new HashSet<>();
        HashSet<Integer> nocuadrados = new HashSet<>();

        HashSet<Integer> col1 = new HashSet<>();
        HashSet<Integer> col2 = new HashSet<>();

        //prueba1
        System.out.println("Entrada: ");
        System.out.println("Iterador de "+col1 + " e iterador de "+col2);
        System.out.println("Esperado: ");
        System.out.println("cuadrados: "+col1+ " no cuadrados "+col2);

        HashSet<Integer> col1_aux = new HashSet<>(col1);
        HashSet<Integer> col2_aux = new HashSet<>(col2);
        separate(col1_aux,col2_aux);
        System.out.println("Obtenido: ");
        System.out.println("cuadrados: "+col1_aux+ " nocuadrados "+col2_aux);
        assertEquals("separate: Respuesta incorrecta ", col1, col1_aux );
        assertEquals("separate: Respuesta incorrecta ", col2, col2_aux );

        //prueba2
        col1.clear();
        col2.clear();
        for (Integer i: nocuadrados_test) {
            col1.add(i);
            nocuadrados.add(i);
        }
        System.out.println("Entrada: ");
        System.out.println("Iterador de "+col1 + " e iterador de "+col2);
        System.out.println("Esperado: ");
        System.out.println("cuadrados: "+cuadrados+ " no cuadrados "+nocuadrados);

        col1_aux = new HashSet<>(col1);
        col2_aux = new HashSet<>(col2);
        separate(col1_aux,col2_aux);
        System.out.println("Obtenido: ");
        System.out.println("cuadrados: "+col1_aux+ " no cuadrados "+col2_aux);
        assertEquals("separate: Respuesta incorrecta ", col2, col1_aux );
        assertEquals("separate: Respuesta incorrecta ", col1, col2_aux );

        //prueba3
        col1.clear();
        col2.clear();
        for (Integer i: nocuadrados_test) {
            col2.add(i);
            //nocuadrados.add(nocuadrados_test[i]);
        }
        System.out.println("Entrada: ");
        System.out.println("Iterador de "+col1 + " e iterador de "+col2);
        System.out.println("Esperado: ");
        System.out.println("cuadrados: "+cuadrados+ " no cuadrados "+nocuadrados);

        col1_aux = new HashSet<>(col1);
        col2_aux = new HashSet<>(col2);
        separate(col1_aux,col2_aux);
        System.out.println("Obtenido: ");
        System.out.println("cuadrados: "+col1_aux+ " nocuadrados "+col2_aux);
        assertEquals("separate: Respuesta incorrecta ", col1, col1_aux );
        assertEquals("separate: Respuesta incorrecta ", col2, col2_aux );
        System.out.println();

        //prueba4
        col1.clear();
        col2.clear();
        for (Integer i: nocuadrados_test) {
            col2.add(i);
            col1.add(i);
        }
        System.out.println("Entrada: ");
        System.out.println("Iterador de "+col1 + " e iterador de "+col2);
        System.out.println("Esperado: ");
        cuadrados.addAll(nocuadrados);
        nocuadrados.remove(1);
        cuadrados.removeAll(nocuadrados);
        System.out.println("cuadrados: "+cuadrados+ " no cuadrados "+nocuadrados);

        col1_aux = new HashSet<>(col1);
        col2_aux = new HashSet<>(col2);
        separate(col1_aux,col2_aux);
        System.out.println("Obtenido: ");
        System.out.println("cuadrados: "+col1_aux+ " no cuadrados "+col2_aux);
        assertEquals("separate: Respuesta incorrecta ", cuadrados, col1_aux);
        assertEquals("separate: Respuesta incorrecta ", nocuadrados, col2_aux );
        System.out.println();

        //private int[] vobligatorios1 = {3,9};
        //private int[] opcionales1 = {2, 1, 16};
        //prueba5

        cuadrados.clear();
        nocuadrados.clear();
        cuadrados.add(vobligatorios1[1]);
        nocuadrados.add(vobligatorios1[0]);

        col1.clear();
        col2.clear();
        for (Integer i: vobligatorios1) {
            col1.add(i);
        }


        System.out.println("Entrada: ");
        System.out.println("Iterador de "+col1 + " e iterador de "+col2);
        System.out.println("Esperado: ");
        System.out.println("cuadrados: "+cuadrados+ " no cuadrados "+nocuadrados);

        col1_aux = new HashSet<>(col1);
        col2_aux = new HashSet<>(col2);
        separate(col1_aux,col2_aux);
        System.out.println("Obtenido: ");
        System.out.println("cuadrados: "+col1_aux+ " no cuadrados "+col2_aux);
        assertEquals("separate: Respuesta incorrecta ", cuadrados, col1_aux);
        assertEquals("separate: Respuesta incorrecta ", nocuadrados, col2_aux );

        System.out.println();
       // col1.clear();
        //col2.clear();
        //for (Integer i: vobligatorios1) {
          //  col1.add(i);
        //}

        for (int i : opcionales1) {
            col2.add(i);
            nocuadrados.add(i);

            System.out.println("Entrada: ");
            System.out.println("Iterador de "+col1 + " e iterador de "+col2);
            System.out.println("Esperado: ");
            System.out.println("cuadrados: "+cuadrados+ " no cuadrados "+nocuadrados);

            col1_aux = new HashSet<>(col1);
            col2_aux = new HashSet<>(col2);
            separate(col1_aux,col2_aux);

            assertEquals("separate: Respuesta incorrecta ", cuadrados, col1_aux);
            assertEquals("separate: Respuesta incorrecta ", nocuadrados, col2_aux );
            System.out.println();
        }

        col1.clear();
        col2.clear();
        for (Integer i: vobligatorios1) {
          col2.add(i);
        }
        nocuadrados.clear();
        nocuadrados.add(vobligatorios1[0]);
        for (int i : opcionales1) {
            col2.add(i);
            nocuadrados.add(i);

            System.out.println("Entrada: ");
            System.out.println("Iterador de "+col1 + " e iterador de "+col2);
            System.out.println("Esperado: ");
            System.out.println("cuadrados: "+cuadrados+ " no cuadrados "+nocuadrados);

            col1_aux = new HashSet<>(col1);
            col2_aux = new HashSet<>(col2);
            separate(col1_aux,col2_aux);

            assertEquals("separate: Respuesta incorrecta ", cuadrados, col1_aux);
            assertEquals("separate: Respuesta incorrecta ", nocuadrados, col2_aux );
            System.out.println();
        }

        //3, 9 y el resto en col1. Luego en col2
        col1.clear();
        col2.clear();
        col1.add(vobligatorios1[0]);
        col2.add(vobligatorios1[1]);
        nocuadrados.clear();
        nocuadrados.add(vobligatorios1[0]);
        for (int i : opcionales1) {
            col1.add(i);
            nocuadrados.add(i);

            System.out.println("Entrada: ");
            System.out.println("Iterador de "+col1 + " e iterador de "+col2);
            System.out.println("Esperado: ");
            System.out.println("cuadrados: "+cuadrados+ " no cuadrados "+nocuadrados);

            col1_aux = new HashSet<>(col1);
            col2_aux = new HashSet<>(col2);
            separate(col1_aux,col2_aux);

            assertEquals("separate: Respuesta incorrecta ", cuadrados, col1_aux);
            assertEquals("separate: Respuesta incorrecta ", nocuadrados, col2_aux );
            System.out.println();
        }

        col1.clear();
        col2.clear();
        col1.clear();
        col2.clear();
        col1.add(vobligatorios1[0]);
        col2.add(vobligatorios1[1]);
        nocuadrados.clear();
        nocuadrados.add(vobligatorios1[0]);

        for (int i : opcionales1) {
            col2.add(i);
            nocuadrados.add(i);

            System.out.println("Entrada: ");
            System.out.println("Iterador de "+col1 + " e iterador de "+col2);
            System.out.println("Esperado: ");
            System.out.println("cuadrados: "+cuadrados+ " no cuadrados "+nocuadrados);

            col1_aux = new HashSet<>(col1);
            col2_aux = new HashSet<>(col2);
            separate(col1_aux,col2_aux);

            assertEquals("separate: Respuesta incorrecta ", cuadrados, col1_aux);
            assertEquals("separate: Respuesta incorrecta ", nocuadrados, col2_aux );
            System.out.println();
        }

        //9, 3 y el resto en col1. Luego en col2
        col1.clear();
        col2.clear();
        col1.add(vobligatorios1[1]);
        col2.add(vobligatorios1[0]);
        nocuadrados.clear();
        nocuadrados.add(vobligatorios1[0]);
        for (int i : opcionales1) {
            col1.add(i);
            nocuadrados.add(i);

            System.out.println("Entrada: ");
            System.out.println("Iterador de "+col1 + " e iterador de "+col2);
            System.out.println("Esperado: ");
            System.out.println("cuadrados: "+cuadrados+ " no cuadrados "+nocuadrados);

            col1_aux = new HashSet<>(col1);
            col2_aux = new HashSet<>(col2);
            separate(col1_aux,col2_aux);

            assertEquals("separate: Respuesta incorrecta ", cuadrados, col1_aux);
            assertEquals("separate: Respuesta incorrecta ", nocuadrados, col2_aux );
            System.out.println();
        }

        col1.clear();
        col2.clear();
        col1.clear();
        col2.clear();
        col1.add(vobligatorios1[1]);
        col2.add(vobligatorios1[0]);
        nocuadrados.clear();
        nocuadrados.add(vobligatorios1[0]);

        for (int i : opcionales1) {
            col2.add(i);
            nocuadrados.add(i);

            System.out.println("Entrada: ");
            System.out.println("Iterador de "+col1 + " e iterador de "+col2);
            System.out.println("Esperado: ");
            System.out.println("cuadrados: "+cuadrados+ " no cuadrados "+nocuadrados);

            col1_aux = new HashSet<>(col1);
            col2_aux = new HashSet<>(col2);
            separate(col1_aux,col2_aux);

            assertEquals("separate: Respuesta incorrecta ", cuadrados, col1_aux);
            assertEquals("separate: Respuesta incorrecta ", nocuadrados, col2_aux );
            System.out.println();
        }

        col1.clear();
        col2.clear();
        col1.clear();
        col2.clear();
        col1.add(vobligatorios1[0]);
        col2.add(vobligatorios1[1]);
        nocuadrados.clear();
        nocuadrados.add(vobligatorios1[0]);

        col1.add(opcionales1[0]);
        col2.add(opcionales1[1]);
        col1.add(opcionales1[2]);
        for (int i:opcionales1)
            nocuadrados.add(i);


        System.out.println("Entrada: ");
        System.out.println("Iterador de " + col1 + " e iterador de " + col2);
        System.out.println("Esperado: ");
        System.out.println("cuadrados: " + cuadrados + " no cuadrados " + nocuadrados);

        col1_aux = new HashSet<>(col1);
        col2_aux = new HashSet<>(col2);
        separate(col1_aux, col2_aux);

        assertEquals("separate: Respuesta incorrecta ", cuadrados, col1_aux);
        assertEquals("separate: Respuesta incorrecta ", nocuadrados, col2_aux);
        System.out.println();

    }



}




