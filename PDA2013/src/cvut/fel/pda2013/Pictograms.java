package cvut.fel.pda2013;

import java.util.ArrayList;
import java.util.List;

public class Pictograms {

	static private List<Pictogram> furniture = new ArrayList<Pictogram>();
	static private List<Pictogram> food = new ArrayList<Pictogram>();
	static int c = 1;
	
	static public void init() {	
		furniture.add(new Pictogram("knihovna", "knihovna", c++));
		furniture.add(new Pictogram("pcstul", "PC stùl", c++));
		furniture.add(new Pictogram("pohovka", "pohovka", c++));
		furniture.add(new Pictogram("tabule", "tabule", c++));		
		
		food.add(new Pictogram("donut", "donut", c++));
		food.add(new Pictogram("dort", "dort", c++));
		food.add(new Pictogram("hruska", "hruska", c++));
		food.add(new Pictogram("lizatko", "lizatko", c++));
		food.add(new Pictogram("nanuk", "nanuk", c++));
		food.add(new Pictogram("salek", "kava", c++));
		food.add(new Pictogram("sklenice", "martini", c++));
		
	}
	
	static public List<Pictogram> getFurniture() {
		if (furniture.isEmpty()) init();
		return furniture;
	}
	
	static public List<Pictogram> getFood() {
		if (food.isEmpty()) init();
		return food;
	}
	
	static public Pictogram getPictById(int id) {		
		for(Pictogram p : furniture) {
			if (p.getId() == id) return p;
		}
		
		for(Pictogram p : food) {
			if (p.getId() == id) return p;
		}
		
		return null;
	}
	
}
