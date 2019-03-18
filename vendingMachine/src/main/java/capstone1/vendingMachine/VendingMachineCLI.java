package capstone1.vendingMachine;

import java.math.BigDecimal;
import capstone1.vendingMachine.menu.Menu;

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

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT_PROGRAM = "Exit Program";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT_PROGRAM };

	private static final String PURCHASE_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] MAIN_MENU_PURCHASE_OPTIONS = { PURCHASE_OPTION_FEED_MONEY,
			PURCHASE_OPTION_SELECT_PRODUCT, PURCHASE_OPTION_FINISH_TRANSACTION };

	BigDecimal total = new BigDecimal(0);

	private Menu menu;

	private static VendingMachine vendingMachine;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		while (true) {

			// The run method will continue to display terminal menu options until the user chooses the "Exit Program" option.
			// The Main Menu consists of:
			// 1) Display Vending Machine Items
			// 2) Purchase
			// 3) Exit Program

			menu.mainMenuBanner();
			
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_EXIT_PROGRAM)) {
				System.out.print("Thank you. Come again!");
				break;
			}

			// Selecting "Display Vending Machine Items" displays current slot inventory for
			// all items
			// EXAMPLE: A1 (Inventory : 5) Potato Crisps 3.05 Chip

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {

				menu.inventoryBanner();

				System.out.print(vendingMachine.inventoryToString());
			}

			// If the user selects the "Purchase" option from the main menu, they will enter
			// into the
			// purchase sub-menu. Here, they have three actions to complete:
			// 1) Insert change into machine
			// 2) Make slot selection
			// 3) Complete transaction and receive change

			if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				while (!choice.equals(PURCHASE_OPTION_FINISH_TRANSACTION)) {

					choice = (String) menu.getChoiceFromOptions(MAIN_MENU_PURCHASE_OPTIONS);

					// User indicates funds they wish to enter into the machine.

					if (choice.equals(PURCHASE_OPTION_FEED_MONEY)) {

						BigDecimal insertedChange = vendingMachine.getInsertedFunds();

						if (insertedChange.compareTo(new BigDecimal(0)) == 1) {
							vendingMachine.setBalance(insertedChange);
						} else {
							System.out.println("Invalid denomination");
						}

						// Tell user of new balance and record into Log.txt audit
						System.out.println("Your total is: $" + vendingMachine.getBalance());
						String stringForLog = vendingMachine.logActionFeedMoney(insertedChange);
						vendingMachine.printToLog(stringForLog);

					}

					// User indicates they wish to make a snack selection.

					if (choice.equals(PURCHASE_OPTION_SELECT_PRODUCT)) {

						String userInput = vendingMachine.getSelectedSlot();
						Slot selectedSlot = vendingMachine.getSlotByName(userInput);

						if (selectedSlot != null) {
							if (selectedSlot.getInventory() > 0) {

								BigDecimal insertedTotal = vendingMachine.getBalance();
								BigDecimal snackPrice = vendingMachine.getMap().get(selectedSlot).getSnackPrice();
								String snackName = vendingMachine.getMap().get(selectedSlot).getSnackName();
								Snack selectedSnack = vendingMachine.getMap().get(selectedSlot);

								// Confirm user's balance is sufficient to make snack selection

								if (insertedTotal.compareTo(snackPrice) >= 0) {

									// Confirm snack selection, subtract from balance total and subtract item from vending machine inventory
									System.out.println("You chose " + snackName + " $" + snackPrice);

									selectedSlot.subtractInventory(1);
									vendingMachine.addSnackToPurchaseList(selectedSnack);

									vendingMachine.subtractFromBalance(snackPrice);
									System.out.println("Your balance is now: $" + vendingMachine.getBalance());

									// Record successful sale to the Log.txt audit file.
									String stringForLog = vendingMachine.logActionChooseSnack(userInput, selectedSlot);
									vendingMachine.printToLog(stringForLog);

									// Update SalesReport.txt file with new updated total sales.
									vendingMachine.updateSalesReport();

								} else {
									System.out.println("Insufficient Funds.");
								}
							} else {
								System.out.print("Sorry, this item is sold out");
							}
						} else {
							System.out.println("Your entered an invalid option");
						}

					}

					// User indicates they wish to complete transaction and receive change (if there
					// is any remaining balance)
					if (choice.equals(PURCHASE_OPTION_FINISH_TRANSACTION)) {

						// User is greeted with each snack sound and purchaseList is cleared of all
						// snack items
						// EXAMPLE: Bag of Chips Sound = "Crunch Crunch, Yum!"
						for (Snack item : vendingMachine.purchaseList) {
							System.out.println(item.getSound());
							System.out.flush();
						}
						vendingMachine.purchaseList.clear();

						// Return change to user and record action to Log.txt audit file.
						BigDecimal balance = vendingMachine.getBalance();
						vendingMachine.getChange(balance);
						String stringForLog = vendingMachine.logChange(balance);
						vendingMachine.printToLog(stringForLog);

					}
				}
			}
		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		vendingMachine = new VendingMachine();
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}

}
