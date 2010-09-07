public class attack {
	public zorkCondition condition;
	public String print;
	public String[] action;
	
	public attack(zorkCondition c, String p, String[] a) {
		condition = c;
		print = p;
		action = a;
	}
	public void info() {
		condition.info();
		System.out.println(print);
		System.out.print("Actions: ");
		for (int i = 0; i < action.length; i++) {
			if (action[i] != null) {
				System.out.print(" [" + action[i] + "]");
			}
		}
		System.out.println();
	}
}
