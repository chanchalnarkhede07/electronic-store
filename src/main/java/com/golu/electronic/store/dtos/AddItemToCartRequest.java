package com.golu.electronic.store.dtos;


import com.golu.electronic.store.entities.Product;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddItemToCartRequest {

    private String productId;
    private int quantity;
}
