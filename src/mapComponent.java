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
	
	public void searchForTrigger(List<mapComponent> map, String seek, String status) {
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
							System.out.println(roomObject.trigger[j].description);
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
							System.out.println(itemObject.trigger[j].description);
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
							System.out.println(contObject.trigger[j].description);
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
							System.out.println(creatureObject.trigger[j].description);
						}
					}
				}
			}
		}
		/*for (int j = 0; j < objectType.length; j++) {
			if (findObject(map, seek, objectType[j]) != null && findObject(map, seek, objectType[j]).name.equals(seek)) {
				if (j == 0) {
					room tRoom = (room) findObject(map, seek, objectType[j]);
					for (int i = 0; i < tRoom.trigger.length; i++) {
						if (tRoom.trigger[i] != null && tRoom.trigger[i].condition.object != null) {
							if (tRoom.trigger[i].condition.object.equals(anObject))
						}
					}
				}
			}
		}*/
	}
}
