package capstone1.vendingMachine;

import java.math.BigDecimal;

public class Snack {

	private String snackName;
	private String snackType;
	private BigDecimal snackPrice;

	public Snack(String snackName, String snackPrice, String snackType) {
		this.snackName = snackName;
		this.snackType = snackType;
		this.snackPrice = new BigDecimal(snackPrice);
	}

	public String toString() {
		return getSnackName() + " " + " " + getSnackPrice() + " " + getSnackType();
	}

	// Getters and Setters

	public String getSnackName() {
		return snackName;
	}

	public void setSnackName(String snackString) {

		snackName = snackString;

	}

	public String getSnackType() {
		return snackType;
	}

	public void setSnackType(String typeString) {
		snackType = typeString;
	}

	public void setSnackPrice(String priceString) {
		snackPrice = new BigDecimal(priceString);
	}

	public BigDecimal getSnackPrice() {
		return snackPrice;
	}
	
	public String getSound() {
		
		String sound = null;
		
		if(getSnackType().equals("Chip")) {
			sound = "Crunch Crunch, Yum!";
		}
		if(getSnackType().equals("Candy")) {
			sound = "Munch Munch, Yum!";
		}
		if(getSnackType().equals("Drink")) {
			sound = "Glug Glug, Yum!";
		}
		if(getSnackType().equals("Gum")) {
			sound = "Chew Chew, Yum!";
		}
		
		return sound;
	}

}
