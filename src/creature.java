public class creature extends mapComponent {
	public String vulnerability;
	public attack attack;
	public zorkTrigger[] trigger;
	
	public creature(String n, String t, String v, attack a, zorkTrigger[] zt) {
		super(n, t);
		vulnerability = v;
		attack = a;
		trigger = zt;
	}
	public void info() {
		System.out.println("!!! " + type + " " + name);
		System.out.println("Vulnerability: " + vulnerability);
		attack.info();
		for (int i = 0; i < trigger.length; i++) {
			if (trigger[i] != null) {
				trigger[i].info();
			}
		}
	}
}
