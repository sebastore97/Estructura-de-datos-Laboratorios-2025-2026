import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;


public class IteradoresJUnit {

	final static private String [][] vAgruparRepetidosCasos = {{}, {"A"}, {"A", "B"}, {"A", "A", "B"}, {"A", "B", "B"}, {"A", "B","C"},
			{"A","A","A","B","B","C"},{"A","A","A","B","B","C","C","C","C"},{"A","A","A","B","C","C","C","C"},
			{"A","A","B","C","D","D","D","E","E"}, {"A","B","C","D","D","D","E","E"},{"A","A","B","C","D","D","E","E"},
			{"A","A","B","C","D","D","D","E"}, {"A","A","B","C","B","D","D","D","E","E"}};

	private static List<Integer> pares(int[] v1, int v2[]) {
		List<Integer> l = new ArrayList<Integer>();
		
		for (int i: v1) if (i % 2 == 0) l.add(i);
		for (int i: v2) if (i % 2 == 0) l.add(i);
		
		return l;
 	}
	
	private static List<Integer> impares(int v1[], int v2[]) {
		List<Integer> l = new ArrayList<Integer>();
		
		for (int i: v1) if (i % 2 == 1) l.add(i);
		for (int i: v2) if (i % 2 == 1) l.add(i);
		
		return l;
		
	}
	
	
	private static boolean equivalenteSetList(Set<Integer> s, List<Integer> l ) {
		return s.containsAll(l) && l.containsAll(s);
	}
	
