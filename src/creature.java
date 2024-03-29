import java.util.List;

public class creature extends mapComponent {
	public List<String> vulnerability = null;
	public String description = null;
	public String status = null;
	public attack attack = null;
	public zorkTrigger[] trigger;
	
	public creature(String n, String t, String d, List<String> v, String st, attack a, zorkTrigger[] zt) {
		super(n, t);
		description = d;
		vulnerability = v;
		status = st;
		attack = a;
		trigger = zt;
	}
	public void info() {
		System.out.println("!!! " + type + " " + name);
		System.out.println("Vulnerability: " + vulnerability);
		if (this.attack != null) {
			attack.info();
		}
		for (int i = 0; i < trigger.length; i++) {
			if (trigger[i] != null) {
				trigger[i].info();
			}
		}
	}
	/*public boolean checkTrigger(String command, List<String> currentInventory, List<mapComponent> map) {
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
						else if (this.trigger[i].condition[i].has != null && this.trigger[i].condition[j].has.equals("yes")) {
							if (currentInventory.contains(this.trigger[i].condition[j].object)) {
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
					else {
						System.out.println("Unhandled creature.checkTrigger");
					}
				}
			}
		}
		
		return false;
	}*/
	
	public unity attack(List<mapComponent> map, List<String> item, String weapon, String currentRoom) {
		unity x = new unity(map, item, currentRoom);
		boolean noCondition = false;
		//System.out.println(this.vulnerability);
		if (this.vulnerability.contains(weapon)) {
			/*if (item.contains(this.attack.condition.object)
					&& ((zorkItem) findObject(map, weapon, "item")).status.equals(this.attack.condition.status)) {*/
			//System.out.println("weapon: " + weapon);
			if (this.attack != null) {
				for (int i = 0; i < this.attack.condition.length; i++) {
					if (i == this.attack.condition.length - 1 && this.attack.condition[i] == null) {
						System.out.println("You assault the " + this.name + " with the " + weapon + ".");
						if (this.attack.description != null) {
							System.out.println(this.attack.description);
						}
						x = takeAction(map, this.attack.action, item, currentRoom);
						noCondition = true;
					}
					else if (this.attack.condition[i] != null) {
						break;
					}
				}
				if (!noCondition) {
					//System.out.println("in here");
					if (this.attack.checkConditionForAttack(item, map)) {
						//System.out.println("step out");
						System.out.println("You assault the " + this.name + " with the " + weapon + ".");
						if (this.attack.description != null) {
							System.out.println(this.attack.description);
						}
						x = takeAction(map, this.attack.action, item, currentRoom);
					}
					else {
						//System.out.println("Unmatching " + weapon + " status");
						System.out.println("Error");
					}
				}
			}
			else {
				System.out.println("You assault the " + this.name + " with the " + weapon + ".");
			}
		}
		else {
			//System.out.println(this.name + " is invulnerable to " + weapon);
			System.out.println("Error");
		}
		return x;
	}
	
	public zorkTrigger hasCommandTrigger(String reqCommand) {
		for (int i = 0; i < this.trigger.length; i++) {
			if (this.trigger[i] != null
					&& this.trigger[i].command != null
					&& this.trigger[i].command.equals(reqCommand)) {
				// Trigger is of type single
				//System.out.println(reqCommand + " expected " + this.trigger[i].command);
				if (this.trigger[i].type != null
						|| this.trigger[i].type.equals("single")) {
					//System.out.println("inside: " + this.trigger[i].hasBeenInvoked);
					if (this.trigger[i].hasBeenInvoked) {
						return null;
					}
					else {
						//System.out.println("returning... ");
						return this.trigger[i];
					}
				}
				// Trigger is permanent
				return this.trigger[i];
			}
		}
		return null;
	}
}
