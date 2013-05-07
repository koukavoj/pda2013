package cvut.fel.pda2013;

/**
 * Trida reprezentujici jednoho konkretniho uzivatele a jeho vlastnosti
 * @author vojta
 *
 */
public class User {

	private int id;
	private String name;
	private String photo;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public User(int id, String name, String photo) {
		super();
		this.id = id;
		this.name = name;
		this.photo = photo;
	}
	
	
	
}
