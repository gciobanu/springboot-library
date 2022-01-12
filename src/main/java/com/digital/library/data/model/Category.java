package com.digital.library.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer categoryId;
    @NonNull
    private String name;

    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Book book;
}
