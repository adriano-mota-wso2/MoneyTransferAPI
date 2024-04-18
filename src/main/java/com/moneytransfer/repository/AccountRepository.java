package com.moneytransfer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.moneytransfer.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{

	public List<Account> findAll();
	
	public Account findById(@Param("id") int id);
}