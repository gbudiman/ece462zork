import java.util.*;

public class room extends mapComponent {
	// Derived class of room
	public String description = null;
	public String roomType = null;
	public String[] item = new String[10];
	public zorkBorder[] border = new zorkBorder[4];
	public String[] creatures = new String[10];
	public String[] container = new String[10];
	protected zorkTrigger[] trigger = new zorkTrigger[10];
	
	public room (String n, String t, String rT, String d, String[] i, String[] c, zorkBorder[] b, zorkTrigger[] trig, String[] zc) {
		super(n, t);
		description = d;
		roomType = rT;
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
		System.out.println("Item(s): " + Arrays.toString(item));
		System.out.println("Container(s): " + Arrays.toString(container));
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
		System.out.print("Creature(s): ");
		for (int i = 0; i < creatures.length; i++) {
			if (creatures[i] != null) {
				System.out.print(" " + creatures[i]);
			}
		}
		System.out.println();
		System.out.println("=================");
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
						System.out.println("Unhandled room.checkTrigger");
					}*/
				}
			}
		}
		
		return false;
	}
	
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
		for (int i = 0; i < this.item.length; i++) {
			if (this.item[i] == null) {
				this.item[i] = newItem;
				i = this.item.length; // immediately exit
			}
		}
	}
	
	public boolean contains(String type, String seek) {
		if (type.equals("creature")) {
			for (int i = 0; i < this.creatures.length; i++) {
				if (this.creatures[i] != null && this.creatures[i].equals(seek)) {
					return true;
				}
			}
		}
		else if (type.equals("container")) {
			for (int i = 0; i < this.container.length; i++) {
				if (this.container[i] != null && this.container[i].equals(seek)) {
					return true;
				}
			}
		}
		return false;
	}
}
