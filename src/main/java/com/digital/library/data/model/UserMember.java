package com.digital.library.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "usermember")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(exclude = "loan")
public class UserMember implements Serializable {
    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NonNull
    private String firstname;
    @NonNull
    private String surname;

    @OneToOne(mappedBy = "usermember", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("usermember")
    private Loan loan;
}
