package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {

    @Id
    String id;
    String password;
    String name;
    String gender;
    String year;
    String month;
    String day;
    String address;
    String postcode;
    String roadAddress;
    String jibunAddress;
    String detailAddress;
    String extraAddress;
    Integer money;


}
