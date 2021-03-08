package com.techelevator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.tenmo.dao.AccountSqlDAO;
import com.techelevator.tenmo.dao.RequestSqlDAO;
import com.techelevator.tenmo.dao.UserSqlDAO;

public class RequestSqlDAOTest extends BaseSqlDAOTest {

	private AccountSqlDAO account;
	private RequestSqlDAO request;
	private UserSqlDAO user;
	
	@Before
	public void setup() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		account = new AccountSqlDAO(jdbcTemplate);
		request = new RequestSqlDAO(jdbcTemplate);
		user = new UserSqlDAO(jdbcTemplate);
		user.create("Test1", "pass");
		user.create("Test2", "word");
	}
	
	//update requests will be covered by the subsequent tests, 
	//doesn't need to be tested independently
	
	@Test
	public void test_reject_request_updates_transfer_correctly() {
		//make a request, reject it, then check the details
	}
	
	@Test
	public void test_approved_request_updates_correctly_with_enough_balance() {
		//approve a valid request, then check both user balances
	}
	
	@Test
	public void test_approved_request_updates_does_nothing_with_improper_balance() {
		//approve an invalid amount requested, then check both user balances
	}
	
	@Test
	public void test_get_request_by_transfer_id_returns_request() {
		//make a request and return it
	}
	
	@Test
	public void test_get_all_requests_returns_list_correctly() {
		//check history, should be null/empty
		//make two request, then check the get history, should be two items only
	}
	
	@Test
	public void test_check_balance_is_returning_correct_boolean() {
	
	}
	
	
	
	
	
	
}
