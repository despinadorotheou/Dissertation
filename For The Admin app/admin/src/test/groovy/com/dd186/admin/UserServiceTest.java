//package com.dd186.admin;
//import com.dd186.admin.Domain.User;
//import com.dd186.admin.Repositories.RoleRepository;
//import com.dd186.admin.Repositories.UserRepository;
//import com.dd186.admin.Services.UserService;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.MockitoAnnotations.initMocks;
//public class UserServiceTest {
//
//    @Mock
//    private UserRepository mockUserRepository;
//    @Mock
//    private RoleRepository mockRoleRepository;
//    @Mock
//    private BCryptPasswordEncoder mockBCryptPasswordEncoder;
//
//    private UserService userServiceUnderTest;
//    private User user;
//
//    @Before
//    public void setUp() {
//        initMocks(this);
//        userServiceUnderTest = new UserService(mockUserRepository,
//                mockRoleRepository,
//                mockBCryptPasswordEncoder);
//
//        User user = new User(2,"Deborah", "Dor","test@test.com","1234567890",1);
//
//        Mockito.when(mockUserRepository.save(any()))
//                .thenReturn(user);
//        Mockito.when(mockUserRepository.findByEmail(anyString()))
//                .thenReturn(user);
//    }
//
//    @Test
//    public void testFindUserByEmail() {
//        // Setup
//        final String email = "test@test.com";
//
//        // Run the test
//        final User result = userServiceUnderTest.findUserByEmail(email);
//
//        // Verify the results
//        assertEquals(email, result.getEmail());
//    }
//
////    @Test
////    public void testSaveUser() {
////        // Setup
////        final String email = "test@test.com";
////
////        // Run the test
////        User result = userServiceUnderTest.saveUserCustom(User.builder().build());
////
////        // Verify the results
////        assertEquals(email, result.getEmail());
////    }
//}