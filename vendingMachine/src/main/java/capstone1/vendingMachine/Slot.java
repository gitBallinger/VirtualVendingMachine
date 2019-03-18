package capstone1.vendingMachine;

public class Slot {

	private String slotName;
	private int inventory = 5;
	
	public Slot(String name) {
		this.slotName = name;
	}
	
	public String toString() {
		return slotName + " (Inventory : " + getInventory() + ")";
	}
	
	//Getters Setters
	
	public String getName() {
		return slotName;
	}
	
	public void setSlot(String name) {
		slotName = name;
	}

	public int getInventory() {
		return inventory;
	}
	
	public void setInventory(int newAmount) {
		inventory = newAmount;
	}
	
	public void subtractInventory(int newAmount) {
		inventory -= newAmount;
	}
	
	
}
