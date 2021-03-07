package com.techelevator;

import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.tenmo.dao.AccountSqlDAO;
import com.techelevator.tenmo.dao.RequestSqlDAO;
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
	}
	
}
