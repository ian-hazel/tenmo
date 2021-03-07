package com.techelevator;

import org.junit.Before;
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
		
		
		
	}
	
	
	
}
