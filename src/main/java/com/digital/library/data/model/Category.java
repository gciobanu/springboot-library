package com.digital.library.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(exclude = "bookSet")
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "categorySet")
    @JsonIgnoreProperties("categorySet")
    private Set<Book> bookSet;

}
