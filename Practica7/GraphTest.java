import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class GraphTest {

    private static <T, W> Set<T> convertFromIndex(EDGraph<T, W> graph, Set<Integer> indexes) {
        Set<T> ret = new HashSet<>();

        for (int i : indexes)
            ret.add(graph.getNodeValue(i));

        return ret;
    }

    private static <T, W> boolean compareGraphs(EDGraph<T, W> first, EDGraph<T, W> second) {
        Map<T, Integer> map1 = first.getNodes();
        Map<T, Integer> map2 = second.getNodes();

        if (map1.size() != map2.size()) {
            System.out.println("Los grafos tienen distinto tamaño");
            return false;
        }

        if (!map1.keySet().equals(map2.keySet())) {
            System.out.println(" El contenido de los nodos es ditinto");
            System.out.println(" 1 -> " + map1.keySet());
            System.out.println(" 2 -> " + map1.keySet());
            return false;
        }
        for (T item : map1.keySet()) {
            Set<T> val1 = convertFromIndex(first, first.getOutgoing(map1.get(item)));
            Set<T> val2 = convertFromIndex(second, first.getOutgoing(map2.get(item)));
            if (!val1.equals(val2)) {
                System.out.println("Los arcos de nodo " + item + " son distintos");
                System.out.println("1 -> " + val1);
                System.out.println("2 -> " + val2);
                return false;
            }
        }

        return true;
    }

    private static String[] testFiles = {"laberinto1.txt", "laberinto2.txt", "laberinto3.txt", "laberinto4.txt",
            "laberinto5.txt", "grafoDir1.txt", "grafoDir2.txt", "grafoDir3.txt", "grafoDir4.txt"};

    @Test
    public void EDListGraphTest() {
        for (String file : testFiles) {
            System.out.println("\nPRUEBA CON FICHERO " + file + "\n");
            System.out.println("GRAFO CON MATRIZ DE ADYACENCIA");
            EDMatrixGraph<String, Object> caso = new EDMatrixGraph<>(file);
            caso.printGraphStructure();

            System.out.println("\nGRAFO CON LISTAS DE ADYACENCIA");
            EDListGraph<String, Object> esperado = new EDListGraph<>(caso);
            esperado.printGraphStructure();

            System.out.println("\nCOMPARANDO GRAFOS...");
            assertTrue(compareGraphs(caso, esperado));
            System.out.print("  OK");
        }
    }

    /**
     * Crea grafo de Characteres con pesos dirigido vac�o
     */
    private EDListGraph<Character, Double> createDirectedGraph()  {
        return  new EDListGraph<Character,Double>(true);
    }

    /**
     * * Crea grafo de Characteres con pesos no dirigido vac�o
     */
    private EDListGraph<Character, Double> createUnDirectedGraph()  {
        return  new EDListGraph<Character,Double>();
    }

    @Test
    public void testInsertEdge() {
        System.out.println("\nValidando insertEdge (Edge e)...\n");
        EDListGraph<Character,Double> g = createDirectedGraph();

        System.out.println("adding an edge in an empty graph ...");
        EDEdge<Double> e = new EDEdge<Double> (0,1,1.0);

        System.out.println("trying to add an edge before corresponding nodes ...");
        assertFalse("insertEdge returns false ...",g.insertEdge(e));

        Character col1= 'A';
        Character col2 = 'B';
        Character col3 = 'C';

        int index1 = g.insertNode(col1);
        int index2 = g.insertNode(col2);
        int index3 = g.insertNode(col3);

        e = new EDEdge<Double>(index3,index1,1.0);

        EDEdge<Double> checkEdge = g.getEdge(index1, index2);
        assertEquals("arco no existe ...", checkEdge, null);

        g.insertEdge(e);
        checkEdge = g.getEdge(index3, index1);
        assertNotEquals("arco existe ...", checkEdge, null);
        System.out.println("retrieved edge ..."+checkEdge.toString());
        assertEquals ("Comprobar origen ...", checkEdge.getSource(), index3);
        Character origen = g.getNodeValue(checkEdge.getSource());
        assertTrue ("Comprobar origen ...", origen.equals('C'));
        assertEquals ("Comprobar destino ...", checkEdge.getTarget(), index1);
        assertTrue ("Comprobar destino ...", g.getNodeValue(checkEdge.getTarget()).equals('A'));

        EDEdge<Double> reverse = g.getEdge(index1, index3);
        assertEquals("arco inverso no exise en grafo dirigido ...",reverse,null);

        System.out.println("Comprobar que no se insertan arcos repetidos ...");
        assertFalse(g.insertEdge(checkEdge));

        EDListGraph<Character,Double> g2 = createUnDirectedGraph();


        index1 = g2.insertNode(col1);
        index2 = g2.insertNode(col2);
        index3 = g2.insertNode(col3);

        e = new EDEdge<Double>(index3,index1,2.0);

        checkEdge = g2.getEdge(index1, index2);
        assertEquals("arco no existe ...", checkEdge, null);

        g2.insertEdge(e);
        checkEdge = g2.getEdge(index3, index1);
        assertNotEquals("arco existe ...", checkEdge, null);
        assertEquals ("Comprobar origen ...", checkEdge.getSource(), index3);
        assertTrue ("Comprobar origen ...", g2.getNodeValue(checkEdge.getSource()).equals('C'));
        assertEquals ("Comprobar destino ...", checkEdge.getTarget(), index1);
        assertTrue ("Comprobar destino ...", g2.getNodeValue(checkEdge.getTarget()).equals('A'));


        reverse = g2.getEdge(index1, index3);
        assertNotEquals("arco inverso existe ...",reverse,null);
        assertEquals("arcos inversos iguales ...", checkEdge.getSource(),reverse.getTarget());
        assertEquals("arcos inversos iguales ...", checkEdge.getTarget(), reverse.getSource());
        assertEquals("arcos inversos iguales ...", checkEdge.getWeight(), reverse.getWeight());

        System.out.println("Comprobar que no se insertan arcos repetidos ...");
        assertFalse(g2.insertEdge(checkEdge));
        assertFalse(g2.insertEdge(reverse));
    }

    private <T> Set<T> exitDegree(EDGraph<T, ?> graph) {
        Map<T, Integer> map = graph.getNodes();
        Set<Integer> first = new HashSet<>();
        Set<Integer> some = new HashSet<>();

        for (T item : map.keySet()) {
            Set<Integer> out = graph.getOutgoing(map.get(item));
            first.removeAll(out);
            Set<Integer> aux = new HashSet<>(out);
            aux.removeAll(some);
            first.addAll(aux);
            some.addAll(out);
        }
        Set<T> ret = new HashSet<>();
        for (int id : first)
            ret.add(graph.getNodeValue(id));

        return ret;
    }

    @Test
    public void degree1ListTest() {
        for (String file : testFiles) {
            System.out.println("\nPRUEBA CON FICHERO " + file + "\n");
            EDMatrixGraph<String, Object> temp = new EDMatrixGraph<>(file);
            EDListGraph<String, Object> caso = new EDListGraph<>(temp);

            caso.printGraphStructure();

            System.out.println("degree1()");
            System.out.println("RESULTADO ESPERADO");
            Set<String> esperado = exitDegree(caso);
            System.out.println(esperado);

            System.out.println("RESULTADO OBTENIDO");
            Set<String> obtenido = caso.degree1();
            System.out.println(obtenido);

            assertEquals(esperado, obtenido);
        }
    }

    @Test
    public void degree1MatrixTest() {
        for (String file : testFiles) {
            System.out.println("\nPRUEBA CON FICHERO " + file + "\n");
            EDMatrixGraph<String, Object> caso = new EDMatrixGraph<>(file);
            caso.printGraphStructure();

            System.out.println("degree1()");
            System.out.println("RESULTADO ESPERADO");
            Set<String> esperado = exitDegree(caso);
            System.out.println(esperado);

            System.out.println("RESULTADO OBTENIDO");
            Set<String> obtenido = caso.degree1();
            System.out.println(obtenido);

            assertEquals(esperado, obtenido);
        }
    }

    private <T> boolean checkPath(EDGraph<T, ?> g, List<T> path, T first, T last ) {
        if (path.size() <= 1)
            return false;

        Iterator<T> iter = path.iterator();
        T source = iter.next();

        if (g.getNodeIndex(source) == -1) {
            System.out.println("El nodo " + source + " no esta en el grafo");
            return false;
        }

        while (iter.hasNext()) {
            T target = iter.next();
            if (g.getNodeIndex(target) == -1) {
                System.out.println("El nodo " + target + " no esta en el grafo");
                return false;
            }

            if (g.getEdge(g.getNodeIndex(source), g.getNodeIndex(target)) == null){
                System.out.println("No existe el arco entre "+  source + " y " + target);
                return false;
            }

            source = target;
         }


        if (!first.equals (path.get(0))) {
            System.out.println("El primer nodo del camino no es correcto, es " + first +
                    " y debería ser "+ path.get(0));
            return false;
        }

        if (!last.equals (path.get(path.size()-1))) {
            System.out.println("El ultimo nodo del camino no es correcto, es " + last +
                    " y debería ser "+ path.get(path.size()-1));
            return false;
        }

        return true;
    }

    @Test
    public void findPathTest() {
        for (String file : testFiles) {
            System.out.println("\nPRUEBA CON FICHERO " + file + "\n");
            EDMatrixGraph<String, Object> matrix = new EDMatrixGraph<>(file);
            EDListGraph<String, Object> caso = new EDListGraph<>(matrix);
            caso.printGraphStructure();

            System.out.println("findPath(\"A\", \"S\")");
            System.out.println("RESULTADO OBTENIDO");
            int s = caso.getNodeIndex("A");
            int d = caso.getNodeIndex("S");
            System.out.println("(" + s + ", " + d + ")");
            List<String> camino = caso.findPath(s, d);
            System.out.println(camino);

            assertTrue(checkPath(caso, camino, "A", "S"));
        }
    }
}
