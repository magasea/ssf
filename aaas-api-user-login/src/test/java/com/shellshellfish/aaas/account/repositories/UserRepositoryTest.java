package com.shellshellfish.aaas.account.repositories;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.shellshellfish.aaas.account.model.dao.User;
import com.shellshellfish.aaas.account.repositories.mysql.UserRepository;
import com.shellshellfish.aaas.account.utils.MD5;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="prod")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp(){

    }

    @Test
    
    public void testCrud() {
        User user = new User();
        user.setActivated(true);
        user.setBirthAge("91");
        user.setCellPhone("13611442221");
        user.setOccupation("登录密码232");
        user.setPasswordHash(MD5.getMD5("abccd4djsN-999"));
        user.setCreatedBy("dev2");
        user.setUuid(UUID.randomUUID().toString());
        user.setId(28);
        userRepository.save(user);
        
        List<User> targetuser = userRepository.findByCellPhoneAndPasswordHash(user.getCellPhone(),user.getPasswordHash());
        System.out.println(targetuser);
        
    }
}
