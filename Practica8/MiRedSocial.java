import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class MiRedSocial {

    private Map<String, Set<String>> graph = new HashMap<String,Set<String>>();

    public boolean addDirectedEdge (String source, String target) {
        if (source == null || target == null)
            throw new NullPointerException();

        if (source.equals(target))
            return false;

        if (!this.graph.containsKey(source))
            this.graph.put(source, new HashSet<String>());

        if (!this.graph.containsKey(target))
            this.graph.put(target, new HashSet<String>());

        return this.graph.get(source).add(target);
    }


    public boolean containsNode (String name) {
        if (name == null)
            throw new NullPointerException();

        return this.graph.containsKey(name);
    }

    public int size() {
        return this.graph.size();
    }

    public Set<String> getMembers() {
        return new HashSet<>(this.graph.keySet());
    }

    public Set<String> getFollowers (String name) {
        if (name == null)
            throw new NullPointerException();

        Set<String> set = new HashSet<>();

        for (String s : this.graph.keySet())
            if (this.graph.get(s).contains(name))
                set.add(s);

        return set;
    }

    public Set<String> getFollowed (String name) {
        if (name == null)
            throw new NullPointerException();

        if (!this.graph.containsKey(name))
            return new HashSet<String>();

        return new HashSet<>(this.graph.get(name));
    }

    public Set<String> commonFollowers (String name1, String name2) {
        if (name1 == null || name2 == null)
            throw new NullPointerException();

        Set<String> commonFollowers = this.getFollowers(name1);
        commonFollowers.retainAll(this.getFollowers(name2));

        return commonFollowers;
    }

    public Set<String> suggestions (String name) {
        if (name == null)
            throw new NullPointerException();

        if (!this.graph.containsKey(name))
            return null;

        Set<String> suggestions = new HashSet<>();
        Set<String> nameFollowed = this.getFollowed(name);

        for (String nFollowed : nameFollowed)
            for (String s : this.graph.get(nFollowed))
                if (!nameFollowed.contains(s) && !s.equals(name))
                    suggestions.add(s);

        return suggestions;
    }

    public String mostInfluencer () {
        if (!this.getMembers().isEmpty()) {
            int max = -1;
            String mostInfluencer = "";
            for (String s : this.getMembers()) {
                int sValue = this.getFollowers(s).size();
                if (sValue > max) {
                    max = sValue;
                    mostInfluencer = s;
                }
            }
            return mostInfluencer;
        }

        return "";
    }

    public Map<String, Integer> distanceToAll(String name) {
        if (name == null)
            throw new NullPointerException();

        Queue<String> queuePendingVisit = new LinkedList<>();
        Map<String, Integer> mapDistances = new HashMap<>();
        mapDistances.put(name, 0);
        queuePendingVisit.add(name);

        while (!queuePendingVisit.isEmpty()) {
            String current = queuePendingVisit.remove();
            for (String s : this.getFollowed(current))
                if (!mapDistances.containsKey(s)) {
                    queuePendingVisit.add(s);
                    mapDistances.put(s, mapDistances.get(current) + 1);
                }
        }

        return mapDistances;
    }



    public void  readGraph(String nomfich) {

        try {
            Scanner inf = new Scanner (new FileInputStream(nomfich));

            while (inf.hasNext()) {
                String origen = inf.next();
                //System.out.println("origen "+origen);
                String destino = null;
                if (inf.hasNext()) {
                    destino = inf.next();
                    //System.out.println("destino "+destino);
                }

                //System.out.println("añadiendo el arco "+origen+"-->"+destino);
                if (destino != null) {
                    addDirectedEdge(origen,destino);
                }
                else {
                    System.out.println("Error en el fichero de entrada. No hay dato");
                    System.exit(1);
                }
            }

            inf.close();
        } catch(FileNotFoundException e){
            System.out.println("Error al abrir el fichero " + nomfich);
            //return null;
        }

    }


    public void printGraphStructure () {
        System.out.println("Graph structure followed");
        for (String s: graph.keySet()) {
            System.out.print(s+" follows: ");
            for (String t: graph.get(s)) {
                System.out.print(t+", ");
            }
            System.out.println();
        }

        System.out.println();

    }



}
