package com.shellshellfish.account.repositories;


import com.shellshellfish.account.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
<<<<<<< HEAD
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
=======
import org.springframework.test.context.junit4.SpringRunner;

>>>>>>> 50543af3caa4cda557bb8a85136440fb3f005896
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
<<<<<<< HEAD
@ActiveProfiles(profiles="prod")
=======
>>>>>>> 50543af3caa4cda557bb8a85136440fb3f005896
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp(){

    }

    @Test
<<<<<<< HEAD
    
=======
>>>>>>> 50543af3caa4cda557bb8a85136440fb3f005896
    public void testCrud() {
        User user = new User();
        user.setActivated(true);
        user.setBirthAge("90年代");
<<<<<<< HEAD
        user.setCellPhone("1361168339");
        user.setOccupation("快递小哥");
        user.setPasswordHash("pwd-to-be-hashed");
        user.setCreatedBy("dev2");
        user.setUuid(UUID.randomUUID().toString());
        user.setId(3);

        userRepository.save(user);
        
        List<User> users = userRepository.findAll();
        System.out.println(users);
        
=======
        user.setCellPhone("13611683358");
        user.setOccupation("快递小哥");
        user.setPasswordHash("pwd-to-be-hashed");
        user.setUuid(UUID.randomUUID().toString());

        userRepository.save(user);
>>>>>>> 50543af3caa4cda557bb8a85136440fb3f005896
    }
}
