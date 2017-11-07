package com.shellshellfish.aaas.userinfo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shellshellfish.aaas.userinfo.model.BankCard;

public interface BankCodeRepository extends JpaRepository<BankCard, Long>{

}
