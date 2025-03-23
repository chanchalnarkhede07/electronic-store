package com.golu.electronic.store.services;

import com.golu.electronic.store.dtos.AddItemToCartRequest;
import com.golu.electronic.store.dtos.CartDto;

public interface CartService {


    //add to cart
    //case1:if cart not availabe then create then add
    //case2:add to cart if cart is already available

    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove item from cart

    void removeItemFromCart(String userId, int cartItemId);

    //clear cart
    void clearCart(String userId);

    CartDto getCart(String userId);



}
