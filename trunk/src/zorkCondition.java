import java.util.*;

public class zorkCondition {
	public String has = null;
	public String object = null;
	public String owner = null;
	public String status = null;
	
	public zorkCondition(String h, String obj, String own, String s) {
		has = h;
		object = obj;
		owner = own;
		status = s;
	}
	public void info() {
		System.out.println("Has [" + has 
							+ "] [" + object 
							+ "] in [" + owner 
							+ "] status [" + status 
							+ "]");
	}
	public boolean assess(List<String> inventory, List<mapComponent> map) {
		if (this.owner != null
				&& this.owner.equals("inventory")) {
			// check inventory
			if (this.has != null && this.has.equals("yes")) {
				// If player's inventory contains this object, override
				if (inventory.contains(this.object)) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				// If player's inventory doesn't contain this object, override
				if (!inventory.contains(this.object)) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		else {
			// Only container and room can have the "has" property
			//zorkContainer seekContainer = ((zorkContainer) findObject(map, this.object, "container"));
			// Container
			if (this.has != null) {
				if (((zorkContainer) findObject(map, this.owner, "container")) != null) {
					zorkContainer ciq = ((zorkContainer) findObject(map, this.owner, "container"));
					if (this.has.equals("yes")) {
						if (ciq.item.contains(this.object)) {
							return true;
						}
						else {
							return false;
						}
					}
					else {
						if (!ciq.item.contains(this.object)) {
							return true;
						}
						else {
							return false;
						}
					}
				}
				// Room
				else {
					room riq = ((room) findObject(map, this.owner, "room"));
					if (this.has.equals("yes")) {
						if (riq.item.contains(this.object)) {
							return true;
						}
						else {
							return false;
						}
					}
					else {
						if (!riq.item.contains(this.object)){
							return true;
						}
						else {
							return false;
						}
					}
				}
			}
			// Otherwise, all objects can have status
			else {
				//System.out.println("+>> " + this.object + " " + this.status);
				if (((room) findObject(map, this.object, "room")) != null) {
					if (this.status.equals(((room) findObject(map, this.object, "room")).status)) {
						return true;
					}
				}
				else if (((zorkItem) findObject(map, this.object, "item")) != null) {
					//System.out.println("+>>>" + ((zorkItem) findObject(map, this.object, "item")).status + " expected " + this.status);
					if (this.status.equals(((zorkItem) findObject(map, this.object, "item")).status)) {
						return true;
					}
				}
				else if (((zorkContainer) findObject(map, this.object, "container")) != null) {
					if (this.status.equals(((zorkContainer) findObject(map, this.object, "container")).status)) {
						return true;
					}
				}
				else if (((creature) findObject(map, this.object, "creature")) != null) {
					if (this.status.equals(((creature) findObject(map, this.object, "creature")).status)) {
						return true;
					}
				}
				/*if (this.status.equals(seekContainer.status)) {
					return false; // Command is not overridden
				}
				else {
					return true; // Status mismatched, override command
				}*/
			}
		}
		return false;
	}
	
	public static mapComponent findObject(List<mapComponent> mapContainer
			, String identifier, String objectType) {
		boolean isFound = false;
		mapComponent soughtObject = null;
		ListIterator<mapComponent> i = mapContainer.listIterator();
		while (i.hasNext() && !isFound) {
			mapComponent currentObject = i.next();
			if (currentObject.name.equals(identifier) &&
				currentObject.type.equals(objectType)) {
				isFound = true;
				soughtObject = currentObject;
			}
		}
		
		return soughtObject;
	}
}
