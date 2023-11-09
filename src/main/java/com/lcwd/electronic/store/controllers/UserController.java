package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
@Api(value = "UserController", description = "REST APIs related to perform user operations !!")
//@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    // Create
    @PostMapping
    @ApiOperation(value = "create new user !!")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 401, message = "not authorized !!"),
            @ApiResponse(code = 201, message = "new user created !!")
    })
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){

        UserDto userDto1 = userService.createUser(userDto);

        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }
    // update
    @PutMapping("/{userId}")
    @ApiOperation(value = "Update single user details by userid !!")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId, @Valid @RequestBody UserDto userDto){

       UserDto userDto1 = userService.updateUser(userDto, userId);

       return  new ResponseEntity<>(userDto1, HttpStatus.OK);
    }

    // delete
    @DeleteMapping("/{userId}")
    @ApiOperation(value = "Delete single user by userid !!")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId){

        userService.deleteUser(userId);
       ApiResponseMessage message = ApiResponseMessage.builder().message("User delete successfully !!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // Get all
    @GetMapping
    @ApiOperation(value = "get all users", tags = {"user-controller"})
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){

        PageableResponse<UserDto> userDtoList = userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(userDtoList,HttpStatus.OK);

    }

    // Get by id
    @GetMapping("/{userId}")
    @ApiOperation(value = "Get single user by userid !!")
    public ResponseEntity<UserDto> getByUserId(@PathVariable("userId") String userId){
        UserDto userDto = userService.getUserById(userId);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    // Get by email

    @GetMapping("/email/{email}")
    @ApiOperation(value = "Get single user by Email !!")
    public ResponseEntity<UserDto> getByUserByEmail(@PathVariable("email") String email){
        UserDto userDto = userService.getUserByEmail(email);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    // Search
    @GetMapping("/search/{keywords}")
    @ApiOperation(value = "Get users by keyword !!")
    public ResponseEntity<List<UserDto>> searchByKeyword(@PathVariable("keywords") String keywords){
        List<UserDto> userDtos = userService.searchUsers(keywords);
        return new ResponseEntity<>(userDtos,HttpStatus.FOUND);
    }

    // Upload file
    //upload user image
    @PostMapping("/image/{userId}")
    @ApiOperation(value = "Upload user pic by userId !!")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws IOException {
        String imageName = fileService.uploadFile(image, imageUploadPath);
        UserDto user = userService.getUserById(userId);
        user.setImageName(imageName);
        UserDto userDto = userService.updateUser(user, userId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true).message("image is uploaded successfully ").status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);

    }

    //serve user image
    @GetMapping(value = "/image/{userId}")
    @ApiOperation(value = "Serve user pic to user !!")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user = userService.getUserById(userId);
        logger.info("User image name : {} ", user.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }

}
