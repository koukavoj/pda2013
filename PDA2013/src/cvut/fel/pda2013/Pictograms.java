package cvut.fel.pda2013;

import java.util.ArrayList;
import java.util.List;

public class Pictograms {

	static private List<Pictogram> furniture = new ArrayList<Pictogram>();
	static private List<Pictogram> food = new ArrayList<Pictogram>();
	static private List<Pictogram> pronouns = new ArrayList<Pictogram>();
	
	static int c = 1;
	
	static public void init() {	
		furniture.add(new Pictogram("knihovna", "knihovna", c++));
		furniture.add(new Pictogram("kreslo", "køeslo", c++)); 	
		furniture.add(new Pictogram("postel", "postel", c++));
		furniture.add(new Pictogram("psaci_stul", "psací stùl", c++));
		furniture.add(new Pictogram("rejzozidle", "režisérská židle", c++));
		furniture.add(new Pictogram("skrin", "skøíò", c++));
		furniture.add(new Pictogram("stena", "TV stìna", c++));
		furniture.add(new Pictogram("stolicka", "stolièka", c++));
		furniture.add(new Pictogram("stul", "stùl", c++));
		furniture.add(new Pictogram("tabule", "tabule", c++));
		furniture.add(new Pictogram("velka_pohovka", "velká pohovka", c++));						
		
		food.add(new Pictogram("dort", "dort", c++));
		food.add(new Pictogram("chleba", "chleba", c++));
		food.add(new Pictogram("jablko", "jablko", c++));
		food.add(new Pictogram("klasek", "klasek", c++));
		food.add(new Pictogram("klobasa", "klobasa", c++));
		food.add(new Pictogram("kornout", "kornout", c++));
		food.add(new Pictogram("kure", "kure", c++));
		food.add(new Pictogram("mleko", "mleko", c++));
		food.add(new Pictogram("mrkev", "mrkev", c++));
		food.add(new Pictogram("okurka", "okurka", c++));
		food.add(new Pictogram("rajce", "rajce", c++));
		food.add(new Pictogram("ryba", "ryba", c++));
		food.add(new Pictogram("syr", "syr", c++));
		food.add(new Pictogram("vajicko", "vajicko", c++));
		food.add(new Pictogram("visne", "visne", c++));
		food.add(new Pictogram("zmrzlina", "zmzlina", c++));
		
		
		pronouns.add(new Pictogram("ja", "já", c++));
		pronouns.add(new Pictogram("my", "my", c++));
		pronouns.add(new Pictogram("oni", "oni", c++));
		pronouns.add(new Pictogram("ty", "ty", c++));
		pronouns.add(new Pictogram("vy", "vy", c++));
		
		
	}
	
	static public List<Pictogram> getFurniture() {
		if (furniture.isEmpty()) init();
		return furniture;
	}
	
	static public List<Pictogram> getFood() {
		if (food.isEmpty()) init();
		return food;
	}

	static public List<Pictogram> getPronouns() {
		if (pronouns.isEmpty()) init();
		return pronouns;
	}

	
	static public Pictogram getPictById(int id) {		
		for(Pictogram p : furniture) {
			if (p.getId() == id) return p;
		}
		
		for(Pictogram p : food) {
			if (p.getId() == id) return p;
		}
		
		for(Pictogram p : pronouns) {
			if (p.getId() == id) return p;
		}
		
		return null;
	}
	
}
