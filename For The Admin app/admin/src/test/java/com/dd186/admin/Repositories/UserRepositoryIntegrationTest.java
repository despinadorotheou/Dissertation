package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.User;
import com.dd186.admin.Repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

//    @Test
//    public void whenFindById_thenReturnUser() {
//
//        // given
//        User alex = new User(1,"Alex", "Johnson", "aj123@student.le.ac.uk", "$2a$10$P6ENyTd74quDcQSsKiAqV.U3hcsvn.d/79pw3UyRvjLKCdechChyq", 1);
//        User existingUser = entityManager.find(User.class , 1);
//        if (existingUser == null){
//            entityManager.persist(alex);
//        }else{
//            entityManager.merge(alex);
//        }
//        entityManager.flush();
//
//        // when
//        User found = userRepository.findById(alex.getId());
//
//        // then
//        assertThat(found.getId())
//                .isEqualTo(alex.getId());
//    }

    @Test
    public void whenFindByEmail_thenReturnUser() {

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
}
