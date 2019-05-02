package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.Offer.Offer;
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
public class OfferRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;
    @Qualifier("offerRepository")
    @Autowired
    private OfferRepository offerRepository;

    @Test
    public void testFindOfferById() {

        // given
        Offer offer = new Offer("Offer 1", 2.00);
        int id = entityManager.persistAndGetId(offer, Integer.class);
        entityManager.flush();

        // when
        Offer found = offerRepository.findById(id);

        // then
        assertThat(found.getId())
                .isEqualTo(offer.getId());
    }

    @Test
    public void testSaveOffer() {

        // given
        Offer offer = new Offer("Offer 1", 2.00);

        // when
        offerRepository.save(offer);

        // then
        Offer found = offerRepository.findById(offer.getId());
        assertNotNull(found);
    }

    @Test
    public void testDeleteOffer() {

        // given
        Offer offer = new Offer("Offer 1", 2.00);
        int id = entityManager.persistAndGetId(offer, Integer.class);
        entityManager.flush();

        // when
        offerRepository.delete(offer);
        Offer found = offerRepository.findById(id);

        // then
        assertNull(found);
    }
}
