public class zorkTrigger {
	public String type;
	public String command;
	public zorkCondition condition;
	public String description = null;
	public String[] action = null;
	public boolean hasBeenInvoked = false;
	
	public zorkTrigger(String t, String c, zorkCondition zc, String d, String[] a) {
		type = t;
		command = c;
		condition = zc;
		description = d;
		action = a;
	}
	public void info() {
		System.out.println("***** TRIGGER *****");
		System.out.println("Type: " + type + " invoked?: " + hasBeenInvoked);
		System.out.println("Command: " + command);
		System.out.print("Action: ");
		for (int i = 0; i < action.length; i++) {
			if (action[i] != null) {
				System.out.print(" [" + action[i] + "]");
			}
		}
		System.out.println();
		System.out.println("Print: " + description);
		condition.info();
	}
}
