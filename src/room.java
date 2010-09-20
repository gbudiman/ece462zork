import java.util.*;

public class room extends mapComponent {
	// Derived class of room
	public String description = null;
	public String roomType = null;
	public String status = null;
	public List<String> item = new ArrayList<String>();
	public zorkBorder[] border = new zorkBorder[4];
	public List<String> creatures = new ArrayList<String>();
	public List<String> container = new ArrayList<String>();
	protected zorkTrigger[] trigger = new zorkTrigger[10];
	
	public room (String n, String t, String rT, String d, String st, List<String> i, List<String> c, zorkBorder[] b, zorkTrigger[] trig, List<String> zc) {
		super(n, t);
		description = d;
		roomType = rT;
		status = st;
		item = i;
		container = c;
		border = b;
		trigger = trig;
		creatures = zc;
	}
	
	public void info() {
		super.info();
		System.out.println("Room type: " + roomType);
		System.out.println("Description: " + description);
		System.out.println("Item(s): " + Arrays.toString(item.toArray()));
		System.out.println("Container(s): " + Arrays.toString(container.toArray()));
		System.out.print("Border(s): ");
		for (int i = 0; i < border.length; i++) {
			if (border[i] != null) {
				System.out.print(border[i].info() + " ");
			}
		}
		System.out.println();
		for (int i = 0; i < trigger.length; i++) {
			if (trigger[i] != null) {
				trigger[i].info();
			}
		}
		System.out.print("Creature(s): " + Arrays.toString(creatures.toArray()));
		System.out.println();
		System.out.println("=================");
	}
	
	public zorkTrigger hasCommandTrigger(String reqCommand) {
		for (int i = 0; i < this.trigger.length; i++) {
			/*if (this.trigger[i] != null && this.trigger[i].command != null) {
				System.out.print("Analyzing trigger: " + this.trigger[i].command + " for " + reqCommand + ": ");
			}*/
			if (this.trigger[i] != null
					&& this.trigger[i].command != null
					&& this.trigger[i].command.equals(reqCommand.split(" ")[0])) {
				// Trigger is of type single
				if (this.trigger[i].type != null) {
					if (this.trigger[i].type.equals("single")) {
						if (this.trigger[i].hasBeenInvoked) {
							//System.out.println("passed...");
							return null;
						}
						else {
							//System.out.println("Overridden!");
							return this.trigger[i];
						}
					}
				}
				// Trigger is permanent
				return this.trigger[i];
			}
		}
		return null;
	}
	
	// Moved to mapComponent.java
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
					}
					else if (this.trigger[i].condition[j] != null
							&& this.trigger[i].condition[j].owner == null 
							&& this.trigger[i].condition[j].object != null) {
						//System.out.println("Expecting : " + ((zorkContainer) findObject(map, this.trigger[i].condition[j].object, "container")).status);
						//System.out.println("Trigger status : " + this.trigger[i].condition[j].status);
						if (((zorkContainer) findObject(map, this.trigger[i].condition[j].object, "container")).status != null
								&& ((zorkContainer) findObject(map, this.trigger[i].condition[j].object, "container")).status != null
								&& ((zorkContainer) findObject(map, this.trigger[i].condition[j].object, "container")).status.equals(this.trigger[i].condition[j].status)) {
							System.out.println(this.trigger[i].description);
							this.trigger[i].hasBeenInvoked = true;
							return true;
						}
						else if (((zorkItem) findObject(map, this.trigger[i].condition[j].object, "item")) != null
								&&((zorkItem) findObject(map, this.trigger[i].condition[j].object, "item")).status != null
								&& ((zorkItem) findObject(map, this.trigger[i].condition[j].object, "item")).status.equals(this.trigger[i].condition[j].status)) {
							System.out.println(this.trigger[i].description);
							this.trigger[i].hasBeenInvoked = true;
							return true;
						}
					}
					else {
						System.out.println("Unhandled room.checkTrigger");
					}
				}
			}
		}
		
		return false;
	}*/
	
	public String zorkMove(String command) {
		//System.out.println("Border 0" + this.border[0].direction);
		if (this.border != null) {
			for (int i = 0; i < this.border.length; i++) {
				if (this.border[i] != null) {
					char direction = this.border[i].direction.toCharArray()[0];
					if (command.toCharArray()[0] == direction) {
						return this.border[i].name;
					}
				}
			}
		}
		return null;
	}
	
	public void addItem(String newItem) {
		// Add new item to current room
		this.item.add(newItem);
	}
	
	public void dropItem(List<String> inventory, String itemToDrop) {
		if (inventory.contains(itemToDrop)) {
			this.item.add(itemToDrop);
			inventory.remove(itemToDrop);
			System.out.println(itemToDrop + " dropped.");
		}
		else {
			//System.out.println("Can't drop item not in your inventory");
			System.out.println("Error");
		}
	}
	
	public boolean contains(String type, String seek) {
		// Returns whether sought creature/container exists in the current room
		if (type.equals("creature")) {
			if (this.creatures.contains(seek)) {
				return true;
			}
		}
		else if (type.equals("container")) {
			if (this.container.contains(seek)) {
				return true;
			}
		}
		return false;
	}
	
	public void detachBorder(String adjacentRoom) {
		for (int i = 0; i < this.border.length; i++) {
			if (this.border[i] != null
					&& this.border[i].name != null
					&& this.border[i].name.equals(adjacentRoom)) {
				this.border[i] = null;
			}
		}
	}
}
