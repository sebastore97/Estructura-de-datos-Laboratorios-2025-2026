import java.util.*;

public class ExpertsDB {
	// Ejercicio 1: Atributos privados
	// mapExperts: clave = nombre del experto, valor = conjunto de topics en los que es experto
	private Map<String, HashSet<String>> mapExperts ;
	// mapTopics: clave = topic, valor = conjunto de expertos que conocen el topic
	private Map<String, HashSet<String>> mapTopics ;

	public ExpertsDB() {
		// Ejercicio 1: Constructor
		// Inicializa los mapas vacíos.
		this.mapExperts = new HashMap<>();
		this.mapTopics = new HashMap<>();
	}

	public int expertsSize () {
		// Ejercicio 2
		// Devuelve el número de expertos registrados (nº de claves en mapExperts)
		return this.mapExperts.size();
	}

	public int topicsSize() {
		// Ejercicio 2
		// Devuelve el número de tópicos registrados (nº de claves en mapTopics)
		return this.mapTopics.size();
	}

	public Set<String> getExperts() {
		// Ejercicio 3
		// Devuelve una copia de las claves (nombres de expertos).
		return new HashSet<>(this.mapExperts.keySet());
	}

	public Set<String> getTopics() {
		// Ejercicio 3
		// Devuelve una copia de los tópicos registrados.
		return new HashSet<>(this.mapTopics.keySet());
	}

	public Set<String> getExperts (String topic) {
		// Ejercicio 4
		// Devuelve el conjunto de expertos asociados a "topic".
		return this.mapTopics.get(topic);
	}

	public Set<String> getTopics (String expert) {
		// Ejercicio 4
		// Devuelve el conjunto de tópicos asociados a "expert".
		return this.mapExperts.get(expert);
	}

	public void addExpert(String expert, Collection<String> topics) {
		// Ejercicio 5
		// Añade un experto con una colección de tópicos.
		// Si el experto no existe, se crea una entrada en mapExperts con una copia de los tópicos.
		// Se asegura que el conjunto del experto incluye todos los tópicos pasados.
		// Para cada tópico, se añade el experto en mapTopics creando la entrada si hace falta.

		if (!this.mapExperts.containsKey(expert))
			this.mapExperts.put(expert, new HashSet<>(topics));

		this.mapExperts.get(expert).addAll(topics);

		for (String topic : topics)
			if (!this.mapTopics.containsKey(topic)) {
				// Si el tópico no existe, se crea set vacío y se añade el experto
				this.mapTopics.put(topic, new HashSet<>());
				this.mapTopics.get(topic).add(expert);
			} else
				// Si ya existe, se añade el experto al conjunto
				this.mapTopics.get(topic).add(expert);
	}
	
	public void addTopic(String topic, Collection<String> experts) {
		// Ejercicio 6
		// Añade un tópico con una colección de expertos.
		// Si el tópico no existe, se crea la entrada en mapTopics con copia de expertos.
		// Se asegura que mapTopics contiene a todos los expertos pasados.
		// Para cada experto, se crea/actualiza su entrada en mapExperts añadiendo el tópico.

		if (!this.mapTopics.containsKey(topic))
			this.mapTopics.put(topic, new HashSet<>(experts));

		this.mapTopics.get(topic).addAll(experts);

		for (String expert : experts)
			if (!this.mapExperts.containsKey(expert)) {
				this.mapExperts.put(expert, new HashSet<>());
				this.mapExperts.get(expert).add(topic);
			} else
				this.mapExperts.get(expert).add(topic);
	}


	public Collection<String> removeExpert(String name) {
		// Ejercicio 7
		// Elimina un experto y limpia todas las referencias en mapTopics.
		// Si no existe el experto, devuelve null.
		// Se copia los tópicos del experto.
		// Se elimina la entrada del experto en mapExperts.
		// Para cada tópico del conjunto original, se elimina al experto de mapTopics.
		// Si tras la eliminación el conjunto de expertos queda vacío, se elimina el tópico del mapa.
		// Devuelve la copia de topics que tenía el experto.

		if (!this.mapExperts.containsKey(name))
			return null;

		Set<String> oldTopics = new HashSet<>(this.mapExperts.get(name));
		this.mapExperts.remove(name);

		for (String topic : oldTopics) {
			// Se elimina la referencia al experto en cada tópico
			this.mapTopics.get(topic).remove(name);
			// Si ese tópico ya no tiene expertos, se borra completamente
			if (this.mapTopics.get(topic).isEmpty())
				this.mapTopics.remove(topic);
		}

		return oldTopics;
	}
	
	public Collection<String> removeTopic(String topic) {
		// Ejercicio 8
		// Elimina un tópico y limpia todas las referencias en mapExperts.
		// Si no existe el tópico, devuelve null.
		// Extrae y elimina el conjunto de expertos asociado a ese tópico.
		// Para cada experto extraído, se elimina la referencia al tópico en mapExperts.
		// Si el experto se queda sin tópicos, se elimina al experto del mapa.
		// Devuelve el conjunto de expertos que estaban asociados al tópico eliminado.

		if (!this.mapTopics.containsKey(topic))
			return null;

		Set<String> oldExperts = this.mapTopics.remove(topic);

		for (String expert : oldExperts) {
			this.mapExperts.get(expert).remove(topic);
			if (this.mapExperts.get(expert).isEmpty())
				this.mapExperts.remove(expert);
		}

		return oldExperts;
	}

	public Collection<String> getExperts(Collection<String> topics) {
		// Ejercicio 9
		// Devuelve el conjunto de expertos que son expertos en TODOS los topics de la colección "topics" (intersección de conjuntos).
		// Se obtiene el conjunto de expertos del primer tópico. Si es null (topic no existe) -> devuelve conjunto vacío.
		// Se itera sobre los siguientes tópicos y se hace result.retainAll(expertsTopic) para hacer la intersección
		// Si en cualquier punto un tópico no existe, se devuelve conjunto vacío.
		// Si la intersección queda vacía, se sale del bucle.

		Iterator<String> itTopics = topics.iterator();
		Set<String> experts = this.getExperts(itTopics.next());

		if (experts == null)
			// Si el primer tópico no existe, no hay expertos que cumplan -> devuelve conjunto vacío.
			return new HashSet<>();

		Set<String> result = new HashSet<>(experts);

		while (itTopics.hasNext()) {
			Set<String> expertsTopic = this.getExperts(itTopics.next());
			if (expertsTopic == null)
				// Si alguno de los tópicos no existe, la intersección es vacía
				return new HashSet<>();
			result.retainAll(expertsTopic); // preserva sólo los expertos comunes
			if (result.isEmpty())
				break; // ya no hay expertos comunes, se sale del bucle
		}

		return result;
	}
}
