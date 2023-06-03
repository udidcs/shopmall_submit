package com.example.demo.repository;

import com.example.demo.domain.Member;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MemberRepository {

    public void save(Connection connection, Member member) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("insert into member(id, password, name, gender, year, month, day, address, money)" +
                    " values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, member.getId());
            preparedStatement.setString(2, member.getPassword());
            preparedStatement.setString(3, member.getName());
            preparedStatement.setString(4, member.getGender());
            preparedStatement.setString(5, member.getYear());
            preparedStatement.setString(6, member.getMonth());
            preparedStatement.setString(7, member.getDay());
            preparedStatement.setString(8, member.getAddress());
            preparedStatement.setInt(9, member.getMoney());
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeStatement(preparedStatement);
        }
        return;
    }

    public List<Member> findByName(Connection connection, Member member) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("select * from member where member.name = (?)");
            preparedStatement.setString(1, member.getName());
            resultSet = preparedStatement.executeQuery();

            List<Member> lst = new ArrayList<>();

            while (resultSet.next()) {
                Member member1 = new Member();
                member1.setId(resultSet.getString("id"));
                member1.setPassword(resultSet.getString("password"));
                member1.setName(resultSet.getString("name"));
                member1.setGender(resultSet.getString("gender"));
                member1.setYear(resultSet.getString("year"));
                member1.setMonth(resultSet.getString("month"));
                member1.setDay(resultSet.getString("day"));
                member1.setAddress(resultSet.getString("address"));
                member1.setMoney(resultSet.getInt("money"));
                lst.add(member1);
            }
            return lst;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeStatement(preparedStatement);
            JdbcUtils.closeResultSet(resultSet);
        }
    }

    public Member findById(Connection connection, String id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("select * from member where member.id = (?)");
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            Member member1 = new Member();

            if(resultSet.next()) {
                member1.setId(resultSet.getString("id"));
                member1.setPassword(resultSet.getString("password"));
                member1.setName(resultSet.getString("name"));
                member1.setGender(resultSet.getString("gender"));
                member1.setYear(resultSet.getString("year"));
                member1.setMonth(resultSet.getString("month"));
                member1.setDay(resultSet.getString("day"));
                member1.setAddress(resultSet.getString("address"));
                member1.setMoney(resultSet.getInt("money"));
                return member1;
            }
            else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            JdbcUtils.closeStatement(preparedStatement);
            JdbcUtils.closeResultSet(resultSet);
        }

    }

    public void updateMoney(Connection connection, String memid, Integer pdtprice, Integer count) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("update member set money = money-(?)*(?) where member.id=(?)");
            preparedStatement.setInt(1, pdtprice);
            preparedStatement.setInt(2, count);
            preparedStatement.setString(3, memid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        finally {
            JdbcUtils.closeStatement(preparedStatement);
        }

    }
}



