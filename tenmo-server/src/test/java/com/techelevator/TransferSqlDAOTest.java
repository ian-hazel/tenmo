package com.techelevator;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.tenmo.dao.AccountSqlDAO;

import com.techelevator.tenmo.dao.TransferSqlDAO;
import com.techelevator.tenmo.dao.UserSqlDAO;

public class TransferSqlDAOTest extends BaseSqlDAOTest {

	private AccountSqlDAO account;
	private TransferSqlDAO transfer;
	private UserSqlDAO user;
	
	@Before
	public void setup() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		account = new AccountSqlDAO(jdbcTemplate);
		transfer = new TransferSqlDAO(jdbcTemplate);
		user = new UserSqlDAO(jdbcTemplate);
		user.create("Test1", "pass");
		user.create("Test2", "word");
	}
	
	//insert transfers will be covered by the subsequent tests, 
	//doesn't need to be tested independently
	
	@Test
	public void test_request_money_inserts_transfer_correctly() {
		//make a request, then check the get transfer history, should be only one in there
	}
	
	@Test
	public void test_approved_send_money_inserts_transfer_correctly() {
		//make a valid send, then check the get transfer history, should be only one in there
	}
	
	@Test
	public void test_rejected_send_money_inserts_transfer_correctly() {
		//make a rejected send, then check the get transfer history, should be only one in there
	}
	
	@Test
	public void test_get_transfer_history_returns_list_correctly() {
		//check history, should be null/empty
		//make two request, then check the get transfer history, should be two items only
	}
	
	@Test
	public void test_check_balance_is_returning_correct_boolean() {
	
	}

}
