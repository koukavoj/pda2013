package cvut.fel.pda2013;

import java.io.Serializable;

/**
 * Trida reprezentujici jednoho konkretniho uzivatele a jeho vlastnosti
 * @author vojta
 *
 */
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4871772442206782719L;
	private int id;
	private String name;
	private String photo;
	public int getId() {
		return id;
	}
	public void setId(int id) {	//je to potreba?
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
	@Override
	public String toString() {
		return "User [name=" + name + ", hash=" + hashCode() + "]";
	}
	@Override
	public int hashCode() {	
		return id;
	}
	@Override
	public boolean equals(Object o) {
		return id == ((User) o).getId();
	}
	
	
	
	
	
	
}
