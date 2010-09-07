import java.util.*;

public class room extends mapComponent {
	// Derived class of room
	public String description;
	private String[] item;
	public zorkBorder[] border;
	public String[] container;
	private zorkTrigger trigger;
	
	public room (String n, String t, String d, String[] i, String[] c, zorkBorder[] b, zorkTrigger trig) {
		super(n, t);
		description = d;
		item = i;
		container = c;
		border = b;
		trigger = trig;
	}
	
	public void info() {
		super.info();
		System.out.println("Description: " + description);
		System.out.println("Item(s): " + Arrays.toString(item));
		System.out.println("Container(s): " + Arrays.toString(container));
		System.out.print("Border(s): ");
		for (int i = 0; i < border.length; i++) {
			if (border[i] != null) {
				System.out.print(border[i].info() + " ");
			}
		}
		System.out.println();
		if (trigger != null) {
			trigger.info();
		}
		System.out.println("=================");
	}
}
