package com.dd186.admin.Services;

import com.dd186.admin.Domain.Order.Order;
import com.dd186.admin.Repositories.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.MockitoAnnotations.initMocks;

public class OrderServiceIntegrationTest {

    @Mock
    private OrderRepository mockOrderRepository;

    private OrderService orderServiceUnderTest;
    private Order order;

    @Before
    public void setUp() {
        initMocks(this);
        orderServiceUnderTest = new OrderService(mockOrderRepository);
        order = new Order(2.0, 1);
        order.setId(1);
    }

    @Test
    public void testFindOfferById() {
        // Setup
        Mockito.when(orderServiceUnderTest.findById(anyInt()))
                .thenReturn(order);
        final int id = 1;

        // Run the test
        final Order result = orderServiceUnderTest.findById(id);

        // Verify the results
        assertEquals(id, result.getId());
    }

    @Test
    public void testSaveOffer() {
        // Setup
        Mockito.when(orderServiceUnderTest.findById(anyInt()))
                .thenReturn(null).thenReturn(order);
        final int id = 1;

        // Run the test
        Order existing = orderServiceUnderTest.findById(id);
        orderServiceUnderTest.save(order);
        Order result = orderServiceUnderTest.findById(id);

        // Verify the results
        assertNotNull(result);
        assertEquals(result, order);
        assertNull(existing);
    }

    @Test
    public void testDeleteOffer() {
        // Setup
        Mockito.when(orderServiceUnderTest.findById(anyInt()))
                .thenReturn(order).thenReturn(null);

        // Run the test
        final Order beforeDelete = orderServiceUnderTest.findById(1);
        orderServiceUnderTest.delete(beforeDelete);
        final Order result = orderServiceUnderTest.findById(1);


        // Verify the results
        assertNotNull(beforeDelete);
        assertEquals(beforeDelete, order);
        assertNull(result);
    }

    @Test
    public void testFindAllOffers() {
        // Setup
        Order order1 = new Order(3.0, 1);
        List<Order> deals = new ArrayList<>();
        deals.add(order);
        deals.add(order1);


        Mockito.when(mockOrderRepository.findAll())
                .thenReturn(deals);

        // Run the test
        final List<Order> result = orderServiceUnderTest.getAll();

        // Verify the results
        assertEquals(deals, result);
    }
}
