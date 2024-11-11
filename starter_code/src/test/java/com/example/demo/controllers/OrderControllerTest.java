package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Transactional
public class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;

    List<Item> lstItem;
    User user;
    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        user = MockData.makeRandomUser();
        user.setCart(MockData.makeRandomCart(user, 3));
        lstItem = MockData.makeRandomItems(5);
    }

    @Test
    public void submit_normal() throws Exception{
        when(userRepository.findByUsername("test001")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("test001");
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(3,userOrder.getItems().size());
        assertEquals(new BigDecimal(3000L),userOrder.getTotal());
        assertEquals("My name at 1",userOrder.getItems().get(0).getName());
    }
    @Test
    public void submit_noUser() throws Exception{
        when(userRepository.findByUsername("test001")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("test001");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUser_normal() throws Exception{
        when(userRepository.findByUsername("test001")).thenReturn(user);

        ResponseEntity<UserOrder> responseUser = orderController.submit("test001");
        UserOrder userOrder = responseUser.getBody();
        List<UserOrder> lstUserOrder = new ArrayList<>();
        lstUserOrder.add(userOrder);

        when(orderRepository.findByUser(user)).thenReturn(lstUserOrder);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test001");
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> lstUserOrderFind = response.getBody();
        assertNotNull(lstUserOrderFind);
        assertEquals(1,lstUserOrderFind.size());
        assertEquals(new BigDecimal(3000L),lstUserOrderFind.get(0).getTotal());
        assertEquals("My name at 2",lstUserOrderFind.get(0).getItems().get(1).getName());
    }
    @Test
    public void getOrdersForUser_noUser() throws Exception{
        when(userRepository.findByUsername("test001")).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test001");
        assertEquals(404, response.getStatusCodeValue());
    }
}
