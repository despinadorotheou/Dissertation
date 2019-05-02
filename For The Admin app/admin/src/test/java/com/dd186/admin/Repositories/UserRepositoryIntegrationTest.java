package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.User;
import com.dd186.admin.Repositories.UserRepository;
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
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindUserById() {

        // given
        User alex = new User("Alex", "Johnson", "aj123@student.le.ac.uk", "$2a$10$P6ENyTd74quDcQSsKiAqV.U3hcsvn.d/79pw3UyRvjLKCdechChyq");
        int id = entityManager.persistAndGetId(alex, Integer.class);
        entityManager.flush();


        // when
        User found = userRepository.findById(id);

        // then
        assertThat(found.getId())
                .isEqualTo(alex.getId());
    }

    @Test
    public void testFindUserByEmail() {

        // given
        User alex = new User("Alex", "Johnson", "aj123@student.le.ac.uk", "$2a$10$P6ENyTd74quDcQSsKiAqV.U3hcsvn.d/79pw3UyRvjLKCdechChyq");
        entityManager.persist(alex);
        entityManager.flush();

        // when
        User found = userRepository.findByEmail(alex.getEmail());

        // then
        assertThat(found.getEmail())
                .isEqualTo(alex.getEmail());
    }

    @Test
    public void testSaveUser() {

        // given
        User alex = new User("Alex", "Johnson", "aj123@student.le.ac.uk", "$2a$10$P6ENyTd74quDcQSsKiAqV.U3hcsvn.d/79pw3UyRvjLKCdechChyq");


        // when
        userRepository.save(alex);

        // then
        User found = userRepository.findByEmail(alex.getEmail());
        assertNotNull(found);
    }

    @Test
    public void testDeleteUser() {

        // given
        User alex = new User("Alex", "Johnson", "aj123@student.le.ac.uk", "$2a$10$P6ENyTd74quDcQSsKiAqV.U3hcsvn.d/79pw3UyRvjLKCdechChyq");
        entityManager.persist(alex);
        entityManager.flush();

        // when
        userRepository.delete(alex);
        User found = userRepository.findByEmail(alex.getEmail());

        // then
        assertNull(found);
    }
}