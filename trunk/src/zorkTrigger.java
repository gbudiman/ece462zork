import java.util.List;

public class zorkTrigger {
	public String type = null;
	public String command = null;
	public zorkCondition[] condition;
	public String description = null;
	public String[] action = null;
	public boolean hasBeenInvoked = false;
	
	public zorkTrigger(String t, String c, zorkCondition[] zc, String d, String[] a) {
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
		for (int i = 0; i < condition.length; i++) {
			if (condition[i] != null) {
				condition[i].info();
			}
		}
	}
	
	public boolean checkCondition(List<String> inventory, List<mapComponent> map) {
		boolean result = false;
		//System.out.println("++checking condition for trigger " + this.command);
		if (this.condition != null) {
			for (int i = 0; i < this.condition.length; i++) {
				if (this.condition[i] != null) {
					result = this.condition[i].assess(inventory, map);
					//System.out.println("+---" + result + " " + this.condition[i].object);
					if (this.type == null || this.type.equals("single")) {
						if (!this.hasBeenInvoked) {
							if (result && this.description != null) {
								System.out.println(this.description);
								return result;
							}
							else if (result) {
								return result;
							}
						}
					}
					else {
						if (result && this.description != null) {
							System.out.println(this.description);
							return result;
						}
						else if (result) {
							return result;
						}
					}
				}
			}
		}
		return result;
	}
}
