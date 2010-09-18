import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public class XMLReader {
	public XMLReader(List<mapComponent> mapContainer, String fileLocation) {
		try {
			File file = new File(fileLocation);
			DocumentBuilder builder =
				DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(file);
			
			// Normalize XML file
			doc.getDocumentElement().normalize();
			
			// Traverse "map" children
			Node map = doc.getElementsByTagName("map").item(0);
			Node child = map.getFirstChild();
			while (child != null) {
				if (child.getNodeType() != Node.TEXT_NODE) {
					NodeList subchild = child.getChildNodes();
					/*System.out.println("Traversing: " 
										+ child.getNodeName()
										+ " "
										+ child.getTextContent());*/
					if (child.getNodeName().equals("room")) {
						instantiateRoom(subchild, mapContainer);
					}
					else if (child.getNodeName().equals("item")) {
						instantiateItem(subchild, mapContainer);
					}
					else if (child.getNodeName().equals("container")) {
						instantiateContainer(subchild, mapContainer);
					}
					else if (child.getNodeName().equals("creature")) {
						instantiateCreature(subchild, mapContainer);
					}
				}
				child = child.getNextSibling();
			}
		}
		catch (Exception e) {
			System.out.println("User defined exception caught: ");
			e.printStackTrace();
		}
	}

	private void instantiateCreature(NodeList parent,
			List<mapComponent> mapContainer) {
		creature temp = new creature(XMLGetValue(parent, "name")
						, ((Node) parent).getNodeName()
						, XMLGetValue(parent, "vulnerability")
						, XMLGetValue(parent, "status")
						, XMLAttachAttack(parent)
						, XMLAttachTrigger(parent));
		mapContainer.add(temp);
		
	}

	private void instantiateItem(NodeList parent,
			List<mapComponent> mapContainer) {
		zorkItem tempItem = new zorkItem(XMLGetValue(parent, "name")
							, ((Node) parent).getNodeName()
							, XMLGetValue(parent, "writing")
							, XMLGetValue(parent, "status")
							, XMLAttachTurnOn(parent)
							, XMLAttachTrigger(parent));
		mapContainer.add(tempItem);
		
	}

	private void instantiateContainer(NodeList parent,
			List<mapComponent> mapContainer) {
		//System.out.println("Adding " + XMLGetValue(parent, "name"));
		zorkContainer tempContainer = new zorkContainer(XMLGetValue(parent, "name")
									, ((Node) parent).getNodeName()
									, XMLGetValue(parent, "description")
									, XMLGetList(parent, "item")
									, XMLGetValue(parent, "accept")
									, XMLGetValue(parent, "status")
									, XMLAttachTrigger(parent));
		mapContainer.add(tempContainer);
	}

	private void instantiateRoom(NodeList parent, List<mapComponent> mapContainer) {
		// Parse XML "room" element
		room tempRoom = new room(XMLGetValue(parent, "name")
								, ((Node) parent).getNodeName()
								, XMLGetValue(parent, "type")
								, XMLGetValue(parent, "description")
								, XMLGetValue(parent, "status")
								, XMLGetList(parent, "item")
								, XMLGetList(parent, "container")
								, XMLGetBorder(parent)
								, XMLAttachTrigger(parent)
								, XMLGetList(parent, "creature"));
		mapContainer.add(tempRoom);
	}
	
	private attack XMLAttachAttack(NodeList parent) {
		// Attach attack to creature
		attack creatureAttack = null;
		zorkCondition creatureCondition = null;
		String print = null;
		String[] action = new String[10];
		int actionCount = 0;
		boolean hasAttack = false;
		
		for (int i = 0; i < parent.getLength() && !hasAttack; i++) {
			if (parent.item(i).getNodeType() != Node.TEXT_NODE
					&& (parent.item(i).getNodeName().equals("attack"))) {
				hasAttack = true;
				NodeList attackNode = parent.item(i).getChildNodes();
				for (int j = 0; j < attackNode.getLength(); j++) {
					if (attackNode.item(j).getNodeName().equals("condition")) {
						creatureCondition = XMLAttachCondition(attackNode.item(j).getChildNodes());
					}
					else if (attackNode.item(j).getNodeName().equals("print")) {
						print = attackNode.item(j).getTextContent();
					}
					else if (attackNode.item(j).getNodeName().equals("action")) {
						action[actionCount++] = attackNode.item(j).getTextContent();
					}
				}
				creatureAttack = new attack(creatureCondition, print, action);
			}
		}
		return creatureAttack;
	}
	
	private zorkTurnOn XMLAttachTurnOn(NodeList parent) {
		// Attach turnon to item
		String tPrint = null;
		String tAction[] = new String[10];
		int actionCounter = 0;
		zorkTurnOn to = null;
		boolean hasTurnOn = false;
		
		for (int i = 0; i < parent.getLength() && !hasTurnOn; i++) {
			if (parent.item(i).getNodeType() != Node.TEXT_NODE
					&& (parent.item(i).getNodeName().equals("turnon"))) {
				hasTurnOn = true;
				NodeList turnOnNode = parent.item(i).getChildNodes();
				for (int j = 0; j < turnOnNode.getLength(); j++) {
					if (turnOnNode.item(j).getNodeType() != Node.TEXT_NODE) {
						if (turnOnNode.item(j).getNodeName().equals("print")) {
							tPrint = turnOnNode.item(j).getTextContent();
						}
						else if (turnOnNode.item(j).getNodeName().equals("action")) {
							tAction[actionCounter++] = turnOnNode.item(j).getTextContent();
						}
					}
				}
				to = new zorkTurnOn(tPrint, tAction);
			}
		}
		return to;
	}
	
	private zorkTrigger[] XMLAttachTrigger(NodeList parent) {
		// Attach trigger to room/item.
		int triggerCount = 0;
		String tType = null;
		String tCommand = null;
		String[] tAction = new String[10];
		int actionCount = 0;
		String tPrint = null;
		zorkTrigger[] temp = new zorkTrigger[10];
		zorkCondition[] cond = new zorkCondition[10];
		int conditionCount = 0;
		
		for (int i = 0; i < parent.getLength(); i++) {
			if (parent.item(i).getNodeType() != Node.TEXT_NODE
					&& (parent.item(i).getNodeName().equals("trigger"))) {
				NodeList triggerNode = parent.item(i).getChildNodes();
				// Re-initialize variables for different triggers
				cond = new zorkCondition[10];
				conditionCount = 0;
				for (int j = 0; j < triggerNode.getLength(); j++) {
					if (triggerNode.item(j).getNodeType() != Node.TEXT_NODE) {
						if (triggerNode.item(j).getNodeName().equals("type")) {
							tType = triggerNode.item(j).getTextContent();
						}
						else if (triggerNode.item(j).getNodeName().equals("command")) {
							tCommand = triggerNode.item(j).getTextContent();
						}
						else if (triggerNode.item(j).getNodeName().equals("action")) {
							tAction[actionCount++] = triggerNode.item(j).getTextContent();
						}
						else if (triggerNode.item(j).getNodeName().equals("print")) {
							tPrint = triggerNode.item(j).getTextContent();
						}
						else if (triggerNode.item(j).getNodeName().equals("condition")) {
							cond[conditionCount++] = XMLAttachCondition(triggerNode.item(j).getChildNodes());
						}
					}
				}
				temp[triggerCount++] = new zorkTrigger(tType, tCommand, cond, tPrint, tAction);
			}
		}
		return temp;
	}

	private zorkCondition XMLAttachCondition(NodeList condition) {
		// Attach condition to trigger
		String has = null;
		String object = null;
		String owner = null;
		String status = null;
		for (int i = 0; i < condition.getLength(); i++) {
			if (condition.item(i).getNodeName().equals("has")) {
				has = condition.item(i).getTextContent();
			}
			else if (condition.item(i).getNodeName().equals("object")) {
				object = condition.item(i).getTextContent();
			}
			else if (condition.item(i).getNodeName().equals("owner")) {
				owner = condition.item(i).getTextContent();
			}
			else if (condition.item(i).getNodeName().equals("status")) {
				status = condition.item(i).getTextContent();
			}
		}
		
		zorkCondition temp = new zorkCondition(has, object, owner, status);
		
		return temp;
	}

	private zorkBorder[] XMLGetBorder(NodeList parent) {
		// Return 4-direction borders
		zorkBorder[] extracted = new zorkBorder[4];
		int index = 0;
		
		for (int i = 0; i < parent.getLength(); i++) {
			if (parent.item(i).getNodeType() != Node.TEXT_NODE
					&& parent.item(i).getNodeName().equals("border")) {
				NodeList border = parent.item(i).getChildNodes();
				extracted[index++] = new zorkBorder(XMLGetValue(border, "direction")
												, XMLGetValue(border, "name"));
			}
		}
		return extracted;
	}

	private String XMLGetValue(NodeList parent, String seek) {
		// Extract a scalar
		boolean isFound = false;
		String extracted = null;
		
		for (int i = 0; i < parent.getLength() && !isFound; i++) {
			if (parent.item(i).getNodeType() != Node.TEXT_NODE
					&& parent.item(i).getNodeName() == seek) {
				isFound = true;
				extracted = parent.item(i).getTextContent();
			}
		}
		
		return extracted;
	}
	
	private String[] XMLGetArray(NodeList parent, String seek) {
		// Extract multiple items
		String[] extracted = new String[8]; 
		int index = 0;
		
		for (int i = 0; i < parent.getLength(); i++) {
			if (parent.item(i).getNodeType() != Node.TEXT_NODE
					&& parent.item(i).getNodeName().equals(seek)) {
				extracted[index++] = parent.item(i).getTextContent();
			}
			
			if (index > extracted.length) {
				System.out.println("!!! XMLGetArray cannot grab more than "
									+ extracted.length
									+ " " + seek);
				break;
			}
		}
		
		return extracted;
	}

	private List<String> XMLGetList(NodeList parent, String seek) {
		List<String> extracted = new ArrayList<String>();
		
		for (int i = 0; i < parent.getLength(); i++) {
			if (parent.item(i).getNodeType() != Node.TEXT_NODE
					&& parent.item(i).getNodeName().equals(seek)) {
				extracted.add(parent.item(i).getTextContent());
			}
		}
		
		return extracted;
	}
}