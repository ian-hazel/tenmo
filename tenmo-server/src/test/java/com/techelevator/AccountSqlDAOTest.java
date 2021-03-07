package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.tenmo.dao.AccountSqlDAO;
import com.techelevator.tenmo.dao.UserSqlDAO;

public class AccountSqlDAOTest extends BaseSqlDAOTest {

	private AccountSqlDAO account;
	private UserSqlDAO user;
	
	@Before
	public void setup() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		account = new AccountSqlDAO(jdbcTemplate);
		user = new UserSqlDAO(jdbcTemplate);
		user.create("Test1", "pass");
		user.create("test2", "word");
	}
	
	@Test
	public void test_get_balance_works_correctly() {
//		Assert.assertEquals(1000, account.getBalance(user));
	}
	
	
	
	
	
	
}
