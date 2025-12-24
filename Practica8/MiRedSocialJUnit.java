
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

import static org.junit.Assert.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MiRedSocialJUnit {

    private class Edge {
        private String source;
        private String target;

        public Edge(String source, String target) {
            this.source = source;
            this.target = target;
        }

        public boolean equals(Edge other) {
            return this.source.equals(other.source) && this.target.equals(other.target);
        }

        public String toString() {
            return "(" + source + "," + target + ")\n";
        }
    }

    private Set<Edge> myArcs = new HashSet<>();

    /*private void printMyArcs () {
        for (Edge e : myArcs) {
            System.out.println(e.toString());
        }
    }*/

    private static String[] fellows1 = {"Vecna", "Once", "Billy", "Dustin"};
    private static String[] fellows2 = {"Mike", "Jim", "Steve", "Will", "Billy"};

    private boolean myContains(Set<Edge> myArcs, Edge ed) {
        for (Edge e : myArcs) {
            if (ed.equals(e)) {
                return true;
            }
        }
        return false;
    }

    @org.junit.Test()
    public void testAddDirectedEdgeException() {

        MiRedSocial red = new MiRedSocial();
        int cuenta = 0;
        String source = null;
        String target = "Lucy";

        System.out.println("ESTADO FINAL ESPERADO: NullPointerException");

        boolean excepcion = false;
        try {
            red.addDirectedEdge(source, target);
        } catch (NullPointerException e) {
            System.out.println(" ...OK");
            excepcion = true;
        }
        assertTrue("Excepcion no lanzada", excepcion);
        cuenta++;

        source = "Doris";
        target = null;

        System.out.println("ESTADO FINAL ESPERADO: NullPointerException");

        excepcion = false;
        try {
            red.addDirectedEdge(source, target);
        } catch (NullPointerException e) {
            System.out.println(" ...OK");
            excepcion = true;
        }
        assertTrue("Excepcion no lanzada", excepcion);
        cuenta++;

        source = null;
        target = null;

        System.out.println("ESTADO FINAL ESPERADO: NullPointerException");

        excepcion = false;
        try {
            red.addDirectedEdge(source, target);
        } catch (NullPointerException e) {
            System.out.println(" ...OK");
            excepcion = true;
        }
        assertTrue("Excepcion no lanzada", excepcion);
        cuenta++;
    }

    @org.junit.Test()
    public void testAddDirectedEdge() {

        MiRedSocial red = new MiRedSocial();
        int cuenta = 0;
        boolean expected, obtained;

        for (int i = 0; i < fellows1.length; i++) {
            for (int j = 0; j < fellows1.length; j++) {
                System.out.println("Estado de la red: ");
                red.printGraphStructure();
                System.out.println("Prueba " + cuenta);
                System.out.println("Insertando arco de " + fellows1[i] + " a " + fellows1[j]);
                if (i == j)
                    expected = false;
                else {
                    Edge my = new Edge(fellows1[i], fellows1[j]);
                    expected = true;
                    if (myContains(myArcs, my))
                        expected = false;
                    myArcs.add(my);
                }
                obtained = red.addDirectedEdge(fellows1[i], fellows1[j]);
                assertEquals(expected, obtained);
                //Comprobar que el grafo tienen los dos nodos correctamente
                if (obtained) {
                    boolean esta1 = red.containsNode(fellows1[i]);
                    if (!esta1)
                        System.out.println("Falta nodo "+fellows1[i]);
                    boolean esta2 = red.containsNode(fellows1[j]);
                    if (!esta2)
                        System.out.println("Falta nodo "+fellows1[j]);
                    assertTrue(esta1 && esta2);
                }
                cuenta++;
            }
        }

        for (int i = 0; i < fellows1.length; i++) {
            for (int j = 0; j < fellows2.length; j++) {
                System.out.println("Estado de la red: ");
                red.printGraphStructure();
                System.out.println("Prueba " + cuenta);
                System.out.println("Insertando arco de " + fellows1[i] + " a " + fellows2[j]);
                if (fellows1[i].equals(fellows2[j]))
                    expected = false;
                else {
                    Edge my = new Edge(fellows1[i], fellows2[j]);
                    expected = true;
                    if (myContains(myArcs, my))
                        expected = false;
                    myArcs.add(my);
                }
                obtained = red.addDirectedEdge(fellows1[i], fellows2[j]);
                assertEquals(expected, obtained);

                //Comprobar que el grafo tienen los dos nodos correctamente
                if (obtained) {
                    boolean esta1 = red.containsNode(fellows1[i]);
                    if (!esta1)
                        System.out.println("Falta nodo "+fellows1[i]);
                    boolean esta2 = red.containsNode(fellows1[j]);
                    if (!esta2)
                        System.out.println("Falta nodo "+fellows1[j]);
                    assertTrue(esta1 && esta2);
                }
                cuenta++;
            }
        }

        for (int i = 0; i < fellows2.length; i++) {
            for (int j = 0; j < fellows1.length; j++) {
                System.out.println("Estado de la red: ");
                red.printGraphStructure();
                System.out.println("Prueba " + cuenta);
                System.out.println("Insertando arco de " + fellows2[i] + " a " + fellows1[j]);
                if (fellows2[i].equals(fellows1[j]))
                    expected = false;
                else {
                    Edge my = new Edge(fellows2[i], fellows1[j]);
                    expected = true;
                    if (myContains(myArcs, my))
                        expected = false;
                    myArcs.add(my);
                }
                obtained = red.addDirectedEdge(fellows2[i], fellows1[j]);
                assertEquals(expected, obtained);

                //Comprobar que el grafo tienen los dos nodos correctamente
                if (obtained) {
                    boolean esta1 = red.containsNode(fellows1[i]);
                    if (!esta1)
                        System.out.println("Falta nodo "+fellows1[i]);
                    boolean esta2 = red.containsNode(fellows1[j]);
                    if (!esta2)
                        System.out.println("Falta nodo "+fellows1[j]);
                    assertTrue(esta1 && esta2);
                }
                cuenta++;
            }
        }

        //Probar que no se insertan arcos repetidos
        for (int i = 0; i < fellows1.length; i++) {
            for (int j = 0; j < fellows1.length; j++) {
                System.out.println("Estado de la red: ");
                red.printGraphStructure();
                System.out.println("Prueba " + cuenta);
                System.out.println("Insertando arco de " + fellows1[i] + " a " + fellows1[j]);
                if (i == j)
                    expected = false;
                else {
                    Edge my = new Edge(fellows1[i], fellows1[j]);
                    expected = true;
                    if (myContains(myArcs, my))
                        expected = false;
                    myArcs.add(my);
                }
                obtained = red.addDirectedEdge(fellows1[i], fellows1[j]);
                assertEquals(expected, obtained);
                cuenta++;
            }
        }


    }

    @org.junit.Test()
    public void testContainsNodeException() {

        MiRedSocial red = new MiRedSocial();
        int cuenta = 0;
        String source = null;

        System.out.println("ESTADO FINAL ESPERADO: NullPointerException");

        boolean excepcion = false;
        try {
            red.containsNode(source);
        } catch (NullPointerException e) {
            System.out.println(" ...OK");
            excepcion = true;
        }
        assertTrue("Excepcion no lanzada", excepcion);
        cuenta++;

    }

    @org.junit.Test()
    public void testContainsNode() {
        MiRedSocial red = new MiRedSocial();
        for (int i = 0; i < fellows1.length; i++)
            for (int j = 0; j < fellows2.length; j++)
                red.addDirectedEdge(fellows1[i], fellows2[j]);
        int cuenta = 0;
        for (int i = 0; i < fellows1.length; i++) {
            System.out.println("Comprobando containsNode " + fellows1[i]);
            boolean expected = red.containsNode(fellows1[i]);
            assertTrue(expected);
            cuenta++;
        }
        for (int i = 0; i < fellows2.length; i++) {
            System.out.println("Comprobando containsNode " + fellows2[i]);
            boolean expected = red.containsNode(fellows2[i]);
            assertTrue(expected);
            cuenta++;
        }
        String otros[] = {"no", "otro"};
        for (int i = 0; i < otros.length; i++) {
            System.out.println("Comprobando containsNode " + otros[i]);
            boolean expected = red.containsNode(otros[i]);
            assertFalse(expected);
            cuenta++;
        }

    }

    @org.junit.Test()
    public void testDSize() {
        System.out.println("\n ===== PROBANDO MÉTODO size() =========");
        int prueba = 1;
        MiRedSocial red = new MiRedSocial();
        System.out.println("red social vacía");
        assertEquals(0, red.size());

        String inicial = fellows1[0];
        Set<String> esperado = new HashSet<>();
        esperado.add(inicial);
        for (int i = 1; i < fellows1.length; i++) {
            prueba++;
            System.out.println("prueba " + prueba + ": añadiendo arco " + inicial + " --> " + fellows1[i]);
            red.addDirectedEdge(inicial, fellows1[i]);
            if (!esperado.contains(fellows1[i]))
                esperado.add(fellows1[i]);
            System.out.println("esperado =" + esperado.size());
            int obtenido = red.size();
            assertEquals(esperado.size(), obtenido);
        }
        inicial = fellows1[1];
        for (int i = 2; i < fellows1.length; i++) {
            prueba++;
            System.out.println("prueba " + prueba + ": añadiendo arco " + inicial + " --> " + fellows1[i]);
            red.addDirectedEdge(inicial, fellows1[i]);
            if (!esperado.contains(fellows1[i]))
                esperado.add(fellows1[i]);
            System.out.println("esperado =" + esperado.size());
            int obtenido = red.size();
            assertEquals(esperado.size(), obtenido);
        }
        inicial = fellows2[0];
        esperado.add(inicial);
        for (int i = 0; i < fellows1.length; i++) {
            prueba++;
            System.out.println("prueba " + prueba + ": añadiendo arco " + inicial + " --> " + fellows1[i]);
            red.addDirectedEdge(inicial, fellows1[i]);
            if (!esperado.contains(fellows1[i]))
                esperado.add(fellows1[i]);
            System.out.println("esperado =" + esperado.size());
            int obtenido = red.size();
            assertEquals(esperado.size(), obtenido);
        }
    }

    @org.junit.Test()
    public void testGetMembers() {
        System.out.println("\n ===== PROBANDO MÉTODO getMembers() =========");
        int prueba = 1;
        Map<String, Set<String>> esperado = new HashMap<>();

        MiRedSocial red = new MiRedSocial();
        System.out.println("red social vacía");
        assertEquals(0, red.size());
        Set<String> obtenido = red.getMembers();
        assertTrue(obtenido.isEmpty());

        String inicial = fellows1[0];
        if (!esperado.containsKey(inicial))
            esperado.put(inicial, new HashSet<>());
        for (int i = 1; i < fellows1.length; i++) {
            prueba++;
            System.out.println("prueba " + prueba + ": añadiendo arco " + inicial + " --> " + fellows1[i]);
            red.addDirectedEdge(inicial, fellows1[i]);
            esperado.get(inicial).add(fellows1[i]);
            if (!esperado.containsKey(fellows1[i]))
                esperado.put(fellows1[i], new HashSet<>());
            System.out.println("esperado =" + esperado.keySet().toString());
            Set<String> comprobar1 = red.getFollowed(inicial);
            obtenido = red.getMembers();
            System.out.println("obtenido =" + obtenido.toString());
            System.out.println("número de nodos esperado: " + esperado.size() + " número de nodos obtenido: " + obtenido.size());
            assertEquals(esperado.keySet().size(), obtenido.size());
            assertTrue(esperado.keySet().containsAll(obtenido) && obtenido.containsAll(esperado.keySet()));
            assertTrue(esperado.get(inicial).containsAll(red.getFollowed(inicial)) && red.getFollowed(inicial).containsAll(esperado.get(inicial)));

        }

        inicial = fellows1[1];
        for (int i = 2; i < fellows1.length; i++) {
            prueba++;
            System.out.println("prueba " + prueba + ": añadiendo arco " + inicial + " --> " + fellows1[i]);
            red.addDirectedEdge(inicial, fellows1[i]);
            esperado.get(inicial).add(fellows1[i]);
            if (!esperado.containsKey(fellows1[i]))
                esperado.put(fellows1[i], new HashSet<>());
            System.out.println("esperado =" + esperado.keySet().toString());
            obtenido = red.getMembers();
            System.out.println("obtenido =" + obtenido.toString());
            System.out.println("número de nodos esperado: " + esperado.keySet().size() + " número de nodos obtenido: " + obtenido.size());
            assertEquals(esperado.keySet().size(), obtenido.size());
            assertTrue(esperado.keySet().containsAll(obtenido) && obtenido.containsAll(esperado.keySet()));
            assertTrue(esperado.get(inicial).containsAll(red.getFollowed(inicial)) && red.getFollowed(inicial).containsAll(esperado.get(inicial)));

        }

        inicial = fellows2[0];
        if (!esperado.containsKey(inicial))
            esperado.put(inicial, new HashSet<>());
        for (int i = 0; i < fellows1.length; i++) {
            prueba++;
            System.out.println("prueba " + prueba + ": añadiendo arco " + inicial + " --> " + fellows1[i]);
            red.addDirectedEdge(inicial, fellows1[i]);
            esperado.get(inicial).add(fellows1[i]);
            if (!esperado.containsKey(fellows1[i]))
                esperado.put(fellows1[i], new HashSet<>());
            System.out.println("esperado =" + esperado.keySet().toString());
            obtenido = red.getMembers();
            System.out.println("obtenido =" + obtenido.toString());
            System.out.println("número de nodos esperado: " + esperado.keySet().size() + " número de nodos obtenido: " + obtenido.size());
            assertEquals(esperado.keySet().size(), obtenido.size());
            assertTrue(esperado.keySet().containsAll(obtenido) && obtenido.containsAll(esperado.keySet()));
            assertTrue(esperado.get(inicial).containsAll(red.getFollowed(inicial)) && red.getFollowed(inicial).containsAll(esperado.get(inicial)));
        }

        //devuelve una copia
        prueba++;
        System.out.println("prueba " + prueba + ": añadiendo arco " + fellows2[1] + " --> " + fellows2[2]);
        red.addDirectedEdge(fellows1[1], fellows2[2]);
        Set<String> conjuntoNodos = red.getMembers();
        System.out.println("Obtenido: "+conjuntoNodos.toString());
        assertTrue(conjuntoNodos.size()>obtenido.size());
    }


    @org.junit.Test()
    public void testDGetFollowersException() {

        MiRedSocial red = new MiRedSocial();
        int cuenta = 0;
        String source = null;

        System.out.println("ESTADO FINAL ESPERADO: NullPointerException");

        boolean excepcion = false;
        try {
            red.getFollowers(source);
        } catch (NullPointerException e) {
            System.out.println(" ...OK");
            excepcion = true;
        }
        assertTrue("Excepcion no lanzada", excepcion);
        cuenta++;

    }



    @org.junit.Test()
    public void testDGetFollowers() {
        System.out.println("\n ===== PROBANDO MÉTODO getFollowers() =========");
        int prueba = 1;
        Map<String, Set<String>> esperado = new HashMap<>();

        MiRedSocial red = new MiRedSocial();
        System.out.println("red social vacía");
        assertEquals(0, red.size());
        Set<String> obtenido = red.getFollowers("xx");
        assertTrue(obtenido.isEmpty());

        prueba++;
        String inicial = fellows1[0];
        obtenido = red.getFollowers(inicial);
        System.out.println("Prueba " + prueba + " Probando getFollowers(" + inicial + ")");
        System.out.println("esperado: " + new HashSet<>());
        System.out.println("obtenido " + obtenido.toString());

        assertTrue(obtenido.size() == 0);

        Set<String> contrario = new HashSet<>();
        for (int i = 1; i < fellows1.length; i++) {
            prueba++;
            //System.out.println("prueba "+prueba+": añadiendo arco "+inicial+" --> "+fellows1[i]);
            System.out.println("prueba " + prueba + ": Probando getFollowers( " + fellows1[i] + ")");
            red.addDirectedEdge(inicial, fellows1[i]);
            if (!esperado.containsKey(fellows1[i]))
                esperado.put(fellows1[i], new HashSet<>());
            esperado.get(fellows1[i]).add(inicial);
            System.out.println("esperado =" + esperado.get(fellows1[i]).toString());
            obtenido = red.getFollowers(fellows1[i]);
            System.out.println("obtenido =" + obtenido.toString());
            System.out.println("número de nodos esperado: " + esperado.get(fellows1[i]).size() + " número de nodos obtenido: " + obtenido.size());
            assertEquals(esperado.get(fellows1[i]).size(), obtenido.size());
            assertTrue(esperado.get(fellows1[i]).containsAll(obtenido) && obtenido.containsAll(esperado.get(fellows1[i])));
            prueba++;
            System.out.println("prueba " + prueba + ": Probando getFollowers( " + inicial + ")");
            assertTrue(red.getFollowers(inicial).isEmpty());

        }

        inicial = fellows1[1];
        for (int i = 2; i < fellows1.length; i++) {
            prueba++;
            //System.out.println("prueba "+prueba+": añadiendo arco "+inicial+" --> "+fellows1[i]);
            red.addDirectedEdge(inicial, fellows1[i]);
            if (!esperado.containsKey(fellows1[i]))
                esperado.put(fellows1[i], new HashSet<>());
            esperado.get(fellows1[i]).add(inicial);
            System.out.println("esperado =" + esperado.get(fellows1[i]).toString());
            System.out.println("prueba " + prueba + ": Probando getFollowers( " + fellows1[i] + ")");
            obtenido = red.getFollowers(fellows1[i]);
            System.out.println("obtenido =" + obtenido.toString());
            int seguidores = esperado.get(fellows1[i]).size();
            System.out.println("número de nodos esperado: " + seguidores + " número de nodos obtenido: " + obtenido.size());
            assertEquals(seguidores, obtenido.size());
            assertTrue(esperado.get(fellows1[i]).containsAll(obtenido) && obtenido.containsAll(esperado.get(inicial)));

        }

        inicial = fellows2[0];
        for (int i = 0; i < fellows1.length; i++) {
            prueba++;
            //System.out.println("prueba "+prueba+": añadiendo arco "+inicial+" --> "+fellows1[i]);
            red.addDirectedEdge(inicial, fellows1[i]);
            if (!esperado.containsKey(fellows1[i]))
                esperado.put(fellows1[i], new HashSet<>());
            esperado.get(fellows1[i]).add(inicial);

            System.out.println("esperado =" + esperado.get(fellows1[i]).toString());
            System.out.println("prueba " + prueba + ": Probando getFollowers( " + fellows1[i] + ")");
            obtenido = red.getFollowers(fellows1[i]);
            System.out.println("obtenido =" + obtenido.toString());
            int seguidores = esperado.get(fellows1[i]).size();
            System.out.println("número de nodos esperado: " + seguidores + " número de nodos obtenido: " + obtenido.size());
            assertEquals(seguidores, obtenido.size());
            assertTrue(esperado.get(fellows1[i]).containsAll(obtenido) && obtenido.containsAll(esperado.get(fellows1[i])));
        }
    }


    @org.junit.Test()
    public void testCommonFollowersException() {

        MiRedSocial red = new MiRedSocial();
        int cuenta = 0;
        String source = null;
        String target = "Lucy";

        System.out.println("ESTADO FINAL ESPERADO: NullPointerException");

        boolean excepcion = false;
        try {
            red.commonFollowers(source, target);
        } catch (NullPointerException e) {
            System.out.println(" ...OK");
            excepcion = true;
        }
        assertTrue("Excepcion no lanzada", excepcion);
        cuenta++;

        source = "Doris";
        target = null;

        System.out.println("ESTADO FINAL ESPERADO: NullPointerException");

        excepcion = false;
        try {
            red.commonFollowers(source, target);
        } catch (NullPointerException e) {
            System.out.println(" ...OK");
            excepcion = true;
        }
        assertTrue("Excepcion no lanzada", excepcion);
        cuenta++;

        source = null;
        target = null;

        System.out.println("ESTADO FINAL ESPERADO: NullPointerException");

        excepcion = false;
        try {
            red.commonFollowers(source, target);
        } catch (NullPointerException e) {
            System.out.println(" ...OK");
            excepcion = true;
        }
        assertTrue("Excepcion no lanzada", excepcion);
        cuenta++;
    }



    @org.junit.Test()
    public void testECommonFollowers() {
        System.out.println("\n ===== PROBANDO MÉTODO commonFollowers() =========");
        int prueba = 1;
        Map<String, Set<String>> esperado = new HashMap<>();

        MiRedSocial red = new MiRedSocial();
        System.out.println("red social vacía");
        assertEquals(0, red.size());
        Set<String> obtenido = red.commonFollowers("Lucy", "Jane");
        assertTrue(obtenido.isEmpty());

        prueba++;
        String name1 = fellows1[0];
        String name2 = fellows2[0];
        obtenido = red.commonFollowers(name1, name2);
        System.out.println("Prueba " + prueba + " Probando commonFollowers(" + name1 + ", " + name2 + ")");
        System.out.println("esperado: " + new HashSet<>());
        System.out.println("obtenido " + obtenido.toString());

        assertTrue(obtenido.size() == 0);

        for (int i = 1; i < fellows1.length; i++) {
            prueba++;
            System.out.println("prueba " + prueba + ": commonFollowers(" + name1 + ", " + fellows1[i] + ")");
            red.addDirectedEdge(name1, fellows1[i]);
            if (!esperado.containsKey(fellows1[i]))
                esperado.put(fellows1[i], new HashSet<>());
            esperado.get(fellows1[i]).add(name1);
            HashSet<String> es = new HashSet<>(esperado.get(fellows1[i]));
            if (!esperado.containsKey(name1))
                es.clear();
            else
                es.retainAll(esperado.get(name1));
            System.out.println("esperado =" + es.toString());
            obtenido = red.commonFollowers(name1, fellows1[i]);
            System.out.println("obtenido =" + obtenido.toString());
            System.out.println("número de nodos esperado: " + es.size() + " número de nodos obtenido: " + obtenido.size());
            assertEquals(es.size(), obtenido.size());
            assertTrue(es.containsAll(obtenido));

            //Comprobar que no se ha modificado los followers
            Set<String> follows1 = red.getFollowers(name1);
            Set<String> follows2 = red.getFollowers(fellows1[i]);
            if (!esperado.containsKey(name1))
                assertEquals(0, follows1.size());
            else {
                assertEquals(esperado.get(name1).size(), follows1.size());
                assertTrue(esperado.get(name1).containsAll(follows1));
            }
            if (!esperado.containsKey(fellows1[i]))
                assertEquals(0, follows2.size());
            else {
                assertEquals(esperado.get(fellows1[i]).size(), follows2.size());
                assertTrue(esperado.get(fellows1[i]).containsAll(follows2));
            }
            prueba++;
            System.out.println("prueba " + prueba + ": commonFollowers(" + fellows1[i] + ", " + name1 + ")");
            System.out.println("esperado =" + es.toString());
            obtenido = red.commonFollowers(fellows1[i], name1);
            System.out.println("obtenido =" + obtenido.toString());
            System.out.println("número de nodos esperado: " + es.size() + " número de nodos obtenido: " + obtenido.size());
            assertEquals(es.size(), obtenido.size());
            assertTrue(es.containsAll(obtenido));

        }

        name1 = fellows1[1];
        for (int i = 2; i < fellows1.length; i++) {
            prueba++;
            System.out.println("Prueba " + prueba + " Probando commonFollowers(" + name1 + ", " + fellows1[i] + ")");
            red.addDirectedEdge(name1, fellows1[i]);
            if (!esperado.containsKey(fellows1[i]))
                esperado.put(fellows1[i], new HashSet<>());
            esperado.get(fellows1[i]).add(name1);
            HashSet<String> es = new HashSet<>(esperado.get(fellows1[i]));
            if (!esperado.containsKey(name1))
                es.clear();
            else
                es.retainAll(esperado.get(name1));
            System.out.println("esperado =" + es.toString());
            obtenido = red.commonFollowers(name1, fellows1[i]);
            System.out.println("obtenido =" + obtenido.toString());
            System.out.println("número de nodos esperado: " + es.size() + " número de nodos obtenido: " + obtenido.size());
            assertEquals(es.size(), obtenido.size());
            assertTrue(es.containsAll(obtenido));

            //Comprobar que no se ha modificado los followers
            Set<String> follows1 = red.getFollowers(name1);
            Set<String> follows2 = red.getFollowers(fellows1[i]);
            assertEquals(esperado.get(name1).size(), follows1.size());
            assertTrue(esperado.get(name1).containsAll(follows1));
            assertEquals(esperado.get(fellows1[i]).size(), follows2.size());
            assertTrue(esperado.get(fellows1[i]).containsAll(follows2));

            prueba++;
            System.out.println("prueba " + prueba + ": commonFollowers(" + fellows1[i] + ", " + name1 + ")");
            System.out.println("esperado =" + es.toString());
            obtenido = red.commonFollowers(fellows1[i], name1);
            System.out.println("obtenido =" + obtenido.toString());
            System.out.println("número de nodos esperado: " + es.size() + " número de nodos obtenido: " + obtenido.size());
            assertEquals(es.size(), obtenido.size());
            assertTrue(es.containsAll(obtenido));

        }

        name1 = fellows2[0];
        for (int i = 0; i < fellows1.length; i++) {
            prueba++;
            System.out.println("Prueba " + prueba + " Probando commonFollowers(" + name1 + ", " + fellows1[i] + ")");
            red.addDirectedEdge(name1, fellows1[i]);
            if (!esperado.containsKey(fellows1[i]))
                esperado.put(fellows1[i], new HashSet<>());
            esperado.get(fellows1[i]).add(name1);

            HashSet<String> es = new HashSet<>(esperado.get(fellows1[i]));
            if (!esperado.containsKey(name1))
                es.clear();
            else
                es.retainAll(esperado.get(name1));
            System.out.println("esperado =" + es.toString());
            obtenido = red.commonFollowers(name1, fellows1[i]);
            System.out.println("obtenido =" + obtenido.toString());
            System.out.println("número de nodos esperado: " + es.size() + " número de nodos obtenido: " + obtenido.size());
            assertEquals(es.size(), obtenido.size());
            assertTrue(es.containsAll(obtenido));

            //Comprobar que no se ha modificado los followers
            Set<String> follows1 = red.getFollowers(name1);
            Set<String> follows2 = red.getFollowers(fellows1[i]);
            if (esperado.containsKey(name1)) {
                assertEquals(esperado.get(name1).size(), follows1.size());
                assertTrue(esperado.get(name1).containsAll(follows1));
            } else
                assertEquals(0, follows1.size());
            if (esperado.containsKey(fellows1[i])) {
                assertEquals(esperado.get(fellows1[i]).size(), follows2.size());
                assertTrue(esperado.get(fellows1[i]).containsAll(follows2));
            } else
                assertEquals(0, follows2.size());

            prueba++;
            System.out.println("prueba " + prueba + ": commonFollowers(" + fellows1[i] + ", " + name1 + ")");
            System.out.println("esperado =" + es.toString());
            obtenido = red.commonFollowers(fellows1[i], name1);
            System.out.println("obtenido =" + obtenido.toString());
            System.out.println("número de nodos esperado: " + es.size() + " número de nodos obtenido: " + obtenido.size());
            assertEquals(es.size(), obtenido.size());
            assertTrue(es.containsAll(obtenido));
        }




    }


    @org.junit.Test()
    public void testSuggestionsException() {

        MiRedSocial red = new MiRedSocial();
        int cuenta = 0;
        String source = null;

        System.out.println("suggestions(null)");
        System.out.println("ESTADO FINAL ESPERADO: NullPointerException");

        boolean excepcion = false;
        try {
            red.suggestions(source);
        } catch (NullPointerException e) {
            System.out.println(" ...OK");
            excepcion = true;
        }
        assertTrue("Excepcion no lanzada", excepcion);
        cuenta++;


    }


    @org.junit.Test()
    public void testSuggestions() {
        System.out.println("\n ===== PROBANDO MÉTODO suggestions(name) =========");
        int prueba = 1;
        Map<String, Set<String>> miMapa = new HashMap<>();
        Set<String> esperado = new HashSet<>();

        MiRedSocial red = new MiRedSocial();


        String primero = fellows1[0];
        miMapa.put(primero, new HashSet<>());
        for (int i = 1; i < fellows1.length; i++) {
            red.addDirectedEdge(primero, fellows1[i]);
            miMapa.get(primero).add(fellows1[i]);
        }
        String source = fellows2[0];

        red.printGraphStructure();
        System.out.println("Prueba " + prueba + ": suggestions(" + source + ")");
        System.out.println("ESTADO FINAL ESPERADO: null");
        Set<String> obtenido = red.suggestions(source);
        assertNull(obtenido);


        prueba++;
        String name1 = fellows1[0];
        obtenido = red.suggestions(name1);
        System.out.println("Prueba " + prueba + " Probando suggestions(" + name1 + ")");
        System.out.println("esperado: " + new HashSet<>());
        System.out.println("obtenido " + obtenido.toString());

        assertTrue(obtenido.size() == 0);

        String anterior = name1;
        for (int i = 1; i < fellows1.length; i++) {
            prueba++;
            System.out.println("prueba " + prueba + ": suggestions(" + name1 + ")");
            red.addDirectedEdge(anterior, fellows1[i]);
            if (!miMapa.containsKey(anterior))
                miMapa.put(anterior, new HashSet<>());
            miMapa.get(anterior).add(fellows1[i]);
            if (anterior.equals(name1))
                esperado.remove(fellows1[i]);
            else if (miMapa.get(name1).contains(anterior) && !miMapa.get(name1).contains(fellows1[i]))
                esperado.add(fellows1[i]);

            System.out.println("esperado =" + esperado.toString());
            obtenido = red.suggestions(name1);
            System.out.println("obtenido =" + obtenido.toString());
            System.out.println("número de nodos esperado: " + esperado.size() + " número de nodos obtenido: " + obtenido.size());
            red.printGraphStructure();
            assertEquals(esperado.size(), obtenido.size());
            assertTrue(esperado.containsAll(obtenido));

            //Comprobar que no se ha modificado los followeds
            System.out.println("Comprobar que no se modifica el grafo");
            Set<String> follows1 = red.getFollowed(name1);
            Set<String> follows2 = red.getFollowed(anterior);
            Set<String> follows3 = red.getFollowed(fellows1[i]);
            System.out.println("seguidos por " + name1 + ": " + follows1);
            System.out.println("seguidos por " + anterior + ": " + follows2);
            System.out.println("seguidos por " + fellows1[i] + ": " + follows3);


            if (!miMapa.containsKey(name1))
                assertEquals(0, follows1.size());
            else {
                assertEquals(miMapa.get(name1).size(), follows1.size());
                assertTrue(miMapa.get(name1).containsAll(follows1));
            }
            if (!miMapa.containsKey(anterior))
                assertEquals(0, follows2.size());
            else {
                assertEquals(miMapa.get(anterior).size(), follows2.size());
                assertTrue(miMapa.get(anterior).containsAll(follows2));
            }
            if (!miMapa.containsKey(fellows1[i]))
                assertEquals(0, follows3.size());
            else {
                assertEquals(miMapa.get(fellows1[i]).size(), follows3.size());
                assertTrue(miMapa.get(fellows1[i]).containsAll(follows3));
            }
            anterior = fellows1[i];

        }

        anterior = fellows1[1];
        for (int i = 0; i < fellows2.length; i++) {
            prueba++;
            System.out.println("prueba " + prueba + ": suggestions(" + name1 + ")");
            red.addDirectedEdge(anterior, fellows2[i]);
            if (!miMapa.containsKey(anterior))
                miMapa.put(anterior, new HashSet<>());
            miMapa.get(anterior).add(fellows2[i]);
            if (anterior.equals(name1))
                esperado.remove(fellows2[i]);
            else if (miMapa.get(name1).contains(anterior) && !miMapa.get(name1).contains(fellows2[i]))
                esperado.add(fellows2[i]);

            System.out.println("esperado =" + esperado.toString());
            obtenido = red.suggestions(name1);
            System.out.println("obtenido =" + obtenido.toString());
            System.out.println("número de nodos esperado: " + esperado.size() + " número de nodos obtenido: " + obtenido.size());
            red.printGraphStructure();
            assertEquals(esperado.size(), obtenido.size());
            assertTrue(esperado.containsAll(obtenido));

            //Comprobar que no se ha modificado los followeds
            System.out.println("Comprobar que no se modifica el grafo");
            Set<String> follows1 = red.getFollowed(name1);
            Set<String> follows2 = red.getFollowed(anterior);
            Set<String> follows3 = red.getFollowed(fellows2[i]);
            System.out.println("seguidos por " + name1 + ": " + follows1);
            System.out.println("seguidos por " + anterior + ": " + follows2);
            System.out.println("seguidos por " + fellows2[i] + ": " + follows3);


            if (!miMapa.containsKey(name1))
                assertEquals(0, follows1.size());
            else {
                assertEquals(miMapa.get(name1).size(), follows1.size());
                assertTrue(miMapa.get(name1).containsAll(follows1));
            }
            if (!miMapa.containsKey(anterior))
                assertEquals(0, follows2.size());
            else {
                assertEquals(miMapa.get(anterior).size(), follows2.size());
                assertTrue(miMapa.get(anterior).containsAll(follows2));
            }
            if (!miMapa.containsKey(fellows2[i]))
                assertEquals(0, follows3.size());
            else {
                assertEquals(miMapa.get(fellows2[i]).size(), follows3.size());
                assertTrue(miMapa.get(fellows2[i]).containsAll(follows3));
            }

        }

        anterior = name1;
        for (int i = 0; i < fellows2.length; i++) {
            prueba++;
            System.out.println("prueba " + prueba + ": suggestions(" + name1 + ")");
            red.addDirectedEdge(anterior, fellows2[i]);
            if (!miMapa.containsKey(anterior))
                miMapa.put(anterior, new HashSet<>());
            miMapa.get(anterior).add(fellows2[i]);
            if (anterior.equals(name1))
                esperado.remove(fellows2[i]);
            else if (miMapa.get(name1).contains(anterior) && !miMapa.get(name1).contains(fellows2[i]))
                esperado.add(fellows2[i]);

            System.out.println("esperado =" + esperado.toString());
            obtenido = red.suggestions(name1);
            System.out.println("obtenido =" + obtenido.toString());
            System.out.println("número de nodos esperado: " + esperado.size() + " número de nodos obtenido: " + obtenido.size());
            red.printGraphStructure();
            assertEquals(esperado.size(), obtenido.size());
            assertTrue(esperado.containsAll(obtenido));

            //Comprobar que no se ha modificado los followeds
            System.out.println("Comprobar que no se modifica el grafo");
            Set<String> follows1 = red.getFollowed(name1);
            Set<String> follows2 = red.getFollowed(anterior);
            Set<String> follows3 = red.getFollowed(fellows2[i]);
            System.out.println("seguidos por " + name1 + ": " + follows1);
            System.out.println("seguidos por " + anterior + ": " + follows2);
            System.out.println("seguidos por " + fellows2[i] + ": " + follows3);


            if (!miMapa.containsKey(name1))
                assertEquals(0, follows1.size());
            else {
                assertEquals(miMapa.get(name1).size(), follows1.size());
                assertTrue(miMapa.get(name1).containsAll(follows1));
            }
            if (!miMapa.containsKey(anterior))
                assertEquals(0, follows2.size());
            else {
                assertEquals(miMapa.get(anterior).size(), follows2.size());
                assertTrue(miMapa.get(anterior).containsAll(follows2));
            }
            if (!miMapa.containsKey(fellows2[i]))
                assertEquals(0, follows3.size());
            else {
                assertEquals(miMapa.get(fellows2[i]).size(), follows3.size());
                assertTrue(miMapa.get(fellows2[i]).containsAll(follows3));
            }

        }

        //comprobar que no se incluye el propio nodo
        prueba++;
        red = new MiRedSocial();
        Map<String, Set<String>> mapa=new HashMap<>();
        for (int i=0; i<fellows1.length; i++) {
            mapa.put(fellows1[i],new HashSet<>());
        }
        mapa.get(fellows1[0]).add(fellows1[3]);
        red.addDirectedEdge(fellows1[0], fellows1[1]);
        red.addDirectedEdge(fellows1[0], fellows1[2]);
        red.addDirectedEdge(fellows1[1], fellows1[0]);
        red.addDirectedEdge(fellows1[1], fellows1[2]);
        red.addDirectedEdge(fellows1[1], fellows1[3]);
        red.addDirectedEdge(fellows1[3], fellows1[2]);
        red.printGraphStructure();
        System.out.println("Prueba "+prueba+ " suggestions("+fellows1[0]+")");
        System.out.println("Esperado "+mapa.get(fellows1[0]).toString());
        obtenido = red.suggestions(fellows1[0]);
        System.out.println("Obtenido "+obtenido.toString());
        assertEquals(mapa.get(fellows1[0]).size(), obtenido.size());
        assertTrue(mapa.get(fellows1[0]).containsAll(obtenido));
    }

    @org.junit.Test()
    public void testXDistanceToAll() {
        System.out.println("\n ===== PROBANDO MÉTODO distanceToAll =========");
        int prueba = 1;

        Map<String, Integer> esperado = new HashMap<>();

        MiRedSocial red = new MiRedSocial();


        String primero = fellows1[0];
        esperado.put(primero, 0);
        int cuenta = 1;
        for (int i = 1; i < fellows1.length; i++) {
            red.addDirectedEdge(primero, fellows1[i]);
            esperado.put(fellows1[i], cuenta);
        }

        red.printGraphStructure();
        System.out.println("Prueba " + prueba + ": " + "distaceToAll(" + primero + ")");
        Map<String, Integer> obtenido = red.distanceToAll(primero);
        assertEquals(esperado.keySet().size(), obtenido.keySet().size());
        for (String k : esperado.keySet()) {
            if (!obtenido.containsKey(k))
                System.out.println("Error: no se ha obtenido la distancia para " + k);
            else {
                System.out.println("Distancia esperada a "+k+": "+esperado.get(k));
                System.out.println("Distancia obtenida a "+k+": "+obtenido.get(k));
                assertEquals(esperado.get(k), obtenido.get(k));
            }

        }
        assertTrue(esperado.keySet().containsAll(obtenido.keySet()));

        prueba++;
        red = new MiRedSocial();

        primero = fellows1[0];
        esperado.put(primero, 0);
        cuenta = 0;
        for (int i = 0; i < fellows1.length-1; i++) {
            red.addDirectedEdge(fellows1[i], fellows1[i+1]);
            esperado.put(fellows1[i+1], ++cuenta);
        }

        red.printGraphStructure();
        System.out.println("Prueba " + prueba + ": " + "distaceToAll(" + primero + ")");
        obtenido = red.distanceToAll(primero);
        assertEquals(esperado.keySet().size(), obtenido.keySet().size());
        for (String k : esperado.keySet()) {
            if (!obtenido.containsKey(k))
                System.out.println("Error: no se ha obtenido la distancia para " + k);
            else {
                System.out.println("Distancia esperada a " + k + ": " + esperado.get(k));
                System.out.println("Distancia obtenida a " + k + ": " + obtenido.get(k));
                assertEquals(esperado.get(k), obtenido.get(k));
            }
        }
        assertTrue(esperado.keySet().containsAll(obtenido.keySet()));

    }

    static public String ficheroRef = "miredsocialNueva.ref";

    private static int nNodos;
    private static String [] nodos; // nodos
    private static List<String>[] seguidosComp; //nodos adyacentes
    private static List<String>[] seguidoresComp;
    private static String popular;

    private static ArrayList<String> nodos2 = new ArrayList<>();
    private static ArrayList<Map<String,Integer>> distanciasComp = new ArrayList<>();

    private static class Data {
        String name;
        ArrayList<String> elements;
        public Data() {
            elements=new ArrayList<String>();
        }
    }

    private static class Tinfo {
        String name;
        ArrayList<Data> lista;

        public Tinfo() {
            lista = new ArrayList<Data>();
        }
    }

    //followers
    private static ArrayList<Data> seg = new ArrayList<> ();
    //sugerencias
    private static ArrayList<Data> su = new ArrayList<>();
    //common Followers
    private static ArrayList<Tinfo> coF = new ArrayList<Tinfo>();
    //common followed
    private static ArrayList<Tinfo> coff = new ArrayList<Tinfo>();

    private static void leerDatos() {
        RandomAccessFile ref=null;
        try {
            ref = new RandomAccessFile(ficheroRef, "r");

        } catch(FileNotFoundException e) {
            System.err.println("  No se pudo abrir el fichero");
            return;
        }

        try {
            //leer Nodos y Arcos
            nNodos=ref.readInt();
            System.out.println("nNodos: "+nNodos);
            nodos = new String[nNodos];
            seguidosComp =   new ArrayList[nNodos];
            seguidoresComp = new ArrayList[nNodos];
            for (int i=0; i<nNodos; i++) {
                nodos[i]=ref.readUTF();
                int nseguidos = ref.readInt();
                seguidosComp[i] = new ArrayList();
                for (int j=0; j<nseguidos; j++)
                    seguidosComp[i].add(ref.readUTF());
                int nseguidores = ref.readInt();
                seguidoresComp[i] = new ArrayList();
                for (int j=0; j< nseguidores; j++)
                    seguidoresComp[i].add(ref.readUTF());
            }

            popular = ref.readUTF(); //most popular

            for (int i=0; i<nNodos; i++) {
                String name = ref.readUTF();
                nodos2.add(name);
                Map<String, Integer> dist = new HashMap<>();
                int nkeys = ref.readInt();
                for (int j=0; j<nkeys; j++) {
                    dist.put(ref.readUTF(), ref.readInt());
                }
                distanciasComp.add(dist);
            }


            for (int i=0; i<nNodos; i++) {
                String n1 = ref.readUTF();
                Data inf_se = new Data();
                Data inf_su = new Data();
                //inf_se.name =n1;
                //int nse = ref.readInt();
                //for (int k=0; k<nse; k++)
                //    inf_se.elements.add(ref.readUTF());

                //seg.add(inf_se);

                inf_su.name=n1;
                int nsu = ref.readInt();
                for (int k=0; k<nsu; k++)
                    inf_su.elements.add(ref.readUTF());

                su.add(inf_su);

                Tinfo inf_cfers = new Tinfo();
                Tinfo inf_cfed = new Tinfo();
                inf_cfers.name =n1;
                inf_cfed.name =n1;
                for (int j=0; j<nNodos-1; j++) {
                    Data d = new Data();
                    String n2 = ref.readUTF();
                    d.name = n2;
                    int nElem = ref.readInt();
                    for (int k=0; k<nElem; k++)
                        d.elements.add(ref.readUTF());
                    inf_cfers.lista.add(d);

                    Data d2 = new Data();
                    d2.name = n2;
                    nElem = ref.readInt();
                    for (int k=0; k<nElem; k++)
                        d2.elements.add(ref.readUTF());
                    inf_cfed.lista.add(d2);
                }
                coF.add(inf_cfers);
                coff.add(inf_cfed);

            }
            ref.close();
        } catch (IOException e) {
            System.err.println("Error en la lectura del fichero de referencia");
            return;
        }
    }

    @org.junit.Test()
    public final void  testZComprobarGrafo() {

        String filename = "miredsocial.txt";
        MiRedSocial red = new MiRedSocial();
        red.readGraph(filename);

        if (red == null)
            fail("No se pudo leer el grafo");

        leerDatos();

        System.out.println(" Comprobando el grafo ");

        red.printGraphStructure();

        System.out.println("  Numero de nodos ");
        assertEquals(nNodos, red.size());

        boolean expected;
        System.out.println("Comprobando que están todos los nodos");
        for (int i=0; i<nNodos; i++) {
            expected = red.containsNode(nodos[i]);
            System.out.println("\n Comprobar que está el nodo  "+nodos[i]);
            assertTrue(expected);


            Set<String> seguidos = red.getFollowed(nodos[i]);
            System.out.println("Comprobar el número de nodos seguidos: "+seguidos.size());
            assertEquals(seguidosComp[i].size(),seguidos.size());
            System.out.println("Comprobar los seguidos (adyacentes) ");
            System.out.println("esperado: "+seguidosComp[i]);
            System.out.println("obtenido: "+seguidos);

            assertTrue(seguidos.containsAll(seguidosComp[i]));

            System.out.println("Comprobando los seguidores");
            Set<String> seguidores = red.getFollowers(nodos[i]);
            System.out.println("Comprobar el número de nodos seguidores: "+seguidores.size());
            assertEquals(seguidoresComp[i].size(),seguidores.size());
            System.out.println("Comprobar los seguidores (adyacentes desde)");
            System.out.println("esperado: "+seguidoresComp[i]);
            System.out.println("obtenido: "+seguidores);

            assertTrue(seguidores.containsAll(seguidoresComp[i]));
        }

        System.out.println("\n Comprobando mostInfluencer");
        String obtenido = red.mostInfluencer();
        System.out.println("Esperado: "+ popular +" Obtenido: "+obtenido );
        assertEquals(popular,red.mostInfluencer());

        System.out.println("\n Comprobando las distancias entre cada par de nodos ");

        for (int i=0; i<nNodos; i++) {
            String name=nodos2.get(i);
            Map<String, Integer> dobtenidas = red.distanceToAll(nodos[i]);
            Map<String, Integer> desperadas = distanciasComp.get(i);
            System.out.println("Distancias desde el nodo "+name);
            for (String k: dobtenidas.keySet()) {
                System.out.println( "  ... para "+ k +": esperada "+desperadas.get(k)+", obtenida "+dobtenidas.get(k));
                assertEquals(dobtenidas.get(k),desperadas.get(k));
            }
            System.out.println();
        }
    }
}
