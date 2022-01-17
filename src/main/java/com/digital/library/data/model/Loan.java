package com.digital.library.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "loan")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Loan implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NonNull
    @OneToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @JsonIgnoreProperties("loan")
    private Book book;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "usermember_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties("loanSet")
    private UserMember usermember;

    @NonNull
    //@DateTimeFormat(pattern="dd-MM-yyyy")
    private LocalDate loanDate;
    //@DateTimeFormat(pattern="dd-MM-yyyy")
    private LocalDate returnDate;

}
