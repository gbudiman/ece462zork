import java.util.*;
import java.io.*;

public class IPA1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length >= 1) {
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
			else {
				System.out.println("Please specify map XML file. Usage: java IPA1 <map XML>");
			}
			
		}
		catch (Exception e) {
			System.out.println("User-defined stack-trace");
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
		String patternQuery = "^(q(uery)?)$";
		String patternQuit = "^(exit|quit|stop)$";
		System.out.println("Game commencing!");
		boolean overridden = false;
		
		currentRoom = zorkCheckEntrance(mapContainer);
		
		while (!exitFound) {
			System.out.print("> ");
			try {
				command = br.readLine();
				// Check for command override on all command
				overridden = ((room) findObject(mapContainer, currentRoom, "room")).checkTrigger(command, currentItem, mapContainer);
				//System.out.println("Command caught: " + command);
				
				/*********************************************
				 * Move command. Accepts n|s|e|w ONLY!
				 *********************************************/
				if (command.matches(patternMove)) {
					// Command is not overridden by trigger, so execute command
					if (!overridden) {
						// if direction does not point to a valid room, cancel the command
						if (((room) findObject(mapContainer, currentRoom, "room")).zorkMove(command) == null) {
							System.out.println("Can't go there [" + command + "]");
						}
						else {
							String testNextRoom = null;
							testNextRoom = ((room) findObject(mapContainer, currentRoom, "room")).zorkMove(command);
							if ((room) findObject(mapContainer, testNextRoom, "room") == null) {
								System.out.println("Oops... Room " 
										+ ((room) findObject(mapContainer, currentRoom, "room")).zorkMove(command) 
										+ " no longer exists. You're screwed!");
							}
							else {
								currentRoom = testNextRoom;
								// Only print room description when it has one
								if (((room) findObject(mapContainer, currentRoom, "room")).description != null) {
									System.out.println(((room) findObject(mapContainer, currentRoom, "room")).description);
								}
							}
						}
					}
					// Overridden condition need not be specified
				}
				/*********************************************
				 * Check inventory. Accepts i only
				 *********************************************/
				else if (command.matches(patternInventory)) {
					// Explicit message if inventory is empty.
					if ((currentItem.toArray()).length > 0) {
						System.out.println("Inventory: " + Arrays.toString(currentItem.toArray()));
					}
					else if (!overridden) {
						System.out.println("Inventory is empty");
					}
				}
				/*********************************************
				 * Take item from container or from room.
				 *********************************************/
				else if (command.matches(patternTake)) {
					if (command.split(" ").length != 2) {
						System.out.println("Incorrect command. Usage: take [item]");
					}
					else if (!overridden) {
						boolean itemFound = false;
						// First, check if item exists in current room
						if (((room) findObject(mapContainer, currentRoom, "room")).item.contains(command.split(" ")[1])) {
							currentItem.add(command.split(" ")[1]);
							((room) findObject(mapContainer, currentRoom, "room")).item.remove(command.split(" ")[1]);
							itemFound = true;
						}
						// Branch: look for containers
						else {
							List<String> containersInRoom = ((room) findObject(mapContainer, currentRoom, "room")).container;
							ListIterator<String> i = containersInRoom.listIterator(); // List containers in current room only
							while (i.hasNext() && !itemFound) {
								String inspection = i.next();
								if (((zorkContainer) findObject(mapContainer, inspection, "container")).item != null
										&& ((zorkContainer) findObject(mapContainer, inspection, "container")).item.contains(command.split(" ")[1])) {
									// User shouldn't see this message
									// But this is for information purpose only
									if (!((zorkContainer) findObject(mapContainer, inspection, "container")).take(currentItem, command.split(" ")[1])) {
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
									+ currentRoom + "]");
						}
						else {
							System.out.println("Item [" 
									+ command.split(" ")[1] 
									+ "] added to inventory");
						}
					}
				}
				/*********************************************
				 * Open exit/container
				 *********************************************/
				else if (command.matches(patternOpen)) {
					if (command.split(" ").length != 2) {
						System.out.println("Incorrect command. Usage: open [target]");
					}
					else if (!overridden) {
						if (command.trim().equals("open exit")) {
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
				}
				/*********************************************
				 * Read item description in inventory
				 *********************************************/
				else if (command.matches(patternRead)) {
					if (command.split(" ").length != 2) {
						System.out.println("Incorrect command. Usage: read [inventory]");
					}
					else if (!overridden) {
						if (currentItem.contains(command.split(" ")[1])) {
							if (((zorkItem) findObject(mapContainer, command.split(" ")[1], "item")).writing != null) {
								System.out.println(((zorkItem) findObject(mapContainer, command.split(" ")[1], "item")).writing);
							}
							else {
								System.out.println("Item " + command.split(" ")[1] + " has no description.");
							}
						}
						else {
							System.out.println("No such item in inventory");
						}
					}
				}
				/*********************************************
				 * Drop item to room
				 *********************************************/
				else if (command.matches(patternDrop)) {
					if (command.split(" ").length != 2) {
						System.out.println("Incorrect command. usage: drop [item]");
					}
					else if (!overridden) {
						((room) findObject(mapContainer, currentRoom, "room")).dropItem(currentItem, command.split(" ")[1]);
					}
				}
				/*********************************************
				 * Put item to container
				 *********************************************/
				else if (command.matches(patternPut)) {
					if (command.split(" ").length != 4) {
						System.out.println("Incorrect command. Usage: put [item] in [target]");
					}
					else if (!overridden) {
						if (((room) findObject(mapContainer, currentRoom, "room")).contains("container", command.split(" ")[3])) {
							mapContainer = ((zorkContainer) findObject(mapContainer, command.split(" ")[3], "container")).put(mapContainer, currentItem, command.split(" ")[1], currentRoom);
						}
						else {
							System.out.println("No such container");
						}
					}
				}
				/*********************************************
				 * Activate item in inventory
				 *********************************************/
				else if (command.matches(patternTurnOn)) {
					if (command.split(" ").length != 3) {
						System.out.println("Incorrect command. Usage: turn on [item]");
					}
					else if (!overridden) {
						if (currentItem.contains(command.split(" ")[2])) {
							mapContainer = ((zorkItem) findObject(mapContainer, command.split(" ")[2], "item")).activate(mapContainer, currentRoom);
						}
						else {
							System.out.println("Can't turn on non-inventorized item [" + command.split(" ")[2] + "]");
						}
					}
				}
				/*********************************************
				 * Attack a creature
				 *********************************************/
				else if (command.matches(patternAttack)) {
					//System.out.println("attacking monster...");
					if (command.split(" ").length != 4) {
						System.out.println("Incorrect command. Usage: attack [target] with [object]");
					}
					else if (!overridden) {
						if (((room) findObject(mapContainer, currentRoom, "room")).contains("creature", command.split(" ")[1])) {
							//System.out.println("attacking...");
							if (currentItem.contains(command.split(" ")[3])) {
								mapContainer = 
									((creature) findObject(mapContainer, command.split(" ")[1], "creature")).attack(mapContainer, currentItem, command.split(" ")[3], currentRoom);
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
				else if (command.matches(patternQuery)) {
					System.out.println("Programmer-defined command for inspecting room: ");
					room inspectedRoom = (room) findObject(mapContainer, currentRoom, "room");
					System.out.println("Monsters: " + inspectedRoom.creatures);
					System.out.println("Containers: " + inspectedRoom.container);
					System.out.println("Items: " + inspectedRoom.item);
				}
				else {
					System.out.println("Unimplemented or unrecognized command");
				}
				
				if (!overridden) {
					mapContainer = ((mapComponent) findObject(mapContainer, currentRoom, "room")).searchForTrigger(mapContainer, currentItem, currentRoom);
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
			if ((iterator.next()).name.equals("Entrance")) {
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
