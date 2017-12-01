package com.shellshellfish.aaas.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.shellshellfish.aaas.account.model.dao.BankCard;
import com.shellshellfish.aaas.account.model.dto.BankCardDTO;
import com.shellshellfish.aaas.account.repositories.mysql.BankCardRepository;
import com.shellshellfish.aaas.account.service.BankCardService;
import com.shellshellfish.aaas.account.utils.MyBeanUtils;;

@Service
public class BankCardServiceImpl implements BankCardService {

	@Autowired
	private BankCardRepository bankCardRepository;

	@Override
	public Page<BankCardDTO> selectBankCardById(Pageable pageable, Long userId)
			throws InstantiationException, IllegalAccessException {
		Page<BankCard> bankCardPage = bankCardRepository.findByUserId(userId, pageable);
		Page<BankCardDTO> bankCardDtoPage = MyBeanUtils.convertPageDTO(pageable, bankCardPage, BankCardDTO.class);
		return bankCardDtoPage;
	}

}
