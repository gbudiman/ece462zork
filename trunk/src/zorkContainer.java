import java.util.List;

public class zorkContainer extends mapComponent {
	public String description = null;
	public String item = null;
	public String accept = null;
	public String status = null;
	protected zorkTrigger[] trigger;
	public boolean takeAble = false;
	
	public zorkContainer(String n, String t, String d, String i, String a, String s, zorkTrigger[] zt) {
		super(n, t);
		description = d;
		item = i;
		accept = a;
		status = s;
		trigger = zt;
		takeAble = false;
	}
	
	public void info() {
		super.info();
		System.out.println("Description: " + description);
		System.out.println("Contains: " + item);
		System.out.println("Status: " + status);
		System.out.println("Accepts: " + accept);
		for (int i = 0; i < trigger.length; i++) {
			if (trigger[i] != null) {
				trigger[i].info();
			}
		}
		System.out.println("=================");
	}
	
	public void addItem(String newItem) {
		this.item = newItem;
	}
	
	public void open() {
		this.takeAble = true;
		if (this.item == null) {
			System.out.println(this.name + " is empty");
		}
		else {
			System.out.println(this.name + " contains " + this.item);
		}
	}
	
	public boolean take(List<String> inventory) {
		if (this.takeAble == true) {
			inventory.add(this.item);
			this.item = null;
			return true;
		}
		return false;
	}
	
	public List<mapComponent> put(List<mapComponent> map, List<String> inventory, String newItem) {
		if (this.item == null) {
			if (this.accept != null) {
				if (!this.accept.equals(newItem)) {
					System.out.println("Container " + this.name + " only accepts " + this.accept);
				}
				else {
					this.item = newItem;
					inventory.remove(newItem);
					map = checkTrigger(map, this.trigger);
				}
			}
			else {
				this.item = newItem;
				if (!inventory.remove(newItem)) {
					System.out.println(newItem + " is not in your inventory");
				}
				else {
					System.out.println("Item " + newItem + " added to " + this.name);
				}
			}
		}
		else if (this.item != null) {
			System.out.println("Container " + this.name + " is not empty");
		}
		
		return map;
	}
	
	public List<mapComponent> checkTrigger(List<mapComponent> map, zorkTrigger[] t) {
		for (int i = 0; i < t.length; i++) {
			for (int j = 0; t[i] != null && j < t[i].condition.length; j++) {
				if (t[i].condition[j] != null) {
					if ((room) findObject(map, t[i].condition[j].owner, "room") != null) {
						room tso = (room) findObject(map, t[i].condition[j].owner, "room");
						if (t[i].condition[j].has.equals("yes")) {
							if (tso.contains("item", t[i].condition[j].object)) {
								map = takeAction(map, t[i].action);
								System.out.println(t[i].description);
							}
						}
						else {
							if (!tso.contains("item", t[i].condition[j].object)){
								map = takeAction(map, t[i].action);
								System.out.println(t[i].description);
							}
						}
					}
					else if ((zorkContainer) findObject(map, t[i].condition[j].owner, "container") != null) {
						zorkContainer tso = (zorkContainer) findObject(map, t[i].condition[j].owner, "container");
						if (t[i].condition[j].has.equals("yes")) {
							if (tso.item.equals(t[i].condition[j].object)) {
								map = takeAction(map, t[i].action);
								System.out.println(t[i].description);
							}
						}
						else {
							if (!tso.item.equals(t[i].condition[j].object)) {
								map = takeAction(map, t[i].action);
								System.out.println(t[i].description);
							}
						}
					}
				}
			}
		}
		
		return map;
	}
	
	public zorkTrigger hasCommandTrigger(String reqCommand) {
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
		return null;
	}
}
