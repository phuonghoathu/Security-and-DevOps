package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
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
import static org.mockito.Mockito.when;

@Transactional
public class CartControllerTest {
    @InjectMocks
    private CartController cartController;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    List<Item> lstItem;
    User user;

    ModifyCartRequest modifyCartRequest;
    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        user = MockData.makeRandomUser();
        user.setCart(new Cart());
        lstItem = MockData.makeRandomItems(5);
        modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername("test001");
    }

    @Test
    public void addTocart_normal() throws Exception{
        when(userRepository.findByUsername("test001")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(lstItem.get(1)));

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart =  response.getBody();
        assertNotNull(cart);
        assertEquals(2,cart.getItems().size());
        assertEquals(new BigDecimal(2000L),cart.getTotal());
        assertEquals("My name at 2",cart.getItems().get(0).getName());
    }

    @Test
    public void addTocart_noUser() throws Exception{
        when(userRepository.findByUsername("test001")).thenReturn(null);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addTocart_noItems() throws Exception{
        when(userRepository.findByUsername("test001")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromcart_normal() throws Exception{
        user.setCart(MockData.makeRandomCart(user, 4));
        when(userRepository.findByUsername("test001")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(lstItem.get(1)));

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart =  response.getBody();
        assertNotNull(cart);
        assertEquals(3,cart.getItems().size());
        assertEquals(new BigDecimal(5000L),cart.getTotal());
        assertEquals("My name at 3",cart.getItems().get(1).getName());
    }

    @Test
    public void removeFromcart_noUser() throws Exception{
        when(userRepository.findByUsername("test001")).thenReturn(null);

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromcart_noItems() throws Exception{
        when(userRepository.findByUsername("test001")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertEquals(404, response.getStatusCodeValue());
    }

}
