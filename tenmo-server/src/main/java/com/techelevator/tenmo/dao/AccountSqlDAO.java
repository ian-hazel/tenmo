package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccountSqlDAO implements AccountDAO {

	private JdbcTemplate jdbcTemplate;
	
	public AccountSqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
	@Override
	public double getBalanceById(Long accountId) {
		String sqlGetBalance = "SELECT balance FROM accounts WHERE account_id = ?";
		return jdbcTemplate.queryForObject(sqlGetBalance, Long.class, accountId);
	}

	@Override
	public void setBalanceById(Long accountId, double balance) {
		String sqlUpdateBalance = "UPDATE accounts SET balance = ? WHERE account_id = ?";
		jdbcTemplate.update(sqlUpdateBalance, balance, accountId);
	}
	
//	private Account mapRowToAccount(SqlRowSet rs) {
//		Account account = new Account();
//		account.setAccountId(rs.getLong("account_id"));
//		account.setUserId(rs.getLong("user_id"));
//		account.setBalance(rs.getDouble("balance"));
//		return account;
//	} //end mapRowToAccount() if we ever need it

}
