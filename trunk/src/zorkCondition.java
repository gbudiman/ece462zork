public class zorkCondition {
	public String has;
	public String object;
	public String owner;
	public String status;
	
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
}
