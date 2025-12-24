package practica1;

import com.sun.jdi.IntegerValue;

import java.util.*;

public class Practica1 {

    //EJERCICIO 1
    public static Set<Integer> multiplos (Iterator<Integer> it) {
        List<Integer> lista_base = new ArrayList<>();
        Set<Integer> setMultiplos = new HashSet<>();

        // Recorre el iterador y agrega los valores distintos de cero a la lista base
        while (it.hasNext()) {
            int val = it.next();
            if (val != 0)
                lista_base.add(val);
        }

        // Para cada elemento, verifica si es múltiplo de algún otro elemento posterior
        for (int i = 0; i < lista_base.size(); i++) {
            int val_A = lista_base.get(i);
            for (int j = i + 1; j < lista_base.size(); j++) {
                int val_B = lista_base.get(j);
                if (val_A % val_B == 0) {
                    setMultiplos.add(val_A);
                    break;
                }
            }
        }

        // Retorna el conjunto de múltiplos encontrados
        return setMultiplos;
    }

    //EJERCICIO 2
    public static void separate (Set<Integer> cuadrados, Set<Integer> noCuadrados)  {
        List<Integer> listaA = new ArrayList<>(cuadrados);
        List<Integer> listaB = new ArrayList<>(noCuadrados);
        Set<Integer> setUnion = new HashSet<>(cuadrados);
        setUnion.addAll(noCuadrados);
        Set<Integer> setCuadrados = new HashSet<>();

        //Se comprueba si hay algún cuadrado en los elementos del primer conjunto y se añade al conjunto de cuadrados
        //Se excluye comprobar si el elemento en la misma posición es cuadrado de sí mismo
        for (int i = 0; i < listaA.size(); i++) {
            int elementI = listaA.get(i);
            for ( int j = listaA.size() - 1; j >= 0; j--) {
                int elementJ = listaA.get(j);
                if (j != i) {
                    if (elementI == elementJ * elementJ)
                        setCuadrados.add(elementI);
                    if (elementJ == elementI * elementI)
                        setCuadrados.add(elementJ);
                }
            }
        }

        //Se comprueba si hay algún cuadrado en los elementos del segundo conjunto y se añade al conjunto de cuadrados
        //Se excluye comprobar si el elemento en la misma posición es cuadrado de sí mismo
        for (int i = 0; i < listaB.size(); i++) {
            int elementI = listaB.get(i);
            for ( int j = listaB.size() - 1; j >= 0; j--) {
                int elementJ = listaB.get(j);
                if (j != i) {
                    if (elementI == elementJ * elementJ)
                        setCuadrados.add(elementI);
                    if (elementJ == elementI * elementI)
                        setCuadrados.add(elementJ);
                }
            }
        }

        //Comprueba si alguno de los elementos del segundo conjunto son el cuadrado de alguno de los elementos
        // del primer conjunto y viceversa y lo añade al conjunto de cuadrados
        for (int elementX : listaA)
            for (int elementZ : listaB) {
                if (elementZ == elementX * elementX)
                    setCuadrados.add(elementZ);
                if (elementX == elementZ * elementZ)
                    setCuadrados.add(elementX);
            }

        cuadrados.clear();
        cuadrados.addAll(setCuadrados);
        noCuadrados.clear();
        noCuadrados.addAll(setUnion);
        noCuadrados.removeAll(setCuadrados);
    }

    //EJERCICIO 3
    public static <T> Collection<Set<T>> divideInSets(Iterator<T> it) {
        // Lista de conjuntos donde se almacenarán los resultados
        List<Set<T>> listaConjuntos = new ArrayList<>();
        // Mapa para contar cuántas veces aparece cada elemento
        Map<T, Integer> mapApariciones = new HashMap<>();

        while (it.hasNext()) {
            T element = it.next();
            // Obtiene el número de apariciones actual del elemento
            Integer nApariciones = mapApariciones.get(element);
            if (nApariciones == null)
                nApariciones = 1; // Primera vez que aparece
            else
                nApariciones += 1; // Incrementa el contador si ya apareció antes
            mapApariciones.put(element, nApariciones);
            // Asegura que haya suficientes conjuntos en la lista para la aparición actual
            while (listaConjuntos.size() < nApariciones) {
                Set<T> setX = new HashSet<>();
                listaConjuntos.add(setX);
            }
            // Agrega el elemento al conjunto correspondiente a su aparición
            Set<T> conjuntoActual = listaConjuntos.get(nApariciones - 1);
            conjuntoActual.add(element);
        }

        // Devuelve la colección de conjuntos generados
        return listaConjuntos;
    }

    //EJERCICIO 4
    public static<T> Collection<Set<T>> coverageSet2 (Set<T> u,ArrayList<Set<T>> col) {
        // Inicializa conjuntos auxiliares y la colección de respuesta
        Set<T> setA = new HashSet<>();
        Set<T> setB = new HashSet<>();
        Set<T> setX = new HashSet<>();
        Collection<Set<T>> resp = new HashSet<>();

        // Recorre todas las parejas posibles de conjuntos en col
        for (int i = 0; i < col.size(); i++) {
            for (int j = i + 1; j < col.size(); j++) {
                // Asigna setA y setB si no son iguales a u
                if (!col.get(i).equals(u))
                    setA = col.get(i);
                if (!col.get(j).equals(u))
                    setB = col.get(j);
                // Une setA y setB en setX
                setX.addAll(setA);
                setX.addAll(setB);
                // Si la unión es igual a u, retorna ambos conjuntos
                if (u.equals(setX)) {
                    resp.add(setA);
                    resp.add(setB);
                    return resp;
                }
                // Limpia setX para la siguiente iteración
                setX.clear();
            }
        }

        // Si no se encontró pareja, retorna colección vacía
        return resp;
    }
}
