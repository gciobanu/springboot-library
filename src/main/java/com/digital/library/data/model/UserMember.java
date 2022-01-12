package com.digital.library.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "usermember")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NonNull
    private String firstname;
    @NonNull
    private String surname;

    @OneToOne(mappedBy = "usermember")
    private Loan loan;
}
