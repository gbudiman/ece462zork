import java.util.List;
import java.util.ListIterator;

public class mapComponent {
	// Generic class component in map
	public String name;
	public String type;
	
	public mapComponent(String n, String t) {
		name = n;
		type = t;
	}
	
	public void info() {
		System.out.println("Info for object type [" + type + "] with identity: " + name);
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
	
	public List<mapComponent> takeAction(List<mapComponent> map, String[] actionArray, List<String> inventory, String currentRoom) {
		for (int i = 0; i < actionArray.length; i++) {
			if (actionArray[i] != null) {
				String[] btsCommand = actionArray[i].split(" ");
				if (btsCommand[0].equals("Update")) {
					// Syntax: Update (...) to (...)
					
					if ((zorkItem) findObject(map, btsCommand[1], "item") != null) {
						((zorkItem) findObject(map, btsCommand[1], "item")).status = btsCommand[3];
					}
					else if ((room) findObject(map, btsCommand[1], "room") != null) {
						((room) findObject(map, btsCommand[1], "room")).status = btsCommand[3];
					}
					else if ((zorkContainer) findObject(map, btsCommand[1], "container") != null) {
						((zorkContainer) findObject(map, btsCommand[1], "container")).status = btsCommand[3];
					}
					else if ((room) findObject(map, btsCommand[1], "creature") != null) {
						((creature) findObject(map, btsCommand[1], "creature")).status = btsCommand[3];
					}
					
					//(List<mapComponent> map).searchForTrigger(map, btsCommand[1]);
					//searchForTrigger(map, btsCommand[1], btsCommand[3]);
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
					
					((room) findObject(map, currentRoom, "room")).creatures.remove(btsCommand[1]);
					((room) findObject(map, currentRoom, "room")).item.remove(btsCommand[1]);
					((room) findObject(map, currentRoom, "room")).container.remove(btsCommand[1]);
					((room) findObject(map, currentRoom, "room")).detachBorder(btsCommand[1]);
				}
				else if (actionArray[i].equals("Game Over")) {
					System.out.println("Victory!");
					System.exit(0);
				}
			}
		}
		//System.out.println("<<< re-looping");
		map = findObject(map, currentRoom, "room").searchForTrigger(map, inventory, currentRoom);
		//System.out.println(">>> re-looping done");
		return map;
	}
	
	public void removeCreature(String ctr) {
		((room) this).creatures.remove(ctr);
	}
	
	/*public void searchForTrigger(List<mapComponent> map, String seek, String status) {
		//String[] objectType = {"room", "item", "container", "creature"};
		Boolean isFound = false;
		ListIterator<mapComponent> i = map.listIterator();
		
		while (i.hasNext() && !isFound) {
			mapComponent currentObject = i.next();
			if (currentObject.type.equals("room")) {
				room roomObject = (room) currentObject;
				for (int j = 0; roomObject.trigger != null && j < roomObject.trigger.length; j++) {
					for (int k = 0; roomObject.trigger[j] != null
									&& roomObject.trigger[j].condition != null 
									&& k < roomObject.trigger[j].condition.length; k++) {
						if (roomObject.trigger[j] != null 
								&& roomObject.trigger[j].condition[k] != null
								&& roomObject.trigger[j].condition[k].object != null
								&& roomObject.trigger[j].condition[k].status != null
								&& roomObject.trigger[j].condition[k].status.equals(status)) {
							if ((roomObject.trigger[j].type == null
									|| roomObject.trigger[j].type.equals("single"))
									&& roomObject.trigger[j].hasBeenInvoked == false) {
								roomObject.trigger[j].hasBeenInvoked = true;
								System.out.println(roomObject.trigger[j].description);
							}
							else if ((roomObject.trigger[j].type.equals("permanent"))) {
								roomObject.trigger[j].hasBeenInvoked = true;
								System.out.println(roomObject.trigger[j].description);
							}
							//System.out.println(roomObject.trigger[j].description);
						}
					}
				}
			}
			else if (currentObject.type.equals("item")) {
				zorkItem itemObject = (zorkItem) currentObject;
				for (int j = 0; itemObject.trigger != null && j < itemObject.trigger.length; j++) {
					for (int k = 0; itemObject.trigger[j] != null
									&& itemObject.trigger[j].condition != null 
									&& k < itemObject.trigger[j].condition.length; k++) {
						if (itemObject.trigger[j] != null 
								&& itemObject.trigger[j].condition[k] != null
								&& itemObject.trigger[j].condition[k].object != null
								&& itemObject.trigger[j].condition[k].status != null
								&& itemObject.trigger[j].condition[k].status.equals(status)) {
							if ((itemObject.trigger[j].type == null
									|| itemObject.trigger[j].type.equals("single"))
									&& itemObject.trigger[j].hasBeenInvoked == false) {
								itemObject.trigger[j].hasBeenInvoked = true;
								System.out.println(itemObject.trigger[j].description);
							}
							else if ((itemObject.trigger[j].type.equals("permanent"))) {
								itemObject.trigger[j].hasBeenInvoked = true;
								System.out.println(itemObject.trigger[j].description);
							}
							//System.out.println(itemObject.trigger[j].description);
						}
					}
				}
			}
			else if (currentObject.type.equals("container")) {
				zorkContainer contObject = (zorkContainer) currentObject;
				for (int j = 0; contObject.trigger != null && j < contObject.trigger.length; j++) {
					for (int k = 0; contObject.trigger[j] != null 
									&& contObject.trigger[j].condition != null 
									&& k < contObject.trigger[j].condition.length; k++) {
						if (contObject.trigger[j] != null 
								&& contObject.trigger[j].condition[k] != null
								&& contObject.trigger[j].condition[k].object != null
								&& contObject.trigger[j].condition[k].status != null
								&& contObject.trigger[j].condition[k].status.equals(status)) {
							if ((contObject.trigger[j].type == null
									|| contObject.trigger[j].type.equals("single"))
									&& contObject.trigger[j].hasBeenInvoked == false) {
								contObject.trigger[j].hasBeenInvoked = true;
								System.out.println(contObject.trigger[j].description);
							}
							else if ((contObject.trigger[j].type.equals("permanent"))) {
								contObject.trigger[j].hasBeenInvoked = true;
								System.out.println(contObject.trigger[j].description);
							}
							//System.out.println(contObject.trigger[j].description);
						}
					}
				}
			}
			else if (currentObject.type.equals("creature")) {
				creature creatureObject = (creature) currentObject;
				for (int j = 0; creatureObject.trigger != null && j < creatureObject.trigger.length; j++) {
					for (int k = 0; creatureObject.trigger[j] != null 
									&& creatureObject.trigger[j].condition != null 
									&& k < creatureObject.trigger[j].condition.length; k++) {
						if (creatureObject.trigger[j] != null 
								&& creatureObject.trigger[j].condition[k] != null
								&& creatureObject.trigger[j].condition[k].object != null
								&& creatureObject.trigger[j].condition[k].status != null
								&& creatureObject.trigger[j].condition[k].status.equals(status)) {
							if ((creatureObject.trigger[j].type == null
									|| creatureObject.trigger[j].type.equals("single"))
									&& creatureObject.trigger[j].hasBeenInvoked == false) {
								creatureObject.trigger[j].hasBeenInvoked = true;
								System.out.println(creatureObject.trigger[j].description);
							}
							else if ((creatureObject.trigger[j].type.equals("permanent"))) {
								creatureObject.trigger[j].hasBeenInvoked = true;
								System.out.println(creatureObject.trigger[j].description);
							}
						}
					}
				}
			}
		}
	}*/
	
	public boolean checkTrigger(String command, List<String> currentItem, List<mapComponent> map) {
		boolean result = false;
		if (this.type.equals("room")) {
			if (((room) findObject(map, this.name, "room")).hasCommandTrigger(command) != null) {
				result = (((room) findObject(map, this.name, "room")).hasCommandTrigger(command).checkCondition(currentItem, map));
				/*if (result) {
					System.out.println(((room) findObject(map, this.name, "room")).hasCommandTrigger(command).description);
				}*/
			}
		}
		return result;
	}
	
	public List<mapComponent> searchForTrigger(List<mapComponent> map, List<String> inventory, String currentRoom) {
		// begin by searching for item first
		ListIterator<String> itemInspected = ((room) this).item.listIterator();
		boolean trigger = false;
		while (itemInspected.hasNext()) {
			String elementInspected = itemInspected.next();
			if (((zorkItem) findObject(map, elementInspected, "item")).trigger != null) {
				zorkTrigger[] elementTrigger = ((zorkItem) findObject(map, elementInspected, "room")).trigger;
				for (int i = 0; i < elementTrigger.length; i++) {
					if (elementTrigger[i] != null) {
						trigger = elementTrigger[i].checkCondition(inventory, map);
						if (trigger)  {
							if (elementTrigger[i].type == null || elementTrigger[i].type.equals("single")) {
								if (!elementTrigger[i].hasBeenInvoked) {
									System.out.println(elementTrigger[i].description);
									elementTrigger[i].hasBeenInvoked = true;
									map = takeAction(map, elementTrigger[i].action, inventory, currentRoom);
								}
							}
							else {
								System.out.println(elementTrigger[i].description);
								elementTrigger[i].hasBeenInvoked = true;
								map = takeAction(map, elementTrigger[i].action, inventory, currentRoom);
							}
						}
					}
				}
			}
		}
		
		// Then containers
		itemInspected = ((room) this).container.listIterator();
		trigger = false;
		while (itemInspected.hasNext()) {
			String elementInspected = itemInspected.next();
			if (((zorkContainer) findObject(map, elementInspected, "container")).trigger != null) {
				zorkTrigger[] elementTrigger = ((zorkContainer) findObject(map, elementInspected, "container")).trigger;
				for (int i = 0; i < elementTrigger.length; i++) {
					if (elementTrigger[i] != null) {
						trigger = elementTrigger[i].checkCondition(inventory, map);
						if (trigger)  {
							if (elementTrigger[i].type == null || elementTrigger[i].type.equals("single")) {
								if (!elementTrigger[i].hasBeenInvoked) {
									elementTrigger[i].hasBeenInvoked = true;
									map = takeAction(map, elementTrigger[i].action, inventory, currentRoom);
								}
							}
							else {
								elementTrigger[i].hasBeenInvoked = true;
								map = takeAction(map, elementTrigger[i].action, inventory, currentRoom);
							}
						}
					}
				}
			}
		}
		
		// Then creature
		itemInspected = ((room) this).creatures.listIterator();
		trigger = false;
		while (itemInspected.hasNext()) {
			String elementInspected = itemInspected.next();
			if (((creature) findObject(map, elementInspected, "creature")).trigger != null) {
				zorkTrigger[] elementTrigger = ((creature) findObject(map, elementInspected, "creature")).trigger;
				for (int i = 0; i < elementTrigger.length; i++) {
					if (elementTrigger[i] != null) {
						trigger = elementTrigger[i].checkCondition(inventory, map);
						if (trigger)  {
							if (elementTrigger[i].type == null || elementTrigger[i].type.equals("single")) {
								if (!elementTrigger[i].hasBeenInvoked) {
									elementTrigger[i].hasBeenInvoked = true;
									map = takeAction(map, elementTrigger[i].action, inventory, currentRoom);
								}
							}
							else {
								elementTrigger[i].hasBeenInvoked = true;
								map = takeAction(map, elementTrigger[i].action, inventory, currentRoom);
							}
						}
					}
				}
			}
		}
		
		return map;
	}
}
