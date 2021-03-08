package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.exceptions.TransferNotFoundException;

@Component
public class TransferSqlDAO implements TransferDAO {

	private JdbcTemplate jdbcTemplate;

	public TransferSqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int sendMoney(Transfer transfer) throws AccountNotFoundException {
		int result = 0;
		try {
			result = addTransfer(transfer);
		}
		catch (DataAccessException e) {
		}
		return result;
	}
	
	@Override
	public int requestMoney(Transfer transfer) throws AccountNotFoundException {
		int result = 0;
		try {
			result = addTransfer(transfer);
		}
		catch (DataAccessException e) {
		}
		return result;
	}
	
	private int addTransfer(Transfer transfer) {
		String sqlAddTransfer = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)"
				+ "VALUES (DEFAULT, ?, ? , ?, ?, ?)";
		int result = 0;
		try {
			result = jdbcTemplate.update(sqlAddTransfer, transfer.getType().getValue(), transfer.getStatus().getValue(), transfer.getUserFromId(), transfer.getUserToId(), transfer.getAmount());
		}
		catch (DataAccessException e) {
		}
		return result;
	}

	@Override
	public List<Transfer> getTransferHistory(Principal principal) {
		List<Transfer> transfers = new ArrayList<>();
		String sqlGetAllTransfer = "SELECT transfer_id, transfer_type_id, "
				+ "transfer_status_id, account_from, account_to, amount "
				+ "FROM transfers WHERE account_from = ? OR account_to = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllTransfer, getAccountFromId(principal), getAccountFromId(principal));
		while(results.next()){
			Transfer transfer = mapRowToTransfer(results);
			transfers.add(transfer);
		}
		return transfers;
	}
	
	@Override
	public List<Transfer> getPendingRequests(Principal principal) {
		List<Transfer> requests = new ArrayList<>();
		String sqlGetPendingRequests = "SELECT transfer_id, transfer_type_id, "
										+ "transfer_status_id, account_from, "
										+ "account_to, amount "
										+ "FROM transfers WHERE account_to = ? "
										+ "AND transfer_type_id = ? "
										+ "AND transfer_status_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetPendingRequests, getAccountFromId(principal), Transfer.Type.REQUEST.getValue(), Transfer.Status.PENDING.getValue());
		while(results.next()){
			Transfer transfer = mapRowToTransfer(results);
			requests.add(transfer);
		}
		return requests;
	}

	@Override
	public Transfer getTransferDetails(Long transferId) {
		Transfer transfer = new Transfer();
		String sqlGetTransferDetails = "SELECT transfer_id, transfer_type_id, "
				+ "transfer_status_id, account_from, account_to, amount "
				+ "FROM transfers WHERE transfer_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransferDetails, transferId);
		while(results.next()) {
			transfer = mapRowToTransfer(results);
		}
		return transfer;
	}
	
	@Override
	public int approveRequest(Transfer transfer) throws TransferNotFoundException {
		int result = 0;
		try {
			result = approveHelper(transfer);
		}
		catch (DataAccessException e) {
		}
		return result;
	}
	
	private int approveHelper(Transfer transfer) throws TransferNotFoundException {
		int result = 0;
		String sqlUpdateApprove = "UPDATE transfers "
								+ "SET transfer_status_id = ?"
								+ "WHERE transfer_id = ?";
		try {
			result = jdbcTemplate.update(sqlUpdateApprove, Transfer.Status.APPROVED.getValue(), transfer.getTransferId());
		}
		catch (DataAccessException e) {
		}
		return result;
	}

	@Override
	public int rejectRequest(Transfer transfer) throws TransferNotFoundException {
		int result = 0;
		String sqlUpdateApprove = "UPDATE transfers "
				+ "SET transfer_status_id = ?"
				+ "WHERE transfer_id = ?";
		try {
			result = jdbcTemplate.update(sqlUpdateApprove, Transfer.Status.REJECTED.getValue(), transfer.getTransferId());
		}
		catch (DataAccessException e) {
		}
		return result;
	}
	/*// TODO: delete, not needed?
	private int rejectHelper(Transfer transfer, Long transferId) throws TransferNotFoundException {
		int result = 0;
		String sqlUpdateApprove = "UPDATE transfers "
				+ "SET transfer_status_id = ?"
				+ "WHERE transfer_id = ?";
		try {
			result = jdbcTemplate.update(sqlUpdateApprove, Transfer.Type.valueOf("Rejected"), transferId);
		}
		catch (DataAccessException e) {
		}
		return result;
	}
	*/

	private Long getAccountFromId(Principal principal) {
		Long accountFromId = jdbcTemplate.queryForObject("SELECT account_id FROM accounts "
				+ "JOIN users USING(user_id) WHERE username = ?", Long.class, principal.getName());
		return accountFromId;
	}

	private Long getAccountToId(Long userToId) {
		Long accountToId = jdbcTemplate.queryForObject("SELECT account_id FROM accounts WHERE user_id = ?", Long.class, userToId);
		return accountToId;
	}

	private Transfer mapRowToTransfer(SqlRowSet results) {
		Transfer transfer = new Transfer();
		transfer.setTransferId(results.getLong("transfer_id"));
		transfer.setType(Transfer.Type.valueOfType(results.getInt("transfer_type_id")));
		transfer.setStatus(Transfer.Status.valueOfStatus(results.getInt("transfer_status_id")));
		transfer.setUserFromId(results.getLong("account_from"));
		transfer.setUserToId(results.getLong("account_to"));
		transfer.setAmount(results.getBigDecimal("amount"));
		return transfer;
	}
}


