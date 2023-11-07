package com.lcwd.electronic.store.entities;

import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<Product>();
}
