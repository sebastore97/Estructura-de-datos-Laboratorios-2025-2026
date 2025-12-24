import java.util.*;


public class EDListGraph<T,W> implements EDGraph<T,W> {
	@SuppressWarnings("hiding")
	private class Node<U> {
		U data;
		List<EDEdge<W> > lEdges;
		
		Node (U data) {
			this.data = data;
			this.lEdges = new LinkedList< EDEdge<W> >();
		}



        @Override
		public boolean equals (Object other) {
			if (this == other)
			    return true;
			if (getClass()!= other.getClass() )
			    return false;
			Node<T> anotherNode = (Node<T>) other;
			return data.equals(anotherNode.data);
		}
	}
	
	// Private data
	private ArrayList<Node<T>> nodes;
	private int size; //real number of nodes
	private boolean directed;
	private boolean weighted;
	


	public EDListGraph() {
		directed = false; //not directed
		weighted = false;
		nodes =  new ArrayList<>();
		size =0;
	}


	public EDListGraph (boolean dir) {
		directed = dir;
		weighted = false;
		nodes =  new ArrayList<Node<T>>();
		size =0;
	}
	
	public EDListGraph (boolean dir, boolean wei) {
		directed = dir;
		weighted = wei;
		nodes =  new ArrayList<Node<T>>();
		size =0;
	}
	
	public EDListGraph (EDMatrixGraph<T,W> g) {
		this.weighted = g.isWeighted();
		this.directed = g.isDirected();
		this.size=g.getSize();
		
		this.nodes = new ArrayList<Node<T>>();
		
		HashMap<T,Integer> nodesSet = g.getNodes();
		boolean matrix [][] = null;
        matrix = g.getAdjacencyMatrix();

        for (int i=0; i<this.size; i++)
            this.nodes.add(null);

		
		for (T v : nodesSet.keySet()) {
			int matPos = nodesSet.get(v);
			Node<T> node = new Node<T>(v);
			nodes.set(matPos, node);
		}
		
		for (T v : nodesSet.keySet()) {
			int matPos = nodesSet.get(v);
			for (int i=0; i<g.getNodesCapacity(); i++) {
				EDEdge<W> ed = new EDEdge<W>(matPos,i);
				if (matrix[matPos][i]) this.insertEdge(ed);
			}			
		}
		
	}

