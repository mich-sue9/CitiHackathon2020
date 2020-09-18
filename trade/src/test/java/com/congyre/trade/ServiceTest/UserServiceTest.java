package com.congyre.trade.ServiceTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.congyre.trade.entity.User;
import com.congyre.trade.repository.UserRepository;
import com.congyre.trade.service.UserService;

import org.bson.types.ObjectId;
import org.junit.Test;

import org.junit.runner.RunWith;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes= UserServiceTest.Config.class)
public class UserServiceTest {

    private static ObjectId usereId = new ObjectId("3b23a3d545bee629d17fd7b2");
    private static User user = new User();



    @Configuration
    public static class Config {

        @Bean
        public UserRepository repo() {

           
            List<User> users = new ArrayList<>();
            
            user.setUserId(usereId);
            users.add(user);

            // setup the mock
            UserRepository repo = mock(UserRepository.class);
            when(repo.findAll()).thenReturn(users);
            when(repo.insert(any(User.class))).thenReturn(user);
            when(repo.save(any(User.class))).thenReturn(user);
            when(repo.findById(usereId)).thenReturn(Optional.of(user));
            return repo;
        }

        
        @Bean
        public UserService userService(){
            return new UserService();
        }


    }

    @Autowired
    UserService service;

    @Test
    public void testGetAllUsers(){
        Iterable<User> trades= service.getAllUsers();
        Stream<User> stream = StreamSupport.stream(trades.spliterator(), false);
        assertThat(stream.count(), equalTo(1L));
    }
    
    @Test
    public void testUpdateUser(){
        User updatedUser = service.updateUser(new User());
        assertThat(updatedUser, equalTo(user));

    }
    

    @Test
    public void testGetUser(){
        User getUser = service.getUser(usereId);
        assertThat(getUser, equalTo(user));
    }



  

    
}