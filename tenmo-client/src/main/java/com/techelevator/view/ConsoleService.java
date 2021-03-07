package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Scanner;

import com.techelevator.tenmo.models.Request;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}
	
	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

		
	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}
	
	public Long appendRequests() {
		Long result = null;
		do {
			out.print("1: Approve");
			out.print("2: Reject");
			out.print("0: Don't Approve or Reject");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Long.parseLong(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result != 0 || result != 1 || result != 2);
		return result;
	}
		
	public Long printTransferHistory(Transfer[] transfers) {
		out.println("===================================");
		out.println("Transfers                          ");
		out.println("ID       From/To             Amount");
		out.println("===================================");
		for(Transfer transfer : transfers) {
			Long id = transfer.getTransferId();
			String type = transfer.getType();
			String fromName = transfer.getFromName();
			String toName = transfer.getToName();
			BigDecimal amount = transfer.getAmount();
			if(type.equals("Send")) {
				out.println(String.format("%-10s", id) + String.format("%6s", "To:") + String.format("%-14", toName) 
				+ String.format("%7s", NumberFormat.getCurrencyInstance().format(amount)));
			} else {
				out.println(String.format("%-10s", id) + String.format("%6s", "From:") + String.format("%-14", fromName) 
				+ String.format("%6s", NumberFormat.getCurrencyInstance().format(amount)));
			}	
		}
		out.println("===================================");
		out.print(System.lineSeparator() + "Please enter transfer ID to view details (0 to cancel)");
		Long result = null;
		do {
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Long.parseLong(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}
	
	public Long requestsMenu(Request[] requests) {
		out.println("=====================================");
		out.println("Transfers                            ");
		out.println("ID        To                   Amount");
		out.println("=====================================");
		for(Request request : requests) {
			out.println(String.format("%-10s", request.getTransferId()) + String.format("%-20s", request.getUsername())
			+ String.format("%7s", NumberFormat.getCurrencyInstance().format(request.getAmount())));
		}
		out.println("=====================================");
		out.print(System.lineSeparator() + "Please enter transfer ID to approve/reject (0 to cancel): ");
		Long result = null;
		do {
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Long.parseLong(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}
	
	public BigDecimal getTransferAmount() {
		BigDecimal amount = null;
		do {
			out.print(System.lineSeparator() + "Enter Transfer Amount: ");
			out.flush();
			String userInput = in.nextLine();
			try {
				amount = new BigDecimal(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(amount == null);
		return amount;
	}
	
	public Long requestTransfer(User[] users) {
		out.println("===================================");
		out.println("User                               ");
		out.println("ID       Name                      ");
		out.println("===================================");
		for(User user : users) {
			out.println(String.format("%-10s", user.getId()) + String.format("%-20s", user.getUsername()));
		}
		out.print(System.lineSeparator() + "Enter ID of user you are requesting from (0 to cancel): ");
		Long userId = null;
		do {
			out.flush();
			String userInput = in.nextLine();
			try {
				userId = Long.parseLong(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(userId == null);
		return userId;
	}
	
	public Long sendTransfer(User[] users) {
		out.println("===================================");
		out.println("User                               ");
		out.println("ID       Name                      ");
		out.println("===================================");
		for(User user : users) {
			out.println(String.format("%-10s", user.getId()) + String.format("%-20s", user.getUsername()));
		}
		out.print(System.lineSeparator() + "Enter ID of user you are sending to (0 to cancel): ");
		Long userId = null;
		do {
			out.flush();
			String userInput = in.nextLine();
			try {
				userId = Long.parseLong(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(userId == null);
		return userId;
	}
	
	public void transferDetails(Transfer transfer) {
		out.println("===================================");
		out.println("Transfer Details                   ");
		out.println("===================================");
		out.println(String.format("%7s", "ID: ") + String.format("%-20", transfer.getTransferId()));
		out.println(String.format("%7s", "From: ") + String.format("%-20", transfer.getFromName()));
		out.println(String.format("%7s", "To: ") + String.format("%-20", transfer.getToName()));
		out.println(String.format("%7s", "Type: ") + String.format("%-20", transfer.getType()));
		out.println(String.format("%7s", "Status: ") + String.format("%-20", transfer.getStatus()));
		out.println(String.format("%7s", "Amount: ") + String.format("%-20", transfer.getAmount()));
		out.flush();
	}
	
	public void printBalance(BigDecimal balance) {
		out.println("Your balance is " + NumberFormat.getCurrencyInstance().format(balance));
	}
	
	public void printGoodBye() {
		out.println("Have a nice day!");
		out.flush();
	}
	
	public void message(String message) {
		out.println(message);
		out.flush();
	}
	
	public void printHello() {
		out.println("*********************");
		out.println("* Welcome to TEnmo! *");
		out.println("*********************");
		out.flush();
	}
}
