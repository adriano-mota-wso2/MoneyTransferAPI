package com.moneytransfer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneytransfer.domain.Account;
import com.moneytransfer.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

	private AccountService accountService;
	
	public AccountController(AccountService service) {
		this.accountService = service;
	}
	
	@GetMapping("/")
	public List<Account> findAll() {
		return this.accountService.findAll();
	}
	
	@GetMapping("/id")
	public @ResponseBody ResponseEntity<String> findById(@RequestParam("id") int id) throws JsonProcessingException {
		Account account = this.accountService.findById(id);
		if(account==null) {
			return new ResponseEntity<String>("Account ID "+id+" Not Found.", HttpStatus.NOT_FOUND);
		} else {
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(account);
			
			return  new ResponseEntity<String>(jsonString, HttpStatus.OK);
		}
	}
	
	@PostMapping("/")
	public @ResponseBody ResponseEntity<String> createAccount(int id, double initialAmount) {
		Account account = new Account();
		account.setId(id);
		account.setAmount(initialAmount);
		
		this.accountService.save(account);
		
		return new ResponseEntity<String>("Account ID "+account.getId()+" created.", HttpStatus.CREATED);
	}
	
	@PutMapping("/")
	public @ResponseBody ResponseEntity<String> updateAccount(int id, double addedAmount) {
		Account account = this.accountService.findById(id);
		
		if(account==null) {
			return new ResponseEntity<String>("Account ID "+id+" Not Found.", HttpStatus.NOT_FOUND);
		} else {
			double newAmount = account.getAmount() + addedAmount;
			account.setAmount(newAmount);
			
			this.accountService.save(account);
			
			return new ResponseEntity<String>("Account ID "+id+" Updated. New amount is "+account.getAmount() , HttpStatus.OK);
		}
	}
	
	@PutMapping("/transfer")
	public @ResponseBody ResponseEntity<String> transferAccount(int originAccountId, int destinationAccountId, double transferAmount) {
		Account originAccount = this.accountService.findById(originAccountId);
		
		if(originAccount==null) {
			return new ResponseEntity<String>("Account ID "+originAccountId+" Not Found.", HttpStatus.NOT_FOUND);
		} else {
		
			if(originAccount.getAmount() <= transferAmount) {
				return new ResponseEntity<String>("Account ID "+originAccountId+" has not enough money for the transfer ", HttpStatus.FORBIDDEN);
				
			} else {
				double newAmount = 0;
				
				Account destinationAccount = this.accountService.findById(destinationAccountId);
				
				newAmount = destinationAccount.getAmount() + transferAmount;
				destinationAccount.setAmount(newAmount);
				
				this.accountService.save(destinationAccount);
				
				newAmount = originAccount.getAmount() - transferAmount;
				originAccount.setAmount(newAmount);
				
				this.accountService.save(originAccount);
				
				return new ResponseEntity<String>("Amount Transfer Completed ", HttpStatus.OK);
			}
		}
		
		
	}
}