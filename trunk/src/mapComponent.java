import java.util.*;

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
	
	public unity takeAction(List<mapComponent> map, String[] actionArray, List<String> inventory, String currentRoom) {
		for (int i = 0; i < actionArray.length; i++) {
			if (actionArray[i] != null) {
				String[] btsCommand = actionArray[i].split(" ");
				if (btsCommand[0].equals("Update")) {
					// Syntax: Update (...) to (...)
					//System.out.println("update called");
					if ((zorkItem) findObject(map, btsCommand[1], "item") != null) {
						//System.out.println("called here");
						((zorkItem) findObject(map, btsCommand[1], "item")).status = btsCommand[3];
						//System.out.println(((zorkItem) findObject(map, btsCommand[1], "item")).status);
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
					if (btsCommand[3].equals("inventory")) {
						inventory.add(btsCommand[1]);
					}
					else if (((room) findObject(map, btsCommand[3], "room")) != null) {
						((room) findObject(map, btsCommand[3], "room")).addItem(btsCommand[1], map);
					}
					// Then search for container, if hasn't found in room
					else if (((zorkContainer) findObject(map, btsCommand[3], "container")) != null) {
						((zorkContainer) findObject(map, btsCommand[3], "container")).addItem(btsCommand[1]);
					}
				}
				else if (btsCommand[0].equals("Delete")) {
					// Syntax: Delete (...)
					
					if (((room) findObject(map, currentRoom, "room")).container.contains(btsCommand[1])) {
						List<String> itemToDelete = ((zorkContainer) findObject(map, btsCommand[1], "container")).item;
						Iterator<String> itd = itemToDelete.iterator();
						while(itd.hasNext()) {
							((room) findObject(map, currentRoom, "room")).item.remove(itd.next());
						}
					}
					
					((room) findObject(map, currentRoom, "room")).creatures.remove(btsCommand[1]);
					((room) findObject(map, currentRoom, "room")).item.remove(btsCommand[1]);
					((room) findObject(map, currentRoom, "room")).container.remove(btsCommand[1]);
					((room) findObject(map, currentRoom, "room")).detachBorder(btsCommand[1]);
					inventory.remove(btsCommand[1]);
					
				}
				else if (actionArray[i].equals("Game Over")) {
					System.out.println("Victory!");
					System.exit(0);
				}
				else if (btsCommand[0].equals("drop")) {
					// Syntax: drop (...)
					if (inventory.contains(btsCommand[1])) {
						inventory.remove(btsCommand[1]);
						((room) findObject(map, currentRoom, "room")).item.add(btsCommand[1]);
					}
				}
				else if (btsCommand[0].equals("take")) {
					if (((room) findObject(map, currentRoom, "room")).item.contains(btsCommand[1])) {
						inventory.add(btsCommand[1]);
						((room) findObject(map, currentRoom, "room")).item.remove(btsCommand[1]);
					}
					else {
						List<String> containerInRoom = ((room) findObject(map, currentRoom, "room")).container;
						Iterator<String> icir = containerInRoom.listIterator();
						while (icir.hasNext()) {
							String currentContainer = icir.next();
							if (((zorkContainer) findObject(map, currentContainer, "container")).item.contains(btsCommand[1])
									&& ((zorkContainer) findObject(map, currentContainer, "currentContainer")).takeAble) {
								inventory.add(btsCommand[1]);
								((zorkContainer) findObject(map, currentContainer, "container")).item.remove(btsCommand[1]);
							}
						}
					}
				}
				else if (btsCommand[0].equals("open")) {
					/*if (btsCommand[0].equals("open exit")) {
						if (((room) findObject(map, currentRoom, "room")).roomType != null
								&& ((room) findObject(map, currentRoom, "room")).roomType.equals("exit")) {
							exitFound = true;
						}
						else {
							System.out.println("Error");
						}
					}*/
					if (!((room) findObject(map, currentRoom, "room")).contains("container", btsCommand[1])) {
						//System.out.println("No such container in room " + currentRoom);
						System.out.println("Error");
					}
					else {
						//System.out.println("opening " + command.split(" ")[1]);
						((zorkContainer) findObject(map, btsCommand[1], "container")).open();
					}
				}
				else if (btsCommand[0].equals("put")) {
					if (((room) findObject(map, currentRoom, "room")).contains("container", btsCommand[3])) {
						map = ((zorkContainer) findObject(map, btsCommand[3], "container")).put(map, inventory, btsCommand[1], currentRoom);
					}
					else {
						//System.out.println("No such container");
						System.out.println("Error");
					}
				}
				else if (btsCommand[0].matches("[nsew]")) {
					String testNextRoom = null;
					testNextRoom = ((room) findObject(map, currentRoom, "room")).zorkMove(btsCommand[0]);
					if ((room) findObject(map, testNextRoom, "room") == null) {
						System.out.println("Error");
					}
					else {
						currentRoom = testNextRoom;
						// Only print room description when it has one
						if (((room) findObject(map, currentRoom, "room")).description != null) {
							System.out.println(((room) findObject(map, currentRoom, "room")).description);
						}
					}
				}
				else if (btsCommand[0].equals("attack")) {
					//System.out.println("attacking monster...");
					if (((room) findObject(map, currentRoom, "room")).contains("creature", btsCommand[1])) {
						//System.out.println("attacking...");
						if (inventory.contains(btsCommand[3])) {
							unity x =
								((creature) findObject(map, btsCommand[1], "creature")).attack(map, inventory, btsCommand[3], currentRoom);
							map = x.map;
							inventory = x.inventory;
							currentRoom = x.currentRoom;
						}
						else {
							//System.out.println("Item " + command.split(" ")[3] + " is not in your inventory");
							System.out.println("Error");
						}
					}
					else {
						//System.out.println("No such creature in room " + currentRoom);
						System.out.println("Error");
					}
				}
				else if (btsCommand[0].equals("i")) {
					if ((inventory.toArray()).length > 0) {
						System.out.println("Inventory: " + (Arrays.toString(inventory.toArray())).replace("[", "").replace("]", ""));
					}
					else {
						System.out.println("Inventory: empty");
					}
				}
				else if (btsCommand[0].equals("read")) {
					if (inventory.contains(btsCommand[1])) {
						if (((zorkItem) findObject(map, btsCommand[1], "item")).writing != null) {
							System.out.println(((zorkItem) findObject(map, btsCommand[1], "item")).writing);
						}
						else {
							//System.out.println("Item " + command.split(" ")[1] + " has no description.");
							System.out.println("Nothing written.");
						}
					}
					else {
						//System.out.println("No such item in inventory");
						System.out.println("Error");
					}
				}
				else if (btsCommand[0].equals("turn")) {
					if (inventory.contains(btsCommand[2])
							&& ((zorkItem) findObject(map, btsCommand[2], "item")).turnon != null) {
						unity x = ((zorkItem) findObject(map, btsCommand[2], "item")).activate(map, inventory, currentRoom);
						map = x.map;
						inventory = x.inventory;
					}
					else {
						//System.out.println("Can't turn on non-inventorized item [" + command.split(" ")[2] + "]");
						System.out.println("Error");
					}
				}
			}
		}
		//System.out.println("<<< re-looping");
		unity x = findObject(map, currentRoom, "room").searchForTrigger(map, inventory, currentRoom);
		x.currentRoom = currentRoom;
		//System.out.println(">>> re-looping done");
		return x;
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
		else if (this.type.equals("creature")) {
			if (((creature) findObject(map, this.name, "creature")).hasCommandTrigger(command) != null) {
				result = (((creature) findObject(map, this.name, "creature")).hasCommandTrigger(command).checkCondition(currentItem, map));
			}
		}
		return result;
	}
	
	public unity searchForTrigger(List<mapComponent> map, List<String> inventory, String currentRoom) {
		// begin by searching for item first
		unity x = new unity(map, inventory, currentRoom);
		ListIterator<String> itemInspected = ((room) this).item.listIterator();
		boolean trigger = false;
		while (itemInspected.hasNext()) {
			String elementInspected = itemInspected.next();
			if (((zorkItem) findObject(map, elementInspected, "item")) != null
					&& ((zorkItem) findObject(map, elementInspected, "item")).trigger != null) {
				zorkTrigger[] elementTrigger = ((zorkItem) findObject(map, elementInspected, "room")).trigger;
				for (int i = 0; i < elementTrigger.length; i++) {
					if (elementTrigger[i] != null) {
						trigger = elementTrigger[i].checkConditionQuiet(inventory, map);
						if (trigger)  {
							if (elementTrigger[i].type == null || elementTrigger[i].type.equals("single")) {
								if (!elementTrigger[i].hasBeenInvoked) {
									if (elementTrigger[i].command == null) {
										//System.out.println(elementTrigger[i].description);
										elementTrigger[i].hasBeenInvoked = true;
										x = takeAction(map, elementTrigger[i].action, inventory, currentRoom);
									}
								}
							}
							else {
								if (elementTrigger[i].command == null) {
									//System.out.println(elementTrigger[i].description);
									elementTrigger[i].hasBeenInvoked = true;
									x = takeAction(map, elementTrigger[i].action, inventory, currentRoom);
								}
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
						trigger = elementTrigger[i].checkConditionQuiet(inventory, map);
						if (trigger)  {
							if (elementTrigger[i].type == null || elementTrigger[i].type.equals("single")) {
								if (!elementTrigger[i].hasBeenInvoked) {
									if (elementTrigger[i].command == null) {
										//System.out.println(elementTrigger[i].description);
										elementTrigger[i].hasBeenInvoked = true;
										x = takeAction(map, elementTrigger[i].action, inventory, currentRoom);
									}
								}
							}
							else {
								if (elementTrigger[i].command == null) {
									//System.out.println(elementTrigger[i].description);
									elementTrigger[i].hasBeenInvoked = true;
									x = takeAction(map, elementTrigger[i].action, inventory, currentRoom);
								}
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
			//System.out.println("inspect" + elementInspected); // TODO
			if (((creature) findObject(map, elementInspected, "creature")).trigger != null) {
				zorkTrigger[] elementTrigger = ((creature) findObject(map, elementInspected, "creature")).trigger;
				for (int i = 0; i < elementTrigger.length; i++) {
					if (elementTrigger[i] != null) {
						trigger = elementTrigger[i].checkConditionQuiet(inventory, map);
						if (trigger)  {
							if (elementTrigger[i].type == null || elementTrigger[i].type.equals("single")) {
								if (!elementTrigger[i].hasBeenInvoked) {
									//System.out.println("HAH! " + elementTrigger[i].command);
									if (elementTrigger[i].command == null) {
										//System.out.println(elementTrigger[i].description);
										elementTrigger[i].hasBeenInvoked = true;
										x = takeAction(map, elementTrigger[i].action, inventory, currentRoom);
									}
								}
							}
							else {
								if (elementTrigger[i].command == null) {
									//System.out.println(elementTrigger[i].description);
									elementTrigger[i].hasBeenInvoked = true;
									x = takeAction(map, elementTrigger[i].action, inventory, currentRoom);
								}
							}
						}
					}
				}
			}
		}
		return x;
		
	}
}
