package com.golu.electronic.store.dtos;


import com.golu.electronic.store.entities.Order;
import com.golu.electronic.store.entities.Product;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
    private int orderItemId;
    private int quantity;
    private int totalPrice;
    private OrderDto orderDto;
    private ProductDto productDto;
}