	private static int partirTestData[][] = {
		{5, 2, 9, 10, 8, 7},
		{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
		{0, 2, 4, 6, 8, 10},
		{1, 3, 5, 7, 9},
		{},
		{0, 1, 2, 3, 4, 5},
		{6, 7, 8, 9, 10}
	};
	
	@Test
	public void testPartir() {
		System.out.println("\nValidando método partir()...");
		System.out.println("  conj1: [conjunto 1]\n  conj2: [conjunto 2]\n  -> pares:   [pares]\n  -> impares: [impares]\n");
		Set<Integer> s1 = new HashSet<Integer>();
		Set<Integer> s2 = new HashSet<Integer>();
		
		for (int i = 0; i < partirTestData.length; i++)
			for(int j = i; j < partirTestData.length; j++) {	
				int v1[] = partirTestData[i];
				int v2[] = partirTestData[j];
				
				s1.clear(); s2.clear();
				
				for (int n: v1) s1.add(n);
				for (int n: v2) s2.add(n);
				
				System.out.println("  conj1: " + s1.toString() + "\n  conj2: "  + s2.toString());
				
				Iteradores.partir(s1, s2);
				
				System.out.println("  -> pares:  " + s1.toString() + "\n  -> impares:"  + s2.toString());
				
				assertTrue(equivalenteSetList(s1, pares(v1, v2)));
				assertTrue(equivalenteSetList(s2, impares(v1, v2)));
				System.out.println("  ...ok\n");

				
				s1.clear(); s2.clear();
				
				for (int n: v2) s1.add(n);
				for (int n: v1) s2.add(n);
				
				System.out.println("  conj1: " + s1.toString() + "\n  conj2: "  + s2.toString());
				
				Iteradores.partir(s1, s2);
				
				System.out.println("  -> pares:  " + s1.toString() + "\n  -> impares:"  + s2.toString());
				assertTrue(equivalenteSetList(s1, pares(v1, v2)));
				assertTrue(equivalenteSetList(s2, impares(v1, v2)));
				System.out.println("  ...ok\n");
				
			}
	}
	
	private static int[] vintercambiar(int v[]) {
		int res[] = new int[v.length];
		
		for (int i = 0; i < v.length-1; i += 2) {
			res[i] = v[i+1];
			res[i+1] = v[i];
		}
		
		if (res.length %2 == 1)
			res[v.length-1] = v[v.length-1];
		
		return res;
	}
	
	private boolean equivalenteListVector(List<Integer> l, int v[]) {
		if (l.size() != v.length) return false;
		
		for(int i = 0; i < l.size(); i++)
			if (l.get(i) != v[i]) return false;
		
		return true; 
	}
	
	private static String printListaIterados(List<Integer> l, ListIterator<Integer> iter) {
		int idx = iter.nextIndex();
		StringBuilder str = new StringBuilder("[");
		
		ListIterator<Integer> iter2 = l.listIterator();
		
		if (idx == 0)
			str.append(" *it* ");
		
		while (iter2.hasNext()) {
			str.append(iter2.next());
			
			if (iter2.nextIndex() == idx)
				str.append(" *it *");
			else if (iter2.hasNext())
				str.append(", ");
		}
		str.append("]");
		
		return str.toString();
		
	}
	
	private int intercambioTestData[][] = {
		{},
		{3},
		{5, 9},
		{12, 13, 14},
		{18, 19, 20, 0},
		{0, 9, 0, 9, 0},
		{0, 1, 1, 2, 3, 5},
		{0, 1, 4, 16, 32, 64, 128, 256, 512},
		{0, 1, 2, 3, 4, 5, 6, 6, 5, 4, 3, 2, 1, 0}
	};
	
	
	private void testIntercambioCore(int v[], int pos) {
		List<Integer> l = new LinkedList<Integer>();
		for (int n: v) l.add(n);
		
		ListIterator<Integer> iter = l.listIterator(pos);
		
		System.out.println("  " + l + " (pos. iter:" + iter.nextIndex() + ")");
		
		int retVect[] = vintercambiar(v);
		int retVal = Iteradores.intercambio(iter);
		
		System.out.println("  -> " + l + " - " + retVal);
		
		assertEquals(retVal, v.length);
		assertTrue(equivalenteListVector(l, retVect));
		
		System.out.println("  ...ok\n");
	}
	
	@Test
	public void testIntercambio() {
		System.out.println("\nValidando método intercambio()...");
		System.out.println("  [lista de entrada]\n -> [lista modificada] - talla calculada por el método\n");
		
		for(int i = 0; i < intercambioTestData.length; i++) {
			int v[] = intercambioTestData[i];
			
			if (v.length < 4) {
				for (int j = 0; j <= v.length; j++)  
					testIntercambioCore(v, j);
			} else {
				testIntercambioCore(v, 0);
				testIntercambioCore(v, v.length/4);
				testIntercambioCore(v, v.length/2);
				testIntercambioCore(v, (v.length/4)*3);
				testIntercambioCore(v, v.length);
			}
		}
	}

	
	private static int[] detresentres(int v[]) {
		int retV[] = new int[v.length];
		int iq = v.length - v.length/3;
		int i = 0;
		int j;
		
		for (j = 0; j < v.length/3; j++) {
			retV[i] = v[j*3];
			retV[i+1] = v[j*3+1];
			retV[iq] = v[j*3+2];
			i += 2;
			iq++;
		}
		
		if (v.length%3 != 0) {
			j = j*3;
			while (j<v.length)
				retV[i++] = v[j++];
		}
		
		return retV;
	}
	
	private int trespatrasTestData[][] = {
			{},
			{1},
			{1, 2},
			{1, 2, 3},
			{1, 2, 3, 4},
			{1, 2, 3, 4, 5},
			{1, 2, 3, 4, 5, 6},
			{0, 0, 1, 0, 0, 2, 0, 0, 3, 0, 0},
			{0, 1, 2, 3, 4, 5, 0, 1, 2, 3, 4, 5, 0, 1}
		};
		
	
	private void testTrespatrasCore(int v[], int pos) {
		List<Integer> l = new LinkedList<Integer>();
		for (int n: v) l.add(n);
		
		ListIterator<Integer> iter = l.listIterator(pos);
		
		System.out.println("  " + l + " (pos. iter:" + iter.nextIndex() + ")");
		
		int retVect[] = detresentres(v);
		int retVal = Iteradores.trespatras(iter);
		
		System.out.println("  -> " + l + " - " + retVal);
		
		assertEquals(retVal, v.length/3);
		assertTrue(equivalenteListVector(l, retVect));
		
		System.out.println("  ...ok\n");
	}
	
	@Test
	public void testTrespatras() {
		System.out.println("\nValidando método trespatras()...");
		System.out.println("  [lista de entrada]\n -> [lista modificada] - elementos \n");
		
		for(int i = 0; i < intercambioTestData.length; i++) {
			int v[] = trespatrasTestData[i];
			
			if (v.length < 4) {
				for (int j = 0; j <= v.length; j++)  
					testTrespatrasCore(v, j);
			} else {
				testTrespatrasCore(v, 0);
				testTrespatrasCore(v, v.length/4);
				testTrespatrasCore(v, v.length/2);
				testTrespatrasCore(v, (v.length/4)*3);
				testTrespatrasCore(v, v.length);
			}
		}
	}

	private static<T> List<T> comprobarAgrupar (List<T> l) {
		HashMap<T,Integer> elems = new HashMap<>();
		ArrayList<T> claves = new ArrayList<>();
		for (T elem: l) {
			if (!elems.containsKey(elem)) {
				elems.put(elem, 1);
				claves.add(elem);
			}
			else
				elems.put(elem, elems.get(elem) + 1);
		}
		ArrayList<T> res = new ArrayList<>();
		for (T elem: claves) {
			int n=elems.get(elem);
			for (int k=0; k<n; k++)
				res.add(elem);
		}
		return res;
	}


	@org.junit.Test
	public void agruparRepetidosTest () {
		int cuenta=1;
		for (int i=0; i<vAgruparRepetidosCasos.length; i++) {
			List<String> caso = new ArrayList<>();
			caso = Arrays.asList(vAgruparRepetidosCasos[i]);
			for (int j=0; j<caso.size()-1; j++) {
				List<String> copia = new ArrayList<>(caso);
				Collections.shuffle(copia);
				System.out.println("Caso "+cuenta);
				System.out.println("Entrada: "+copia.toString());
				Iteradores.agruparRepetidos(copia.listIterator());
				System.out.println("Obtenido: "+copia.toString());
				List<String> esperado  = comprobarAgrupar(copia);
				System.out.println("Esperado: "+esperado.toString());
				assertEquals(esperado,copia);
				cuenta++;
			}
		}
	}
}
