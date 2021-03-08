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

@Component
public class TransferSqlDAO implements TransferDAO {

	private JdbcTemplate jdbcTemplate;

	public TransferSqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
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

	@Override
	public Transfer sendMoney(Transfer transfer, Principal principal) {
		Long accountFromId = getAccountFromId(principal);
		Long accountToId = getAccountToId(transfer.getAccountTo());
		if (checkBalance(transfer.getAmount(), principal)) {
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

	@Override
	public List<Transfer> getTransferHistory(Principal principal) {
		List<Transfer> transfers = new ArrayList<>();
		String sqlGetAllTransfer = "SELECT transfer_id, transfer_type_id, "
				+ "transfer_status_id, account_from, account_to, amount "
				+ "FROM transfers  WHERE account_from = ? OR account_to = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllTransfer, Transfer.class, getAccountFromId(principal), getAccountFromId(principal));
		while(results.next()){
			Transfer transfer = mapRowToTransfer(results);
			transfers.add(transfer);
		}
		return transfers;
	}
	
	@Override
	public Transfer getTransferDetails(Long transferId, Principal principal) {
		Transfer transfer = new Transfer();
		String sqlGetTransferDetails = "SELECT transfer_id, transfer_type_id, "
				+ "transfer_status_id, account_from, account_to, amount "
				+ "FROM transfers WHERE transfer_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransferDetails, Transfer.class, transferId);
		while(results.next()) {
			transfer = mapRowToTransfer(results);
		}
		return transfer;
	}
	
	private boolean checkBalance(BigDecimal amount, Principal principal) {
		BigDecimal balance = jdbcTemplate.queryForObject("SELECT balance FROM accounts "
				+"JOIN users USING(user_id) WHERE username = ?", BigDecimal.class, principal.getName());
		if (balance.compareTo(amount) > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private String getUsername(Long accountId) {
		String sqlGetName = "SELECT name FROM accounts WHERE account_id = ?";
		return jdbcTemplate.queryForObject(sqlGetName, String.class, accountId);
	}
	
	private Long getAccountFromId(Principal principal) {
		Long accountFromId = jdbcTemplate.queryForObject("SELECT account_id FROM accounts "
				+ "JOIN users USING(user_id) WHERE username = ?", Long.class, principal.getName());
		return accountFromId;
	}
	
	private Long getAccountToId(Long userToId) {
		Long accountToId = jdbcTemplate.queryForObject("SELECT account_id FROM accounts WHERE user_id = ?", Long.class, userToId);
		return accountToId;
	}
	
	private void insertTransfers(Long status, Long accountFromId, Long accountToId, BigDecimal amount) {
		String sqlSendMoney = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) "
				+ "VALUES (2, ? , ?, ?, ?)";
		try {
			jdbcTemplate.update(sqlSendMoney, status, accountFromId, accountToId, amount);
		} catch (DataAccessException e) {
		}
	}
	
	private Transfer mapRowToTransfer(SqlRowSet results) {
		Transfer transfer = new Transfer();
		transfer.setTransferId(results.getLong("transfer_id"));
		Long type = results.getLong("transfer_type_id");
		if(type == 1) {
			transfer.setType("Request");
		} else {
			transfer.setType("Send");
		}
		Long status = results.getLong("transfer_status_id");
		if(status == 1) {
			transfer.setStatus("Pending");
		} else if(status == 2) {
			transfer.setStatus("Approved");
		} else {
			transfer.setStatus("Rejected");
		}
		transfer.setAccountFrom(getUsername(results.getLong("account_from")));
		transfer.setAccountTo(getUsername(results.getLong("account_to")));
		transfer.setAmount(results.getBigDecimal("amount"));
		return transfer;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
