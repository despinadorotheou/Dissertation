package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CategoryRepository categoryRepository;

    //when find by category then return the category object
    @Test
    public void testFindByCategory() {

        // given
        Category category = new Category("Soft Drinks");
        entityManager.persist(category);
        entityManager.flush();

        // when
        Category found = categoryRepository.findByCategory(category.getCategory());

        // then
        assertThat(found.getCategory())
                .isEqualTo(category.getCategory());
    }

    @Test
    public void testSaveCategory() {

        // given
        Category category = new Category("Soft Drinks");


        // when
        categoryRepository.save(category);

        // then
        Category found = categoryRepository.findByCategory(category.getCategory());
        assertNotNull(found);
    }

    @Test
    public void testDeleteCategory() {

        // given
        Category category = new Category("Soft Drinks");
        entityManager.persist(category);
        entityManager.flush();


        // when
        categoryRepository.delete(category);
        Category found = categoryRepository.findByCategory(category.getCategory());

        // then
        assertNull(found);
    }
}
