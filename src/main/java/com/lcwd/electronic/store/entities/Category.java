package com.lcwd.electronic.store.entities;

import lombok.*;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @Column(name = "id")
    private String categoryId;

    @Column(name = "category_title", nullable = false, length = 60)
    private String title;

    @Column(name = "category_desc", length = 50)
    private String description;

    private String coverImage;
}
