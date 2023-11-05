package com.lcwd.electronic.store.dtos;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private String categoryId;

    @NotBlank(message = "Title required")
    @Size(min = 4, message = " Title must be minimum 4 Character !!")
    private String title;

    @NotBlank(message = "Description required")
    private String description;

    private String coverImage;
}
