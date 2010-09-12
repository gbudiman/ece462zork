import java.util.List;

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
	public boolean checkTrigger(String command, List<String> currentInventory, List<mapComponent> map) {
		if (this.trigger == null) {
			return false;
		}
		for (int i = 0; this.trigger[i] != null; i++) {
			if (this.trigger[i].command != null && this.trigger[i].command.equals(command)) {
				// Check if non-permanent trigger has been invoked.
				if (this.trigger[i].type != null
						&& !this.trigger[i].type.equals("permanent")) {
					if (this.trigger[i].hasBeenInvoked == true) {
						return false;
					}
				}
				for (int j = 0; j < this.trigger[i].condition.length; j++) {
					if (this.trigger[i].condition[j] != null
							&& this.trigger[i].condition[j].owner != null 
							&& this.trigger[i].condition[j].owner.equals("inventory")) {
						if (this.trigger[i].condition[i].has != null && this.trigger[i].condition[j].has.equals("no")) {
							// Command overridden if item is not in inventory
							if (!currentInventory.contains(this.trigger[i].condition[j].object)) {
								System.out.println(this.trigger[i].description);
								this.trigger[i].hasBeenInvoked = true;
								return true;
							}
						}
					}
					else if (this.trigger[i].condition[j] != null
							&& this.trigger[i].condition[j].owner == null 
							&& this.trigger[i].condition[j].object != null) {
						if (((zorkContainer) findObject(map, this.trigger[i].condition[j].object, "container")).status.equals(this.trigger[i].condition[j].status)) {
							System.out.println(this.trigger[i].description);
							this.trigger[i].hasBeenInvoked = true;
							return true;
						}
						else if (((zorkItem) findObject(map, this.trigger[i].condition[j].object, "item")).status.equals(this.trigger[i].condition[j].status)) {
							System.out.println(this.trigger[i].description);
							this.trigger[i].hasBeenInvoked = true;
							return true;
						}
					}
					/*else {
						System.out.println("Unhandled creature.checkTrigger");
					}*/
				}
			}
		}
		
		return false;
	}
	
	public void attack(String weapon) {
		if (this.vulnerability.equals(weapon)) {
			System.out.println(this.attack.print);
		}
		else {
			System.out.println(this.name + " is invulnerable to " + weapon);
		}
	}
}
