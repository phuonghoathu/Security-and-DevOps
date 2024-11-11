package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Transactional
public class ItemControllerTest {
    @InjectMocks
    private ItemController itemController;
    @Mock
    private ItemRepository itemRepository;

    List<Item> lstItem;
    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        lstItem = MockData.makeRandomItems(5);
    }

    @Test
    public void getItems_normal() throws Exception{

        when(itemRepository.findAll()).thenReturn(lstItem);

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(200, response.getStatusCodeValue());

        List<Item> lstItemFind = response.getBody();
        assertNotNull(lstItemFind);
        assertEquals(5, lstItemFind.size());
        assertEquals(new BigDecimal(1000L), lstItemFind.get(1).getPrice());
        assertEquals("My name at 3", lstItemFind.get(2).getName());
        assertEquals("Item 4", lstItemFind.get(3).getDescription());
    }

    @Test
    public void getItemById_normal() throws Exception{

        when(itemRepository.findById(2L)).thenReturn(Optional.ofNullable(lstItem.get(1)));

        ResponseEntity<Item> response = itemController.getItemById(2L);
        assertEquals(200, response.getStatusCodeValue());

        Item itemFind = response.getBody();
        assertNotNull(itemFind);
        assertEquals(new BigDecimal(1000L), itemFind.getPrice());
        assertEquals("My name at 2", itemFind.getName());
        assertEquals("Item 2", itemFind.getDescription());
    }

    @Test
    public void getItemsByName_normal() throws Exception{

        when(itemRepository.findByName(anyString())).thenReturn(lstItem.subList(0,3));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item");
        assertEquals(200, response.getStatusCodeValue());

        List<Item> lstItemFind = response.getBody();
        assertNotNull(lstItemFind);
        assertEquals(3, lstItemFind.size());
        assertEquals(new BigDecimal(0L), lstItemFind.get(0).getPrice());
        assertEquals("My name at 2", lstItemFind.get(1).getName());
        assertEquals("Item 3", lstItemFind.get(2).getDescription());
    }
}
