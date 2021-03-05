package com.techelevator.tenmo.dao;

import java.security.Principal;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Request;

@Component
public class RequestSqlDAO implements RequestDAO {

	private JdbcTemplate jdbcTemplate;
	
	public RequestSqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
	@Override
	public List<Request> getAll(Principal principal) {
		String sqlGetAllRequest = "SELECT t.transfer_id, t.account_to, t.amount FROM transfers t JOIN accounts a "
							  +"ON t.account_from = a.account_id WHERE a.account_id IN "
							  +"(SELECT account_id FROM accounts JOIN users USING(user_id) WHERE username = ?)";
		return jdbcTemplate.queryForList(sqlGetAllRequest, Request.class, principal.getName());
	}

}
