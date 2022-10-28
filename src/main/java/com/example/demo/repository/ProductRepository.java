package com.example.demo.repository;

import com.example.demo.domain.Product;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {
    @PersistenceContext
    EntityManager em;
    public void save(Connection connection, Product pdt) {
        em.persist(pdt);
    }

    public List<Product> findAll() {
        return em.createQuery("select p from Product p", Product.class)
                .getResultList();
    }

    public ArrayList<Product> findTwolines(Connection connection, int pagenum) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Product> list = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("select * from product limit ?, ?");
            preparedStatement.setInt(1, 10*pagenum);
            preparedStatement.setInt(2, 10*pagenum+10);
            resultSet = preparedStatement.executeQuery();


            while(resultSet.next()) {
                Product pdt = new Product();
                pdt.setProductId(resultSet.getInt("product_id"));
                pdt.setName(resultSet.getString("name"));
                pdt.setUnitPrice(resultSet.getInt("unit_price"));
                pdt.setDescription(resultSet.getString("description"));
                pdt.setManufacturer(resultSet.getString("manufacturer"));
                pdt.setUnitsInStock(resultSet.getInt("units_in_Stock"));
                pdt.setFilename(resultSet.getString("filename"));
                list.add(pdt);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            JdbcUtils.closeStatement(preparedStatement);
            JdbcUtils.closeResultSet(resultSet);
        }
    }

    public Product findById(Connection connection, Integer pdtid) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("select * from product where product.product_id = (?)");
            preparedStatement.setInt(1, pdtid);
            resultSet = preparedStatement.executeQuery();
            Product pdt = new Product();

            if(resultSet.next()) {
                pdt.setProductId(resultSet.getInt("product_id"));
                pdt.setName(resultSet.getString("name"));
                pdt.setUnitPrice(resultSet.getInt("unit_price"));
                pdt.setDescription(resultSet.getString("description"));
                pdt.setManufacturer(resultSet.getString("manufacturer"));
                pdt.setUnitsInStock(resultSet.getInt("units_in_Stock"));
                pdt.setFilename(resultSet.getString("filename"));
                return pdt;
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

    public void updateStock(Connection connection, Integer pdtid, Integer count) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("update product set units_in_stock = units_in_stock-(?) where product.product_id=(?)");
            preparedStatement.setInt(1, count);
            preparedStatement.setInt(2, pdtid);
            int i = preparedStatement.executeUpdate();
            System.out.println(i + " updatestock");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            JdbcUtils.closeStatement(preparedStatement);
        }
    }

    public void makeOrder(Connection connection, String id, Integer pdtid, int count, int price) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("insert into orders(count, order_price, member_id, product_product_id) values(?, ?, ?, ?)");
            preparedStatement.setInt(1, count);
            preparedStatement.setInt(2, price);
            preparedStatement.setString(3, id);
            preparedStatement.setInt(4, pdtid);
            boolean execute = preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            JdbcUtils.closeStatement(preparedStatement);
        }
    }

    public List<Product> findAllByKeyword(Connection connection, String keyword) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("select * from product where " +
                    "concat(product_id, ' ', description, ' ', filename, ' ', manufacturer," +
                    " ' ', name, ' ', unit_price, ' ', units_in_stock) like (?)");
            preparedStatement.setString(1, "%"+keyword+"%");
            resultSet = preparedStatement.executeQuery();
            List<Product> lst = new ArrayList<>();

            while(resultSet.next()) {
                Integer product_id = resultSet.getInt("product_id");
                String description = resultSet.getString("description");
                String filename = resultSet.getString("filename");
                String manufacturer = resultSet.getString("manufacturer");
                String name = resultSet.getString("name");
                Integer unit_price = resultSet.getInt("unit_price");
                Integer units_in_stock = resultSet.getInt("units_in_stock");
                lst.add(new Product(product_id, name, unit_price, description, manufacturer, units_in_stock, filename));
            }
            return lst;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(preparedStatement);
        }
    }
}
