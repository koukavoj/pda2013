package cvut.fel.pda2013;

public class Pictogram {

	private String imageName;
	private String desc;
	private int id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Pictogram(String imageName, String desc, int id) {
		super();
		this.imageName = imageName;
		this.desc = desc;
		this.id = id;
	}
		
}
