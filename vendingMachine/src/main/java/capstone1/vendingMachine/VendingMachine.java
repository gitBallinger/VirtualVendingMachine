package capstone1.vendingMachine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class VendingMachine {

	Map<Slot, Snack> inventoryMap = new LinkedHashMap<Slot, Snack>();
	List<Snack> purchaseList = new ArrayList<Snack>();

	protected BigDecimal insertedMoney = new BigDecimal(0);

	/*
	 * Welcome to the Virtual Vending Machine!
	 * 
	 * This purpose of this program is to demonstrate OOP fundamentals through an
	 * I/O Java application. This digital vending machine is composed of various
	 * slot and snack objects. Each slot is composed of five inventory spaces and a
	 * slot identifier. In turn, each slot position is paired with a snack object,
	 * which is composed of a name, price, type, and "sound" that displays once item
	 * is purchased.
	 * 
	 * Through the terminal menu system, a user inserts change and chooses a snack.
	 * Once chosen, the machine updates its slot inventory, returns change, and
	 * updates the Log.txt file that tracks all machine transactions. Additionally,
	 * the SalesReport.txt file is updated, which counts all historical sales per
	 * snack and the total amount of sales.
	 * 
	 * Enjoy!
	 */

	/*
	 * Once the virtual vending machine program is started, the VendingMachine is
	 * automatically stocked with slot and snack objects. Each slot contains five
	 * inventory spaces for a single snack type. All snack and slot information is
	 * taken from the vendingmaching.csv file.
	 */

	public VendingMachine() {

		String sourcePath = "vendingmachine.csv";
		File sourceFile = new File(sourcePath);
		if (sourceFile.exists() && sourceFile.isFile()) {

			try (Scanner newScanner = new Scanner(sourceFile)) {
				while (newScanner.hasNextLine()) {
					String line = newScanner.nextLine();
					String[] lineArray = line.split("[|]");
					Slot newSlot = new Slot(lineArray[0]);
					Snack newSnack = new Snack(lineArray[1], lineArray[2], lineArray[3]);

					inventoryMap.put(newSlot, newSnack);
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("This file does not exist.");
		}
	}

	/*
	 * Returns the the slot name, inventory amount, and all details regarding the
	 * snack stored in that slot position.
	 * 
	 * @return A string containing the above information.
	 */

	public String inventoryToString() {
		String result = "";

		for (Map.Entry<Slot, Snack> item : inventoryMap.entrySet()) {
			result += item.getKey() + " " + item.getValue() + "\n";
		}

		return result;
	}

	/*
	 * Before choosing a snack, a user will be prompted to enter change into the
	 * machine. The total amount entered into the machine will be stored in the
	 * insertedMoney variable. The following methods allow for various manipulations
	 * of the insertedMoney balance.
	 */

	/*
	 * Adds to the insertedMoney balance.
	 * @param The amount to be added.
	 */

	public void setBalance(BigDecimal input) {
		insertedMoney = insertedMoney.add(input);
	}

	/*
	 * Resets insertedMoney back to zero.
	 */

	public void resetBalance() {
		insertedMoney = new BigDecimal(0);
	}

	/*
	 * Returns insertedMoney balance.
	 * @return User's current balance.
	 */

	public BigDecimal getBalance() {
		return insertedMoney;
	}

	/*
	 * Subtracts from insertedMoney balance.
	 * @param The amount to be subtracted.
	 */

	public void subtractFromBalance(BigDecimal input) {
		insertedMoney = insertedMoney.subtract(input);
	}

	/*
	 * When purchasing a snack, a user is able to make multiple purchases as long as
	 * their insertedMoney balance is sufficient. The following methods add the
	 * selected snack objects to a list. Once a transaction is completed, the list resets back to zero.
	 * @param The snack object to add to the purchase list.
	 */

	public void addSnackToPurchaseList(Snack snack) {
		purchaseList.add(snack);
	}

	/*
	 * @return User's current selection of snacks.
	 */

	public List<Snack> getPurchaseList() {
		return purchaseList;
	}

	/*
	 * The getMap method returns the inventoryMap object which stores all
	 * VendingMachine slots and snacks. Each map pair holds a slot object in the key
	 * position and a snack object in the value position.
	 * @return VendingMachine's internal inventoryMap which contains slot and snack objects.
	 */

	public Map<Slot, Snack> getMap() {
		return inventoryMap;
	}

	/*
	 * Prompt's the user for their slot selection.
	 * @return User's selected slot as a string.
	 */

	public String getSelectedSlot() {

		@SuppressWarnings("resource")
		Scanner newScanner = new Scanner(System.in);
		System.out.println("Please enter your snack selection Slot ID");
		String userInput = newScanner.nextLine();

		return userInput;
	}

	/*
	 * The getSlotByName method returns the specific slot object associated with a
	 * specific snack name. Each slot is attached to only one snack. Once a user
	 * indicates their slot selection through the getSelectedSlot method, the slot
	 * object can be chosen.
	 * @return Slot name based on snack name
	 * @param The name of the slot.
	 */

	public Slot getSlotByName(String slotName) {

		for (Slot currentSlot : inventoryMap.keySet()) {
			if (slotName.equals(currentSlot.getName())) {
				return currentSlot;
			}
		}
		return null;
	}

	/*
	 * The getInsertedFunds method prompts the user for the amount of funds they
	 * wish to enter into the machine.
	 * @return User's input is returned as a BigDecimal.
	 */

	public BigDecimal getInsertedFunds() {

		BigDecimal userInputToBigDecimal = new BigDecimal(0);

		try {
			@SuppressWarnings("resource")
			Scanner newScanner = new Scanner(System.in);
			System.out.println("Please enter whole dollar amount:  ");
			String userInput = newScanner.nextLine();
			userInputToBigDecimal = new BigDecimal(userInput);

		} catch (NumberFormatException e) {
			System.out.print("Sorry, that input is invalid");
		}

		return userInputToBigDecimal;
	}

	/*
	 * Once a user confirms they are ready to complete their purchase, the getChange
	 * method returns the appropriate amount of change using the least amount of
	 * coins. Afterwards, the insertedMoney balance is returned back to zero.
	 */

	public void getChange(BigDecimal insertedMoney) {

		double quarter = 0.25;
		double nickel = 0.05;
		double dime = 0.10;
		double penny = 0.01;
		double insertedMoneyDouble = insertedMoney.doubleValue();

		// round changeDue to 2 decimal places and calculate the modulus in a hierarchy
		double modQuarters = ((double) ((int) Math.round((insertedMoneyDouble % quarter) * 100)) / 100.0);
		double modDimes = ((double) ((int) Math.round((modQuarters % dime) * 100)) / 100.0);
		double modNickels = ((double) ((int) Math.round((modQuarters % nickel) * 100)) / 100.0);
		double modPennies = ((double) ((int) Math.round((modQuarters % penny) * 100)) / 100.0);

		// count number of coins
		int numQuarters = (int) ((insertedMoneyDouble - modQuarters) / (quarter));
		int numDimes = (int) ((modQuarters - modDimes) / (dime));
		int numNickels = (int) ((modDimes - modNickels) / (nickel));
		int numPennies = (int) ((modNickels - modPennies) / (penny));

		// return information to user
		System.out.println("Your change is: " + numQuarters + " quarters | " + numDimes + " dimes | " + numNickels + " nickels | " + numPennies + " pennies");

		resetBalance();

	}

	/*
	 * Records all vending machine transactions to the Log.txt file. Actions
	 * recorded include: inserting funds, selecting a snack, and returning change.
	 * @param The string record to be added to the Log.txt audit file.
	 */

	public void printToLog(String input) {

		File logAudit = new File("Log.txt");
		FileWriter fileWriter;

		try {
			fileWriter = new FileWriter(logAudit, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(input);
			bufferedWriter.newLine();
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.print("Could not log!");
		}

	}

	/*
	 * The getCurrentDate method is used in conjunction with the printToLog method
	 * to record the time and date of each transaction.
	 * @return The current date as a string. EXAMPLE: 3/02/2019 08:34:19 PM
	 */

	public String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy hh:mm:ss a ");
		String strDate = formatter.format(date);
		return strDate;
	}

	/*
	 * The lotActionFeedMoney method is used in conjunction with the printToLog
	 * method to record the date and amount of funds entered into machine.
	 * @return The current date, action taken (FEED MONEY), and balance before and
	 * after transaction. EXAMPLE: 3/02/2019 08:34:19 PM FEED MONEY: $50 $100
	 */

	public String logActionFeedMoney(BigDecimal userInput) {
		String result;
		result = getCurrentDate() + " FEED MONEY: " + "$" + userInput + "\t" + "$" + getBalance();
		return result;
	}

	/*
	 * The logActionChooseSnack method is used in conjunction with the printToLog
	 * method to record the time, chosen snack, and User's balance before and after
	 * the transaction.
	 * @return The current date, snack name, slot name, and balance before and after
	 * transaction. EXMAPLE: 3/02/2019 08:34:24 PM Stackers A2 $2.50 $1.00
	 */

	public String logActionChooseSnack(String userInput, Slot selectedSlot) {
		String result;
		BigDecimal snackPrice = getMap().get(selectedSlot).getSnackPrice();
		BigDecimal balanceBeforeSelection = getBalance().add(snackPrice);
		String snackName = getMap().get(selectedSlot).getSnackName();
		String slotName = getSlotByName(userInput).getName();
		result = getCurrentDate() + " " + snackName + " " + slotName + " " + "$" + balanceBeforeSelection + "\t" + " $"
				+ getBalance();

		return result;
	}

	/*
	 * The logChange method is used in conjunction with the printToLog method to
	 * record the time, chosen snack, and User's remaining balance.
	 * @return The current date, action taken (GIVE CHANGE), and balance before and
	 * after event. EXAMPLE: 3/02/2019 08:34:29 PM GIVE CHANGE: $3.00 $0
	 */

	public String logChange(BigDecimal balance) {
		String result = "";
		BigDecimal balanceBeforeChange = getBalance().add(balance);
		BigDecimal balanceAfterChange = getBalance();
		result = getCurrentDate() + " GIVE CHANGE: " + "$" + balanceBeforeChange + "\t" + "$" + balanceAfterChange;

		return result;
	}

	/*
	 * The updateSalesReport method scans through the Log.txt file and creates a
	 * tally for each snack's sale. The summary of all snack sales are printed to
	 * the SalesReport.txt file with each snack's total sales delineated by a pipe
	 * symbol - " | ". At the bottom of the file, the total sales for all snack
	 * items is calculated. 
	 * EXAMPLE: 
	 * -----------------------------
	 * Little League Chew | 5 
	 * Chiclets | 10 
	 * Triplemint | 20 
	 * TOTAL SALES: $56.00
	 */

	public void updateSalesReport() {

		deleteCurrentSalesReportIfExists();

		File salesReport = createNewSalesReport();

		File auditLog = getLogAudit();

		String stringForSalesReport = getStringForSnackCountAndTotalSales(auditLog);

		printToSalesReport(salesReport, stringForSalesReport);
	}

	/*
	 * The printToSalesReport method prints a string to the SalesReport.txt file.
	 * @param The SalesReport.txt file.
	 * @param The string input to be entered into he SalesReport.txt.
	 */

	public void printToSalesReport(File salesReport, String input) {
		try {
			FileWriter fileWriter;
			fileWriter = new FileWriter(salesReport, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(input);
			bufferedWriter.newLine();
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.print("Could not log!");
		}
	}

	/*
	 * The deleteCurrentSalesReportIfExists destroys any existing SalesReport.txt
	 * file so that a new one can be created.
	 */

	public void deleteCurrentSalesReportIfExists() {

		File salesReport = new File("SalesReport.txt");

		if (salesReport.exists()) {
			salesReport.delete();
		}
	}

	/*
	 * The createNewSalesReport method creates a new SalesReport.txt file.
	 * @return A new SalesReport.txt file.
	 */

	public File createNewSalesReport() {
		File salesReport = new File("SalesReport.txt");
		return salesReport;
	}

	/*
	 * The getLogAudit method returns the Log.txt audit file.
	 * @return The Log.txt file.
	 */

	public File getLogAudit() {
		File logAudit = new File("Log.txt");
		return logAudit;
	}

	/*
	 * The getStringForSnackCountAndTotalSales method returns the tallied sales for
	 * each snack type and the total sales of all snacks. The returned string from
	 * this mothod is used in conjuction with the printToSaleReport method.
	 * @return The string containing the sell count for each snack and the total
	 * sales of all snacks.
	 */

	public String getStringForSnackCountAndTotalSales(File logAudit) {

		String stringForSalesReport = "";

		BigDecimal totalSales = new BigDecimal(0.00);

		for (Entry<Slot, Snack> itemPair : inventoryMap.entrySet()) {
			Snack snackItem = itemPair.getValue();
			String snackItemName = snackItem.getSnackName();
			int numSnackAppears = 0;

			try (Scanner scanner = new Scanner(logAudit)) {
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					if (line.contains(snackItemName)) {
						numSnackAppears += 1;
						BigDecimal snackPrice = snackItem.getSnackPrice();
						totalSales = totalSales.add(snackPrice);
					} else {
						numSnackAppears += 0;
					}
				}
			} catch (FileNotFoundException e) {
				System.out.print("Sorry, this file is not found");
			}
			stringForSalesReport += snackItemName + " | " + numSnackAppears + "\n";
		}

		stringForSalesReport += "***TOTAL SALES***" + "$" + totalSales;

		return stringForSalesReport;
	}

}
