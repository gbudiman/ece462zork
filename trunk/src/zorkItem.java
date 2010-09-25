import java.util.*;

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
		if (trigger != null) {
			for (int i = 0; i < trigger.length; i++) {
				if (trigger[i] != null) {
					trigger[i].info();
				}
			}
		}
		System.out.println("=================");
	}
	
	public unity activate(List<mapComponent> map, List<String> inventory, String currentRoom) {
		System.out.println("You activate the " + this.name + ".");
		if (this.turnon.print != null) {
			System.out.println(this.turnon.print);
		}
		unity x = takeAction(map, this.turnon.action, inventory, currentRoom);
		
		return x;
	}
	
	public zorkTrigger hasCommandTrigger(String reqCommand) {
		if (this.trigger != null) {
			for (int i = 0; i < this.trigger.length; i++) {
				if (this.trigger[i] != null
						&& this.trigger[i].command != null
						&& this.trigger[i].command.equals(reqCommand)) {
					// Trigger is of type single
					if (this.trigger[i].type != null
							|| this.trigger[i].type.equals("single")) {
						if (this.trigger[i].hasBeenInvoked) {
							return null;
						}
						else {
							return this.trigger[i];
						}
					}
					// Trigger is permanent
					return this.trigger[i];
				}
			}
		}
		return null;
	}
}
