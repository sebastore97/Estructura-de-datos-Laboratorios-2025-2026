import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestExpertsDB {

	final static private String [] myExperts = {"messi", "ramos", "ronaldo"};
	final static private String [][] myExpertsTopics = {{"derecho financiero", "contabilidad", "matematicas"}, {"matematicas", "fisica", "confeccion"}, {"matematicas", "fisica", "filologia inglesa"}};
    final static private String [] myTopics ={"confeccion","contabilidad","derecho financiero","filologia inglesa","fisica", "matematicas"};
	final static private String [][] myTopicsExperts ={{"ramos"},{"messi"},{"messi"},{"ronaldo"},{"ramos", "ronaldo"}, {"ramos","ronaldo","messi"}};

	private List<String> expertos = new ArrayList<>();
	private List<List<String>> expertosTemas = new ArrayList<>();
	private List<String> temas = new ArrayList<>();
	private List<List<String>> temasExpertos = new ArrayList<>();
	private Set<String> conjExpertos = new TreeSet<>();
	private Set<String> conjTemas = new HashSet<>();

	private void loaddb () {
		for (int i=0; i<myExperts.length; i++) {
			conjExpertos.add(myExperts[i]);
			expertos.add(myExperts[i]);
			List<String> temas = new ArrayList<>();
			temas.addAll(Arrays.asList(myExpertsTopics[i]));
			expertosTemas.add(temas);
		}
		for (int i=0; i<myTopics.length; i++) {
			conjTemas.add(myTopics[i]);
			temas.add(myTopics[i]);
			List<String> expertos = new ArrayList<>();
			expertos.addAll(Arrays.asList(myTopicsExperts[i]));
			temasExpertos.add(expertos);
		}
	}

	private void load (ExpertsDB db) {
		for (int i=0; i<expertos.size(); i++) {
			List<String> temas = expertosTemas.get(i);

			db.addExpert(expertos.get(i), temas);


		}
	}


	private String toString( ExpertsDB db) {
		StringBuilder buff = new StringBuilder();
		buff.append("EXPERTS\n");
		for (String expert: db.getExperts()) {
			buff.append (expert + ": ");
			for (String t: db.getTopics(expert))
				buff.append(t + ", ");
			buff.append("\n");
		}

		buff.append("TOPICS\n");
		for (String topic: db.getTopics()) {
			buff.append (topic + ": ");
			for (String ex: db.getExperts(topic))
				buff.append(ex + ", ");
			buff.append("\n");
		}

		return buff.toString();
	}

	private void printDB (ExpertsDB db) {
		System.out.println("Obtenido: ");
		System.out.println(toString(db));
		System.out.println("Esperado: ");
		System.out.println("EXPERTS");
		for (int i=0; i<expertos.size(); i++) {
			String e = expertos.get(i);
			System.out.print(e+": ");
			for (String t:expertosTemas.get(i))
				System.out.print(t+", ");
			System.out.println();
		}
		System.out.println("TOPICS");
		for (int i=0; i< temas.size(); i++) {
			String t = temas.get(i);
			System.out.print(t+": ");
			for (String e:temasExpertos.get(i))
				System.out.print(e+", ");
			System.out.println();
		}
	}

	@Test
	public void TestAConstructor() {
		System.out.println("Prueba constructor ");
		ExpertsDB db= new ExpertsDB();

		assertEquals(0, db.expertsSize());
		assertEquals(0,db.topicsSize());
		System.out.println("Obtenido: ");
		System.out.println(toString(db));
		System.out.println("Esperado: ");
		System.out.println("EXPERTS");
		System.out.println("TOPICS");


	}

	@Test
	public void TestBAddExpert()  {
		loaddb();

		ExpertsDB db= new ExpertsDB();

		assertEquals(0, db.expertsSize());
		assertEquals(0,db.topicsSize());


		load(db);
		printDB(db);
		assertEquals(conjExpertos.size(),db.expertsSize());
		assertEquals(conjTemas.size(),db.topicsSize());
		assertTrue(conjExpertos.containsAll(db.getExperts()) && db.getExperts().containsAll(conjExpertos));
		assertTrue(conjTemas.containsAll(db.getTopics()) && db.getTopics().containsAll(conjTemas));
		for (int i=0; i<expertos.size(); i++) {
			List<String> esperado = expertosTemas.get(i);
			Set<String>	obtenido = db.getTopics(expertos.get(i));
			System.out.println("Para experto: "+expertos.get(i));
			System.out.println("        Esperado: "+esperado);
			System.out.println("        Obtenido: "+obtenido);
			assertTrue(obtenido.containsAll(esperado) && esperado.containsAll(obtenido));
		}
		for (int i=0; i<temas.size(); i++) {
			List<String> esperado = temasExpertos.get(i);
			Set<String>	obtenido = db.getExperts(temas.get(i));
			System.out.println("Para tema: "+temas.get(i));
			System.out.println("        Esperado: "+esperado);
			System.out.println("        Obtenido: "+obtenido);
			assertTrue(obtenido.containsAll(esperado) && esperado.containsAll(obtenido));
		}


		//Checking adding existing experts and topics
		String expert="ronaldo";
		List<String> topicslist = new ArrayList<>();
		topicslist.add("matematicas");
		System.out.println("\n Adding "+expert+" expert in "+topicslist);
		int oldTopicSize = db.topicsSize();
		int oldExpertSize = db.expertsSize();
		db.addExpert(expert, topicslist);
		assertEquals(oldTopicSize, db.topicsSize());
		assertEquals(oldExpertSize, db.expertsSize());

		printDB(db);

		expert="messi";
		topicslist.add("derecho financiero");
		System.out.println("\n Adding "+expert+" expert in "+topicslist);
		oldTopicSize = db.topicsSize();
		oldExpertSize = db.expertsSize();
		db.addExpert(expert, topicslist);
		assertEquals(oldTopicSize, db.topicsSize());
		assertEquals(oldExpertSize, db.expertsSize());

		printDB(db);

		String topic = "fisica";
		List<String> expertList = new ArrayList<>();
		expertList.add("ramos");
		System.out.println("\n Adding "+topic+" experts "+expertList);
		oldTopicSize = db.topicsSize();
		oldExpertSize = db.expertsSize();
		db.addTopic(topic, expertList);
		assertEquals(oldTopicSize, db.topicsSize());
		assertEquals(oldExpertSize, db.expertsSize());

		printDB(db);

		//adding an existing expert without topics
		expert="messi";
		topicslist.clear();
		System.out.println("\n Adding "+expert+" expert in "+topicslist);
		oldTopicSize = db.topicsSize();
		oldExpertSize = db.expertsSize();
		db.addExpert(expert, topicslist);
		assertEquals(oldTopicSize, db.topicsSize());
		assertEquals(oldExpertSize, db.expertsSize());

		printDB(db);

		//checking previously existing topics
		int i=0;
		while (i<expertos.size() && !expertos.get(i).equals(expert)) {
			//System.out.println("Experto i " +expertos.get(i));
			i++;
		}
		if (i>=expertos.size())
			System.out.println("Error en la comprobación");
		List<String> expected = expertosTemas.get(i);

		Set<String> obtenido = db.getTopics(expert);

		if (!(expected.containsAll(obtenido) && obtenido.containsAll(expected)))
			System.out.println("Error");
		System.out.println("Esperado: "+expected);
		System.out.println("Obtenido: "+obtenido);

		printDB(db);


		//adding a new expert without topics
		expert="nadal";
		topicslist.clear();
		System.out.println("\n Adding "+expert+" expert in "+topicslist);
		oldTopicSize = db.topicsSize();
		oldExpertSize = db.expertsSize();
		db.addExpert(expert, topicslist);
		expertos.add(expert);
		expertosTemas.add(new ArrayList<>());
		conjExpertos.add(expert);
		assertEquals(oldTopicSize, db.topicsSize());
		assertEquals(oldExpertSize, db.expertsSize()-1);

		assertTrue(db.getExperts().contains(expert));
		Set<String> lista = db.getTopics(expert);
		assertTrue(lista.isEmpty());

		printDB(db);

		//adding an existing topic for that expert
		topicslist.clear();
		String t ="fisica";
		topicslist.add(t);
		System.out.println("\n Adding "+expert+" expert in "+topicslist);
		expertosTemas.get(expertos.size()-1).add(t);
		if (conjTemas.contains(t)) {
			i = 0;
			while (i < temas.size() && !temas.get(i).equals(t)) i++;
			System.out.println(temasExpertos.get(i));
			List<String> ex= temasExpertos.get(i);
			ex.add(expert);
		}
		else {
			conjTemas.add(t);
			temasExpertos.add(new ArrayList<>());
			temas.add(t);
			temasExpertos.get(temas.size()-1).add(t);
		}


		oldTopicSize = db.topicsSize();
		oldExpertSize = db.expertsSize();
		db.addExpert(expert, topicslist);
		assertEquals(oldTopicSize, db.topicsSize());
		assertEquals(oldExpertSize, db.expertsSize());

		assertTrue(db.getExperts().contains(expert));
		lista = db.getTopics(expert);
		assertTrue(lista.containsAll(topicslist) && topicslist.containsAll(lista));

		printDB(db);

		//adding a new topic for an existing expert
		topicslist.clear();
		t="geografia";
		topicslist.add(t);
		expertosTemas.get(expertos.size()-1).add(t);
		conjTemas.add(t);
		temas.add(t);
		temasExpertos.add(new ArrayList<>());
		temasExpertos.get(temas.size()-1).add(expert);

		System.out.println("\n Adding "+expert+" expert in "+topicslist);
		oldTopicSize = db.topicsSize();
		oldExpertSize = db.expertsSize();
		db.addExpert(expert, topicslist);
		assertEquals(oldTopicSize, db.topicsSize()-1);
		assertEquals(db.topicsSize(),conjTemas.size());
		assertEquals(oldExpertSize, db.expertsSize());

		assertTrue(db.getTopics().contains(t));
		lista = db.getTopics(expert);
		topicslist.clear();
		topicslist.addAll(expertosTemas.get(expertos.size()-1));
		assertTrue(lista.containsAll(topicslist) && topicslist.containsAll(lista));

		lista = db.getExperts(t);
		expertList.clear();
		expertList.addAll(temasExpertos.get(temas.size()-1));
		assertTrue(lista.containsAll(expertList) && expertList.containsAll(lista));

		printDB(db);

		topicslist.clear();
		expert = "ramos";
		t="historia";
		topicslist.add(t);
		System.out.println("\n Adding "+expert+" expert in "+topicslist);
		if (!conjExpertos.contains(expert)) {
			conjExpertos.add(expert);
			expertos.add(expert);
			expertosTemas.add(new ArrayList<>());
			expertosTemas.get(expertos.size()-1).addAll(topicslist);
		}
		else {
			i=expertos.indexOf(expert);
			expertosTemas.get(i).addAll(topicslist);
		}
		for (String tt: topicslist) {
			if (!conjTemas.contains(tt)) {
				conjTemas.add(tt);
				temas.add(tt);
				temasExpertos.add(new ArrayList<>());
				temasExpertos.get(temas.size()-1).add(expert);
			}
			else {
				i = temas.indexOf(tt);
				temasExpertos.get(i).add(expert);
			}
		}

		db.addExpert(expert, topicslist);
		assertEquals(conjTemas.size(), db.topicsSize());
		assertEquals(conjExpertos.size(),db.expertsSize());

		int ii=expertos.indexOf(expert);
		lista = db.getTopics(expert);
		List<String> lista2 = new ArrayList<>(expertosTemas.get(ii));
		assertTrue(lista.containsAll(lista2) && lista2.containsAll(lista));
		System.out.println("Esperado  Temas para el experto "+expert+": "+lista2);
		System.out.println("Obtenido  Temas para el experto "+expert+": "+lista);
		for (String tt: topicslist) {
			assertTrue(db.getTopics().contains(tt));
			lista = db.getExperts(tt);
			ii = temas.indexOf(tt);
			lista2 = new ArrayList<>(temasExpertos.get(ii));
			assertTrue(lista.containsAll(lista2) && lista2.containsAll(lista));
			System.out.println("Esperado  Expertos para " + tt + ": " + lista2);
			System.out.println("Obtenido  Expertos apra " + tt + ": " + lista);
		}

		printDB(db);

		topicslist.clear();
		expert = "beckham";
		t="historia";
		topicslist.add(t);
		t = "finanzas";
		topicslist.add(t);
		t = "geografia";
		topicslist.add(t);
		System.out.println("\n Adding "+expert+" expert in "+topicslist);
		if (!conjExpertos.contains(expert)) {
			conjExpertos.add(expert);
			expertos.add(expert);
			expertosTemas.add(new ArrayList<>());
			expertosTemas.get(expertos.size()-1).addAll(topicslist);
		}
		else {
			i=expertos.indexOf(expert);
			expertosTemas.get(i).addAll(topicslist);
		}
		for (String tt: topicslist) {
			if (!conjTemas.contains(tt)) {
				conjTemas.add(tt);
				temas.add(tt);
				temasExpertos.add(new ArrayList<>());
				temasExpertos.get(temas.size()-1).add(expert);
			}
			else {
				i = temas.indexOf(tt);
				temasExpertos.get(i).add(expert);
			}
		}

		db.addExpert(expert, topicslist);
		assertEquals(conjTemas.size(), db.topicsSize());
		assertEquals(conjExpertos.size(),db.expertsSize());

		ii=expertos.indexOf(expert);
		lista = db.getTopics(expert);
		lista2 = new ArrayList<>(expertosTemas.get(ii));
		System.out.println("Esperado  Temas para el experto "+expert+": "+lista2);
		System.out.println("Obtenido  Temas para el experto "+expert+": "+lista);
		assertTrue(lista.containsAll(lista2) && lista2.containsAll(lista));


		for (String tt: topicslist) {
			assertTrue(db.getTopics().contains(tt));
			lista = db.getExperts(tt);
			ii = temas.indexOf(tt);
			lista2 = new ArrayList<>(temasExpertos.get(ii));
			System.out.println("Esperado  Expertos para " + tt + ": " + lista2);
			System.out.println("Obtenido  Expertos para " + tt + ": " + lista);
			assertTrue(lista.containsAll(lista2) && lista2.containsAll(lista));

		}

		printDB(db);


	}

	@Test
	public void TestCAddTopic()  {
		ExpertsDB db = new ExpertsDB();
		loaddb();
		load(db);
		//printDB(db);
		System.out.println("Estado inicial: "+toString(db));

		//Topic existe, lista de expertos vacía
		String topicAdd = "matematicas";
		List<String> expertosAdd = new ArrayList<>();
		System.out.println("\n Adding "+topicAdd+" expertos: "+expertosAdd);
		Set<String> oldExperts = db.getExperts(topicAdd);
		db.addTopic(topicAdd, expertosAdd);
		assertEquals(conjExpertos.size(), db.expertsSize());
		assertEquals(conjTemas.size(), db.topicsSize());
		Set<String> newExperts = db.getExperts(topicAdd);
		System.out.println("Esperado "+topicAdd+" expertos: "+oldExperts);
		System.out.println("Obtenido "+topicAdd+" expertos: "+newExperts);
		assertTrue(oldExperts.containsAll(newExperts) && newExperts.containsAll(oldExperts));

		//Topic existe, lista de expertos existe
		expertosAdd.add("ramos");
		expertosAdd.add("ronaldo");
		System.out.println("\n Adding "+topicAdd+" expertos: "+expertosAdd);
		oldExperts = db.getExperts(topicAdd);
		db.addTopic(topicAdd, expertosAdd);
		assertEquals(conjExpertos.size(), db.expertsSize());
		assertEquals(conjTemas.size(), db.topicsSize());
		newExperts = db.getExperts(topicAdd);
		System.out.println("Esperado "+topicAdd+" expertos: "+oldExperts);
		System.out.println("Obtenido "+topicAdd+" expertos: "+newExperts);
		assertTrue(oldExperts.containsAll(newExperts) && newExperts.containsAll(oldExperts));

		//Topic existe, algún experto nuevo
		topicAdd = "derecho financiero";
		expertosAdd.clear();
		expertosAdd.add("messi");
		expertosAdd.add("ronaldo");

		if (!conjTemas.contains(topicAdd)) {
			conjTemas.add(topicAdd);
			temas.add(topicAdd);
			temasExpertos.add(new ArrayList<>());
			temasExpertos.get(temasExpertos.size()-1).addAll(expertosAdd);
		}
		else {
			int index = temas.indexOf(topicAdd);
			for (String ex: expertosAdd) {
				if (!temasExpertos.get(index).contains(ex)) {
					temasExpertos.get(index).add(ex);
				}
			}
		}

		for (String ex: expertosAdd) {
			if (!conjExpertos.contains(ex)) {
				conjExpertos.add(ex);
				expertos.add(ex);
				expertosTemas.add(new ArrayList<>());
				expertosTemas.get(expertosTemas.size()-1).add(topicAdd);
			}
			else {
				int index = expertos.indexOf(ex);
				if (!expertosTemas.get(index).contains(topicAdd)) {
					expertosTemas.get(index).add(topicAdd);
				}
			}
		}

		System.out.println("\n Adding "+topicAdd+" expertos: "+expertosAdd);
		oldExperts = db.getExperts(topicAdd);
		db.addTopic(topicAdd, expertosAdd);
		assertEquals(conjExpertos.size(), db.expertsSize());
		assertEquals(conjTemas.size(), db.topicsSize());
		newExperts = db.getExperts(topicAdd);

		int indexCheck = temas.indexOf(topicAdd);
		List<String> esperado = temasExpertos.get(indexCheck);
		System.out.println("Esperado "+topicAdd+" expertos: "+esperado);
		System.out.println("Obtenido "+topicAdd+" expertos: "+newExperts);
		assertTrue(newExperts.containsAll(oldExperts));
		assertTrue(newExperts.containsAll(expertosAdd));
		assertTrue(newExperts.containsAll(esperado) && esperado.containsAll(expertosAdd));

		//Topic existe, algún experto nuevo
		topicAdd = "derecho financiero";
		expertosAdd.clear();
		expertosAdd.add("messi");
		expertosAdd.add("beckham");

		if (!conjTemas.contains(topicAdd)) {
			conjTemas.add(topicAdd);
			temas.add(topicAdd);
			temasExpertos.add(new ArrayList<>());
			temasExpertos.get(temasExpertos.size()-1).addAll(expertosAdd);
		}
		else {
			int index = temas.indexOf(topicAdd);
			for (String ex: expertosAdd) {
				if (!temasExpertos.get(index).contains(ex)) {
					temasExpertos.get(index).add(ex);
				}
			}
		}

		for (String ex: expertosAdd) {
			if (!conjExpertos.contains(ex)) {
				conjExpertos.add(ex);
				expertos.add(ex);
				expertosTemas.add(new ArrayList<>());
				expertosTemas.get(expertosTemas.size()-1).add(topicAdd);
			}
			else {
				int index = expertos.indexOf(ex);
				if (!expertosTemas.get(index).contains(topicAdd)) {
					expertosTemas.get(index).add(topicAdd);
				}
			}
		}

		System.out.println("\n Adding "+topicAdd+" expertos: "+expertosAdd);
		oldExperts = db.getExperts(topicAdd);
		db.addTopic(topicAdd, expertosAdd);
		assertEquals(conjExpertos.size(), db.expertsSize());
		assertEquals(conjTemas.size(), db.topicsSize());
		newExperts = db.getExperts(topicAdd);

		indexCheck = temas.indexOf(topicAdd);
		esperado = temasExpertos.get(indexCheck);
		System.out.println("Esperado "+topicAdd+" expertos: "+esperado);
		System.out.println("Obtenido "+topicAdd+" expertos: "+newExperts);
		assertTrue(newExperts.containsAll(oldExperts));
		assertTrue(newExperts.containsAll(expertosAdd));
		assertTrue(newExperts.containsAll(esperado) && esperado.containsAll(expertosAdd));

		for (String ex:expertosAdd) {
			int index1 = expertos.indexOf(ex);
			List<String> exEsperado = expertosTemas.get(index1);
			Set<String> exObtenido = db.getTopics(ex);
			System.out.println("Esperado temas para experto "+ex+": "+exEsperado);
			System.out.println("Obtenido temas para experto "+ex+": "+exObtenido);
			assertTrue(exEsperado.containsAll(exObtenido) && exObtenido.containsAll(exEsperado));
		}

		//Nuevo topic, algún experto nuevo
		topicAdd = "finanzas";
		expertosAdd.clear();
		expertosAdd.add("beckham");
		expertosAdd.add("nadal");

		if (!conjTemas.contains(topicAdd)) {
			conjTemas.add(topicAdd);
			temas.add(topicAdd);
			temasExpertos.add(new ArrayList<>());
			temasExpertos.get(temasExpertos.size()-1).addAll(expertosAdd);
		}
		else {
			int index = temas.indexOf(topicAdd);
			for (String ex: expertosAdd) {
				if (!temasExpertos.get(index).contains(ex)) {
					temasExpertos.get(index).add(ex);
				}
			}
		}

		for (String ex: expertosAdd) {
			if (!conjExpertos.contains(ex)) {
				conjExpertos.add(ex);
				expertos.add(ex);
				expertosTemas.add(new ArrayList<>());
				expertosTemas.get(expertosTemas.size()-1).add(topicAdd);
			}
			else {
				int index = expertos.indexOf(ex);
				if (!expertosTemas.get(index).contains(topicAdd)) {
					expertosTemas.get(index).add(topicAdd);
				}
			}
		}

		System.out.println("\n Adding "+topicAdd+" expertos: "+expertosAdd);

		db.addTopic(topicAdd, expertosAdd);
		assertEquals(conjExpertos.size(), db.expertsSize());
		assertEquals(conjTemas.size(), db.topicsSize());
		newExperts = db.getExperts(topicAdd);

		indexCheck = temas.indexOf(topicAdd);
		esperado = temasExpertos.get(indexCheck);
		System.out.println("Esperado "+topicAdd+" expertos: "+esperado);
		System.out.println("Obtenido "+topicAdd+" expertos: "+newExperts);
		//assertTrue(newExperts.containsAll(oldExperts));
		assertTrue(newExperts.containsAll(expertosAdd));
		assertTrue(newExperts.containsAll(esperado) && esperado.containsAll(expertosAdd));

		for (String ex:expertosAdd) {
			int index1 = expertos.indexOf(ex);
			List<String> exEsperado = expertosTemas.get(index1);
			Set<String> exObtenido = db.getTopics(ex);
			System.out.println("Esperado temas para experto "+ex+": "+exEsperado);
			System.out.println("Obtenido temas para experto "+ex+": "+exObtenido);
			assertTrue(exEsperado.containsAll(exObtenido) && exObtenido.containsAll(exEsperado));
		}

	}

	@Test
	public void TestRemoveExpert() throws FileNotFoundException {
		ExpertsDB db = new ExpertsDB();

		//borrar de una bd vacía
		System.out.println("Estado inicial: "+toString(db));
		System.out.println("Remove expert ronaldo");
		Collection<String> obtenido = db.removeExpert("ronaldo");
		System.out.println("Esperado: null");
		System.out.println("Obtenido "+obtenido);
		assertNull(obtenido);

		loaddb();
		load(db);
		System.out.println("Estado inicial: "+toString(db));

		//un experto que no existe
		String expert = "nadal";
		System.out.println("\n Remove expert "+expert);
		int oldsize = db.expertsSize();
		Set<String> oldExperts = db.getExperts();
		Collection<String> res = db.removeExpert(expert);
		Set<String> newExperts = db.getExperts();
		System.out.println("Obtenido: "+res);
		System.out.println("Esperado: null");
		assertNull(res);
		assertEquals(oldsize,db.expertsSize());
		assertTrue(oldExperts.containsAll(newExperts) && newExperts.containsAll(oldExperts));

		expert = "ramos";
		System.out.println("\n Remove expert "+expert);
		oldsize = db.expertsSize();
		oldExperts = db.getExperts();
		Set<String> temasBorrar = db.getTopics(expert);
		Set<String> desaparecen = new HashSet<>();
		for (String t:temasBorrar) {
			if (db.getExperts(t).size()==1)
				desaparecen.add(t);
		}
		for (String t:temasBorrar) {
			int index = temas.indexOf(t);
			List<String> lexpertos = temasExpertos.get(index);
			lexpertos.remove(expert);
			if (lexpertos.size()==0) {
				temasExpertos.remove(index);
				temas.remove(index);
				conjTemas.remove(t);
			}
		}
		int indexE = expertos.indexOf(expert);
		Set<String> esperado = new HashSet<>(expertosTemas.get(indexE));
		expertosTemas.remove(indexE);
		expertos.remove(indexE);
		conjExpertos.remove(expert);

		res=db.removeExpert(expert);
		//printDB(db);
		System.out.println("Obtenido: "+res);
		System.out.println("Esperado: "+esperado);
		assertTrue(res.containsAll(esperado) && esperado.containsAll(res));

		newExperts = db.getExperts();
		assertEquals(oldsize-1,db.expertsSize());
		assertFalse(db.getExperts().contains(expert));
		for (String t:temasBorrar) {
			if (!desaparecen.contains(t)) {
				Set<String> expertsc = db.getExperts(t);
				assertFalse(expertsc.contains(expert));
			}
		}
		for (String t:desaparecen) {
			Set<String> expertsc=db.getExperts(t);
			assertNull(expertsc);
		}
		assertNull(db.getTopics(expert));
		System.out.println(toString(db));

		//volver a borrar el mismo
		System.out.println("\n Remove expert "+expert);
		oldsize = db.expertsSize();
		oldExperts = db.getExperts();
		res = db.removeExpert(expert);
		newExperts = db.getExperts();
		System.out.println("Obtenido: "+res);
		System.out.println("Esperado: null");
		assertNull(res);
		assertEquals(oldsize,db.expertsSize());
		assertTrue(oldExperts.containsAll(newExperts) && newExperts.containsAll(oldExperts));

		expert = "ronaldo";
		System.out.println("\n Remove expert "+expert);
		oldsize = db.expertsSize();
		oldExperts = db.getExperts();
		temasBorrar = db.getTopics(expert);
		desaparecen = new HashSet<>();
		for (String t:temasBorrar) {
			if (db.getExperts(t).size()==1)
				desaparecen.add(t);
		}
		for (String t:temasBorrar) {
			int index = temas.indexOf(t);
			List<String> lexpertos = temasExpertos.get(index);
			lexpertos.remove(expert);
			if (lexpertos.size()==0) {
				temasExpertos.remove(index);
				temas.remove(index);
				conjTemas.remove(t);
			}
		}
		indexE = expertos.indexOf(expert);
		esperado = new HashSet<>(expertosTemas.get(indexE));
		expertosTemas.remove(indexE);
		expertos.remove(indexE);
		conjExpertos.remove(expert);

		res=db.removeExpert(expert);
		//printDB(db);
		System.out.println("Obtenido: "+res);
		System.out.println("Esperado: "+esperado);
		assertTrue(res.containsAll(esperado) && esperado.containsAll(res));

		newExperts = db.getExperts();
		assertEquals(oldsize-1,db.expertsSize());
		assertFalse(db.getExperts().contains(expert));
		for (String t:temasBorrar) {
			if (!desaparecen.contains(t)) {
				Set<String> expertsc = db.getExperts(t);
				assertFalse(expertsc.contains(expert));
			}
		}
		for (String t:desaparecen) {
			Set<String> expertsc=db.getExperts(t);
			assertNull(expertsc);
		}
		assertNull(db.getTopics(expert));
		System.out.println(toString(db));

	}

	@Test
	public void TestRemoveTopic() throws FileNotFoundException {
		ExpertsDB db = new ExpertsDB();

		System.out.println("Estado inicial: "+toString(db));
		System.out.println("Remove topic astrofisica");
		Collection<String> res = db.removeTopic("astrofisica");
		System.out.println("Obtenido: "+res);
		System.out.println("Esperado: null");
		assertNull(res);

		loaddb();
		load(db);
		System.out.println("Estado inicial: "+toString(db));

		//un tema que no existe
		String topicR = "informatica";
		System.out.println("\n Remove topic "+topicR);
		int oldsize = db.topicsSize();
		Set<String> oldTopics = db.getTopics();
		res=db.removeExpert(topicR);
		System.out.println("Obtenido: "+res);
		System.out.println("Esperado: null");
		assertNull(res);
		Set<String> newTopics = db.getTopics();
		assertEquals(oldsize,db.topicsSize());
		assertTrue(oldTopics.containsAll(newTopics) && newTopics.containsAll(oldTopics));

		//un tema que si existe
		List<String> listaTemas = new LinkedList<>(db.getTopics("ramos"));
		for (int i=0; i<listaTemas.size(); i++) {
			String topicRR = listaTemas.get(i);
			System.out.println("\n Remove topic " + topicRR);
			oldsize = db.topicsSize();
			oldTopics = db.getTopics();
			Set<String> expertosBorrar = db.getExperts(topicRR);
			Set<String> desaparecen = new HashSet<>();
			for (String e : expertosBorrar) {
				if (db.getTopics(e).size() == 1)
					desaparecen.add(e);
			}
			for (String e : expertosBorrar) {
				int index = expertos.indexOf(e);
				List<String> ltemas = expertosTemas.get(index);
				ltemas.remove(topicRR);
				if (ltemas.size() == 0) {
					expertosTemas.remove(index);
					expertos.remove(index);
					conjExpertos.remove(e);
				}
			}
			int indexT = temas.indexOf(topicRR);
			Set<String> esperado = new HashSet<>(temasExpertos.get(indexT));
			temasExpertos.remove(indexT);
			temas.remove(indexT);
			conjTemas.remove(indexT);

			res=db.removeTopic(topicRR);
			System.out.println("Obtenido: "+res);
			System.out.println("Esperado: "+esperado);
			assertTrue(res.containsAll(esperado) && esperado.containsAll(res));
			//printDB(db);

			newTopics = db.getTopics();
			assertEquals(oldsize - 1, db.topicsSize());
			assertFalse(db.getExperts().contains(topicRR));
			for (String e : expertosBorrar) {
				if (!desaparecen.contains(e)) {
					Set<String> temasE = db.getTopics(e);
					assertFalse(temasE.contains(topicRR));
				}
			}
			for (String e : desaparecen) {
				Set<String> temasE = db.getExperts(e);
				assertNull(temasE);
			}
			assertNull(db.getExperts(topicRR));
			System.out.println(toString(db));

		}

		//volver a borrar
		topicR = "matematicas";
		System.out.println("\n Remove topic "+topicR);
		oldsize = db.topicsSize();
		oldTopics = db.getTopics();
		res=db.removeExpert(topicR);
		System.out.println("Obtenido: "+res);
		System.out.println("Esperado: null");
		assertNull(res);
		newTopics = db.getTopics();
		assertEquals(oldsize,db.topicsSize());
		assertTrue(oldTopics.containsAll(newTopics) && newTopics.containsAll(oldTopics));

	}


	@Test
	public void TestXGetTopics()  {
		ExpertsDB db = new ExpertsDB();

		loaddb();
		load(db);
		System.out.println("Estado inicial: "+toString(db));

		List<String> listaTemas = new ArrayList<>(temas);
		listaTemas.add("geografia");
		for (int i=0; i<listaTemas.size(); i++) {
			String topicR = listaTemas.get(i);
			List<String> listaT = new ArrayList<>();
			listaT.add(topicR);
			System.out.println("\n Todos los expertos en: " + listaT);

			Set<String> esperado = new HashSet<>();

			for (String t : listaT) {
				int index = temas.indexOf(t);
				if (index != -1) {
					esperado.addAll(temasExpertos.get(index));
				}
				else {
					esperado.clear();
				}
			}

			Collection<String> obtenido = db.getExperts(listaT);

			System.out.println("    Esperado: " + esperado);
			System.out.println("    Obtenido: " + obtenido);
			assertTrue(esperado.containsAll(obtenido) && obtenido.containsAll(esperado));
		}


		ArrayList<String> listaT = new ArrayList<>();
		for (int i=0; i<listaTemas.size(); i++) {
			listaT.clear();
			listaT.add(listaTemas.get(i));

			for (int j=i+1; j<listaTemas.size(); j++) {

			   listaT.add(listaTemas.get(j));
			System.out.println("\n Todos los expertos en: " + listaT);

			Set<String> esperado = new HashSet<>(expertos);
			for (String t : listaT) {
				int index = temas.indexOf(t);
				if (index != -1) {
					esperado.retainAll(temasExpertos.get(index));
				}
				else
					esperado.clear();
			}
			Collection<String> obtenido = db.getExperts(listaT);

			System.out.println("    Esperado: " + esperado);
			System.out.println("    Obtenido: " + obtenido);
			assertTrue(esperado.containsAll(obtenido) && obtenido.containsAll(esperado));
		}
		}

	}


}
