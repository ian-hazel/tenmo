package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Request;
import com.techelevator.tenmo.model.Transfer;

@Component
public class TransferSqlDAO implements TransferDAO {

	private JdbcTemplate jdbcTemplate;
	
	public TransferSqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
	@Override
	public Integer sendMoney(BigDecimal amount, Long accountToId, Principal principal) {
		Long accountFromId = jdbcTemplate.queryForObject("SELECT account_id FROM accounts WHERE name = ?", Long.class, principal.getName());
		String sqlSendMoney = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) "
				+ "VALUES (2, CASE WHEN amount < balance FROM accounts WHERE account_from = account_id THEN 2 , "
				+ 			 "CASE WHEN amount > balance FROM accounts WHERE account_from = account_id THEN 1 , "
				+ " ?, ?, ?) RETURNING transfer_status_id"; 
		Integer response = jdbcTemplate.queryForObject(sqlSendMoney, Integer.class, accountFromId , accountToId, amount);
		return response;
	}

	@Override
	public Integer requestMoney(BigDecimal amount, Long accountToId, Principal principal) {
		Long accountFromId = jdbcTemplate.queryForObject("SELECT account_id FROM accounts WHERE name = ?", Long.class, principal.getName());
		String sqlRequestMoney = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) "
				+ "VALUES (1, 1, ?, ?, ?) ";
		Integer response = jdbcTemplate.update(sqlRequestMoney, accountFromId, accountToId, amount);
		return response;
	}

	@Override
	public Integer approveTransfer(Long transferId, Principal principal) {
		BigDecimal amount = jdbcTemplate.queryForObject("SELECT amount FROM transfers WHERE transfer_id = ?", BigDecimal.class, transferId);
		Long accountToId = jdbcTemplate.queryForObject("SELECT account_to FROM transfers WHERE transfer_id = ?", Long.class, transferId);
		Integer response = sendMoney(amount, accountToId, principal);
		if(response == 2) {
			String sqlApproveTransfer = "UPDATE transfers SET transfer_status_id = 2 WHERE transfer_id = ?";
			response = jdbcTemplate.update(sqlApproveTransfer, transferId);
			return response;
		} else {
			return rejectTransfer(transferId, principal);
		}	
	}

	@Override
	public Integer rejectTransfer(Long transferId, Principal principal) {
		String sqlRejectTransfer = "UPDATE transfers SET transfer_status_id = 3 WHERE transfer_id = ?";
		Integer response = jdbcTemplate.update(sqlRejectTransfer, transferId);
		return response;
	}

//	@Override
//	public List<Transfer> getAll(Principal principal) {
//		String sqlGetAllTransfer = "SELECT t.transfer_id, t.account_to, t.amount FROM transfers t JOIN accounts a "
//				  +"ON t.account_from = a.account_id WHERE a.account_id IN "
//				  +"(SELECT account_id FROM accounts JOIN users USING(user_id) WHERE username = ?)";
//		return jdbcTemplate.queryForList(sqlGetAllTransfer, Transfer.class, principal.getName());
//	}

}
