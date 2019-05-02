package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.Deal.Deal;
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
public class DealRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;
    @Qualifier("dealRepository")
    @Autowired
    private DealRepository dealRepository;

    @Test
    public void testFindDealById() {

        // given
        Deal deal = new Deal("Deal 1", 2.00);
        int id = entityManager.persistAndGetId(deal, Integer.class);
        entityManager.flush();

        // when
        Deal found = dealRepository.findById(id);

        // then
        assertThat(found.getId())
                .isEqualTo(deal.getId());
    }

    @Test
    public void testSaveDeal() {

        // given
        Deal deal = new Deal("Deal 1", 2.00);


        // when
        dealRepository.save(deal);
        // then
        Deal found = dealRepository.findById(deal.getId());
        assertNotNull(found);
    }

    @Test
    public void testDeleteDeal() {

        // given
        Deal deal = new Deal("Deal 1", 2.00);
        int id = entityManager.persistAndGetId(deal, Integer.class);
        entityManager.flush();

        // when
        dealRepository.delete(deal);
        Deal found = dealRepository.findById(id);

        // then
        assertNull(found);
    }
}
