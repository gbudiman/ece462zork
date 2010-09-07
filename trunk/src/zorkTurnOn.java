public class zorkTurnOn {
	public String print = null;
	public String[] action = null;
	
	public zorkTurnOn (String p, String[] a) {
		print = p;
		action = a;
	}
	
	public void info() {
		System.out.println("Turn on action: " + print);
		System.out.print("Action: ");
		for (int i = 0; i < action.length; i++) {
			if (action[i] != null) {
				System.out.print(" [" + action[i] + "]");
			}
		}
	}
}
