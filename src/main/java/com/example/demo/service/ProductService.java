package com.example.demo.service;

import com.example.demo.ConnectionPool;
import com.example.demo.Trie;
import com.example.demo.domain.Member;
import com.example.demo.domain.Product;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ProductRepository;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    private Trie trie = new Trie();
    private HashMap<String, ArrayList<String>> hashMap = new HashMap();
    public void saveImage(Product pdt) {
        Connection connection = ConnectionPool.getConnection();
        productRepository.save(connection, pdt);
        JdbcUtils.closeConnection(connection);
    }

    public Product getProduct(Integer pdtid) {
        Connection connection = ConnectionPool.getConnection();
        Product pdt = productRepository.findById(connection, pdtid);
        JdbcUtils.closeConnection(connection);
        return pdt;
    }

    public List<Product> getProductsByKeyWord(String keyword) {
        Connection connection = ConnectionPool.getConnection();
        List<Product> allByKeyword = productRepository.findAllByKeyword(connection, keyword);
        JdbcUtils.closeConnection(connection);
        return allByKeyword;
    }

    synchronized public void buyProduct(HttpServletRequest req, Integer pdtid, Integer count, Model model) {

        HttpSession session = req.getSession(false);
        Member memberById = (Member) session.getAttribute("login");
        Product product = getProduct(pdtid);
        Connection connection = ConnectionPool.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if (memberById.getMoney() < product.getUnitPrice()*count) {
                model.addAttribute("message", "잔액이 부족합니다.");
                throw new RuntimeException("잔액이 부족합니다.");
            }
            memberRepository.updateMoney(connection, memberById.getId(), product.getUnitPrice(), count);
            if (product.getUnitsInStock() < count) {
                model.addAttribute("message", "남은 재고가 없습니다.");
                throw new RuntimeException("남은 재고가 없습니다.");
            }
            productRepository.updateStock(connection, pdtid, count);
            productRepository.makeOrder(connection, memberById.getId(), pdtid, count, product.getUnitPrice());
            connection.commit();

            model.addAttribute("message", "주문 성공하였습니다.");
            memberById.setMoney(memberById.getMoney() - product.getUnitPrice());
            session.setAttribute("login", memberById);

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            JdbcUtils.closeConnection(connection);
        }

    }

    @PostConstruct
    public void makeKeywordDic() {
        List<Product> all = productRepository.findAll();
        Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);
        for (Product product : all) {
            String strToAnalyze = "";
            strToAnalyze += product.getDescription() + " " + product.getManufacturer() + " " + product.getName();
            KomoranResult analyzeResultList = komoran.analyze(strToAnalyze);
            List<Token> tokenList = analyzeResultList.getTokenList();

            for (Token token : tokenList) {
                if (token.getPos().equals("NNP") || token.getPos().equals("NNG") || token.getPos().equals("NNB")
                || token.getPos().equals("NR")) {
                    trie.add(token.getMorph());
                }
            }
        }
    }

    @ResponseBody
    public ArrayList<String> getRecommendedWords(String word) {

        ArrayList<String> strings = hashMap.get(word);
        if (strings != null) {
            return strings;
        }
        HashMap<Character, Object> node = trie.findNode(word);
        if (node == null) {
            return null;
        }
        ArrayList<String> allWords = trie.getAllWords(node);
        hashMap.put(word, allWords);
        return allWords;

    }



}
