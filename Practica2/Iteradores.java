import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;


public class Iteradores {


	public static void partir(Set<Integer> pares, Set<Integer> impares) {
	    // Listas auxiliares para acumular por paridad (resultado final)
	    List<Integer> listaPares = new ArrayList<>();
	    List<Integer> listaImpares = new ArrayList<>();

	    // Iteradores sobre los conjuntos de entrada44
	    Iterator<Integer> itPares = pares.iterator();
	    Iterator<Integer> itImpAres = impares.iterator();

	    // Se recorren 'pares' y se clasifica cada elemento por su paridad
	    while (itPares.hasNext()) {
	        Integer element = itPares.next();
	        if (element % 2 == 0)
	            listaPares.add(element);     // par -> a listaPares
	        else
	            listaImpares.add(element);   // impar -> a listaImpares
	    }

	    // Se recorre 'impares' y se vuelve a clasificar
	    while (itImpAres.hasNext()) {
	        Integer element = itImpAres.next();
	        if (element % 2 != 0)
	            listaImpares.add(element);   // impar -> a listaImpares
	        else
	            listaPares.add(element);     // par -> a listaPares
	    }

	    // Se limpia e inserta el resultado clasificado
	    pares.clear();
	    impares.clear();
	    pares.addAll(listaPares);     // 'pares' contendrá todos los pares de ambos conjuntos
	    impares.addAll(listaImpares); // 'impares' contendrá todos los impares de ambos conjuntos
	}
	

	public static int intercambio(ListIterator<Integer> iter) {
        List<Integer> lista = new ArrayList<>();

        // Se mueve el iterador al principio de la lista
        while (iter.hasPrevious())
            iter.previous();

        // Se añaden todos los elementos de la lista original a "lista"
        while (iter.hasNext())
            lista.add(iter.next());

        // Se vacía la lista original
        while (iter.hasPrevious()) {
            iter.previous();
            iter.remove();
        }

        // Se intercambian los elementos de las posiciones pares e impares de lista
        for (int i = 0; i < lista.size() - 1; i += 2) {
            int temp = lista.get(i);
            lista.set(i, lista.get(i + 1));
            lista.set(i + 1, temp);
        }

        // Se añaden los valores de lista ya intercambiados a la lista original
        for (Integer e : lista)
            iter.add(e);

        return lista.size();
	}
		
	

	public static int trespatras(ListIterator<Integer> iter) {
        int cont  = 0, pos = 1;
        List<Integer> listaMult3 = new ArrayList<>();

        // Se mueve el iterador al principio para recorrer la lista entera sin importar la posición en que se inicializa el iterador
        while (iter.hasPrevious())
            iter.previous();

        while (iter.hasNext()) {
            Integer element = iter.next();
            // Si el elemento que esta en la posición "pos" es múltiplo de 3 se elimina de la lista para después añadirlo al final
            if (pos % 3 == 0) {
                listaMult3.add(element);
                iter.remove();
                cont++;
            }
            pos++;
        }

        // Se añaden los elementos múltiplos de 3 al final de la lista que recorre el iterador
        for (Integer e : listaMult3)
            iter.add(e);

        return cont;
	}

	public static<T> void agruparRepetidos (ListIterator<T> iter) {
        List<T> lista = new ArrayList<>();
        List<T> listaAgrupada = new ArrayList<>();
        Set<T> setAgrupado = new HashSet<>();

        // Se mueve el iterador al principio de la lista.
        while (iter.hasPrevious())
            iter.previous();

        // Se añaden todos los elementos a "lista"
        while (iter.hasNext()) {
            T element = iter.next();
            lista.add(element);
        }

        for (int i = 0; i < lista.size() - 1; i++) {
            T elementI = lista.get(i);
            // Si ya se agrupó el elemento elementI antes, se salta para no duplicar grupos
            if (setAgrupado.contains(elementI))
                continue;
            // Se Marca como procesado y añade la primera aparición al resultado
            setAgrupado.add(elementI);
            listaAgrupada.add(elementI);
            // Se busca desde la siguiente posición todas las repeticiones de 'elementI' en la lista original
            for (int j = i + 1; j < lista.size(); j++)
                if (lista.get(j).equals(elementI))
                    // Se añade cada repetición para que queden contiguas en 'listaAgrupada'
                    listaAgrupada.add(lista.get(j));
        }

        // Se borran los elementos de la lista original
        while (iter.hasPrevious()) {
            iter.previous();
            iter.remove();
        }

        // Se añade a la lista original (ahora vacía) la lista agrupada
        for (T e : listaAgrupada)
            iter.add(e);
	}

 }
