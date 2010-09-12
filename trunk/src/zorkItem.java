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
	
	public void activate(List<mapComponent> map) {
		for (int i = 0; i < this.turnon.action.length; i++) {
			if (this.turnon.action[i] != null) {
				String[] btsCommand = this.turnon.action[i].split(" ");
				if (btsCommand[0].equals("Update")) {
					// Syntax: Update (...) to (...)
					((zorkItem) findObject(map, btsCommand[1], "item")).status = btsCommand[3];
					//(List<mapComponent> map).searchForTrigger(map, btsCommand[1]);
					searchForTrigger(map, btsCommand[1], btsCommand[3]);
				}
				else if (btsCommand[0].equals("Add")) {
					// Syntax: Add (...) to (...)
					// Need to search for room first
					if (((room) findObject(map, btsCommand[3], "room")) != null) {
						((room) findObject(map, btsCommand[3], "room")).addItem(btsCommand[1]);
					}
					// Then search for container, if hasn't found in room
					else if (((zorkContainer) findObject(map, btsCommand[3], "container")) != null) {
						((zorkContainer) findObject(map, btsCommand[3], "container")).addItem(btsCommand[1]);
					}
				}
				else if (btsCommand[0].equals("Delete")) {
					// Syntax: Delete (...)
					String[] objectType = {"room", "item", "container", "creature"};
					for (int j = 0; j < objectType.length; j++) {
						if (findObject(map, btsCommand[1], objectType[j]) != null && findObject(map, btsCommand[1], objectType[j]).name.equals(btsCommand[1])) {
							map.remove(findObject(map, btsCommand[1], objectType[j]));
						}
					}
				}
				// TODO: Game Over
			}
		}
		System.out.println(this.status);
	}
}
