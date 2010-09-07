public class zorkBorder {
	public String direction;
	public String name;
	
	public zorkBorder(String d, String n) {
		direction = d;
		name = n;
	}
	public String info() {
		return direction + ": " + name;
	}
}