    @Override
    public int getSize() {
	    return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

    @Override
    public boolean isWeighted() {
		
		return this.weighted;
	}

	public boolean isDirected() {
		return this.directed;
	}
	

	@Override
	public int getNodesCapacity() {
		return nodes.size();
	}
	
	@Override


	public int insertNode(T item) {

	    int i = 0; int pos=-1; int free=nodes.size();
	    while (i<nodes.size() && pos==-1) {

	    	if (nodes.get(i).data == null) free = i;
	    	else if (nodes.get(i).data.equals(item)) pos = i;
	    	i++;
	    }

	    if (pos == -1) { //No esta
	    	Node<T> newNode = new Node<T>(item);
	    	if (free<nodes.size()) nodes.set(free,newNode);
	    	else {free=size; nodes.add(newNode);}
	    	size++;
	    	//System.out.println("Insertado en posicion "+i);
	    	return free;
	    }
	    else return pos;
	}
	

    @Override
    public int getNodeIndex(T item) {
		Node<T> aux = new Node<T>(item);
		return nodes.indexOf(aux);
	}

	@Override
	public T getNodeValue(int index) throws IndexOutOfBoundsException{
		return nodes.get(index).data;
		
	}


    @Override
    public boolean insertEdge(EDEdge<W> edge) {
		//Ejercicio1
		if (edge != null)
            if (edge.getSource() >= 0 && edge.getSource() < this.nodes.size() && edge.getTarget() >= 0 && edge.getTarget() < this.nodes.size()) {
				if (this.getNodeValue(edge.getSource()) != null && this.getNodeValue(edge.getTarget()) != null ) {
					Node<T> sourceNode = this.nodes.get(edge.getSource());
					if (sourceNode.lEdges.contains(edge))
						return false;
					sourceNode.lEdges.add(edge);
					if (!this.isDirected()) {
						EDEdge<W> e = new EDEdge<>(edge.getTarget(), edge.getSource(), edge.getWeight());
						Node<T> targetNode = this.nodes.get(e.getSource());
						targetNode.lEdges.add(e);
					}
					return true;
				}
			}

		return false;
	}

    public Set<T> degree1() {
        //Ejercicio 2
		Set<T> setNodeDegree1 = new HashSet<>();

		if (!this.isDirected()) {
			for (Node<T> n : this.nodes)
				if (n != null && n.lEdges.size() == 1)
					setNodeDegree1.add(n.data);
		} else {
			int[] cont = new int[this.nodes.size()];
			for (Node<T> n : this.nodes) {
				if (n != null)
					for (EDEdge<W> e : n.lEdges)
						cont[e.getTarget()]++;
			}
			for (int i = 0; i < cont.length; i++)
				if (cont[i] == 1)
					setNodeDegree1.add(this.getNodeValue(i));
		}

		return setNodeDegree1;
    }

    public List<T> findPath(int entry, int exit) {
        //Ejercicio 4
		if (entry < 0 || entry >= this.getNodesCapacity() || exit < 0 || exit >= this.getNodesCapacity())
			throw new IndexOutOfBoundsException();

		if (this.getNodeValue(entry) != null) {
			int[] parent = new int[this.getNodesCapacity()];
			for (int i = 0; i < parent.length; i++)
				parent[i] = -1;
			parent[entry] = entry;
			return findPath(entry, exit, parent);
		}

        return null;
    }

	private List<T> findPath(int current, int target, int[] parent) {
		if (current == target) {
			List<T> path = new ArrayList<>();
			int actual = target;
			while (actual != parent[actual]) {
				path.add(0, this.getNodeValue(actual));
				actual = parent[actual];
			}
			path.add(0, this.getNodeValue(actual));
			return path;
		}

		for (EDEdge<W> edge : this.nodes.get(current).lEdges) {
			if (parent[edge.getTarget()] == -1) {
				parent[edge.getTarget()] = current;
				List<T> path = findPath(edge.getTarget(), target, parent);
				if (path != null)
					return path;
			}
		}

		return null;
	}

	/* Otra forma de realizar el ejercicio 4 con recursividad */
	/*
	public List<T> findPath(int entry, int exit) {
		// Validaciones (igual que antes)
		if (entry < 0 || entry >= this.getNodesCapacity() || exit < 0 || exit >= this.getNodesCapacity())
			throw new IndexOutOfBoundsException();

		if (this.getNodeValue(entry) != null) {
			// CAMBIO: Usamos boolean[] en lugar de int[] parent
			boolean[] visited = new boolean[this.getNodesCapacity()];

			// Llamamos a la recursividad.
			// Fíjate que NO pasamos ninguna lista, la lista "nacerá" abajo del todo.
			return findPath(entry, exit, visited);
		}
		return null;
	}

	private List<T> findPath(int current, int target, boolean[] visited) {
		// 1. Marcar como visitado para no volver a entrar aquí (evitar círculos)
		visited[current] = true;

		// 2. CASO BASE: ¡He encontrado la salida!
		if (current == target) {
			List<T> path = new ArrayList<>();
			path.add(this.getNodeValue(current)); // Añado el nodo final (ej: "C")
			return path; // Devuelvo la lista con solo ["C"]
		}

		// 3. RECURSIVIDAD: Preguntar a los vecinos
		for (EDEdge<W> edge : this.nodes.get(current).lEdges) {
			int neighbor = edge.getTarget();

			// Si el vecino NO ha sido visitado...
			if (!visited[neighbor]) {
				// Llamada recursiva: "Vecino, ¿tú sabes llegar?"
				// Guardamos lo que nos responde en 'resultFromNeighbor'
				List<T> resultFromNeighbor = findPath(neighbor, target, visited);

				// Si es distinto de null, es que el vecino encontró el camino
				if (resultFromNeighbor != null) {
					// AQUÍ ESTÁ LA MAGIA:
					// El vecino me da el camino desde él hasta el final (ej: ["B", "C"])
					// Yo me añado AL PRINCIPIO de esa lista.
					resultFromNeighbor.add(0, this.getNodeValue(current));

					// Ahora la lista es ["A", "B", "C"]
					return resultFromNeighbor; // La paso hacia arriba
				}
			}
		}

		// 4. Si termino el bucle y nadie me dio solución, es un camino sin salida
		return null;
	}
	*/
	
	public boolean insertEdge (T fromNode, T toNode) {
        throw new UnsupportedOperationException();
	}

	@Override
	public EDEdge<W> getEdge(int source, int target) {	
		if (source <0 || source >= nodes.size()) return null;
		
		Node<T> node = nodes.get(source);
		if (node.data == null ) return null;
		for (EDEdge<W> edge: node.lEdges)
			
			if (edge.getTarget() == target) return edge;
		
		return null;
	}

    @Override
    public Map<T, Integer> getNodes() {
        Map<T, Integer> ret = new HashMap<>();

        for (int i = 0;i < nodes.size(); i++)
            ret.put(nodes.get(i).data, i);

        return ret;
    }

    @Override
    public Set<Integer> getOutgoing(int index) {

	    if (index<0 || index> nodes.size()) return null;

	    if (nodes.get(index) == null) return null;

        Set<Integer> ret = new HashSet<>();

        for (EDEdge<W> edge: nodes.get(index).lEdges)
            ret.add(edge.getTarget());

        return ret;
    }

	@Override
	public EDEdge<W> removeEdge(int source, int target) {
        throw new UnsupportedOperationException();
	}

	@Override
	public T removeNode(int index) {
        throw new UnsupportedOperationException();
	}


	public void printGraphStructure() {
		//System.out.println("Vector size= " + nodes.length);
		System.out.println("Vector size " + nodes.size());
		System.out.println("Nodes: "+ this.getSize());
		for (int i=0; i<nodes.size(); i++) {
			System.out.print("pos "+i+": ");
	        Node<T> node = nodes.get(i);
			System.out.print(node.data+" -- ");
			Iterator<EDEdge<W>> it = node.lEdges.listIterator();
			while (it.hasNext()) {
					EDEdge<W> e = it.next();
					System.out.print("("+e.getSource()+","+e.getTarget()+", "+e.getWeight()+")->" );
			}
			System.out.println();
		}
	}

	
}
