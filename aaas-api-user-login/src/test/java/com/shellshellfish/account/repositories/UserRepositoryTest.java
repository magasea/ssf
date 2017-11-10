package com.shellshellfish.account.repositories;


import com.shellshellfish.account.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="test")
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
        user.setCellPhone("13611683358");
        user.setOccupation("快递小哥");
        user.setPasswordHash("pwd-to-be-hashed");
        user.setUuid(UUID.randomUUID().toString());
        user.setCreatedBy("sys");

        userRepository.save(user);
    }
}
