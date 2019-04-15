package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.Order.Order;
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
public class OrderRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;
    @Qualifier("orderRepository")
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void whenFindById_thenReturnTheOffer() {

        // given
        Order order = new Order(2.0, 1);
        int id = entityManager.persistAndGetId(order, Integer.class);
        entityManager.flush();

        // when
        Order found = orderRepository.findById(id);

        // then
        assertThat(found.getId())
                .isEqualTo(order.getId());
    }

    @Test
    public void saveOffer() {

        // given
        Order order = new Order(2.0, 1);
        int id = entityManager.persistAndGetId(order, Integer.class);
        entityManager.flush();

        // when
        Order found = orderRepository.findById(id);

        // then
        assertNotNull(found);
    }

    @Test
    public void deleteOffer() {

        // given
        Order order = new Order(2.0, 1);
        int id = entityManager.persistAndGetId(order, Integer.class);
        entityManager.flush();

        // when
        orderRepository.delete(order);
        Order found = orderRepository.findById(id);

        // then
        assertNull(found);
    }
}
