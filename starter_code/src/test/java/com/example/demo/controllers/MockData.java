package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MockData {
    public static List<Item> makeRandomItems(int size) {
        List<Item> lstItem = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Item item = new Item();
            item.setId((long) (i+1));
            item.setDescription("Item " + (i + 1));
            item.setName("My name at " + (i + 1) );
            item.setPrice(BigDecimal.valueOf(1000*(long)i));
            lstItem.add(item);
        }
        return lstItem;
    }
    public static Cart makeRandomCart(User user, int itemSize) {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setTotal(new BigDecimal(0L));
        cart.setItems(makeRandomItems(itemSize));
        for (Item item : cart.getItems()) {
            cart.setTotal(cart.getTotal().add(item.getPrice()));
        }

        return cart;
    }

    public static User makeRandomUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test001");
        user.setCart(new Cart());

        return user;
    }
}
