package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccountSqlDAO implements AccountDAO {

	private JdbcTemplate jdbcTemplate;
	
	public AccountSqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
	@Override
	public BigDecimal getBalance(Principal principal) {
		String sqlGetBalance = "SELECT balance FROM accounts JOIN users USING(user_id) WHERE username = ?";
		return jdbcTemplate.queryForObject(sqlGetBalance, BigDecimal.class, principal.getName());
	}

	@Override
	public void setBalance(Principal principal, BigDecimal balance) {
		String sqlUpdateBalance = "UPDATE accounts SET balance = ? WHERE account_id = ?";
		jdbcTemplate.update(sqlUpdateBalance, balance, principal.getName());
	}
	
}
