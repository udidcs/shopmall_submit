package com.example.demo.controller;

import com.example.demo.ConnectionPool;
import com.example.demo.domain.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.MemberService;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository repository;

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource returnimage(@PathVariable String filename) throws MalformedURLException {
        String path = "file:\\" + System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\" + filename;
//                String path = "file:/home/ec2-user/jenkins/images/" + filename;
        return new UrlResource(path);
    }

//    @GetMapping("/download/{filename}")
//    public ResponseEntity<Resource> returnimagefile(@PathVariable String filename, HttpServletResponse res) throws MalformedURLException {
//        res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
//        String path = "file:\\" + System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\" + filename;
//        return ResponseEntity.ok().body(new UrlResource(path));
//    }

    @GetMapping("/addProduct")
    public String addProduct(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session != null)
            session.invalidate();
        return "redirect:home";
    }





    @GetMapping("/productForm")
    public String productForm(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return "redirect:home";
        }
        return "productForm";
    }



    @Transactional
    @PostMapping("/upload")
    public String uploadProduct(HttpServletRequest req, @ModelAttribute Product pdt, @RequestParam MultipartFile pdtimg) throws InterruptedException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            return "redirect:home";
        }

        try {
            int i =  pdtimg.getOriginalFilename().lastIndexOf('.');
            String substring = pdtimg.getOriginalFilename().substring(i + 1);

            String str = String.valueOf(UUID.randomUUID().toString()) + "." + substring;
            pdt.setFilename(str);
//          pdtimg.transferTo(new File(System.getProperty("user.dir")
//                    + "\\src\\main\\resources\\static\\images\\" + str));
            pdtimg.transferTo(new File("/home/ec2-user/jenkins/images/" +str));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        productService.saveImage(pdt);
        return "redirect:home";
    }

    @PostMapping("/buyProduct/{pdtid}")
    public String buyProduct(HttpServletRequest req, @PathVariable Integer pdtid, @RequestParam Integer count, Model model) {

        productService.buyProduct(req, pdtid, count, model);
        Product product = productService.getProduct(pdtid);
        model.addAttribute("prodt", product);
        return "productInfo";
    }

    @GetMapping("/getProductsByKeyWord")
    public String getProductsByKeyWord(@RequestParam String keyword, HttpServletRequest req, Model model) {

        List<Product> productsBySearch = productService.getProductsByKeyWord(keyword);
        model.addAttribute("pdts", productsBySearch);
        System.out.println(productsBySearch);
        return "home";
    }

    @ResponseBody
    @GetMapping("/getRecommendedWords")
    public ArrayList<String> getRecommendedWords(@RequestParam String word) {
        if (word == "")
            return null;
        return productService.getRecommendedWords(word);
    }

    @ResponseBody
    @GetMapping("/findTwolines")
    public ArrayList<Product> findTwolines(@RequestParam Integer pagenum) {
        Connection connection = ConnectionPool.getConnection();
        ArrayList<Product> twolines = repository.findTwolines(connection, pagenum);
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return twolines;
    }
}