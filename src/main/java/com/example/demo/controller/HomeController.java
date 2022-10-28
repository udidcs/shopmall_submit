package com.example.demo.controller;

import com.example.demo.ConnectionPool;
import com.example.demo.domain.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProductRepository repository;
    @GetMapping("/home")
    public String home(Model model) {
        Connection connection = ConnectionPool.getConnection();
        List<Product> products = repository.findTwolines(connection, 0);
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("pdts", products);

        return "home";
    }

}
