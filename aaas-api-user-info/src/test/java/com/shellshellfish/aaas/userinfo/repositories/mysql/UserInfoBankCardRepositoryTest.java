package com.shellshellfish.aaas.userinfo.repositories.mysql;

import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author pierre
 * 18-1-12
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("it")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserInfoBankCardRepositoryTest {

	@Autowired
	UserInfoBankCardsRepository userInfoBankCardsRepository;


	@Test
	public void deleteByCardNumberTest() {
		String bankNo = "12112312341";

		UiBankcard bankcardDemo = new UiBankcard();
		bankcardDemo.setCardNumber(bankNo);
		bankcardDemo.setBankName("mytestBanName");
		//bankcardDemo.setCreatedBy("pierre");
		userInfoBankCardsRepository.save(bankcardDemo);
		List<UiBankcard> bankcards = userInfoBankCardsRepository.findUiBankcardByCardNumberIsAndStatusIsNot
				(bankNo, -1);

		System.out.println(bankcards);
	}
}
