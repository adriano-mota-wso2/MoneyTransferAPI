package com.moneytransfer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moneytransfer.domain.Account;
import com.moneytransfer.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	public List<Account> findAll() {
		return this.accountRepository.findAll();
	}
	
	public Account findById(int id) {
		return this.accountRepository.findById(id);
	}

	public void save(Account account) {
		this.accountRepository.save(account);
	}
	
}