package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
	private AccountService accountService;
	private TransferService transferService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService(API_BASE_URL), new TransferService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService, TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.transferService = transferService;
	}

	public void run() {
		console.printHeader();
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		console.message(NumberFormat.getCurrencyInstance().format(accountService.getBalance(currentUser)));	
	}

	private void viewTransferHistory() {
		// TODO: move some to console service to streamline
		System.out.println("---------------------------------------------");
		System.out.println("Transfers                                    ");
		System.out.println("ID            From/To                  Amount");
		System.out.println("---------------------------------------------");
		Transfer[] transfers = transferService.getTransferHistory(currentUser);
		for (Transfer transfer : transfers) {
			if (transfer.getUserFromId() == (long)currentUser.getUser().getId()) {
				System.out.println(transfer.getTransferId() + "          "
								+ "To:  " + transferService.getUserById(transfer.getUserToId(),currentUser).getUsername()
								+ "                 " + transfer.getAmount());				
			}
			else {
				System.out.println(transfer.getTransferId() + "          "
								+ "From:  " + transferService.getUserById(transfer.getUserFromId(),currentUser).getUsername()
								+ "                 " + transfer.getAmount());
			}
		}
		System.out.println("---------");
		Long transferId = (long)console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");
		if (transferId != 0) {
			System.out.println("---------------------------------------------");
			System.out.println("Transfer Details");
			System.out.println("---------------------------------------------");
			Transfer transfer = transferService.getTransferDetails(currentUser, transferId);
			System.out.println("Id: " + transfer.getTransferId());
			System.out.println("From: " + transferService.getUserById(transfer.getUserFromId(),currentUser).getUsername());
			System.out.println("To: " + transferService.getUserById(transfer.getUserToId(),currentUser).getUsername());
			System.out.println("Type: " + transfer.getType().toString());
			System.out.println("Status: " + transfer.getStatus().toString());
			System.out.println("Amount: " + NumberFormat.getCurrencyInstance().format(transfer.getAmount()));
		}
	}
	
	// STUB could be implemented into above method
	private void viewTransferDetails() {
		System.out.println("---------------------------------------------");
		System.out.println("Transfer Details");
		System.out.println("---------------------------------------------");
		Long transferId = null;
		Transfer transfer = transferService.getTransferDetails(currentUser, transferId);
	}

	private void viewPendingRequests() {
		//TODO: move formatting/display header to console service/make pretty
		Transfer[] pendingRequests = transferService.getPendingRequests(currentUser);
		if (pendingRequests.length != 0) {
			System.out.println("---------------------------------------------");
			System.out.println("Pending Transfer                             ");
			System.out.println("ID          To                   Amount      ");
			System.out.println("---------------------------------------------");
			
			for (Transfer pending : pendingRequests) {
				System.out.println(pending.getTransferId() + "           " + transferService.getUserById(pending.getUserFromId(),currentUser).getUsername() + "           " + pending.getAmount());
			}
			System.out.println("----------");
			Long transferId = (long)console.getUserInputInteger("Please enter transfer ID to approve/reject (0 to cancel):");
			
			if (transferId != 0) {
				approveOrRejectRequest(transferId);		
			}
		}
		else {
			System.out.println("No pending requests!");
		}
	}
	
	private void approveOrRejectRequest(Long transferId) {
		System.out.println("1: Approve");
		System.out.println("2: Reject");
		System.out.println("0: Don't approve or reject");
		System.out.println("----------");
		int userSelection = console.getUserInputInteger("Please choose an option:");
		// TODO: if user chooses an option beyond 1, 2, 0 will program crash?
		if (userSelection == 1) {
			// TODO: needs to update request to approved and try to transfer funds
			String outcome = transferService.approveRequest(transferService.getTransferDetails(currentUser, transferId), currentUser);
			System.out.println(outcome);
		}
		else if (userSelection == 2) {
			String outcome = transferService.rejectRequest(transferService.getTransferDetails(currentUser, transferId), currentUser);
			System.out.println(outcome);
		}
		else if (userSelection != 0) {
			System.out.println("Invalid input!");
		}
	}

	private void sendBucks() {
//		TODO: move formatting/display header to console service/make pretty
		System.out.println("---------------------------------------------");
		System.out.println("Users                                        ");
		System.out.println("ID               Name                        ");
		System.out.println("---------------------------------------------");
		
		User[] allUsers = transferService.getAllUsers(currentUser);
		for (User user : allUsers) {
			System.out.println(user.getId() + "           " + user.getUsername() + "           ");
		}
		System.out.println("----------");
		System.out.println();
		Long userToId = (long)console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel):");
		if (userToId != 0) {
			BigDecimal amount = BigDecimal.valueOf(console.getUserInputInteger("Enter amount:"));
			
			String outcome = transferService.sendTransfer(amount, userToId, currentUser);
			System.out.println(outcome);			
		}
	}

	private void requestBucks() {
//		TODO: move formatting/display header to console service/make pretty
		System.out.println("---------------------------------------------");
		System.out.println("Users                                        ");
		System.out.println("ID               Name                        ");
		System.out.println("---------------------------------------------");
		
		User[] allUsers = transferService.getAllUsers(currentUser);
		for (User user : allUsers) {
			System.out.println(user.getId() + "           " + user.getUsername() + "           ");
		}
		System.out.println("----------");
		System.out.println();
		Long userToId = (long)console.getUserInputInteger("Enter ID of user you are requesting from (0 to cancel):");
		BigDecimal amount = BigDecimal.valueOf(console.getUserInputInteger("Enter amount:"));
		
		transferService.requestTransfer(amount, userToId, currentUser);
	}
	
	private void exitProgram() {
		console.printGoodBye();
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}

}
