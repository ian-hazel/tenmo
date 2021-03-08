package com.techelevator;

import java.math.BigDecimal;

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
		user.create("Test2", "word");
	}
	
	@Test 
	public void test_account_id_match_from_separate_query_searches() {
		Long account1 = account.getAcctIdFromUserName("Test1");
		Long account2 = account.getAcctIdFromUserName("Test2");
		Assert.assertEquals(account1, account.getAcctIdFromUserId(account1)); //these will match because the database syncs the user & 
		Assert.assertEquals(account2, account.getAcctIdFromUserId(account2)); //account id in order by unintentional design for now
	}
	
	
	
	@Test
	public void test_get_balance_works_correctly() {
		Long account1 = account.getAcctIdFromUserName("Test1");
		Long account2 = account.getAcctIdFromUserName("Test2");
		Assert.assertEquals(new BigDecimal("1000.00"), account.getBalance(account1));
		Assert.assertEquals(new BigDecimal("1000.00"), account.getBalance(account2));
	}
	
	@Test
	public void test_increase_balance_works_correctly() {
		account.increaseBalance(account.getAcctIdFromUserName("Test1"), new BigDecimal(1000));
		Assert.assertEquals(new BigDecimal("2000.00"), account.getBalance(account.getAcctIdFromUserName("Test1")));
	}
	
	@Test
	public void test_decrease_balance_works_correctly() {
		account.decreaseBalance(account.getAcctIdFromUserName("Test1"), new BigDecimal(500));
		Assert.assertEquals(new BigDecimal("500.00"), account.getBalance(account.getAcctIdFromUserName("Test1")));
	}
		
}
