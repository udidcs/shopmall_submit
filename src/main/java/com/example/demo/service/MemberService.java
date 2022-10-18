package com.example.demo.service;

import com.example.demo.ConnectionPool;
import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class MemberService {
    @Autowired
    MemberRepository memberRepository;

    synchronized public Member join(Member member) throws IllegalAccessException {
        Connection connection = ConnectionPool.getConnection();
        Member mem = memberRepository.findById(connection, member.getId());
        if (mem != null) {
            return null;
        }
        memberRepository.save(connection, member);
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mem;
    }

    public Member findMemberById(String id) {
        Connection connection = ConnectionPool.getConnection();
        Member byId = memberRepository.findById(connection, id);
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return byId;

    }


}
