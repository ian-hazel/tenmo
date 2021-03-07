package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccountSqlDAO implements AccountDAO {

	private JdbcTemplate jdbcTemplate;
	
	public AccountSqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
	@Override
	public BigDecimal getBalance(String username) {
		String sqlGetBalance = "SELECT balance FROM accounts JOIN users USING(user_id) WHERE username = ?";
		return jdbcTemplate.queryForObject(sqlGetBalance, BigDecimal.class, username);
	}
	
}
