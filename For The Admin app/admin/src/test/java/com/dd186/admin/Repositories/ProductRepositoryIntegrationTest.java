package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;
    @Qualifier("productRepository")
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindProductById() {

        // given
        Product milkBottle = new Product("Milk Bottle", 1.00);
        int id = entityManager.persistAndGetId(milkBottle, Integer.class);
        entityManager.flush();


        // when
        Product found = productRepository.findById(id);

        // then
        assertThat(found.getId())
                .isEqualTo(milkBottle.getId());
    }

    @Test
    public void testSaveProduct() {

        // given
        Product milkBottle = new Product("Milk Bottle", 1.00);


        // when
        productRepository.save(milkBottle);

        // then
        Product found = productRepository.findById(milkBottle.getId());
        assertNotNull(found);
    }

    @Test
    public void testDeleteProduct() {

        // given
        Product milkBottle = new Product("Milk Bottle", 1.00);
        int id = entityManager.persistAndGetId(milkBottle, Integer.class);
        entityManager.flush();


        // when
        productRepository.delete(milkBottle);
        Product found = productRepository.findById(id);

        // then
        assertNull(found);
    }

}
