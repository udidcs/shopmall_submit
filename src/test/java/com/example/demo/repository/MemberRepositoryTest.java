package com.example.demo.repository;

import com.example.demo.ConnectionPool;
import com.example.demo.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class MemberRepositoryTest {
//    @Test
//    void findById() throws SQLException {
//        Connection connection = ConnectionPool.getConnection();
//        PreparedStatement preparedStatement = connection.prepareStatement("insert into member values (1, 2, 3, 4, 5, 6, 7, 8, 9 ,10)");
//        preparedStatement.execute();
//        MemberRepository repo = new MemberRepository();
//        Member byId = repo.findById(connection, "1");
//        Assertions.assertThat(byId).isNotNull();
//    }
}