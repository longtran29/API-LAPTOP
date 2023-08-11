package com.springboot.laptop.dao;

import com.springboot.laptop.model.CategoryEntity;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//
//@Component
//public class CategoryDAOImpl implements CategoryDAO {
//
//    private final DataSource source;
//
//    CategoryDAOImpl(DataSource source) {
//        this.source = source;
//    }
//    @Override
//    public CategoryEntity findById(Long cateId) {
//
//        Connection connection = null;
//        Statement statement = null;
//        ResultSet resultSet = null;
//
//
//        try {
//            connection = source.getConnection();
//            statement = connection.createStatement();
//            resultSet = statement.executeQuery("SELECT * FROM categories where id = " + cateId);
//            if (resultSet.next()){
//                CategoryEntity category = new CategoryEntity();
//                category.setId(cateId);
//                category.setName(resultSet.getString("category_name"));
//                return category;
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        return null;
//    }
//}
