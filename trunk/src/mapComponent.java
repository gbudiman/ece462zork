public class mapComponent {
	// Generic class component in map
	public String name;
	public String type;
	
	public mapComponent(String n, String t) {
		name = n;
		type = t;
	}
	
	public void info() {
		System.out.println("Info for object type [" + type + "] with identity: " + name);
	}
}
