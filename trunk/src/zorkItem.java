public class zorkItem extends mapComponent {
	public String writing = null;
	public String status = null;
	public zorkTurnOn turnon = null;
	public zorkTrigger[] trigger = null;
	
	public zorkItem(String n, String t, String w, String s, zorkTurnOn to, zorkTrigger[] zt) {
		super(n, t);
		writing = w;
		status = s;
		turnon = to;
		zt = trigger;
	}
	
	public void info() {
		System.out.println(super.type + " " + super.name+ " has properties:");
		System.out.println(">> Writing: " + writing);
		System.out.println(">> Status: " + status);
		if (turnon != null) {
			turnon.info();
		}
		for (int i = 0; i < trigger.length; i++) {
			if (trigger[i] != null) {
				trigger[i].info();
			}
		}
		System.out.println("=================");
	}
}
