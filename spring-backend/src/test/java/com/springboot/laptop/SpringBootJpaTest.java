package com.springboot.laptop;

import com.springboot.laptop.dao.CategoryDAO;
//import com.springboot.laptop.dao.CategoryDAOImpl;
import com.springboot.laptop.dao.CategoryDAOImpl2;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

//@ComponentScan(basePackages = {"com.springboot.laptop.dao"})
//@Import(CategoryDAOImpl.class)
@Import(CategoryDAOImpl2.class)
public class SpringBootJpaTest {

    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    CategoryDAO cateDAO;


    @Test
    void testJpaTestSplice() {
        long countBefore = categoryRepository.count();

        categoryRepository.save(new CategoryEntity("Dien thoai di dong 2023"));

        long countAfter = categoryRepository.count();

        categoryRepository.findAll().stream().forEach(cate -> System.out.println("Value cate is " + cate.getName()));

        assertThat(countBefore).isLessThan(countAfter);

    }

//    @Test
//    void testGetCateogry() {
//
//        CategoryEntity author = cateDAO.findById(10L);
//
//        assertThat(author).isNotNull();
//
//    }



    @Test
    void testGetCategoryById() {

        CategoryEntity author = cateDAO.getById(10L);

        assertThat(author).isNotNull();

    }

    @Test
    void testSaveCategory() {

        CategoryEntity cate = cateDAO.saveCategory( new CategoryEntity("Dien thoai vui"));

        System.out.println("Value save category " + cate.getId() + " " + cate.getName() );
        assertThat(cate).isNotNull();

    }

}
