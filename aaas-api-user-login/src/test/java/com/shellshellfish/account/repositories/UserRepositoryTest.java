package com.shellshellfish.account.repositories;


import com.shellshellfish.account.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
        user.setBirthAge("90年代");
        user.setCellPhone("1361168339");
        user.setOccupation("快递小哥");
        user.setPasswordHash("pwd-to-be-hashed");
        user.setCreatedBy("dev2");
        user.setUuid(UUID.randomUUID().toString());
        user.setId(3);

        userRepository.save(user);
        
        List<User> users = userRepository.findAll();
        System.out.println(users);
        
    }
}
