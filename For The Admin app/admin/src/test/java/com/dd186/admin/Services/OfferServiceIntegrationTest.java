package com.dd186.admin.Services;

import com.dd186.admin.Domain.Offer.Offer;
import com.dd186.admin.Domain.Product;
import com.dd186.admin.Repositories.OfferRepository;
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

public class OfferServiceIntegrationTest {
    @Mock
    private OfferRepository mockOfferRepository;

    private OfferService offerServiceUnderTest;
    private Offer offer;

    @Before
    public void setUp() {
        initMocks(this);
        offerServiceUnderTest = new OfferService(mockOfferRepository);
        offer = new Offer("Mock offer", 2.0);
        offer.setId(1);
    }

    @Test
    public void testFindOfferById() {
        // Setup
        Mockito.when(offerServiceUnderTest.findById(anyInt()))
                .thenReturn(offer);
        final int id = 1;

        // Run the test
        final Offer result = offerServiceUnderTest.findById(id);

        // Verify the results
        assertEquals(id, result.getId());
    }

    @Test
    public void testSaveOffer() {
        // Setup
        Mockito.when(offerServiceUnderTest.findById(anyInt()))
                .thenReturn(null).thenReturn(offer);
        final int id = 1;

        // Run the test
        Offer existing = offerServiceUnderTest.findById(id);
        offerServiceUnderTest.save(offer);
        Offer result = offerServiceUnderTest.findById(id);

        // Verify the results
        assertNotNull(result);
        assertEquals(result, offer);
        assertNull(existing);
    }

    @Test
    public void testDeleteOffer() {
        // Setup
        Mockito.when(offerServiceUnderTest.findById(anyInt()))
                .thenReturn(offer).thenReturn(null);

        // Run the test
        final Offer beforeDelete = offerServiceUnderTest.findById(1);
        offerServiceUnderTest.delete(beforeDelete);
        final Offer result = offerServiceUnderTest.findById(1);


        // Verify the results
        assertNotNull(beforeDelete);
        assertEquals(beforeDelete, offer);
        assertNull(result);
    }

    @Test
    public void testFindAllOffers() {
        // Setup
        Offer offer1 = new Offer("Mock offer 2", 1.00);
        List<Offer> offers = new ArrayList<>();
        offers.add(offer);
        offers.add(offer1);


        Mockito.when(mockOfferRepository.findAll())
                .thenReturn(offers);

        // Run the test
        final List<Offer> result = offerServiceUnderTest.findAll();

        // Verify the results
        assertEquals(offers, result);
    }
}
