package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Request;
import com.techelevator.tenmo.model.exceptions.TransferNotFoundException;

@Component
public class RequestSqlDAO implements RequestDAO {

	private JdbcTemplate jdbcTemplate;

	public RequestSqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Request> getAllRequests(String username) {
		List<Request> requests = new ArrayList<>();
		String sqlGetAllRequests = "SELECT t.transfer_id, t.amount, u.user_id "
				+ "FROM transfers t JOIN accounts a ON t.account_to = a.account_id "
				+ "JOIN users u USING(user_id) "
				+ "WHERE t.transfer_status_id = 1 AND a.account_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllRequests, Request.class, username);
		while (results.next()) {
			Request request = mapRowToRequest(results);
			requests.add(request);
		}
		return requests;
	}
	
	@Override
	public Request getRequestByTransferId(Long transferId, String username) 
			 throws TransferNotFoundException {
		Request request = new Request();
		String sqlGetAllRequest = "SELECT t.transfer_id, t.account_to, t.amount "
				+ "FROM transfers t JOIN accounts a ON t.account_from = a.account_id "
				+ "WHERE t.status = 1 AND a.account_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllRequest, Request.class, username);
		while (results.next()) {
			request = mapRowToRequest(results);
		}
		return request;
	}

	@Override
	public void approveRequest(Long transferId, String username) 
			throws TransferNotFoundException {
		BigDecimal amount = jdbcTemplate.queryForObject("SELECT amount FROM transfers WHERE transfer_id = ?",
				BigDecimal.class, transferId);
		Long accountToId = jdbcTemplate.queryForObject("SELECT account_to FROM transfers WHERE transfer_id = ?",
				Long.class, transferId);
		Long accountFromId = getAccountId(username);
		if (checkBalance(amount, username)) {
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
			updateRequests(transferId);
		}
	}

	@Override
	public void rejectRequest(Long transferId) 
			throws TransferNotFoundException {
		String sqlRejectRequest = "UPDATE transfers SET transfer_status_id = 3 WHERE transfer_id = ?";
		jdbcTemplate.update(sqlRejectRequest, transferId);
	}
	
	private void updateRequests(Long transferId){
		String sqlUpdate = "UPDATE transfers SET transfer_status_id = 2 WHERE transfer_id = ?";
		try {
			jdbcTemplate.update(sqlUpdate, transferId);
		} catch (DataAccessException e) {
		}
	}
	
	private boolean checkBalance(BigDecimal amount, String username) {
		BigDecimal balance = jdbcTemplate.queryForObject("SELECT balance FROM accounts "
				+"JOIN users USING(user_id) WHERE username = ?", BigDecimal.class, username);
		if (balance.compareTo(amount) > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private Long getAccountId(String username) {
		Long accountId = jdbcTemplate.queryForObject("SELECT account_id FROM accounts "
				+ "JOIN users USING(user_id) WHERE username = ?", Long.class, username);
		return accountId;
	}
	
	private String getName(Long accountId) {
		String sqlGetName = "SELECT username FROM users WHERE account_id = ?";
		return jdbcTemplate.queryForObject(sqlGetName, String.class, accountId);
	}
	
	private Request mapRowToRequest(SqlRowSet results) {
		Request request = new Request();
		request.setTransferId(Long.valueOf(results.getString("transfer_id")));
		Long accountId = Long.valueOf(results.getString("account_to"));
		request.setAccountTo(getName(accountId));
		request.setAmount(new BigDecimal(results.getString("amount")));
		return request;
	}

}
