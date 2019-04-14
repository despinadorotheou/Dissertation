package com.dd186.admin.Repositories;

import com.dd186.admin.Domain.Role;
import com.dd186.admin.Domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void whenFindByRole_thenReturnTheRole() {

        // given
        Role newRole = new Role("Developer");
        entityManager.persist(newRole);
        entityManager.flush();

        // when
        Role found = roleRepository.findByRole(newRole.getRole());

        // then
        assertThat(found.getRole())
                .isEqualTo(newRole.getRole());
    }
}
