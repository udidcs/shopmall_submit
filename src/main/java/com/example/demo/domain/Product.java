package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
	@Id
	private Integer productId;
	private String name;
	private Integer unitPrice;
	private String description;
	private String manufacturer;
	private Integer unitsInStock;
	private String filename;
}