/*
	@Override
	public void requestMoney(BigDecimal amount, Long userToId, Principal principal) {
		Long accountFromId = getAccountFromId(principal);
		Long accountToId = getAccountToId(userToId);
		String sqlRequestMoney = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) "
				+ "VALUES (1, 1, ?, ?, ?) ";
		try {
			jdbcTemplate.update(sqlRequestMoney, accountFromId, accountToId, amount);
		} catch (DataAccessException e) {

		}
	}
 */

/*
@Override
public void sendMoney(BigDecimal amount, Long userToId, Principal principal) {
	Long accountFromId = getAccountFromId(principal);
	Long accountToId = getAccountToId(userToId);
	if (checkBalance(amount, principal)) {
		String sqlUpdateSender = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
		try {
			jdbcTemplate.update(sqlUpdateSender, amount, accountFromId);
		} catch (DataAccessException e) {
		}
		String sqlUpdateReceiver = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
		try {
			jdbcTemplate.update(sqlUpdateReceiver, amount, accountToId);
		} catch (DataAccessException e) {
		}
		insertTransfers(Long.valueOf(2), accountFromId, accountToId, amount);
	} else {
		insertTransfers(Long.valueOf(3), accountFromId, accountToId, amount);
	}
}
*/

/*
	private void insertTransfers(Long status, Long accountFromId, Long accountToId, BigDecimal amount) {
		String sqlSendMoney = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) "
				+ "VALUES (2, ? , ?, ?, ?)";
		try {
			jdbcTemplate.update(sqlSendMoney, status, accountFromId, accountToId, amount);
		} catch (DataAccessException e) {
		}
	}
 */
/*
	private boolean checkBalance(BigDecimal amount, Principal principal) {
		BigDecimal balance = jdbcTemplate.queryForObject("SELECT balance FROM accounts "
				+"JOIN users USING(user_id) WHERE username = ?", BigDecimal.class, principal.getName());
		if (balance.compareTo(amount) > 0) {
			return true;
		} else {
			return false;
		}
	}
 */

/*
public String getUsername(Long userId) {
	String sqlGetName = "SELECT username FROM users WHERE user_id = ?";
	return jdbcTemplate.queryForObject(sqlGetName, String.class, userId);
}
*/
