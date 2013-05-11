package cvut.fel.pda2013;

public class Login {

	public static int loggedUser = -1;
	public static User getLoggedUser() {
		return Users.getUserById(loggedUser);
	}
	
}
