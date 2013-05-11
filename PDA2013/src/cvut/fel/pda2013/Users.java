package cvut.fel.pda2013;

import java.util.ArrayList;
import java.util.List;

/**
 * Staticka trida nahrazujici vzdaleny seznam uzivatelu
 * @author vojta
 *
 */
public class Users {
	
	private static final List<User> users = new ArrayList<User>();
	
	public static void init() {
		users.clear();
		users.add(new User(0, "Kamarádi", "kamaradi"));
		users.add(new User(1, "Vojta", "vojta"));
		users.add(new User(2, "Honza", "honza"));
		users.add(new User(3, "Pepa", "pepa"));		
		
		users.add(new User(4, "Rodina", "rodina"));
		users.add(new User(5, "Marcelka", "marcelka"));		
		users.add(new User(6, "Ségra", "segra"));
		users.add(new User(7, "Bráchanec", "brachanec"));
		
		
	}

	public static List<User> getAllUsers() {
		if (users.isEmpty()) init();
		return users;		
	}
	
	public static User getUserById(int id) {
		for(User u : users) {
			if (u.getId() == id) return u;
		}
		
		return null;
	}
	
}
