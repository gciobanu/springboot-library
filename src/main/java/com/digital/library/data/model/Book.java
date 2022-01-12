package com.digital.library.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NonNull
    private String title;
    @NonNull
    private String author;

    //@ManyToOne
    //@JoinColumn(name = "categoryId", nullable = false)
    @OneToMany(mappedBy="book")
    @NonNull
    private List<Category> categories;

    @OneToOne(mappedBy = "book")
    private Loan loan;
}
