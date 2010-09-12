import java.util.List;

public class zorkContainer extends mapComponent {
	public String description;
	public String item;
	public String accept;
	public String status;
	protected zorkTrigger[] trigger;
	public boolean takeAble = false;
	
	public zorkContainer(String n, String t, String d, String i, String a, String s, zorkTrigger[] zt) {
		super(n, t);
		description = d;
		item = i;
		accept = a;
		status = s;
		trigger = zt;
		takeAble = false;
	}
	
	public void info() {
		super.info();
		System.out.println("Description: " + description);
		System.out.println("Contains: " + item);
		System.out.println("Status: " + status);
		System.out.println("Accepts: " + accept);
		for (int i = 0; i < trigger.length; i++) {
			if (trigger[i] != null) {
				trigger[i].info();
			}
		}
		System.out.println("=================");
	}
	
	public void addItem(String newItem) {
		this.item = newItem;
	}
	
	public void open() {
		this.takeAble = true;
		System.out.println(this.name + " contains " + this.item);
	}
	
	public boolean take(List<String> inventory) {
		if (this.takeAble == true) {
			inventory.add(this.item);
			this.item = null;
			return true;
		}
		return false;
	}
	
	public void put(List<String> inventory, String newItem) {
		if (this.item == null) {
			this.item = newItem;
			if (!inventory.remove(newItem)) {
				System.out.println(newItem + " is not in your inventory");
			}
			else {
				System.out.println("Item " + newItem + " added to " + this.name);
			}
		}
		else if (this.item != null) {
			System.out.println("Container " + this.name + " is not empty");
		}
	}
}
