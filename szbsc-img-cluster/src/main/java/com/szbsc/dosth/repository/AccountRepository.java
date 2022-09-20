package com.szbsc.dosth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.szbsc.dosth.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String> {

}