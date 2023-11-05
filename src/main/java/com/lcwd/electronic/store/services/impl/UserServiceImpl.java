package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundExceptions;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    public UserDto createUser(UserDto userDto) {
        //  Auto-generated userId
       String userId = UUID.randomUUID().toString();
       userDto.setUserId(userId);
        //DTO -> Entity
        User user = dtoToEntity(userDto);
        User savedUser = userRepository.save(user);
        // Entity -> DTO
        UserDto newDto = entityToDto(savedUser);

        return newDto;
    }



    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
      User user =  userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundExceptions("User not found with id " + userId));
      user.setName(userDto.getName());
      user.setGender(userDto.getGender());
      user.setPassword(userDto.getPassword());
      user.setAbout(userDto.getAbout());
      user.setImageName(userDto.getImageName());
      User updatedUser = userRepository.save(user);
      UserDto updatedUserDto = entityToDto(updatedUser);
       return updatedUserDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user =  userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundExceptions("User not found with id " + userId));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getAllUsers(int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
         Page<User> page =  userRepository.findAll(pageable);
         List<User> userList = page.getContent();

       List<UserDto> userDtoList = userList.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

        return userDtoList;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundExceptions("User not found with id " + userId));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundExceptions("User not found with email " + email));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUsers(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }


    private UserDto entityToDto(User savedUser) {

//        UserDto userDto = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .imageName(savedUser.getImageName())
//                .build();

        return mapper.map(savedUser, UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .imageName(userDto.getImageName())
//                .build();
        return mapper.map(userDto, User.class);
    }
}
