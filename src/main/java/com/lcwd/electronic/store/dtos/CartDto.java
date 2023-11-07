package com.lcwd.electronic.store.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {

    private String cartId;
    private Date createdDate;
    private UserDto userDto;
    private List<CartItemDto> items = new ArrayList<CartItemDto>();
}
