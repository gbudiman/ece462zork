public class zorkContainer extends mapComponent {
	public String description;
	public String item;
	public String accept;
	public String status;
	private zorkTrigger[] trigger;
	
	public zorkContainer(String n, String t, String d, String i, String a, String s, zorkTrigger[] zt) {
		super(n, t);
		description = d;
		item = i;
		accept = a;
		status = s;
		trigger = zt;
	}
	
	public void info() {
		super.info();
		System.out.println("Description: " + description);
		System.out.println("Contains: " + item);
		System.out.println("Status: " + status);
		System.out.println("Accepts: " + accept);
		for (int i = 0; i < trigger.length; i++) {
			if (trigger[i] != null) {
				trigger[i].info();
			}
		}
		System.out.println("=================");
	}
}
