public class attack extends zorkTrigger {
	/*public zorkCondition condition;
	public String print;
	public String[] action;*/
	
	public attack(zorkCondition[] c, String p, String[] a) {
		super(null, null, c, p, a);
		/*condition = c;
		print = p;
		action = a;*/
	}
	public void info() {
		//condition.info();
		for (int i = 0; i < super.condition.length; i++) {
			if (super.condition[i] != null) {
				super.condition[i].info();
			}
		}
		System.out.println(super.description);
		System.out.print("Actions: ");
		for (int i = 0; i < action.length; i++) {
			if (action[i] != null) {
				System.out.print(" [" + action[i] + "]");
			}
		}
		System.out.println();
	}
}
