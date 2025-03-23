package com.golu.electronic.store.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    //PENDING.DELIVERED,DISPATCHED
    private String orderStatus;

    //NOTPAID,PAID
    private String paymentStatus;

    private int orderAmount;

    private String billingAddress;

    private String billingPhone;
    private String billingName;
    private Date orderedDate;
    private Date deliveryDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems=new ArrayList<OrderItem>();

}
