package com.springboot.laptop.dao;

import com.springboot.laptop.model.CategoryEntity;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class CategoryDAOImpl implements CategoryDAO {

    private final EntityManagerFactory emf;

    public CategoryDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }
    @Override
    public CategoryEntity getById(Long id) {
        return getEntityManager().find(CategoryEntity.class, id);
    }

    @Override
    public CategoryEntity findCategoryByName(String name) {
        TypedQuery<CategoryEntity> query = getEntityManager().createQuery("SELECT c FROM CategoryEntity c "  + "WHERE c.name = :category_name", CategoryEntity.class);
        query.setParameter("category_name", name);
        return query.getSingleResult();
    }

    @Override
    public CategoryEntity saveCategory(CategoryEntity category) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(category);
        em.flush();
        em.getTransaction().commit();
        return category;
    }

    @Override
    public CategoryEntity updateCate(CategoryEntity category) {
        return null;
    }

    @Override
    public void deleteCateById(Long id) {

    }

    public EntityManager getEntityManager() {
        return this.emf.createEntityManager();
    }
}
