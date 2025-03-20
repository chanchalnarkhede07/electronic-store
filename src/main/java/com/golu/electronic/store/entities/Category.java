package com.golu.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "id")
    private String categoryId;
    @Column(name = "category_title", nullable = false, length = 100)
    private String title;
    @Column(name = "category_desc", length = 100)
    private String description;
    private String coverImage;

    //Lazy means not load all products when category fetch
    //cascade means change products also when category change
    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products= new ArrayList<>();

}
