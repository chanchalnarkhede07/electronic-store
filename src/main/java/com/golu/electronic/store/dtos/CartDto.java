package com.golu.electronic.store.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {


    private String id;
    private Date createdDate;

    private UserDto user;

    private List<CartItemDto> cartItems = new ArrayList<>();
}
