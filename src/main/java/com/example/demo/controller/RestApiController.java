package com.example.demo.controller;

import com.example.demo.domain.Product;
import com.example.demo.exception.ApikeyError;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
public class RestApiController {

    HashSet<String> hs = new HashSet<>();

    @Autowired
    ProductService productService;
    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/openApi")
    public String editProduct(HttpServletRequest req, Model model) {
        return "openApi";
    }

    @GetMapping("/getRestApiKey")
    public String getRestApiKey(HttpServletRequest req, Model model) {

        String restkey = UUID.randomUUID().toString();
        hs.add(restkey);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                hs.remove(restkey);
            }
        };
        timer.schedule(timerTask, 24*60*1000*30);
        System.out.println(hs.getClass());
        System.out.println(hs);
        model.addAttribute("restkey", restkey);
        return "openApi";
    }
    @ResponseBody
    @GetMapping(value="/productApi/{productId}")
    public void productApi(@PathVariable Integer productId, HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setCharacterEncoding("UTF-8");
        String authorization = req.getHeader("Authorization");
        if (authorization == null) {
            res.setStatus(400);
            res.getWriter().write(objectMapper.writeValueAsString(new ApikeyError("1", "REST API키가 없습니다.")));
            return;
        }
        authorization.trim();

        if (!hs.contains(authorization)) {
            System.out.println(hs);
            res.setStatus(400);
            res.getWriter().write(objectMapper.writeValueAsString(new ApikeyError("2", "유효하지 않은 REST API키 입니다.")));
            return;
        }

        Product product = productService.getProduct(productId);

        if (product == null) {
            res.setStatus(400);
            res.getWriter().write(objectMapper.writeValueAsString(new ApikeyError("3", "존재하지 않은 상품입니다.")));
        } else {
            res.setStatus(200);
            res.getWriter().write(objectMapper.writeValueAsString(product));
        }

        return;
    }
}
