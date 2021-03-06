package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfer;

@Component
public class TransferSqlDAO implements TransferDAO {

	private JdbcTemplate jdbcTemplate;

	public TransferSqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
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
	
	private void updateTransfers(Long transferId){
		String updateTransfers = "UPDATE transfers SET transfer_status_id = 2 WHERE transfer_id = ?";
		try {
			jdbcTemplate.update(updateTransfers, transferId);
		} catch (DataAccessException e) {
			
		}
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


	@Override
	public void approveTransfer(Long transferId, Principal principal) {
		BigDecimal amount = jdbcTemplate.queryForObject("SELECT amount FROM transfers WHERE transfer_id = ?",
				BigDecimal.class, transferId);
		Long accountToId = jdbcTemplate.queryForObject("SELECT account_to FROM transfers WHERE transfer_id = ?",
				Long.class, transferId);
		Long accountFromId = getAccountFromId(principal);
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
			updateTransfers(transferId);
		}
	}

	@Override
	public void rejectTransfer(Long transferId, Principal principal) {
		String sqlRejectTransfer = "UPDATE transfers SET transfer_status_id = 3 WHERE transfer_id = ?";
		jdbcTemplate.update(sqlRejectTransfer, transferId);
	}

	@Override
	public List<Transfer> getAll(Principal principal) {
		String sqlGetAllTransfer = "SELECT t.transfer_id, transfer_type_id, t.account_to, t.amount FROM transfers t JOIN accounts a "
				+ "ON t.account_from = a.account_id WHERE a.account_id IN "
				+ "(SELECT account_id FROM accounts JOIN users USING(user_id) WHERE username = ?)";
		return jdbcTemplate.queryForList(sqlGetAllTransfer, Transfer.class, principal.getName());
	}
	
//	private Request mapRowToRequest(SqlRowSet results) {
//		Request request = new Request();
//		request.setTransferId(results.getLong("transfer_id"));
//		request.setAccountTo(results.getLong("account_to"));
//		request.setAmount(results.getBigDecimal("amount"));
//		return request;
//	}

}
