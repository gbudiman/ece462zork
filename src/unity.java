import java.util.*;

public class unity {
	public List<mapComponent> map;
	public List<String> inventory;
	public String currentRoom;
	public unity(List<mapComponent> m, List<String> i, String cr) {
		map = m;
		inventory = i;
		currentRoom = cr;
	}
}