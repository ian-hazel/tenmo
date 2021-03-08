package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccountSqlDAO implements AccountDAO {

	private JdbcTemplate jdbcTemplate;
	
	public AccountSqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
	@Override
	public Long getAcctIdFromUserId(Long userId) {
		String sqlGetAcctIdFromUserId = "SELECT account_id FROM accounts WHERE user_id = ?";
		return jdbcTemplate.queryForObject(sqlGetAcctIdFromUserId, Long.class, userId);
	}
	
	@Override
	public Long getAcctIdFromUserName(String username) {
		String sqlGetAcctIdFromUserName = "SELECT account_id FROM accounts JOIN users USING(user_id) WHERE username = ?";
		return jdbcTemplate.queryForObject(sqlGetAcctIdFromUserName, Long.class, username);
	}
	
	@Override
	public BigDecimal getBalance(Long accountId) {
		String sqlGetBalance = "SELECT balance FROM accounts WHERE account_id = ?";
		return jdbcTemplate.queryForObject(sqlGetBalance, BigDecimal.class, accountId);
	}

	@Override
	public BigDecimal increaseBalance(Long accountId, BigDecimal amount) {
		String sqlIncreaseBalance = "UPDATE accounts SET balance = ? WHERE account_id = ?";
		BigDecimal currentBalance = getBalance(accountId);
		BigDecimal changedBalance = currentBalance.add(amount);
			
		try {
			jdbcTemplate.update(sqlIncreaseBalance, changedBalance, accountId);
		}
		catch (DataAccessException e) {
		}
		
		return changedBalance;
	}

	@Override
	public BigDecimal decreaseBalance(Long accountId, BigDecimal amount) {
		String sqlIncreaseBalance = "UPDATE accounts SET balance = ? WHERE account_id = ?";
		BigDecimal currentBalance = getBalance(accountId);
		BigDecimal changedBalance = currentBalance.subtract(amount);
		
		try {
			jdbcTemplate.update(sqlIncreaseBalance, changedBalance, accountId);
		}
		catch (DataAccessException e) {
		}
		
		return changedBalance;
	}

	
}
