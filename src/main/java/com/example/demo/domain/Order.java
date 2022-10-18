package com.example.demo.domain;

import javax.persistence.*;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @ManyToOne
    Member member;
    @ManyToOne
    Product product;
    private int orderPrice;
    private int count;

}
