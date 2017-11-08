package com.shellshellfish.aaas.userinfo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.BankCard;

public interface BankCodeRepository extends JpaRepository<BankCard, Long>{

}
