package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

	@Getter
	private final AccountsRepository accountsRepository;

	@Autowired
	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}

	/**
	 * THis method will deposite money in account
	 * @param accountId String
	 * @param money String
	 * @return
	 * @throws InterruptedException 
	 */
	public Account depositeMoney(String accountId,String money) throws InterruptedException {
		return deposite(accountId,money);
	}
	/**
	 * this method will withdraw money from account
	 * @param accountId
	 * @param money
	 * @return
	 * @throws InterruptedException 
	 */

	public Account withdrawMoney(String accountId, String money) throws InterruptedException {
		return withdraw(accountId,money);
	}
	/**
	 * This method will tranfer money from one account to another account
	 * @param accountfromId
	 * @param accounttoId
	 * @param money
	 * @return
	 * @throws InterruptedException 
	 */
	public Account transferMoney(String accountfromId, String accounttoId, String money) throws InterruptedException {
		return transfer(accountfromId,accounttoId,money);
	}
	/**
	 * This method will give actual current total for account
	 * @param accountId
	 * @return
	 */
	public synchronized BigDecimal getTotalBalance (String accountId) {
		Account account=this.accountsRepository.getAccount(accountId);
		BigDecimal total;
		if(null!=account) {
			total=account.getBalance();
		}
        return total;
    }
	public synchronized Account withdraw(String accountId, String money) throws InterruptedException {
		Account account=this.accountsRepository.getAccount(accountId);
		BigDecimal total= getTotalBalance(accountId);
		if(null!=money && !money.equals("")) {
			BigDecimal moneyDec=new BigDecimal(money);
			if(total.compareTo(BigDecimal.ZERO) > 0 && (total.compareTo(moneyDec))>1) {
				total=total.subtract(moneyDec);
				account.setBalance(total);
				accountsRepository.updateAccount(account);
			}
		}
	  }
	public synchronized Account deposite(String accountId, String money) throws InterruptedException {
		Account account=this.accountsRepository.getAccount(accountId);
		BigDecimal total= getTotalBalance(accountId);
		if(null!=money && !money.equals("")) {
			BigDecimal moneyDec=new BigDecimal(money);
			total=total.add(moneyDec);
			account.setBalance(total);
			accountsRepository.updateAccount(account);
		}
	  }

	public synchronized Account transfer(String accountfromId, String accounttoId, String money) throws InterruptedException {
		Account accountFrom=this.accountsRepository.getAccount(accountfromId);
		Account accountTo=this.accountsRepository.getAccount(accounttoId);
		if(accountFrom.getBalance()>0) {
			accountFrom=withdraw(accountfromId,money);
		}
		if(accountTo.getBalance()>0) {
			deposite(accountfromId,money);
		}
		return accountFrom;
	  }

}
