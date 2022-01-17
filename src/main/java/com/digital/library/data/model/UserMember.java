package com.digital.library.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "usermember")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(exclude = "loan")
@JsonIgnoreProperties(value = { "loanSet" })
public class UserMember implements Serializable {
    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NonNull
    private String firstname;
    @NonNull
    private String surname;

    @OneToMany(mappedBy = "usermember")//, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("usermember")
    private Set<Loan> loanSet;
}
