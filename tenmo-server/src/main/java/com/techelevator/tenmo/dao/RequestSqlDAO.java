package com.techelevator.tenmo.dao;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
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
		List<Request> requests = new ArrayList<>();
		String sqlGetAllRequest = "SELECT t.transfer_id, t.account_to, t.amount "
				+ "FROM transfers JOIN accounts a ON t.account_from = a.account_id "
				+ "WHERE t.status = 1 AND a.account_id IN "
				+ "(SELECT account_id FROM accounts JOIN users USING(user_id) WHERE username = ?)";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllRequest, Request.class, principal.getName());
		while (results.next()) {
			Request request = mapRowToRequest(results);
			requests.add(request);
		}
		return requests;
	}

	private Request mapRowToRequest(SqlRowSet results) {
		Request request = new Request();
		request.setTransferId(results.getLong("transfer_id"));
		request.setAccountTo(results.getLong("account_to"));
		request.setAmount(results.getBigDecimal("amount"));
		return request;
	}
}
