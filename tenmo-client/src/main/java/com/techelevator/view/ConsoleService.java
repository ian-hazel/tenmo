package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Scanner;

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
		out.flush();
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
			out.print(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
			out.flush();
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
	
	public Integer appendRequests() {
		Integer result = null;
		do {
			out.println();
			out.println("1: Approve");
			out.println("2: Reject");
			out.println("0: Don't Approve or Reject");
			out.print(System.lineSeparator() + "Please Enter Your Decision >>>");
			out.flush();
			String userInput = in.nextLine();
			try {
				if(userInput.equals("0") || userInput.equals("1") || userInput.equals("2")) {
					result = Integer.parseInt(userInput);
				} 
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}
		
	public Integer printTransferHistory(List<Transfer> transfers) {
		out.println("===================================");
		out.println("Transfer      To/                  ");
		out.println("ID            From           Amount");
		out.println("===================================");
		if(transfers.size() == 0) {
			out.println("\n  You Have No Recorded Transfers");
			out.flush();
			return Integer.valueOf(0);
		} else {
			for(Transfer transfer : transfers) {
				Long id = transfer.getTransferId();
				String username = transfer.getUsername();
				BigDecimal amount = transfer.getAmount();
				out.println(String.format("%-8s", id) + String.format("%-17s", username) 
					+ String.format("%10s", NumberFormat.getCurrencyInstance().format(amount)));
			}
			out.println("===================================");
			Integer result = null;
			do {
				out.print(System.lineSeparator() + "Please enter transfer ID to view details (0 to cancel): ");
				out.flush();
				String userInput = in.nextLine();
				try {
					result = Integer.parseInt(userInput);
				} catch(NumberFormatException e) {
					out.print(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
				}	
			} while(result == null);
			for(Transfer transfer: transfers) {
				if(Long.valueOf(result) == transfer.getTransferId()) {
					return result;
				} 
			} 					
			out.print(System.lineSeparator() + "*** You cannot access transfer #" + result.toString() + " ***" + System.lineSeparator());
			out.flush();
			return 0;	
		}
	}
	
	public Integer requestsMenu(List<Transfer> requests) {
		out.println("=====================================");
		out.println("Transfers                            ");
		out.println("ID        To                   Amount");
		out.println("=====================================");
		if(requests.size() == 0) {
			out.println("\n   You Have No Pending Requests");
			out.flush();
			return Integer.valueOf(0);
		} else {
			for(Transfer request : requests) {
				out.println(String.format("%-10s", request.getTransferId()) + String.format("%-20s", request.getUsername())
				+ String.format("%7s", NumberFormat.getCurrencyInstance().format(request.getAmount())));
			}
			out.println("=====================================");
			Integer result = null;
			do {
				out.print(System.lineSeparator() + "Please enter transfer ID to approve/reject (0 to cancel): ");
				out.flush();
				String userInput = in.nextLine();
				try {
					result = Integer.parseInt(userInput);	
				} catch(NumberFormatException e) {
					out.print(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
				}
			} while(result == null);
			for(Transfer transfer: requests) {
				if(Long.valueOf(result) == transfer.getTransferId()) {
					return result;
				} 
			} 					
			out.print(System.lineSeparator() + "*** You cannot access transfer #" + result.toString() + " ***" + System.lineSeparator());
			out.flush();
			return 0;	
		}
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
				out.print(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(amount == null);
		if(amount.compareTo(BigDecimal.ZERO)< 0) {
			out.print(System.lineSeparator() + "*** " + amount.toString() + " is not valid ***" + System.lineSeparator());
			out.flush();
			return BigDecimal.ZERO;
		} else {
			return amount;
		}
	}
	
	public Integer requestTransfer(List<User> users) {
		out.println("===================================");
		out.println("User                               ");
		out.println("ID       Name                      ");
		out.println("===================================");
		for(User user : users) {
			out.println(String.format("%-10s", user.getId()) + String.format("%-20s", user.getUsername()));
		}
		Integer userId = null;
		do {
			out.print(System.lineSeparator() + "Enter ID of user you are requesting from (0 to cancel): ");
			out.flush();
			String userInput = in.nextLine();
			try {
				userId = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.print(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(userId == null);
		if(userId < 0) {
			out.print(System.lineSeparator() + "*** " + userId.toString() + " is not valid ***" + System.lineSeparator());
			out.flush();
			return 0;
		} else {
			return userId;
		}
	}
	
	public Integer sendTransfer(List<User> users) {
		out.println("===================================");
		out.println("User                               ");
		out.println("ID       Name                      ");
		out.println("===================================");
		for(User user : users) {
			out.println(String.format("%-10s", user.getId()) + String.format("%-20s", user.getUsername()));
		}
		out.print(System.lineSeparator() + "Enter ID of user you are sending to (0 to cancel): ");
		Integer userId = null;
		do {
			out.flush();
			String userInput = in.nextLine();
			try {
				userId = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.print(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(userId == null);
		if(userId < 0) {
			out.print(System.lineSeparator() + "*** " + userId.toString() + " is not valid ***" + System.lineSeparator());
			out.flush();
			return 0;
		} else {
			return userId;
		}
	}
	
	public void transferDetails(Transfer transfer, String fromName, String toName) {
		out.println("===================================");
		out.println("Transfer Details                   ");
		out.println("===================================");
		out.println(String.format("%8s", "ID: ") + String.format("%-20s", transfer.getTransferId()));
		out.println(String.format("%8s", "From: ") + String.format("%-20s", fromName));
		out.println(String.format("%8s", "To: ") + String.format("%-20s", toName));
		out.println(String.format("%8s", "Type: ") + String.format("%-20s", transfer.getType()));
		out.println(String.format("%8s", "Status: ") + String.format("%-20s", transfer.getStatus()));
		out.println(String.format("%8s", "Amount: ") + String.format("%-20s", NumberFormat.getCurrencyInstance().format(transfer.getAmount())));
		out.flush();
	}
	
	public void printBalance(BigDecimal balance) {
		out.println("Your Balance is " + NumberFormat.getCurrencyInstance().format(balance));
		out.flush();
	}
	
	public void printGoodBye() {
		out.println("Have a Nice Day!");
		out.flush();
	}
	
	public void message(String message) {
		out.println(message);
		out.flush();
	}
	
	public void response(String message) {
		out.println("\n" + message);
		out.flush();
	}
	
	public void printHeader() {
		out.println("*********************");
		out.println("* Welcome to TEnmo! *");
		out.println("*********************");
		out.flush();
	}
}
