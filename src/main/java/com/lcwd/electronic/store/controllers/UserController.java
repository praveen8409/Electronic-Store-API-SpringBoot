package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.services.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){

        UserDto userDto1 = userService.createUser(userDto);

        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }
    // update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId, @Valid @RequestBody UserDto userDto){

       UserDto userDto1 = userService.updateUser(userDto, userId);

       return  new ResponseEntity<>(userDto1, HttpStatus.OK);
    }

    // delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId){

        userService.deleteUser(userId);
       ApiResponseMessage message = ApiResponseMessage.builder().message("User delete successfully !!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ){

        List<UserDto> userDtoList = userService.getAllUsers(pageNumber, pageSize);
        return new ResponseEntity<>(userDtoList,HttpStatus.OK);

    }

    // Get by id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getByUserId(@PathVariable("userId") String userId){
        UserDto userDto = userService.getUserById(userId);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    // Get by email

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getByUserByEmail(@PathVariable("email") String email){
        UserDto userDto = userService.getUserByEmail(email);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    // Search
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchByKeyword(@PathVariable("keywords") String keywords){
        List<UserDto> userDtos = userService.searchUsers(keywords);
        return new ResponseEntity<>(userDtos,HttpStatus.FOUND);
    }
}
