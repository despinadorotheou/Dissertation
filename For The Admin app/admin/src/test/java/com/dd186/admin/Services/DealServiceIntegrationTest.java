package com.dd186.admin.Services;

import com.dd186.admin.Domain.Deal.Deal;
import com.dd186.admin.Repositories.DealRepository;
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

public class DealServiceIntegrationTest {

    @Mock
    private DealRepository mockDealRepository;

    private DealService dealServiceUnderTest;
    private Deal deal;

    @Before
    public void setUp() {
        initMocks(this);
        dealServiceUnderTest = new DealService(mockDealRepository);
        deal = new Deal("Mock deal", 2.0);
        deal.setId(1);
    }

    @Test
    public void testFindOfferById() {
        // Setup
        Mockito.when(dealServiceUnderTest.findById(anyInt()))
                .thenReturn(deal);
        final int id = 1;

        // Run the test
        final Deal result = dealServiceUnderTest.findById(id);

        // Verify the results
        assertEquals(id, result.getId());
    }

    @Test
    public void testSaveOffer() {
        // Setup
        Mockito.when(dealServiceUnderTest.findById(anyInt()))
                .thenReturn(null).thenReturn(deal);
        final int id = 1;

        // Run the test
        Deal existing = dealServiceUnderTest.findById(id);
        dealServiceUnderTest.save(deal);
        Deal result = dealServiceUnderTest.findById(id);

        // Verify the results
        assertNotNull(result);
        assertEquals(result, deal);
        assertNull(existing);
    }

    @Test
    public void testDeleteOffer() {
        // Setup
        Mockito.when(dealServiceUnderTest.findById(anyInt()))
                .thenReturn(deal).thenReturn(null);

        // Run the test
        final Deal beforeDelete = dealServiceUnderTest.findById(1);
        dealServiceUnderTest.delete(beforeDelete);
        final Deal result = dealServiceUnderTest.findById(1);


        // Verify the results
        assertNotNull(beforeDelete);
        assertEquals(beforeDelete, deal);
        assertNull(result);
    }

    @Test
    public void testFindAllOffers() {
        // Setup
        Deal deal1 = new Deal("Mock deal 2", 1.00);
        List<Deal> deals = new ArrayList<>();
        deals.add(deal);
        deals.add(deal1);


        Mockito.when(mockDealRepository.findAll())
                .thenReturn(deals);

        // Run the test
        final List<Deal> result = dealServiceUnderTest.findAll();

        // Verify the results
        assertEquals(deals, result);
    }
}
