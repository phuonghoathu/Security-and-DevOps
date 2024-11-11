package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Transactional
public class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createUser_normal() throws Exception{
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("hashPwd");
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test");
        req.setPassword("pwdtest1");
        req.setConfirmPassword("pwdtest1");

        ResponseEntity<User> response = userController.createUser(req);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("hashPwd", user.getPassword());
    }

    @Test
    public void createUser_pwdFail() throws Exception{
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("hashPwd");
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test");
        req.setPassword("pwdtest1");
        req.setConfirmPassword("pwdtest");

        ResponseEntity<User> response = userController.createUser(req);
        assertEquals(400, response.getStatusCodeValue());

    }

    @Test
    public void findById_normal() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        Cart cart = MockData.makeRandomCart(user, 1);
        user.setCart(cart);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(1L);
        assertEquals(200, response.getStatusCodeValue());

        User userFind = response.getBody();
        assertNotNull(userFind);
        assertEquals(1, userFind.getId());
        assertEquals("test", user.getUsername());
        assertEquals(1, user.getCart().getItems().size());
    }

    @Test
    public void findByName_normal() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setCart(new Cart());

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName("test");
        assertEquals(200, response.getStatusCodeValue());

        User userFind = response.getBody();
        assertNotNull(userFind);
        assertEquals(1, userFind.getId());
        assertEquals("test", user.getUsername());
    }
}
