package com.shellshellfish.aaas.account.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shellshellfish.aaas.account.model.dto.BankCardDTO;

public interface BankCardService {
	Page<BankCardDTO> selectBankCardById(Pageable pageable, Long userId)
			throws InstantiationException, IllegalAccessException;
}
