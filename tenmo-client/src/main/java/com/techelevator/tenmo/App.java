package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

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
		List<Transfer> transferList = new ArrayList<>();
		Transfer[] transfers = transferService.getTransferHistory(currentUser);
		for (Transfer transfer : transfers) {
			if (transfer.getUserFromId() == (long)currentUser.getUser().getId()) {
				transfer.setUsername("  To: " + transferService.getUserById(transfer.getUserToId(),currentUser).getUsername());
			} else {
				transfer.setUsername("From: " + transferService.getUserById(transfer.getUserToId(),currentUser).getUsername());
			}
			transferList.add(transfer);
		}
		Long transferId = console.printTransferHistory(transferList);
		if (transferId != 0) {
			viewTransferDetails(transferId);
		}
	}

	private void viewTransferDetails(Long transferId) {
		Transfer transfer = transferService.getTransferDetails(currentUser, transferId);
		String fromName = transferService.getUserById(transfer.getUserFromId(),currentUser).getUsername();
		String toName = transferService.getUserById(transfer.getUserToId(),currentUser).getUsername();
		console.transferDetails(transfer, fromName, toName);
		
	}

	private void viewPendingRequests() {
		List<Transfer> requestList = new ArrayList<>();
		Transfer[] pendingRequests = transferService.getPendingRequests(currentUser);
		if (pendingRequests != null) {
			for(Transfer pending : pendingRequests) {
				Transfer transfer = new Transfer();
				transfer.setTransferId(pending.getTransferId());
				transfer.setUsername(transferService.getUserById(pending.getUserFromId(),currentUser).getUsername());
				transfer.setAmount(pending.getAmount());
				requestList.add(transfer);
			}
			Long transferId = console.requestsMenu(requestList);
			if (transferId != 0) {
				approveOrRejectRequest(transferId);		
			}
		}
	}
	
	private void approveOrRejectRequest(Long transferId) {
		Integer choice = console.appendRequests();
		if (choice == 1) {
			console.response(transferService.approveRequest(transferService.getTransferDetails(currentUser, transferId), currentUser));
		} else if (choice == 2) {
			console.response(transferService.rejectRequest(transferService.getTransferDetails(currentUser, transferId), currentUser));
		}
	}

	private void sendBucks() {
		List<User> userList = new ArrayList<>();
		User[] allUsers = transferService.getAllUsers(currentUser);
		for (User user : allUsers) {
			if(currentUser.getUser().getId() != user.getId()) {
				userList.add(user);
			}
		}
		Long userToId = console.sendTransfer(userList);
		if (userToId != 0) {
			BigDecimal amount = console.getTransferAmount();	
			console.response(transferService.sendTransfer(amount, userToId, currentUser));			
		}
	}

	private void requestBucks() {
		List<User> userList = new ArrayList<>();
		User[] allUsers = transferService.getAllUsers(currentUser);
		for (User user : allUsers) {
			if(currentUser.getUser().getId() != user.getId()) {
				userList.add(user);
			}
		}
		Long userToId = console.requestTransfer(userList);
		if (userToId != 0) {
			BigDecimal amount = console.getTransferAmount();	
			console.response(transferService.requestTransfer(amount, userToId, currentUser));			
		}
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
		console.message("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	console.message("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	console.message("REGISTRATION ERROR: "+e.getMessage());
            	console.message("Please attempt to register again.");
            }
        }
	}

	private void login() {
		console.message("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				console.message("LOGIN ERROR: "+e.getMessage());
				console.message("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}

}
