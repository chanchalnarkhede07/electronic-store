package com.golu.electronic.store.services;

import com.golu.electronic.store.dtos.CreateOrderRequest;
import com.golu.electronic.store.dtos.OrderDto;
import com.golu.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface OrderService {

    //create order
    OrderDto createOrder(CreateOrderRequest orderDto);

    //delete orders
    void deleteOrder(String orderId);

    //get all orders of user
    List<OrderDto> getAllOrdersOfUser(String userId);

    //get all order
    PageableResponse<OrderDto> getAllOrders(int pageNo, int pageSize, String sortBy, String sortDirection);
}
