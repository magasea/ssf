package com.shellshellfish.aaas.account.repositories.mysql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shellshellfish.aaas.account.model.dao.BankCard;

public interface BankCardRepository extends JpaRepository<BankCard, Long> {
	Page<BankCard> findByUserId(Long userId,Pageable pageable);
}
