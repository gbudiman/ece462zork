import java.util.*;

public class room extends mapComponent {
	// Derived class of room
	public String description;
	public String roomType;
	public String[] item;
	public zorkBorder[] border;
	public String[] container;
	private zorkTrigger[] trigger;
	
	public room (String n, String t, String rT, String d, String[] i, String[] c, zorkBorder[] b, zorkTrigger[] trig) {
		super(n, t);
		description = d;
		roomType = rT;
		item = i;
		container = c;
		border = b;
		trigger = trig;
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
		System.out.println("=================");
	}
	
	public boolean checkTrigger(String command, List<String> currentInventory) {
		if (this.trigger == null) {
			return false;
		}
		
		for (int i = 0; i < this.trigger.length; i++) {
			if (this.trigger[i].command != null && this.trigger[i].command.equals(command)) {
				if (this.trigger[i].condition.has.equals("no")) {
					if (this.trigger[i].condition.owner.equals("inventory")) {
						// Command overridden if item is not in inventory
						if (!currentInventory.contains(this.trigger[i].condition.object)) {
							System.out.println(this.trigger[i].description);
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public String move(String command) {
		for (int i = 0; i < this.border.length; i++) {
			if (command.equals(this.border[i].direction.charAt(0))) {
				return this.border[i].name;
			}
		}
		return null;
	}
}
