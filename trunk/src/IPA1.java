import java.util.*;
import java.io.*;

public class IPA1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String xmlFile = args[0];
			List<mapComponent> mapContainer = new ArrayList<mapComponent>();
			new XMLReader(mapContainer, xmlFile);
			if (args.length > 1 && args[1].equals("debug")) {
				ListIterator<mapComponent> iter = mapContainer.listIterator();
				while (iter.hasNext()) {
					//System.out.println(((room) iter.next()).description);
					String componentType = (iter.next().type);
					iter.previous();
					if (componentType.equals("room")) {
						((room)iter.next()).info();
					}
					else if (componentType.equals("item")) {
						((zorkItem)iter.next()).info();
					}
					else if (componentType.equals("container")) {
						((zorkContainer)iter.next()).info();
					}
					else if (componentType.equals("creature")) {
						((creature)iter.next()).info();
					}
					else {
						System.out.println("<<< Unrecognized map component");
					}
				}
			}
			
			playGame(mapContainer);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Insufficient argument");
		}
	}

	private static void playGame(List<mapComponent> mapContainer) {
		String currentRoom = null;
		List<String> currentItem = new ArrayList<String>();
		String command = null;
		boolean exitFound = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String patternMove = "^[nsew]$";
		String patternInventory = "^[i]$";
		String patternTake = "^(take).*";
		String patternOpen = "^(open).*";
		String patternRead = "^(read).*";
		String patternDrop = "^(drop).*";
		String patternPut = "^(put).*";
		String patternTurnOn = "^(turn[ ]?on).*";
		String patternAttack = "^(attack).*";
		String patternQuit = "^(exit|quit|stop)$";
		System.out.println("Game commencing!");
		boolean overridden = false;
		
		currentRoom = zorkCheckEntrance(mapContainer);
		
		while (!exitFound) {
			System.out.print("> ");
			try {
				command = br.readLine();
				//System.out.println("Command caught: " + command);
				if (command.matches(patternMove)) {
					overridden = ((room) findObject(mapContainer, currentRoom, "room")).checkTrigger(command, currentItem, mapContainer);
					if (!overridden) {
						if (((room) findObject(mapContainer, currentRoom, "room")).zorkMove(command) == null) {
							System.out.println("Can't go there [" + command + "]");
						}
						else {
							currentRoom = ((room) findObject(mapContainer, currentRoom, "room")).zorkMove(command);
							System.out.println(((room) findObject(mapContainer, currentRoom, "room")).description);
						}
					}
					/*String transition = zorkMove(mapContainer, currentRoom, currentItem, command);
					
					if (transition.equals("transition error")) {
						System.out.println("Can't go that way.");
					}
					else {
						currentRoom = transition;
						System.out.println(" -->" + currentRoom);
					}*/
				}
				else if (command.matches(patternInventory)) {
					System.out.println("Inventory: " + Arrays.toString(currentItem.toArray()));
				}
				else if (command.matches(patternTake)) {
					if (command.split(" ").length != 2) {
						System.out.println("Incorrect command. Usage: take [item]");
					}
					else {
						boolean itemFound = false;
						/*String[] itemList = ((room) findObject(mapContainer, currentRoom, "room")).item;
						for (int i = 0; i < itemList.length && !itemFound; i++) {
							if ((command.split(" ")[1]).equals(itemList[i])) {
								currentItem.add(command.split(" ")[1]);
								((room) findObject(mapContainer, currentRoom, "room")).item.
								itemFound = true;
							}
						}*/
						if (((room) findObject(mapContainer, currentRoom, "room")).item.contains(command.split(" ")[1])) {
							currentItem.add(command.split(" ")[1]);
							((room) findObject(mapContainer, currentRoom, "room")).item.remove(command.split(" ")[1]);
							itemFound = true;
						}
						/*String[] containerList = ((room) findObject(mapContainer, currentRoom, "room")).container;
						for (int i = 0; i < containerList.length && !itemFound; i++) {
							/*if (containerList[i] != null && containerList[i].equals(command.split(" ")[1])) {
								itemFound = true;
								if (!((zorkContainer) findObject(mapContainer, containerList[i], "container")).take(currentItem)) {
									System.out.println("Can't take item from unopened container");
								}
							}*/
							/*if (containerList[i] != null 
									&& ((zorkContainer) findObject(mapContainer, containerList[i], "container")).item.equals(command.split(" ")[1])) {
								if (!((zorkContainer) findObject(mapContainer, containerList[i], "container")).take(currentItem)) {
									System.out.println("Can't take item from unopened container");
								}
								else {
									itemFound = true;
								}
							}
						}*/
						else {
							List<String> containersInRoom = ((room) findObject(mapContainer, currentRoom, "room")).container;
							ListIterator<String> i = containersInRoom.listIterator();
							while (i.hasNext() && !itemFound) {
								String inspection = i.next();
								if (((zorkContainer) findObject(mapContainer, inspection, "container")).item != null
										&& ((zorkContainer) findObject(mapContainer, inspection, "container")).item.equals(command.split(" ")[1])) {
									if (!((zorkContainer) findObject(mapContainer, inspection, "container")).take(currentItem)) {
										System.out.println("Can't take item from unopened container");
									}
									else {
										itemFound = true;
									}
								}
							}
						}
						if (!itemFound) {
							System.out.println("Item [" 
									+ command.split(" ")[1]
									+ "] does not exist in room ["
									+ currentRoom);
						}
						else {
							System.out.println("Item [" 
									+ command.split(" ")[1] 
									+ "] added to inventory");
						}
					}
				}
				else if (command.matches(patternOpen)) {
					if (command.split(" ").length != 2) {
						System.out.println("Incorrect command. Usage: open [target]");
					}
					else if (command.trim().equals("open exit")) {
						if (((room) findObject(mapContainer, currentRoom, "room")).roomType != null
								&& ((room) findObject(mapContainer, currentRoom, "room")).roomType.equals("exit")) {
							exitFound = true;
						}
					}
					else if (!((room) findObject(mapContainer, currentRoom, "room")).contains("container", command.split(" ")[1])) {
						System.out.println("No such container in room " + currentRoom);
					}
					else {
						//System.out.println("opening " + command.split(" ")[1]);
						((zorkContainer) findObject(mapContainer, command.split(" ")[1], "container")).open();
					}
				}
				else if (command.matches(patternRead)) {
					if (command.split(" ").length != 2) {
						System.out.println("Incorrect command. Usage: read [inventory]");
					}
					else {
						if (currentItem.contains(command.split(" ")[1])) {
							System.out.println(((zorkItem) findObject(mapContainer, command.split(" ")[1], "item")).writing);
						}
						else {
							System.out.println("No such item in inventory");
						}
					}
				}
				else if (command.matches(patternDrop)) {
					if (command.split(" ").length != 2) {
						System.out.println("Incorrect command. usage: drop [item]");
					}
					else {
						((room) findObject(mapContainer, currentRoom, "room")).dropItem(currentItem, command.split(" ")[1]);
					}
				}
				else if (command.matches(patternPut)) {
					if (command.split(" ").length != 4) {
						System.out.println("Incorrect command. Usage: put [item] in [target]");
					}
					else {
						if (((room) findObject(mapContainer, currentRoom, "room")).contains("container", command.split(" ")[3])) {
							mapContainer = ((zorkContainer) findObject(mapContainer, command.split(" ")[3], "container")).put(mapContainer, currentItem, command.split(" ")[1]);
						}
						else {
							System.out.println("No such container");
						}
					}
				}
				else if (command.matches(patternTurnOn)) {
					if (command.split(" ").length != 3) {
						System.out.println("Incorrect command. Usage: turn on [item]");
					}
					else {
						if (currentItem.contains(command.split(" ")[2])) {
							System.out.println("You activated the " + command.split(" ")[2]);
							mapContainer = ((zorkItem) findObject(mapContainer, command.split(" ")[2], "item")).activate(mapContainer);
						}
						else {
							System.out.println("Can't turn on non-inventorized item [" + command.split(" ")[2] + "]");
						}
					}
				}
				else if (command.matches(patternAttack)) {
					//System.out.println("attacking monster...");
					if (command.split(" ").length != 4) {
						System.out.println("Incorrect command. Usage: attack [target] with [object]");
					}
					else {
						if (((room) findObject(mapContainer, currentRoom, "room")).contains("creature", command.split(" ")[1])) {
							//System.out.println("attacking...");
							if (currentItem.contains(command.split(" ")[3])) {
								((creature) findObject(mapContainer, command.split(" ")[1], "creature")).attack(mapContainer, currentItem, command.split(" ")[3]);
							}
							else {
								System.out.println("Item " + command.split(" ")[3] + " is not in your inventory");
							}
						}
						else {
							System.out.println("No such creature in room " + currentRoom);
						}
					}
				}
				else if (command.matches(patternQuit)) {
					System.out.println("Exiting game...");
					System.out.println("Goodbye!");
					break;
				}
				else {
					System.out.println("Unimplemented or unrecognized command");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Input recognition exception caught: ");
				e.printStackTrace();
			}
		}
		if (exitFound) {
			System.out.println("Game Over");
		}
	}

	private static String zorkCheckEntrance(List<mapComponent> mapContainer) {
		// Explicitly changes the game state
		boolean isFound = false;
		ListIterator<mapComponent> iterator = mapContainer.listIterator();
		while (iterator.hasNext() && !isFound) {
			if (((mapComponent) iterator.next()).name.equals("Entrance")) {
				iterator.previous();
				System.out.println(((room) iterator.next()).description);
				isFound = true;
			}
		}
		
		if (!isFound) {
			System.out.println("Room \"Entrance\" is not found in the given XML map");
			System.exit(0);
		}
		return "Entrance";
	}

	private static String zorkMove(List<mapComponent> mapContainer,
			String currentRoom, String[] currentItem, String command) {
		boolean isFound = false;
		ListIterator<mapComponent> iterator = mapContainer.listIterator();
		String nextLocation = null;
		String direction = null;
		switch (command.charAt(0)) {
		case 'n': direction = "north"; break;
		case 's': direction = "south"; break;
		case 'e': direction = "east"; break;
		case 'w': direction = "west"; break;
		}
		
		System.out.println("Current room: " + currentRoom);
		while (iterator.hasNext() && !isFound) {
			if (((mapComponent) iterator.next()).name.equals(currentRoom)) {
				iterator.previous();
				//System.out.println("Borders for room " + ((mapComponent) iterator.next()).name);
				//iterator.previous();
				zorkBorder[] currentBorder = ((room) iterator.next()).border;
				/*for (int i = 0; i < currentBorder.length; i++) {
					if (currentBorder[i] != null) {
						System.out.println("getting info for border " 
								+ currentBorder[i].direction
								+ currentBorder[i].name);
					}
				}*/
				for (int i = 0; i < currentBorder.length; i++) {
					if (currentBorder[i] != null && currentBorder[i].direction.equals(direction)) {
						isFound = true;
						nextLocation = currentBorder[i].name;
					}
				}
			}
		}
		
		if (!isFound) {
			return "transition error";
		}
		else {
			System.out.println(((room) findObject(mapContainer, nextLocation, "room")).description);
			return nextLocation;
		}
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
