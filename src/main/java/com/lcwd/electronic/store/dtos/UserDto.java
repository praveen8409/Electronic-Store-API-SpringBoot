package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.validate.ImageNameValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {


    private String userId;

    @Size(min = 3, max = 15, message = "Invalid Name !!")
    @ApiModelProperty(value = "user_name", name = "username", required = true, notes = "user name of new user !!")
    private String name;

    //    @Email(message = "Invalid User Email !!")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Invalid Email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password must not be empty")
    private String password;

    @Size(min = 4, max = 6, message = "Invalid Gender !!")
    private String gender;

    @NotBlank(message = "Write something about you")
    private String about;

    @ImageNameValid
    private String imageName;

    private Set<RoleDto> roles = new HashSet<>();

}
