package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Set;


@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    User user;
    Role role;

    String roleId;

    @Autowired
    private ModelMapper mapper;


    @BeforeEach
    public void init() {

        role = Role.builder().roleId("abc").roleName("NORMAL").build();

        user = User.builder()
                .name("Praveen")
                .email("praveen@gmail.com")
                .about("This is testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("123")
                .roles(Set.of(role))
                .build();

        roleId = "abc";

    }


    @Test
    public void createUserTest() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(Mockito.anyString())).thenReturn(Optional.of(role));
        UserDto user1 = userService.createUser(mapper.map(user, UserDto.class));
//        System.out.println(user1.getName());
//        Assertions.assertNotNull(user1);
        Assertions.assertEquals("Praveen", user1.getName());

    }
}
