package com.golu.electronic.store.services.impl;

import com.golu.electronic.store.dtos.AddItemToCartRequest;
import com.golu.electronic.store.dtos.CartDto;
import com.golu.electronic.store.entities.Cart;
import com.golu.electronic.store.entities.CartItem;
import com.golu.electronic.store.entities.Product;
import com.golu.electronic.store.entities.User;
import com.golu.electronic.store.exceptions.ResourceNotFoundException;
import com.golu.electronic.store.repositories.CartItemRepository;
import com.golu.electronic.store.repositories.CartRepository;
import com.golu.electronic.store.repositories.ProductRepository;
import com.golu.electronic.store.repositories.UserRepository;
import com.golu.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        String productId = request.getProductId();
        int quantity = request.getQuantity();

        //fetch product from productid
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product not found"));

        //fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not found"));

        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();

        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setId(UUID.randomUUID().toString());
            cart.setCreatedDate(new Date());
        }

        //perform cart operations;
        //if cart item already present then update
        List<CartItem> cartItems = cart.getCartItems();
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        cartItems.stream().map(item -> {

            if (item.getProduct().getProductId().equals(productId)) {
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(product.getDiscountedPrice() * quantity);
                updated.set(true);
            }
            return item;
        }).toList();

        cart.setCartItems(cartItems);
        //else create new and add cartitem
        //create Cart items
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .quantity(quantity)
                    .build();
            cart.getCartItems().add(cartItem);
        }
        cart.setUser(user);
        Cart savedCart = cartRepository.save(cart);

        return modelMapper.map(savedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("cart item not found"));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart not found"));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart not found"));
        return modelMapper.map(cart, CartDto.class);
    }


}
